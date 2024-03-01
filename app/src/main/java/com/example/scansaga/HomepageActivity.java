package com.example.scansaga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        // Initialize buttons
        Button showAllEventsButton = findViewById(R.id.show_all_events_button);
        Button signUpEventButton = findViewById(R.id.sign_up_event_button);
        Button scanAndGoButton = findViewById(R.id.scan_and_attend_button);
        Button editProfileButton = findViewById(R.id.edit_profile_button);
        Button addEventButton = findViewById(R.id.add_event_button);

        // Set click listeners for each button
        showAllEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ShowAllEventsActivity
                Intent intent = new Intent(HomepageActivity.this, ShowAllEvents.class);
                startActivity(intent);
            }
        });

        signUpEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SignUpEventActivity
                Intent intent = new Intent(HomepageActivity.this, EventSignUp.class);
                startActivity(intent);
            }
        });

        scanAndGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ScanAndAttendActivity
                Intent intent = new Intent(HomepageActivity.this, ScanAndGo.class);
                startActivity(intent);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start EditProfileActivity
                Intent intent = new Intent(HomepageActivity.this, MyProfile.class);
                startActivity(intent);
            }
        });

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddEventActivity
                Intent intent = new Intent(HomepageActivity.this, AddEvent.class);
                startActivity(intent);
            }
        });
    }
}
