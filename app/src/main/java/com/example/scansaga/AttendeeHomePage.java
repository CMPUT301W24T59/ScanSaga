package com.example.scansaga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for the homepage of the attendee.
 */
public class AttendeeHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_homepage);

        // Initialize buttons
        Button signUpEventButton = findViewById(R.id.sign_up_event_button);
        Button scanAndGoButton = findViewById(R.id.scan_and_attend_button);
        Button editProfileButton = findViewById(R.id.edit_profile_button);
        Button showAllEventsButton = findViewById(R.id.show_all_events_button);
        Button addEventButton = findViewById(R.id.add_event_button);

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

        // Set click listener for add event button
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddEventActivity
                Intent intent = new Intent(AttendeeHomePage.this, AddEvent.class);
                startActivity(intent);
            }
        });
    }
}
