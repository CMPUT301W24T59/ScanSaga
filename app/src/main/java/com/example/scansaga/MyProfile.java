package com.example.scansaga;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity displays the user's profile information.
 */
public class MyProfile extends AppCompatActivity {
    private TextView firstNameTextView, lastNameTextView, emailTextView, phoneNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_display);

        // Initialize TextViews
        firstNameTextView = findViewById(R.id.first_name_text_view);
        lastNameTextView = findViewById(R.id.last_name_text_view);
        emailTextView = findViewById(R.id.email_text_view);
        phoneNumberTextView = findViewById(R.id.phone_number_text_view);

        // Retrieve the user object passed from the HomepageActivity
        User user = (User) getIntent().getSerializableExtra("user");

        // Display user information in TextViews
        if (user != null) {
            firstNameTextView.setText(user.getFirstname());
            lastNameTextView.setText(user.getLastname());
            emailTextView.setText(user.getEmail());
            phoneNumberTextView.setText(user.getPhone());
        }
    }
}
