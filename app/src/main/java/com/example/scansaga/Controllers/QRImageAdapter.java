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

public class QRImageAdapter extends RecyclerView.Adapter<QRImageAdapter.ViewHolder> {

    private ArrayList<Bitmap> imageList;
    private Context context;

    public QRImageAdapter(ArrayList<Bitmap> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.old_qrs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bitmap = imageList.get(position);
        holder.imageView.setImageBitmap(bitmap);

        // Save the bitmap to the holder
        holder.bitmap = bitmap;
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        Button useButton;
        Bitmap bitmap;

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
