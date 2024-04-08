package com.example.scansaga.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.scansaga.Model.GeoLocationManager;
import com.example.scansaga.Model.MyProfile;
import com.example.scansaga.Model.User;
import com.example.scansaga.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * This activity serves as the homepage of the application, providing options for various functionalities.
 */
public class HomepageActivity extends AppCompatActivity {
    private String deviceId;

    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize buttons
        Button showAllEventsButton = findViewById(R.id.show_all_events_button);
        Button showAllImages = findViewById(R.id.show_all_images_button);
        Button scanAndGoButton = findViewById(R.id.scan_and_attend_button);
        Button editProfileButton = findViewById(R.id.edit_profile_button);
        Button addEventButton = findViewById(R.id.add_event_button);
        Button showAllUsers = findViewById(R.id.show_all_users_button);
        Button showAllSignedUpEvents = findViewById(R.id.show_all_signed_up_events);
        Button organizerEvents = findViewById(R.id.organizer_events);
        Button useExistingQr = findViewById(R.id.show_old_qr);
        Switch geoLocation = findViewById(R.id.geo_location_switch);


        // Retrieve the user details passed from MainActivity
        User user = (User) getIntent().getSerializableExtra("user");

        // If coming back from previous activity, check to see if geolocation is already on and
        // adjust the toggle for this
        if (GeoLocationManager.checkPermissions()){geoLocation.setChecked(true);}

        // Set click listeners for each button
        showAllEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ShowAllEventsActivity
                Intent intent = new Intent(HomepageActivity.this, ShowAllEvents.class);
                startActivity(intent);
            }
        });
        useExistingQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ShowAllEventsActivity
                Intent intent = new Intent(HomepageActivity.this, UseExistingQr.class);
                startActivity(intent);
            }
        });

        showAllImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SignUpEventActivity
                Intent intent = new Intent(HomepageActivity.this, ShowAllImagesFromStorage.class);
                startActivity(intent);
            }
        });


        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start EditProfileActivity
                Intent intent = new Intent(HomepageActivity.this, MyProfile.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddEventActivity
                Intent intent = new Intent(HomepageActivity.this, AttendeeHomePage.AddEvent.class);
                startActivity(intent);
            }
        });

        scanAndGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, ScanAndGo.class);
                startActivity(intent);
            }
        });

        showAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ShowAllUsersActivity
                Intent intent = new Intent(HomepageActivity.this, ShowAllUsers.class);
                startActivity(intent);
            }
        });

        showAllSignedUpEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ShowAllUsersActivity
                Intent intent = new Intent(HomepageActivity.this, MySignedUpEvents.class);
                startActivity(intent);
            }
        });
        organizerEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start ShowAllUsersActivity
                Intent intent = new Intent(HomepageActivity.this, OrganizerEvents.class);
                startActivity(intent);
            }
        });

        geoLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // User turned on the geolocation toggle
                handleGeolocationToggle(true);
            } else {
                // User turned off the geolocation toggle
                handleGeolocationToggle(false);
            }
        });
    }

    private void handleGeolocationToggle(boolean isEnabled) {
        if (isEnabled) {
            // Check permissions and get location if enabled
            if (checkPermissions()) {
                GeoLocationManager.setPermissions(true);
                getLastLocation();
            } else {
                // Request permissions if not granted
                requestPermissions();
            }
        } else {
            // Here you can disable geolocation features of your app when the toggle is off
            GeoLocationManager.setPermissions(false);
        }
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
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
}
