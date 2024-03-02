package com.example.scansaga;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scansaga.AddEventFragment;
import com.example.scansaga.Event;
import com.example.scansaga.EventArrayAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddEvent extends AppCompatActivity implements AddEventFragment.AddEventDialogListener {
    ListView eventList;
    ArrayList<Event> eventDataList;
    EventArrayAdapter eventArrayAdapter;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    @Override
    public void addNewEvent(Event event) {
        eventDataList.add(event);
        eventArrayAdapter.notifyDataSetChanged();
        addEventToFirestore(event);
    }

    @Override
    public void deleteEvent(Event event) {
        eventDataList.remove(event);
        eventArrayAdapter.notifyDataSetChanged();
        deleteEventFromFirestore(event);
    }

    @Override
    public void editEvent(Event event) {
        eventArrayAdapter.notifyDataSetChanged();
    }

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

        eventList.setOnItemClickListener((parent, view, position, id) -> {
            Event selectedEvent = (Event) parent.getItemAtPosition(position);
            if (selectedEvent != null) {
                AddEventFragment editEventFragment = AddEventFragment.newInstance(selectedEvent);
                editEventFragment.show(getSupportFragmentManager(), "Edit Event");
            }
        });

        // Fetch events from Firestore
        fetchEventsFromFirestore();
    }

    // Method to add a new event to Firestore
    private void addEventToFirestore(Event event) {
        // Add the event to the database
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", event.getDate());
        data.put("Venue", event.getVenue());
        // Add more fields as needed

        eventsRef.document(event.getName()).set(data)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Event added successfully!"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error adding event", e));
    }

    // Method to fetch events from Firestore
    private void fetchEventsFromFirestore() {
        eventsRef.addSnapshotListener((querySnapshots, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                return;
            }
            if (querySnapshots != null) {
                eventDataList.clear();
                for (QueryDocumentSnapshot doc : querySnapshots) {
                    String name = doc.getId();
                    String date = doc.getString("Date");
                    String venue = doc.getString("Venue");
                    Bitmap qr = (Bitmap) doc.get("QR");
                    Log.d("Firestore", String.format("Event(%s, %s) fetched", name, date));
                    eventDataList.add(new Event(name, date, venue, qr));
                }
                eventArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    // Method to delete an event from Firestore
    private void deleteEventFromFirestore(Event event) {
        eventsRef.document(event.getName())
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Event deleted successfully!"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error deleting event", e));
    }
}


