package com.example.scansaga;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scansaga.AddEventFragment;
import com.example.scansaga.EventArrayAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.net.InternetDomainName;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomepageActivity extends AppCompatActivity implements AddEventFragment.AddEventDialogListener {

    private ArrayList<Event> dataList;
    private EventArrayAdapter eventAdapter;
    ListView listView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Reference to the collection where you want to store events


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        // Initialize dataList
        dataList = new ArrayList<>();

        // Initialize the ListView
        listView = findViewById(R.id.list);

        // Initialize and set up the ArrayAdapter
        eventAdapter = new EventArrayAdapter(this, dataList);
        listView.setAdapter(eventAdapter);

        // Button to add an event
        FloatingActionButton fab = findViewById(R.id.add_event_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the AddEventFragment when the FloatingActionButton is clicked
                new AddEventFragment().show(getSupportFragmentManager(), "Add Event");
            }
        });
    }

    // Method to add the event to the dataList and update the adapter
    @Override
    public void addEvent(Event event) {
        // Add the new event to the dataList
        dataList.add(event);
        // Notify the adapter that the data set has changed
        eventAdapter.notifyDataSetChanged();
        // Inform the user that the event has been added
        Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show();
    }

    // Method to delete an event
    public void deleteEvent(Event event) {
        // Remove the event from the dataList
        dataList.remove(event);
        // Notify the adapter that the data set has changed
        eventAdapter.notifyDataSetChanged();
    }


























































}