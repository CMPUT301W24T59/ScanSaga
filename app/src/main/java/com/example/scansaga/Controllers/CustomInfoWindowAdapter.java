package com.example.scansaga.Controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scansaga.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private final Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void renderWindowText(Marker marker, View view) {
        String firstName = ""; // Get first name from marker's tag or another data structure
        String lastName = ""; // Get last name from marker's tag or another data structure

        TextView textViewFirstName = view.findViewById(R.id.custom_first_name);
        TextView textViewLastName = view.findViewById(R.id.custom_last_name);

        // Assuming you set the user's first name and last name as a single string to the marker title
        String title = marker.getTitle();
        String[] nameParts = title.split("\\s+", 2);
        if (nameParts.length > 0) {
            firstName = nameParts[0];
            if (nameParts.length > 1) {
                lastName = nameParts[1];
            }
        }

        textViewFirstName.setText(firstName);
        textViewLastName.setText(lastName);

    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}


