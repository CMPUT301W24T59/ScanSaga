package com.example.scansaga.Controllers;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.scansaga.Model.User;
import com.example.scansaga.R;

import java.util.ArrayList;

/**
 * Adapter for displaying a list of checked-in attendees in a RecyclerView.
 * This adapter manages the data set of attendees who have checked in and prepares the views
 * for displaying each attendee's information including their first name, last name,
 * checked-in count, and a unique profile picture.
 */
public class CheckedInAttendeesAdapter extends RecyclerView.Adapter<CheckedInAttendeesAdapter.ViewHolder> {

    private ArrayList<User> attendeesList;
    private LayoutInflater inflater;
    private Context context;

    // Constructor
    /**
     * Constructs a CheckedInAttendeesAdapter with a specified context and a list of attendees.
     *
     * @param context       The Context the adapter is running in, through which it can
     *                      access the current theme, resources, etc.
     * @param attendeesList The list of attendees to be displayed.
     */
    public CheckedInAttendeesAdapter(Context context, ArrayList<User> attendeesList) {
        this.inflater = LayoutInflater.from(context);
        this.attendeesList = attendeesList;
        this.context = context; // Initialization of the context variable
    }

    // ViewHolder class
    /**
     * ViewHolder class that contains the UI components to display information of an attendee.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView firstnameTextView;
        public TextView lastnameTextView;
        public TextView checkedInTitle;
        public TextView checkedInCount;
        public ImageView profileDisplay;

        public ViewHolder(View itemView) {
            super(itemView);
            firstnameTextView = itemView.findViewById(R.id.textview_firstname);
            lastnameTextView = itemView.findViewById(R.id.textview_lastname);
            profileDisplay = itemView.findViewById(R.id.checked_in_profile_image);
            checkedInTitle = itemView.findViewById(R.id.checked_in_count_title);
            checkedInCount = itemView.findViewById(R.id.check_in_count);
        }
    }

    /**
     * Inflates the item layout and creates a ViewHolder for the item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.checked_in_attendee_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the data at the specified position in the data set to the specified ViewHolder.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *               item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User attendee = attendeesList.get(position);
        holder.firstnameTextView.setText(attendee.getFirstname());
        holder.lastnameTextView.setText(attendee.getLastname());
        if (attendee.getCheckedInCount()=="0"){attendee.setCount("1");}
        holder.checkedInCount.setText(attendee.getCheckedInCount());

        // Always generate a unique profile picture
        generateUniqueProfilePicture(attendee.getFirstname(), attendee.getLastname(), holder.profileDisplay);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return attendeesList.size();
    }

    /**
     * Generates and sets a unique profile picture for the attendee based on their first and last name.
     * If a matching image resource is found, it is set as the profile picture. Otherwise, a default
     * image is set. Additionally, it sets a background color generated from the hash of the attendee's
     * combined first and last name.
     *
     * @param firstName The first name of the attendee.
     * @param lastName The last name of the attendee.
     * @param profileImageView The ImageView where the profile picture will be set.
     */
    private void generateUniqueProfilePicture(String firstName, String lastName, ImageView profileImageView){
        // Extract the first letter of the user's first name and convert it to lowercase
        char firstLetter = Character.toLowerCase(firstName.charAt(0));

        // drawable resource name
        String resourceName = firstLetter + "";

        // Get the Android resource ID by name, type, and package
        int resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());

        if (resourceId != 0) {
            // If the resource was found, set it as the icon for the ImageView
            profileImageView.setImageResource(resourceId);
        } else {
            // Handle the case where the resource was not found
            // Use Glide to set a default image
            Glide.with(context)
                    .load(R.drawable.profile_icon_black) // This drawable should exist in your resources
                    .into(profileImageView);
            Log.e("ImageViewSetup", "Resource not found for letter: " + firstLetter);
        }

        // Combine user attributes into a single string and hash it
        String combinedAttributes = firstName + lastName;
        int hash = combinedAttributes.hashCode();

        // Generate a color from the hash code ensuring the alpha channel is set to max
        int color = 0xFF000000 | (hash & 0x00FFFFFF); // This ensures the alpha channel is set to max

        // Set the generated color as the background of the ImageView
        // Note: This will set a color background, it will not affect the image set above.
        // If you want to have a colored drawable, you'll need to create it programmatically.
        profileImageView.setBackgroundColor(color);
    }
}

