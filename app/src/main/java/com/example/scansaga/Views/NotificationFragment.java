package com.example.scansaga.Views;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.scansaga.Model.MainActivity.CHANNEL_ID;
import static com.google.common.reflect.Reflection.getPackageName;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import com.example.scansaga.Model.Event;
import com.example.scansaga.R;

public class NotificationFragment extends DialogFragment {
    private static final int PERMISSION_REQUEST_CODE = 100;
    int notificationId;
    private EditText notificationTitle, notificationMessage;
    private Button notificationSendButton;
    private Event event;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.send_notification, null);
        builder.setView(view);

        notificationTitle = view.findViewById(R.id.notification_title);
        notificationMessage = view.findViewById(R.id.notification_msg);
        notificationSendButton = view.findViewById(R.id.notification_send_button);


        Bundle args = getArguments();
        if (args != null && args.containsKey("event")) {
            event = (Event) args.getSerializable("event");
            if (event != null) {
                if (notificationTitle == null) notificationTitle.setText(event.getName());
            }
        }
        notificationSendButton.setOnClickListener(v -> {
            notificationId += 1;
            Notification notification = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.scansaga_icon)
                    .setContentTitle(notificationTitle.getText())
                    .setContentText(notificationMessage.getText())
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(notificationMessage.getText())
                    ).setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();



            // Display the notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
            // Check if the permission is granted
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request it from the user
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
                return;
            }
            notificationManager.notify(notificationId, notification);


            // Set the intent that fires when the user taps the notification.
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true);
        });
    }
}
