package com.example.scansaga;
import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scansaga.EventArrayAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ShowAllEvents extends AppCompatActivity {
    private FirebaseFirestore db;

    // Initialize Firebase Storage
    private FirebaseStorage storage;
    private CollectionReference eventsRef;
    private Button delete;
    private ListView listView;
    private EventArrayAdapter eventAdapter;
    ArrayList<Event> eventList;

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

        // Fetch users from Firestore
        fetchEventsFromFirestore();

        // Set item click listener for ListView
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected user based on the position clicked
            Event selectedEvent = eventList.get(position);
            if (selectedEvent != null) {
                delete.setOnClickListener(v -> {
                    deleteEventFromFirestore(selectedEvent);
                    eventAdapter.notifyDataSetChanged();

                });
            }
        });
    }

    // Method to fetch users from Firestore
    void fetchEventsFromFirestore() {
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

    private void deleteEventFromFirestore(Event event) {
        eventsRef.document(event.getName())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Event deleted successfully!");
                    // Remove the deleted user from the userList
                    eventList.remove(event);
                    // Notify the adapter of the dataset change
                    eventAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error deleting event", e));
    }
}

