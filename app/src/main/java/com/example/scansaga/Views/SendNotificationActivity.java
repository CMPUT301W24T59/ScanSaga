package com.example.scansaga.Views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scansaga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import static com.example.scansaga.Model.MainActivity.notificationID;

import java.util.ArrayList;
public class SendNotificationActivity extends AppCompatActivity {
    private EditText notificationTitle, notificationMessage;
    private Button notificationSendButton;
    String eventName, eventDate, eventDocument;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        // Retrieve event data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eventName = extras.getString("Name");
            eventDate = extras.getString("Date");
        }

        eventDocument = eventName + "_" + eventDate;

        notificationTitle = findViewById(R.id.notification_title);
        notificationMessage = findViewById(R.id.notification_msg);
        notificationSendButton = findViewById(R.id.notification_send_button);

        notificationTitle.setText(eventName);

        // Implement the logic for sending notifications
        notificationSendButton.setOnClickListener(v -> {
            notificationID++;

            db.collection("events").document(eventDocument)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                ArrayList<String> attendeeTokens = (ArrayList<String>) documentSnapshot.get("signedUpAttendeeTokens");
                                if (attendeeTokens != null && !attendeeTokens.isEmpty()) {
                                    for (String token : attendeeTokens) {
                                        if (!notificationTitle.getText().toString().isEmpty() && !notificationMessage.getText().toString().isEmpty()) {
                                            NotificationSender notificationSender = new NotificationSender(token, notificationTitle.getText().toString(), notificationMessage.getText().toString(), getApplicationContext(), SendNotificationActivity.this);
                                            notificationSender.SendNotifications();
                                        }

                                        else {
                                            Toast.makeText(SendNotificationActivity.this, "Enter all the details", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    Toast.makeText(SendNotificationActivity.this, "Notification sent for " + eventName, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SendNotificationActivity.this, AttendeeHomePage.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                } else {
                                    Log.d("TOKEN", "Did not find tokens in SendNotifications");
                                }
                            } else {
                                Log.d("TOKEN", "No such token document in SendNotifications");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TOKEN", "Error getting document in SendNotifications", e);
                        }
                    });
        });
    }
}