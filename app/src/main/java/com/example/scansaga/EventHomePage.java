package com.example.scansaga;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity displays the homepage of a specific event, including the event name and its QR code.
 */
public class EventHomePage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_homepage);

        // Retrieve event name and QR code bitmap from Intent extras
        String eventName = getIntent().getStringExtra("eventName");
        Bitmap qrCodeBitmap = getIntent().getParcelableExtra("qrCodeBitmap");

        // Check if eventName and qrCodeBitmap are not null before using them
        if (eventName != null && qrCodeBitmap != null) {
            // Set event name to TextView
            TextView eventNameTextView = findViewById(R.id.event_name_text_view);
            eventNameTextView.setText("Welcome to the homepage of " + eventName);

            // Display QR code bitmap in ImageView
            ImageView qrCodeImageView = findViewById(R.id.qr_code_image_view);
            qrCodeImageView.setImageBitmap(qrCodeBitmap);
        } else {
            // Handle the case where either eventName or qrCodeBitmap is null
            // For example, you could display a message or log an error
            Toast.makeText(this, "Event details are missing", Toast.LENGTH_SHORT).show();
            Log.e("EventHomePage", "Event details are missing");
            // Finish the activity to prevent further execution
            finish();
        }
    }
}
