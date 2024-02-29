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

public class EventArrayAdapter extends ArrayAdapter<Event> {
    public EventArrayAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
    }
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View
    getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.content,
                    parent, false);
        } else {
            view = convertView;
        }
        Event event = getItem(position);
        if (event != null) {
            TextView eventNameTextView = view.findViewById(R.id.event_text);
            TextView eventYearTextView = view.findViewById(R.id.year_text);
            ImageView qrCodeImageView = view.findViewById(R.id.qr_code_image);

            eventNameTextView.setText("Event Name: " + event.getName());
            eventYearTextView.setText("Time: " + event.getDate());
            qrCodeImageView.setImageBitmap(event.getQrCodeBitmap());
        }

        return view;
    }

}


