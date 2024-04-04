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

import com.example.scansaga.R;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.Arrays;
import java.util.List;

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
                    checkUserInEvent(qrCodeResult);
                } else {
                    Toast.makeText(this, "QR Code could not be decoded", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to decode QR Code", Toast.LENGTH_SHORT).show();
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
        db.collection("events").document(url).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                    // Check if the "checkedInAttendees" field exists and contains the deviceId
                    if (task.getResult().contains("checkedInAttendees") && task.getResult().get("checkedInAttendees") instanceof List) {
                        List<String> checkedInAttendees = (List<String>) task.getResult().get("checkedInAttendees");
                        if (checkedInAttendees.contains(deviceId)) {
                            Toast.makeText(ScanAndGo.this, "You're already checked in to this event", Toast.LENGTH_LONG).show();
                        } else {
                            // User is not checked in, add them to the list
                            checkInAttendee(url, deviceId);
                        }
                    } else {
                        // "checkedInAttendees" doesn't exist, start the array with the attendee
                        createCheckedInAttendeesDocument(url, deviceId);
                    }
                } else {
                    Toast.makeText(ScanAndGo.this, "Event does not exist", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ScanAndGo.this, "Error checking event", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkInAttendee(String url, String deviceId) {
        db.collection("events").document(url).update("checkedInAttendees", FieldValue.arrayUnion(deviceId))
                .addOnSuccessListener(s -> Toast.makeText(ScanAndGo.this, "You are now checked in. Thank you!", Toast.LENGTH_LONG).show())
                .addOnFailureListener(f -> Toast.makeText(ScanAndGo.this, "Error checking in to event", Toast.LENGTH_LONG).show());
    }

    private void createCheckedInAttendeesDocument(String url, String deviceId) {
        List<String> initialAttendee = Arrays.asList(deviceId);
        db.collection("events").document(url).update("checkedInAttendees", initialAttendee)
                .addOnSuccessListener(s -> Toast.makeText(ScanAndGo.this, "You are now checked in. Thank you!", Toast.LENGTH_LONG).show())
                .addOnFailureListener(f -> Toast.makeText(ScanAndGo.this, "Error creating check-in document", Toast.LENGTH_LONG).show());
    }

}

