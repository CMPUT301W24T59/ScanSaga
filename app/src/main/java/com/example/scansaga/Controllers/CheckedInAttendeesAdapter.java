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

public class CheckedInAttendeesAdapter extends RecyclerView.Adapter<CheckedInAttendeesAdapter.ViewHolder> {

    private ArrayList<User> attendeesList;
    private LayoutInflater inflater;
    private Context context;

    // Constructor
    public CheckedInAttendeesAdapter(Context context, ArrayList<User> attendeesList) {
        this.inflater = LayoutInflater.from(context);
        this.attendeesList = attendeesList;
        this.context = context; // Initialization of the context variable
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView firstnameTextView;
        public TextView lastnameTextView;
        public ImageView profileDisplay;

        public ViewHolder(View itemView) {
            super(itemView);
            firstnameTextView = itemView.findViewById(R.id.textview_firstname);
            lastnameTextView = itemView.findViewById(R.id.textview_lastname);
            profileDisplay = itemView.findViewById(R.id.profile_image_view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.checked_in_attendee_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User attendee = attendeesList.get(position);
        holder.firstnameTextView.setText(attendee.getFirstname());
        holder.lastnameTextView.setText(attendee.getLastname());

        if (attendee.getProfileImageUrl() != null && !attendee.getProfileImageUrl().isEmpty()) {
            Glide.with(holder.profileDisplay.getContext())
                    .load(attendee.getProfileImageUrl())
                    .into(holder.profileDisplay);
        } else {
            // Load default profile picture based on the first letter of the first name
            generateUniqueProfilePicture(attendee.getFirstname(), attendee.getLastname(), holder.profileDisplay);
        }
    }

    @Override
    public int getItemCount() {
        return attendeesList.size();
    }

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

