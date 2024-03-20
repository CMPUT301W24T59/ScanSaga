package com.example.scansaga.Views;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
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

public class MySignedUpEvents extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private String deviceId;
    private CollectionReference eventsRef;
    private ListView listView;
    private EventArrayAdapter eventAdapter;
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
        setContentView(R.layout.show_signedup_events);
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        listView = findViewById(R.id.listView);
        eventList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        eventAdapter = new EventArrayAdapter(this, eventList);
        listView.setAdapter(eventAdapter);

        // Fetch users from Firestore
        Log.d("", "Calling Download");
        DownloadEventFromFirestore();
        fetchEventsFromFirestore();

    }

    /**
     * Fetches all events from Firestore and populates the ListView.
     */

    @SuppressLint("RestrictedApi")
    private void fetchEventsFromFirestore() {
        eventsRef.whereArrayContains("signedUpAttendees", deviceId)
                .addSnapshotListener((querySnapshots, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Firestore error: ", error);
                        return;
                    }
                    if (querySnapshots != null) {
                        eventList.clear();
                        for (QueryDocumentSnapshot doc : querySnapshots) {
                            String name = doc.getString("Name"); // Assuming the document ID is the event name
                            String date = doc.getString("Date");
                            String venue = doc.getString("Venue");
                            String qrCodeUrl = doc.getString("QRCodeUrl"); // Adjust the field name as in your Firestore
                            //Bitmap qr = null;
                            String imageUrl = doc.getString("imageUrl"); // Adjust the field name as in your Firestore
                            Log.d("FirestoreData", "ImageUrl: " + imageUrl);
                            if (imageUrl != null) {
                                eventList.add(new Event(name, date, venue, imageUrl));
                            } else {
                                Log.d("FirestoreData", "Missing imageUrl for event: " + name);
                            }
                        }

                        Log.d("", "EVENT LISTS" + eventList);
                        eventAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void DownloadEventFromFirestore() {

        Log.d("CALL", "TESTINGGG");
        ImageView imageView = findViewById(R.id.poster_image);

        // Initialize Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Reference to your image file in Firebase Storage
        StorageReference imageRef = storage.getReference("events_images").child("https://firebasestorage.googleapis.com/v0/b/lab5-8b633.appspot.com/o/events_images%2F6aabefb8-a71d-4200-85a4-587f3105ef9e?alt=media&token=309840a2-29cb-43c9-b9cf-04ed8664e057");

        // Use Glide to download and display the image
        Log.d("Before Glide", "Before Glide");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL, now use Glide to display the image
                Glide.with(MySignedUpEvents.this)
                        .load(imageRef)
                        .into(imageView);
                Log.d("IMAGESSSSSS", "IMAGESSSS" + imageRef);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}