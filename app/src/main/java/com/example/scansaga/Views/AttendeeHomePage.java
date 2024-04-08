package com.example.scansaga.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.scansaga.Controllers.EventArrayAdapter;
import com.example.scansaga.Model.Event;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Activity for the homepage of the attendee.
 */
public class AttendeeHomePage extends AppCompatActivity {

    /**
     * Called when the activity is created. Initializes UI elements, retrieves user data
     * passed from the previous activity, and sets up click listeners for buttons.
     *
     * @param savedInstanceState  If the activity is being re-initialized after previously
     *                            being shut down then this Bundle contains the data it most
     *                            recently supplied in onSaveInstanceState(Bundle).
     */
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_homepage);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize buttons
        Button scanAndGoButton = findViewById(R.id.scan_and_attend_button);
        Button editProfileButton = findViewById(R.id.edit_profile_button);
        Button showAllEventsButton = findViewById(R.id.show_all_events_button);
        Button addEventButton = findViewById(R.id.add_event_button);
        Button showAllSignedUpEvents = findViewById(R.id.show_all_signed_up_events);
        Button organizerEvents = findViewById(R.id.organizer_events);
        Switch geoLocation = findViewById(R.id.geo_location_switch);

        // Retrieve the user details passed from MainActivity
        User user = (User) getIntent().getSerializableExtra("user");

        // Set click listener for show all events button
        showAllEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ShowAllEventsActivity
                Intent intent = new Intent(AttendeeHomePage.this, ShowAllEventsAttendees.class);
                startActivity(intent);
            }
        });


        // Set click listener for edit profile button
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start EditProfileActivity
                Intent intent = new Intent(AttendeeHomePage.this, MyProfile.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        scanAndGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ScanAndGo Activity
                Intent intent = new Intent(AttendeeHomePage.this, ScanAndGo.class);
                startActivity(intent);
            }
        });

        // Set click listener for add event button
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddEventActivity
                Intent intent = new Intent(AttendeeHomePage.this, AddEvent.class);
                startActivity(intent);
            }
        });
        showAllSignedUpEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ShowAllUsersActivity
                Intent intent = new Intent(AttendeeHomePage.this, MySignedUpEvents.class);
                startActivity(intent);
            }
        });

        organizerEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ShowAllUsersActivity
                Intent intent = new Intent(AttendeeHomePage.this, OrganizerEvents.class);
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

    /**
     * Nested activity within AttendeeHomePage responsible for handling the display
     * and addition of events. Implements the AddEventFragment.AddEventDialogListener
     * to receive data from an AddEventFragment.
     */
    public static class AddEvent extends AppCompatActivity implements AddEventFragment.AddEventDialogListener {
        ListView eventList;
        ArrayList<Event> eventDataList;
        EventArrayAdapter eventArrayAdapter;
        private FirebaseFirestore db;
        private CollectionReference eventsRef;

        /**
         *  Callback method triggered when a new event is to be added from the AddEventFragment.
         *  Updates the UI to reflect the added event.
         *
         * @param event The new Event object to be added to the list
         */
        @Override
        public void addNewEvent(Event event) {
            eventArrayAdapter.notifyDataSetChanged();
        }
        /**
         *  Callback method triggered when an event is deleted from the AddEventFragment.
         *  Removes the event from the list and updates the UI.
         *
         * @param event The Event object to be deleted from the list.
         */

        @Override
        public void deleteEvent(Event event) {
            eventDataList.remove(event);
            eventArrayAdapter.notifyDataSetChanged();
        }

        /**
         * Callback method triggered when an event is edited from the AddEventFragment.
         * Updates the UI to reflect changes to the edited event.
         *
         * @param event The edited Event object.
         */
        @Override
        public void editEvent(Event event) {
            eventArrayAdapter.notifyDataSetChanged();
        }
        /**
         * Called when the activity is created. Initi
         * alizes UI elements, sets up Firestore
         * references, creates the adapter for the event list, and sets an OnClickListener
         * for the FloatingActionButton to trigger the AddEventFragment.
         *
         * @param savedInstanceState  If the activity is being re-initialized after previously
         *                            being shut down then this Bundle contains the data it most
         *                            recently supplied in onSaveInstanceState(Bundle).
         */

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_event);

            FloatingActionButton fab = findViewById(R.id.add_event_button);
            fab.setOnClickListener(v -> new AddEventFragment().show(getSupportFragmentManager(), "Add Event"));

        }


    }
}
