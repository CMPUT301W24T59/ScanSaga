package com.example.scansaga.Views;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scansaga.Model.MyProfile;
import com.example.scansaga.Model.User;
import com.example.scansaga.R;

/**
 * This activity serves as the homepage of the application, providing options for various functionalities.
 */
public class HomepageActivity extends AppCompatActivity {
    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Initialize buttons
        Button showAllEventsButton = findViewById(R.id.show_all_events_button);
        Button showAllImages = findViewById(R.id.show_all_images_button);
        Button scanAndGoButton = findViewById(R.id.scan_and_attend_button);
        Button editProfileButton = findViewById(R.id.edit_profile_button);
        Button addEventButton = findViewById(R.id.add_event_button);
        Button showAllUsers = findViewById(R.id.show_all_users_button);
        Button showAllSignedUpEvents = findViewById(R.id.show_all_signed_up_events);
        Button organizerEvents = findViewById(R.id.organizer_events);
        Switch geoLocation = findViewById(R.id.geo_location_switch);


        // Retrieve the user details passed from MainActivity
        User user = (User) getIntent().getSerializableExtra("user");

        // Set click listeners for each button
        showAllEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ShowAllEventsActivity
                Intent intent = new Intent(HomepageActivity.this, ShowAllEvents.class);
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



    }
}
