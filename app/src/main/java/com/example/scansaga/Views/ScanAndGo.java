package com.example.scansaga.Views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scansaga.Model.GeoLocationManager;
import com.example.scansaga.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScanAndGo extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int REQUEST_CODE_GALLERY = 1; // Add this line

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_scan_page);

        ImageButton takePictureButton = findViewById(R.id.take_picture_button);
        ImageButton getPhotoButton = findViewById(R.id.get_photo_from_gallery_button);


        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(ScanAndGo.this);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scan a QR code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
            }
        });

        getPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to pick an image from the gallery
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // This should be called first

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            try {
                // Handle the gallery result
                Uri imageUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                String qrCodeResult = decodeQRCode(bitmap);
                if (qrCodeResult != null) {
                    // Process QR code content here
                    Log.d("ScanAndGo", "QR result:"+qrCodeResult);
                    checkUserInEvent(qrCodeResult);

                } else {
                    redirectToCheckinResultPage("Failed to decode QR Code", false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                redirectToCheckinResultPage("Failed to decode QR Code", false);
            }
        } else {
            // Handle other onActivityResult scenarios, including the IntentIntegrator result
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(intentResult != null) {
                String content = intentResult.getContents();
                if(content != null) {
                    checkUserInEvent(content);
                }
            }
        }
    }

    private String decodeQRCode(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            QRCodeReader qrCodeReader = new QRCodeReader();
            return qrCodeReader.decode(binaryBitmap).getText();
        } catch (Exception e) {
            Log.e("decodeQRCode", "Error decoding QR code", e);
            return null;
        }
    }


    private void checkUserInEvent(String url) {
        Log.d("ScanAndGo", "Passed content:" + url);
        db.collection("events").document(url).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                Log.d("ScanAndGo", url + " info exists");
                String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                DocumentSnapshot eventDoc = task.getResult();
                if (eventDoc.contains("attendeeCheckInCounts") && eventDoc.get("attendeeCheckInCounts") instanceof Map) {
                    Map<String, Long> attendeeCheckInCounts = (Map<String, Long>) eventDoc.get("attendeeCheckInCounts");
                    Long currentCount = attendeeCheckInCounts.getOrDefault(deviceId, 0L);
                    // Increment the count or add the user with a count of 1 if they're not in the map
                    attendeeCheckInCounts.put(deviceId, currentCount + 1);
                    // Update Firestore document with the new count
                    db.collection("events").document(url).update("attendeeCheckInCounts", attendeeCheckInCounts).addOnSuccessListener(aVoid -> {
                        Log.d("ScanAndGo", "Updated attendee check-in count");
                        // Redirect based on if it's a first check-in or a repeat
                        redirectToCheckinResultPage(currentCount > 0 ? "Checked In Again" : "First Check-In", true);
                    });
                } else {
                    // No attendees have checked in yet, so add this user as the first
                    Map<String, Long> firstCheckIn = new HashMap<>();
                    firstCheckIn.put(deviceId, 1L);
                    db.collection("events").document(url).update("attendeeCheckInCounts", firstCheckIn);
                    redirectToCheckinResultPage("First Check-In", true);
                }
            } else {
                Log.d("ScanAndGo", "Couldn't check into event");
                redirectToCheckinResultPage("Error checking event", false);
            }
        });
    }


    private void uploadLocationAndCheckInAttendee(String url, String deviceId, double latitude, double longitude) {
        Map<String, Object> locationData = new HashMap<>();
        // Upload user info with location and device ID
        locationData.put("deviceId", deviceId);
        locationData.put("latitude", latitude);
        locationData.put("longitude", longitude);
        // Still need to add user to checked in list, so also execute the checkInWithoutLocation
        checkInAttendeeWithoutLocation(url,deviceId);

        db.collection("events").document(url).collection("locationOfCheckedInUsers").document(deviceId)
                .set(locationData)
                .addOnSuccessListener(s -> redirectToCheckinResultPage("Location recorded & checked in successfully", true))
                .addOnFailureListener(f -> redirectToCheckinResultPage("Error saving location data", false));
    }

    // This redirects users to the CheckInResultPage. It displays either a checkin error or succes
    // With text provided for each case
    private void redirectToCheckinResultPage(String message, boolean success) {
        Intent intent = new Intent(ScanAndGo.this, CheckinResultPage.class);
        intent.putExtra("checkInMessage", message);
        intent.putExtra("checkInSuccess", success);
        startActivity(intent);
    }

    // This adds the user to the "Checked in". Users who do not have location enabled will just be
    // Added to this
    private void checkInAttendeeWithoutLocation(String url, String deviceId) {
        db.collection("events").document(url).update("checkedInAttendees", FieldValue.arrayUnion(deviceId))
                .addOnSuccessListener(s -> redirectToCheckinResultPage("Checked in successfully", true))
                .addOnFailureListener(f -> redirectToCheckinResultPage("Error checking in", false));
    }

}

