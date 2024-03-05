package com.example.scansaga;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowAllEvents extends AppCompatActivity {

    private ListView eventsListView;
    private EventArrayAdapter eventAdapter;
    private ArrayList<Event> eventList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_events);

        eventsListView = findViewById(R.id.event_list);
        eventList = new ArrayList<>(); // Populate this list with events

        // Initialize the custom adapter and set it to the ListView
        eventAdapter = new EventArrayAdapter(this, eventList);
        eventsListView.setAdapter(eventAdapter);

        // Fetch events from database
        fetchEventsFromDB();
    }

    private void fetchEventsFromDB() {
        db.collection("events")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Convert Firestore document to Event object
                            Event event = documentSnapshot.toObject(Event.class);
                            // Add event to the list
                            eventList.add(event);
                        }
                        // Notify the adapter that data set has changed
                        eventAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowAllEvents.this, "Failed to fetch events: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
