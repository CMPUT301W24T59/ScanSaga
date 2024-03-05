package com.example.scansaga;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Custom ArrayAdapter for displaying Event objects in a ListView.
 */
public class EventArrayAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;

    /**
     * Constructor for the EventArrayAdapter.
     * @param context The context of the activity or fragment.
     * @param events The list of Event objects to be displayed.
     */
    public EventArrayAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.events = events;
        this.context = context;
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.content, parent, false);
        }

        Event event = events.get(position);

        TextView eventNameTextView = view.findViewById(R.id.event_text);
        TextView eventTimeTextView = view.findViewById(R.id.time_text);
        TextView eventVenueTextView = view.findViewById(R.id.venue_text);
        ImageView qrCodeImageView = view.findViewById(R.id.qr_code_image);

        eventNameTextView.setText(event.getName());
        eventTimeTextView.setText(event.getDate());
        eventVenueTextView.setText(event.getVenue());
        qrCodeImageView.setImageBitmap(event.getQrCodeBitmap());

        return view;
    }
}
