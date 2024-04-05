package com.example.scansaga.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.scansaga.R;

public class CheckinResultPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_result_page);

        // Initializing UI components
        TextView resultText = findViewById(R.id.check_in_result_textview);
        ImageView successImage = findViewById(R.id.check_in_sucess_image); // Corrected the ID
        ImageView failImage = findViewById(R.id.check_in_fail_image); // Corrected the ID

        // Hide both images initially
        successImage.setVisibility(View.GONE);
        failImage.setVisibility(View.GONE);

        // Get extras from intent
        String message = getIntent().getStringExtra("checkInMessage");
        boolean isSuccess = getIntent().getBooleanExtra("checkInSuccess", false);

        // Update UI based on the result
        resultText.setText(message);
        if (isSuccess) {
            successImage.setVisibility(View.VISIBLE);
        } else {
            failImage.setVisibility(View.VISIBLE);
        }

        // Redirect after 5 seconds
        new Handler().postDelayed(() -> {
            Intent redirectIntent;
            if (checkIfUserAdmin()) {
                redirectIntent = new Intent(CheckinResultPage.this, HomepageActivity.class); // Assuming "Homepage" is the admin activity
            } else {
                redirectIntent = new Intent(CheckinResultPage.this, AttendeeHomePage.class); // Assuming "AttendeeHomepage" is the attendee activity
            }
            startActivity(redirectIntent);
            finish();
        }, 3000);
    }

    private boolean checkIfUserAdmin() {
        // Placeholder for the actual checkIfUserAdmin logic
        return true; // Or the actual check logic
    }
}
