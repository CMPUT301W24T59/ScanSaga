package com.example.scansaga.Views;

import static com.example.scansaga.Model.MainActivity.isUserAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.scansaga.R;

/**
 * An activity that displays the result of a check-in attempt to the user.
 * It shows a success or failure message along with an image indicating the result,
 * and then redirects the user to the appropriate home page based on their role
 * (admin or attendee) after a short delay.
 */
public class CheckinResultPage extends AppCompatActivity {

    /**
     * Initializes the activity's UI, sets up the result message and image based on the check-in attempt,
     * and schedules a redirection to the next appropriate activity based on the user's role.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
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
            if (isUserAdmin) {
                redirectIntent = new Intent(CheckinResultPage.this, HomepageActivity.class);
            } else {
                redirectIntent = new Intent(CheckinResultPage.this, AttendeeHomePage.class);
            }
            startActivity(redirectIntent);
            finish();
        }, 3000);
    }

}
