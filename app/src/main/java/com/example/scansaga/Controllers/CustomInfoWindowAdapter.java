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

/**
 * An implementation of the {@link GoogleMap.InfoWindowAdapter} interface for customizing the
 * contents of info windows on a Google Map.
 *
 * This class is responsible for providing a custom layout for info windows that appear when markers
 * on the map are tapped. The layout includes text views for displaying the first and last names
 * associated with each marker.
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private final Context mContext;

    /**
     * Constructs a new CustomInfoWindowAdapter with the specified context.
     *
     * @param context The context used to inflate the custom info window layout.
     */
    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    /**
     * Renders the text within the custom info window based on the marker's details.
     * This method extracts the first and last names from the marker's title and sets them
     * in the respective TextViews within the custom info window layout.
     *
     * @param marker The marker for which the info window is being populated.
     * @param view The custom view used for the info window.
     */
    private void renderWindowText(Marker marker, View view) {
        /**
         * Provides the custom view to be used as the entire info window.
         * This method is called by the Google Maps API to obtain the view that should be
         * used as the entire info window. The method calls {@link #renderWindowText(Marker, View)}
         * to populate the view's contents based on the marker's information.
         *
         * @param marker The marker for which the info window is being created.
         * @return The custom view to be used as the entire info window.
         */
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
        /**
         * Provides the custom view to be used for the contents of the info window.
         * This method is called by the Google Maps API to obtain the view that should be
         * used for the contents of the info window. It delegates to {@link #renderWindowText(Marker, View)}
         * to populate the view's contents. This implementation returns the same view as
         * {@link #getInfoWindow(Marker)}, meaning the entire info window is custom.
         *
         * @param marker The marker for which the info window contents are being created.
         * @return The custom view to be used for the contents of the info window.
         */
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}