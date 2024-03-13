package com.example.scansaga.Views;
import android.graphics.Bitmap;
import android.os.Bundle;
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
 * Activity responsible for displaying a list of events and providing functionality
 * to add new events.  Integrates with the AddEventFragment to handle event creation.
 */
public class AddEvent extends AppCompatActivity implements AddEventFragment.AddEventDialogListener {
    ListView eventList;
    ArrayList<Event> eventDataList;
    EventArrayAdapter eventArrayAdapter;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    /**
     *  Callback method triggered when a new event is to be added from the AddEventFragment.
     *  Adds the new event to the list and updates the UI.
     *
     * @param event The new Event object to be added to the list
     */

    @Override
    public void addNewEvent(Event event) {
        eventDataList.add(event);
        eventArrayAdapter.notifyDataSetChanged();
        addEventToFirestore(event);
    }

    /**
     *  Callback method triggered when an event is deleted from the AddEventFragment.
     *  Removes the event from the list and updates the UI.
     *
     * @param event The Event object to be deleted from the list.
     */
    @Override
    public void deleteEvent(Event event) {
        eventDataList.remove(event);
        eventArrayAdapter.notifyDataSetChanged();
    }
    /**
     * Callback method triggered when an event is edited from the AddEventFragment.
     * Updates the UI to reflect changes to the edited event.
     *
     * @param event The edited Event object.
     */

    @Override
    public void editEvent(Event event) {
        eventArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Called when the activity is created. Initializes UI elements, sets up Firestore
     * references, creates the adapter for the event list, and sets an OnClickListener
     * for the FloatingActionButton to trigger the AddEventFragment.
     *
     * @param savedInstanceState  If the activity is being re-initialized after previously
     *                            being shut down then this Bundle contains the data it most
     *                            recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        eventList = findViewById(R.id.event_list);
        eventDataList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        eventArrayAdapter = new EventArrayAdapter(this, eventDataList);
        eventList.setAdapter(eventArrayAdapter);

        FloatingActionButton fab = findViewById(R.id.add_event_button);
        fab.setOnClickListener(v -> new AddEventFragment().show(getSupportFragmentManager(), "Add Event"));

    }

    // Method to add a new event to Firestore
    private void addEventToFirestore(Event event) {
        // Add the event to the database
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", event.getDate());
        data.put("Venue", event.getVenue());
        data.put("Name", event.getName());
        //data.put("QR", event.getQrCodeBitmap().toString());
        // Add more fields as needed

        eventsRef.document(event.getName()).set(data)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Event added successfully!"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error adding event", e));
    }

}
