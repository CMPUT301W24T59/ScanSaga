package com.example.scansaga.Controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scansaga.R;
import com.example.scansaga.Views.AddEvent;
import com.example.scansaga.Views.AddEventFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * An adapter for managing and displaying a list of QR code images within a RecyclerView.
 * This adapter is responsible for handling QR code images, allowing users to select
 * and use these images in other parts of the application.
 */
public class QRImageAdapter extends RecyclerView.Adapter<QRImageAdapter.ViewHolder> {

    private ArrayList<Bitmap> imageList;
    private Context context;

    /**
     * Constructs a QRImageAdapter with a specified list of QR code images and a context.
     *
     * @param imageList The list of QR code images to be managed by the adapter.
     * @param context The context in which the adapter operates, used for inflating
     *                layouts and starting new activities.
     */
    public QRImageAdapter(ArrayList<Bitmap> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    /**
     * Inflates the layout for each item in the RecyclerView and returns a ViewHolder.
     *
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View, used for view recycling.
     * @return A new instance of the ViewHolder class, containing the inflated view.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.old_qrs, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds QR code images to the view holders, setting the image for each ImageView in the list.
     *
     * @param holder The ViewHolder which should be updated to represent the contents
     *               of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bitmap = imageList.get(position);
        holder.imageView.setImageBitmap(bitmap);

        // Save the bitmap to the holder
        holder.bitmap = bitmap;
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The size of the imageList, representing the number of QR code images.
     */
    @Override
    public int getItemCount() {
        return imageList.size();
    }

    /**
     * A ViewHolder class for the QRImageAdapter, containing an ImageView for displaying
     * QR code images and a button for selecting an image to use.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        Button useButton;
        Bitmap bitmap;

        /**
         * Constructs a ViewHolder, initializing the ImageView and useButton,
         * and setting an OnClickListener for the button.
         *
         * @param itemView The view of the RecyclerView item.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item);
            useButton = itemView.findViewById(R.id.use_button);

            // Set OnClickListener for useButton inside the constructor
            useButton.setOnClickListener(v -> {
                // Convert the bitmap to a byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // Start an activity for result and pass the bitmap byte array
                Intent intent = new Intent(context, AddEvent.class);
                intent.putExtra("bitmapByteArray", byteArray);
                ((Activity) context).startActivityForResult(intent, 1);
            });
        }
    }
}
