package com.example.scansaga;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Adapter class for the RecyclerView to display images.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ArrayList<String> imageList; // List of image URLs
    private Context context; // Context

    /**
     * Constructor to initialize the ImageAdapter.
     *
     * @param imageList List of image URLs
     * @param context   Context
     */
    public ImageAdapter(ArrayList<String> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        String imageUrl = imageList.get(position);
        // Load image into ImageView using Glide library
        Glide.with(context).load(imageUrl).into(holder.imageView);

        // Set click listener for the delete button
        holder.deleteButton.setOnClickListener(v -> {
            // Call the onDeleteClickListener if it's not null
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    /**
     * ViewHolder class to hold the views for each item in the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView; // ImageView for the image
        Button deleteButton; // Button to delete the image

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    // Interface for handling delete button clicks
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    private OnDeleteClickListener onDeleteClickListener;

    /**
     * Method to set the delete button click listener.
     *
     * @param listener Listener for delete button clicks
     */
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        onDeleteClickListener = listener;
    }
}
