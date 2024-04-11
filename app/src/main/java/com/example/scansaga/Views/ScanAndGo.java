package com.example.scansaga.Views;

import static com.example.scansaga.Model.MainActivity.token;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.scansaga.Model.GeoLocationManager;
import com.example.scansaga.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

/**
 * An activity designed to handle the scanning of QR codes for event check-ins.
 * It supports scanning directly using the camera or choosing an image from the gallery.
 * Upon successful scanning, it updates the attendee's check-in status in a Firestore database
 * and optionally uploads their current location if permission is granted and data is available.
 */
public class ScanAndGo extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int REQUEST_CODE_GALLERY = 1; // Add this line
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    private String deviceId;
    private ToggleButton geoLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_scan_page);
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ImageButton takePictureButton = findViewById(R.id.take_picture_button);
        ImageButton getPhotoButton = findViewById(R.id.get_photo_from_gallery_button);

        // Inside onCreate or appropriately initialize this if using after onCreate
        Switch geoLocationSwitch = findViewById(R.id.geo_location_switch);
        geoLocationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> handleGeolocationToggle(isChecked));

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

        geoLocationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // User turned on the geolocation toggle
                handleGeolocationToggle(true);
            } else {
                // User turned off the geolocation toggle
                handleGeolocationToggle(false);
            }
        });


    }

    /**
     * Handles the result from both the QR code scanning intent and the gallery image selection.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    allowing to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller.
     */
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

    /**
     * Decodes a QR code from a Bitmap and returns the text content encoded in the QR code.
     *
     * @param bitmap The bitmap image of the QR code to decode.
     * @return The decoded text from the QR code, or null if decoding fails.
     */
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


    /**
     * Checks the user into the event identified by the URL encoded in the QR code.
     * It updates the Firestore database with the new check-in count or adds the user to the event's attendee list.
     *
     * @param url The URL encoded in the QR code, used to identify the event document in Firestore.
     */
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
                    db.collection("events").document(url).update("signedUpAttendeeTokens", FieldValue.arrayUnion(token))
                            .addOnSuccessListener(v -> {
                                Log.d("TOKEN", "Token successfully added in ScanAndGo");
                            });

                    db.collection("events").document(url).update("attendeeCheckInCounts", attendeeCheckInCounts).addOnSuccessListener(aVoid -> {
                        Log.d("ScanAndGo", "Updated attendee check-in count");
                        // Redirect based on if it's a first check-in or a repeat
                        // Also check if they have location enabled. Add their location to db if it is
                        if (checkPermissions()){
                            uploadLocationAndCheckInAttendee(url,deviceId,GeoLocationManager.getLatitude(),GeoLocationManager.getLongitude());
                        } else {redirectToCheckinResultPage(currentCount > 0 ? "Checked In Again" : "First Check-In", true);}
                    });
                } else {
                    // No attendees have checked in yet, so add this user as the first
                    Map<String, Long> firstCheckIn = new HashMap<>();
                    firstCheckIn.put(deviceId, 1L);

                    db.collection("events").document(url).update("signedUpAttendeeTokens", FieldValue.arrayUnion(token))
                            .addOnSuccessListener(v -> {
                                Log.d("TOKEN", "Token successfully added in ScanAndGo");
                            });

                    db.collection("events").document(url).update("attendeeCheckInCounts", firstCheckIn);
                    if (checkPermissions()){ // need to check to see if location is permitted or not here somehow
                        uploadLocationAndCheckInAttendee(url,deviceId,GeoLocationManager.getLatitude(),GeoLocationManager.getLongitude());
                    } else {redirectToCheckinResultPage("First Check-In", true);}

                }
            } else {
                Log.d("ScanAndGo", "Couldn't check into event");
                redirectToCheckinResultPage("Couldn't Find Event", false);
            }
        });
    }


    /**
     * Optionally uploads the user's current location to Firestore under the event document and marks them as checked in.
     * This method is used when location data is available and permissions are granted.
     *
     * @param url       The URL identifying the event document in Firestore.
     * @param deviceId  The device ID of the user's device, used as a unique identifier.
     * @param latitude  The latitude component of the user's current location.
     * @param longitude The longitude component of the user's current location.
     */
    private void uploadLocationAndCheckInAttendee(String url, String deviceId, double latitude, double longitude) {
        Map<String, Object> locationData = new HashMap<>();
        // Upload user info with location and device ID
        locationData.put("deviceId", deviceId);
        locationData.put("latitude", latitude);
        locationData.put("longitude", longitude);

        db.collection("events").document(url).collection("locationOfCheckedInUsers").document(deviceId)
                .set(locationData)
                .addOnSuccessListener(s -> redirectToCheckinResultPage("Location recorded & checked in successfully", true))
                .addOnFailureListener(f -> redirectToCheckinResultPage("Error saving location data", false));
    }

    /**
     * Redirects the user to the CheckinResultPage activity, displaying a success or failure message
     * based on the result of the check-in attempt or QR code decoding process.
     *
     * @param message The message to display on the CheckinResultPage.
     * @param success A boolean indicating if the check-in was successful or not.
     */
    private void redirectToCheckinResultPage(String message, boolean success) {
        Intent intent = new Intent(ScanAndGo.this, CheckinResultPage.class);
        intent.putExtra("checkInMessage", message);
        intent.putExtra("checkInSuccess", success);
        startActivity(intent);
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            GeoLocationManager.setLatitude(location.getLatitude());
                            GeoLocationManager.setLongitude(location.getLongitude());
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            GeoLocationManager.setLatitude(mLastLocation.getLatitude());
            GeoLocationManager.setLongitude(mLastLocation.getLongitude());
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateGeoLocationToggleState();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
    private void updateGeoLocationToggleState() {
        Switch geoLocation = findViewById(R.id.geo_location_switch);
        geoLocation.setChecked(checkLocationPermissions() && isLocationEnabled());
    }

    // Now the checkPermissions method will be the source of truth for permissions
    private boolean checkLocationPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    // Directly use checkPermissions() instead of GeoLocationManager.checkPermissions()
    private void handleGeolocationToggle(boolean isEnabled) {
        if (isEnabled) {
            if (checkPermissions()) {
                getLastLocation();
            } else {
                requestPermissions();
            }
        } else {
            Toast.makeText(this, "Geolocation is disabled", Toast.LENGTH_SHORT).show();
        }
    }
}

