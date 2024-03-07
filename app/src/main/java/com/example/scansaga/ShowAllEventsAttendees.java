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
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ShowAllEventsAttendees extends AppCompatActivity {
    private FirebaseFirestore db;

    // Initialize Firebase Storage
    private FirebaseStorage storage;
    private CollectionReference eventsRef;
    private Button delete;
    private ListView listView;
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

        // Fetch users from Firestore
        fetchEventsFromFirestore();

    }

    // Method to fetch users from Firestore
    private void fetchEventsFromFirestore() {
        eventsRef.addSnapshotListener((querySnapshots, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                return;
            }
            if (querySnapshots != null) {
                eventList.clear();
                for (QueryDocumentSnapshot doc : querySnapshots) {
                    String name = doc.getString("Name"); // Assuming the document ID is the name; adjust if needed
                    String date = doc.getString("Date");
                    String venue = doc.getString("Venue");
                    String qrImageUrl = doc.getString("qr");
                    String ImageUrl = doc.getString("imageUrl"); // Replace "PosterUrl" with the correct field name
                    Log.d("Firestore", String.format("Event(%s, %s) fetched", name, date));
                    eventList.add(new Event(name, date, venue,ImageUrl)); // Pass null for qrCodeBitmap as we're dealing with URLs now
                }
                eventAdapter.notifyDataSetChanged();
            }
        });
    }

}


