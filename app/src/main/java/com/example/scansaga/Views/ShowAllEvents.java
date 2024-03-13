package com.example.scansaga.Views;
import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

public class ShowAllEvents extends AppCompatActivity {
    private FirebaseFirestore db;

    // Initialize Firebase Storage
    private FirebaseStorage storage;
    private CollectionReference eventsRef;
    private Button delete;
    private ListView listView;
    private EventArrayAdapter eventAdapter;
    private ArrayList<Event> eventList;

    /**
     * Called when the activity is created. Initializes UI elements, sets up Firestore
     * references, creates the adapter for the event list, and sets listeners for
     * fetching data.
     *
     * @param savedInstanceState  If the activity is being re-initialized after previously
     *                            being shut down then this Bundle contains the data it most
     *                            recently supplied in onSaveInstanceState(Bundle).
     */
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
        Log.d("", "Calling Download");
        DownloadEventFromFirestore();
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
    /**
     *  Fetches all events from Firestore and populates the ListView.
     */

    // Method to fetch users from Firestore
    @SuppressLint("RestrictedApi")
    private void fetchEventsFromFirestore() {
        eventsRef.addSnapshotListener((querySnapshots, error) -> {
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

    /**
     * Deletes the selected event from Firestore.
     *
     * @param event The Event object to be deleted.
     */
    private void deleteEventFromFirestore(Event event) {
        eventsRef.document(event.getName() + "_" + event.getDate())
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

    /**
     * Downloads an image from Firebase Storage and displays it in an ImageView.
     * Please note: This function has potential issues, as it seems to be downloading
     * a hardcoded image reference instead of being tied to the events displayed
     * in the ListView.
     */

    private void DownloadEventFromFirestore() {

        Log.d("CALL", "TESTINGGG");
        ImageView imageView = findViewById(R.id.poster_image);

        // Initialize Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Reference to your image file in Firebase Storage
        StorageReference imageRef = storage.getReference("events_images").child("https://firebasestorage.googleapis.com/v0/b/lab5-8b633.appspot.com/o/events_images%2F6aabefb8-a71d-4200-85a4-587f3105ef9e?alt=media&token=309840a2-29cb-43c9-b9cf-04ed8664e057");

        // Use Glide to download and display the image
        Log.d("Before Glide",  "Before Glide");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL, now use Glide to display the image
                Glide.with(ShowAllEvents.this)
                        .load(imageRef)
                        .into(imageView);
                Log.d("IMAGESSSSSS", "IMAGESSSS" +  imageRef);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}


