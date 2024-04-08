package com.example.scansaga.Controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.scansaga.Model.Event;
import com.example.scansaga.Model.ShowCheckedInAttendeesActivity;
import com.example.scansaga.R;
import com.example.scansaga.Views.SendNotificationActivity;
import com.example.scansaga.Views.ShowSignedUpAttendees;

import java.util.ArrayList;

public class SignedUpEventAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;

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
        Button sendNotification = convertView.findViewById(R.id.button_send_notification);
        Button show_check_ins = convertView.findViewById(R.id.see_checked_in_attendees);


        // Get the data item for this position
        Event event = events.get(position);

        // Populate the data into the template view using the data object
        eventName.setText(event.getName());
        eventDate.setText(event.getDate());

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
        // Set OnClickListener for the send_notification button
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
