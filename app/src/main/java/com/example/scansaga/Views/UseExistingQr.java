package com.example.scansaga.Views;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.scansaga.Controllers.QRImageAdapter;
import com.example.scansaga.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UseExistingQr extends AppCompatActivity {
    ArrayList<Bitmap> imageList;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    QRImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showallimages);

        // Initialize variables
        imageList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new QRImageAdapter(imageList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        // Get reference to Firebase Storage location for QR codes
        StorageReference listRefQrCodes = FirebaseStorage.getInstance().getReference().child("old_qr_codes");

        // Fetch QR codes images from Firebase Storage
        listRefQrCodes.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference file : listResult.getItems()) {
                file.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Load image URL into Glide to get Bitmap
                    Glide.with(UseExistingQr.this)
                            .asBitmap()
                            .load(uri)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                    // Add the bitmap to the list
                                    imageList.add(bitmap);
                                    // Notify adapter of data change
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    // Placeholder cleanup
                                }
                            });
                }).addOnFailureListener(exception -> {
                    // Handle any failure in downloading the image
                    Log.e("UseExistingQr", "Failed to download image: " + exception.getMessage());
                }).addOnCompleteListener(task -> {
                    // Set adapter for RecyclerView and hide progress bar when all images are loaded
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                });
            }
        });
    }
}
