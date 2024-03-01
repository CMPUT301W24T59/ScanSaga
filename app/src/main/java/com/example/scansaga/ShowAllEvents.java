package com.example.scansaga;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * This activity displays a list of events fetched from Firestore.
 * Events can be deleted from the list and Firestore database.
 */
public class ShowAllEvents extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    private ListView listView;
    private Button delete;
    private EventArrayAdapter eventAdapter;
    private ArrayList<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_events);

        listView = findViewById(R.id.listView);
        eventList = new ArrayList<>();
        delete = findViewById(R.id.button_delete);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        eventAdapter = new EventArrayAdapter(this, eventList);
        listView.setAdapter(eventAdapter);

        // Fetch events from Firestore
        fetchEventsFromFirestore();

        // Set item click listener for ListView
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected event based on the position clicked
            Event selectedEvent = eventList.get(position);
            if (selectedEvent != null) {
                delete.setOnClickListener(v -> {
                    deleteEventFromFirestore(selectedEvent);
                    eventAdapter.notifyDataSetChanged();
                });
            }
        });
    }

    /**
     * Fetches events from Firestore and updates the event list.
     */
    private void fetchEventsFromFirestore() {
        eventsRef.addSnapshotListener((querySnapshots, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                return;
            }
            if (querySnapshots != null) {
                eventList.clear();
                for (QueryDocumentSnapshot doc : querySnapshots) {
                    String name = doc.getId();
                    String date = doc.getString("Date");
                    String venue = doc.getString("Venue");
                    Bitmap qr = (Bitmap) doc.get("QR");
                    Log.d("Firestore", String.format("Event(%s, %s) fetched", name, date));
                    eventList.add(new Event(name, date, venue, qr));
                }
                eventAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Deletes an event from Firestore and updates the event list.
     * @param event The event to be deleted.
     */
    private void deleteEventFromFirestore(Event event) {
        eventsRef.document(event.getName())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Event deleted successfully!");
                    // Remove the deleted event from the eventList
                    eventList.remove(event);
                    // Notify the adapter of the dataset change
                    eventAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error deleting event", e));
    }
}
