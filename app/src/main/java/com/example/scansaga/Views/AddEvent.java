package com.example.scansaga.Views;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scansaga.Controllers.EventArrayAdapter;
import com.example.scansaga.Model.Event;
import com.example.scansaga.R;
import com.example.scansaga.Views.AddEventFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;


/**
 * An activity class that facilitates adding, editing, and deleting events.
 * It implements the AddEventFragment.AddEventDialogListener to handle dialog interactions
 * for creating or modifying events. This class uses a ListView to display a list of events
 * and a FloatingActionButton for adding new events.
 */
public class AddEvent extends AppCompatActivity implements AddEventFragment.AddEventDialogListener {
    ListView eventList;
    ArrayList<Event> eventDataList;
    EventArrayAdapter eventArrayAdapter;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    public static String deviceId;


    /**
     * Adds a new event to the list and updates the adapter to reflect the change in the dataset.
     * This method is called when a new event is created through the AddEventFragment dialog.
     *
     * @param event The new event to add.
     */
    @Override
    public void addNewEvent(Event event) {
        eventDataList.add(event);
        eventArrayAdapter.notifyDataSetChanged();
    }


    /**
     * Removes an event from the list and updates the adapter to reflect the change in the dataset.
     * This method is called when an event is selected for deletion.
     *
     * @param event The event to delete.
     */
    @Override
    public void deleteEvent(Event event) {
        eventDataList.remove(event);
        eventArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Updates the event list to reflect any changes. This method is called after an event is edited.
     *
     * @param event The event that was edited.
     */
    @Override
    public void editEvent(Event event) {
        eventArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Sets up the activity's user interface and event handling. This method initializes the ListView,
     * FloatingActionButton, and other components essential for the activity's functionality.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);


        // Get the device ID
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        FloatingActionButton fab = findViewById(R.id.add_event_button);
        new AddEventFragment().show(getSupportFragmentManager(), "Add Event");
        fab.setOnClickListener(v -> new AddEventFragment().show(getSupportFragmentManager(), "Add Event"));

    }

}
