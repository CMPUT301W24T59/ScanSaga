package com.example.scansaga.Views;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.scansaga.Controllers.EventArrayAdapter;
import com.example.scansaga.Controllers.SignedUpEventAdapter;
import com.example.scansaga.Model.Event;
import com.example.scansaga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class OrganizerEvents extends AppCompatActivity{
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private String deviceId;
    private CollectionReference eventsRef;
    private ListView listView;
    private SignedUpEventAdapter eventAdapter;
    private ArrayList<Event> eventList;

    /**
     * Called when the activity is created. Initializes UI elements, sets up Firestore
     * references, creates the adapter for the event list, and sets listeners for
     * fetching data.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events_as_organizers);

        listView = findViewById(R.id.listView);
        eventList = new ArrayList<>();

        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        eventAdapter = new SignedUpEventAdapter(this, eventList);
        listView.setAdapter(eventAdapter);

        // Fetch users from Firestore
        fetchEventsFromFirestore();

    }

    /**
     * Fetches all events from Firestore and populates the ListView.
     */

    @SuppressLint("RestrictedApi")
    private void fetchEventsFromFirestore() {
        eventsRef.whereEqualTo("OrganizerDeviceId", deviceId)
                .addSnapshotListener((querySnapshots, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Firestore error: ", error);
                        return;
                    }
                    if (querySnapshots != null) {
                        eventList.clear();
                        for (QueryDocumentSnapshot doc : querySnapshots) {
                            String name = doc.getString("Name");
                            String date = doc.getString("Date");
                            String venue = doc.getString("Venue");
                            eventList.add(new Event(name, date, venue, null, null));
                        }

                        Log.d(TAG, "EVENT LIST: " + eventList);
                        eventAdapter.notifyDataSetChanged();
                    }
                });
    }

}
