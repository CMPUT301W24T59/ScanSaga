package com.example.scansaga.Views;

import static com.example.scansaga.Model.MainActivity.CHANNEL_ID;
import static com.example.scansaga.Model.MainActivity.NOTIFICATION_PERMISSION_REQUEST_CODE;

import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.scansaga.Model.Event;
import com.example.scansaga.R;

public class sendNotificationActivity extends AppCompatActivity {
    int notificationId;
    private EditText notificationTitle, notificationMessage;
    private Button notificationSendButton;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_notification);

        // Retrieve event data from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("event")) {
            Event event = (Event) intent.getSerializableExtra("event");
        }

        Log.d("button", "HEREEEEEE");

        notificationTitle = findViewById(R.id.notification_title);
        notificationMessage = findViewById(R.id.notification_msg);
        notificationSendButton = findViewById(R.id.notification_send_button);

        // Implement the logic for sending notifications
        notificationSendButton.setOnClickListener(v -> {
            sendNotification();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void sendNotification(){
        notificationId += 1;
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.scansaga_icon)
                .setContentTitle(notificationTitle.getText())
                .setContentText(notificationMessage.getText())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationMessage.getText())
                ).setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();



        // Display the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // Check if the permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it from the user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_REQUEST_CODE);
            return;
        }
        notificationManager.notify(notificationId, notification);
    }
}


