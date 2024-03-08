package com.example.scansaga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * The ShowAllImagesFromStorage class displays all images stored in Firebase Storage.
 */
public class ShowAllImagesFromStorage extends AppCompatActivity {
    ArrayList<String> imagelist;
    RecyclerView recyclerView;
    StorageReference root;
    ProgressBar progressBar;
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showallimages);

        // Initialize variables
        imagelist=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerview);
        adapter=new ImageAdapter(imagelist,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar=findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        // Get references to Firebase Storage locations for events images and profile pictures
        StorageReference listRefEvents = FirebaseStorage.getInstance().getReference().child("events_images");
        StorageReference listRefProfile = FirebaseStorage.getInstance().getReference().child("profile_pictures");

        // Fetch event images from Firebase Storage
        listRefEvents.listAll().addOnSuccessListener(listResult -> {
            for(StorageReference file : listResult.getItems()){
                file.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Add image URL to imagelist
                    imagelist.add(uri.toString());
                    Log.e("Itemvalue",uri.toString());
                }).addOnSuccessListener(uri -> {
                    // Set adapter for RecyclerView and hide progress bar
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                });
            }
        });

        // Fetch profile pictures from Firebase Storage
        listRefProfile.listAll().addOnSuccessListener(listResult -> {
            for(StorageReference file : listResult.getItems()){
                file.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Add image URL to imagelist
                    imagelist.add(uri.toString());
                    Log.e("Itemvalue",uri.toString());
                }).addOnSuccessListener(uri -> {
                    // Set adapter for RecyclerView and hide progress bar
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                });
            }
        });

        // Set delete click listener for images
        adapter.setOnDeleteClickListener(position -> {
            // Get the URL of the image to delete from the list
            String imageUrlToDelete = imagelist.get(position);

            // Remove the image URL from the list
            imagelist.remove(position);

            // Notify the adapter of the data change
            adapter.notifyItemRemoved(position);

            // Get the StorageReference of the image to delete
            StorageReference imageRefToDelete = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrlToDelete);

            // Delete the image from Firebase Storage
            imageRefToDelete.delete().addOnSuccessListener(aVoid -> {
                // Image deleted successfully
                Log.d("ShowAllImagesFromStorage", "Image deleted successfully");
            }).addOnFailureListener(e -> {
                // Failed to delete the image
                Log.e("ShowAllImagesFromStorage", "Error deleting image", e);
            });
        });
    }
}
