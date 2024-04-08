package com.example.scansaga.Controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.scansaga.Model.Event;
import com.example.scansaga.R;

import java.util.ArrayList;

/**
 * Custom ArrayAdapter for displaying Event objects in a ListView.
 */
public class EventArrayAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;

    /**
     * Constructor for the EventArrayAdapter.
     *
     * @param context The context of the activity or fragment.
     * @param events  The list of Event objects to be displayed.
     */
    public EventArrayAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.events = events;
        this.context = context;
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content, parent, false);
        }
        // Lookup view for data population
        TextView eventName = convertView.findViewById(R.id.event_text);
        TextView eventDate = convertView.findViewById(R.id.time_text);
        TextView eventVenue = convertView.findViewById(R.id.venue_text);
        ImageView eventImage = convertView.findViewById(R.id.poster_image); // Your ImageView in the layout
        ImageView qrCode = convertView.findViewById(R.id.qr_code_image);

        // Get the data item for this position
        Event event = getItem(position);

        // Populate the data into the template view using the data object
        eventName.setText(event.getName());
        eventDate.setText(event.getDate());
        eventVenue.setText(event.getVenue());

        // Use Glide to load the event image
        Glide.with(getContext())
                .load(event.getImageUrl())
                .into(eventImage);

        // Use Glide to load the event image
        Glide.with(getContext())
                .load(event.getQrUrl())
                .into(qrCode);
        Log.d("EventArrayAdapter", "Loading image from URL: " + event.getImageUrl());

        // Return the completed view to render on screen
        return convertView;
    }
}