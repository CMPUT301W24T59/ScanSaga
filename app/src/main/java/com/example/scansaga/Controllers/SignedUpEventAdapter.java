package com.example.scansaga.Controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

import androidx.annotation.NonNull;

import com.example.scansaga.Model.Event;
import com.example.scansaga.Model.ShowCheckedInAttendeesActivity;
import com.example.scansaga.R;
import com.example.scansaga.Views.SendNotificationActivity;
import com.example.scansaga.Views.ShowSignedUpAttendees;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class SignedUpEventAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Constructor for the EventArrayAdapter.
     *
     * @param context The context of the activity or fragment.
     * @param events  The list of Event objects to be displayed.
     */
    public SignedUpEventAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.events = events;
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_events_as_organizers, parent, false);
        }
        // Lookup view for data population
        TextView eventName = convertView.findViewById(R.id.event_text);
        TextView eventDate = convertView.findViewById(R.id.time_text);
        Button show_attendees = convertView.findViewById(R.id.see_signed_up_attendees);
        Button show_check_ins = convertView.findViewById(R.id.see_checked_in_attendees);
        Button sendNotification = convertView.findViewById(R.id.button_send_notification);


        // Get the data item for this position
        Event event = events.get(position);

        // Populate the data into the template view using the data object
        eventName.setText(event.getName());
        eventDate.setText(event.getDate());

        convertView.setOnLongClickListener(v -> {
            // Show AlertDialog to confirm deletion
            new AlertDialog.Builder(context)
                    .setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete this event?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Perform deletion of the event here
                            String documentName = event.getName() + "_" + event.getDate();
                            db.collection("events").document(documentName)
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        events.remove(position); // Remove the event from the list
                                        notifyDataSetChanged(); // Notify the adapter to refresh the ListView
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle failure
                                    });
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        });

        show_check_ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the event at the clicked position
                Event event = events.get(position);

                // Create an intent to start ShowSignedUpAttendees activity
                Intent intent = new Intent(context, ShowCheckedInAttendeesActivity.class);

                // Pass the event details as extras
                intent.putExtra("extra_event_name_date", event.getName()+"_"+event.getDate());
                Log.d("mapCheck", "event name passed through:"+event.getName() +"_"+event.getDate());

                // Start the activity
                context.startActivity(intent);
            }
        });

        // Set OnClickListener for the "Show Attendees" button
        show_attendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the event at the clicked position
                Event event = events.get(position);

                // Create an intent to start ShowSignedUpAttendees activity
                Intent intent = new Intent(context, ShowSignedUpAttendees.class);

                // Pass the event details as extras
                intent.putExtra("Name", event.getName());
                intent.putExtra("Date", event.getDate());

                // Start the activity
                context.startActivity(intent);
            }
        });

        sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = events.get(position);
                Intent intent = new Intent(context, SendNotificationActivity.class);
                intent.putExtra("Name", event.getName());
                intent.putExtra("Date", event.getDate());
                context.startActivity(intent);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
