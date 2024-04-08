package com.example.scansaga.Views;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.scansaga.Controllers.EventArrayAdapter;
import com.example.scansaga.Model.Event;
import com.example.scansaga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


/**
 * An activity that displays all events and allows users to sign up as attendees.
 * It fetches event data from Firestore, displays each event with its details,
 * and manages attendee sign-ups, including constraints like sign-up limits.
 */
public class ShowAllEventsAttendees extends AppCompatActivity {
    private FirebaseFirestore db;

    // Initialize Firebase Storage
    private FirebaseStorage storage;
    private String deviceId;
    private CollectionReference eventsRef;
    private Button signup;
    private ListView listView;
    private EventArrayAdapter eventAdapter;
    private ArrayList<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_events_attendees);

        listView = findViewById(R.id.listView);
        eventList = new ArrayList<>();
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");
        signup = findViewById(R.id.signup_button);

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
                signup.setOnClickListener(v -> {
                    addSignupInfoToFirestore(selectedEvent);
                    eventAdapter.notifyDataSetChanged();
                });
            }
        });
    }

    //Citation: Sanchhaya Education Private Limited, GeeksforGeeks,2024
    //URL : https://www.geeksforgeeks.org/how-to-retrieve-image-from-firebase-in-realtime-in-android/

    /**
     * Fetches event data from Firestore and updates the ListView with the retrieved events.
     * It listens for real-time updates to the events collection to ensure the UI reflects the current state of the database.
     */
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
                    String qrUrl = doc.getString("qrUrl"); // Adjust the field name as in your Firestore
                    String imageUrl = doc.getString("imageUrl"); // Adjust the field name as in your Firestore

                    Log.d("FirestoreData", "ImageUrl: " + imageUrl);
                    if (imageUrl != null) {
                        eventList.add(new Event(name, date, venue, imageUrl,null,qrUrl));
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
     * Downloads and displays event images and QR codes from Firebase Storage.
     * It utilizes Glide for efficient image handling and display within ImageView elements.
     */
    private void DownloadEventFromFirestore() {

        Log.d("CALL", "TESTINGGG");
        ImageView imageView = findViewById(R.id.poster_image);
        ImageView qrView = findViewById(R.id.qr_code_image);

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
                Glide.with(ShowAllEventsAttendees.this)
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

        // Reference to your image file in Firebase Storage
        StorageReference qrRef = storage.getReference("qr_codes").child("https://firebasestorage.googleapis.com/v0/b/lab5-8b633.appspot.com/o/events_images%2F6aabefb8-a71d-4200-85a4-587f3105ef9e?alt=media&token=309840a2-29cb-43c9-b9cf-04ed8664e057");

        // Use Glide to download and display the image
        Log.d("Before Glide",  "Before Glide");
        qrRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL, now use Glide to display the image
                Glide.with(ShowAllEventsAttendees.this)
                        .load(qrRef)
                        .into(qrView);
                Log.d("QR", "QR" +  qrRef);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    /**
     * Adds the user's device ID to the list of signed-up attendees for a selected event in Firestore.
     * It checks for sign-up limits and existing sign-ups to prevent duplicates and enforce constraints.
     *
     * @param event The event object to which the user wants to sign up.
     */
    private void addSignupInfoToFirestore(Event event) {
        // Get the reference to the document for the selected event
        DocumentReference eventRef = FirebaseFirestore.getInstance()
                .collection("events")
                .document(event.getName() + "_" + event.getDate());

        // Check if the device ID already exists in the list of signed-up attendees
        eventRef.get().addOnSuccessListener(documentSnapshot -> {
            List<String> signedUpAttendees = (List<String>) documentSnapshot.get("signedUpAttendees");
            String limitStr = (String) documentSnapshot.get("Limit");

            if (limitStr != null && !limitStr.isEmpty()) {
                int limit = Integer.parseInt(limitStr);
                if (signedUpAttendees != null && signedUpAttendees.size() >= limit) {
                    // Limit reached, show dialog box
                    showDialog("Limit Reached", "Sorry, the sign-up limit for this event has been reached.");
                } else if (signedUpAttendees != null && signedUpAttendees.contains(deviceId)) {
                    // Device ID already exists in the list, show dialog box
                    showDialog("Already Signed Up", "You are already signed up for this event!");
                } else {
                    // Device ID doesn't exist, add it to the list and update Firestore

                    // Update the signedUpAttendees field by appending the new deviceId
                    eventRef.update("signedUpAttendees", FieldValue.arrayUnion(deviceId))
                            .addOnSuccessListener(aVoid -> {
                                // Show success message
                                Toast.makeText(ShowAllEventsAttendees.this, "You have signed up successfully for the event!", Toast.LENGTH_SHORT).show();
                                Log.d("Firestore", "Device signed up successfully for the event!");
                            })
                            .addOnFailureListener(e -> {
                                // Handle failure to update Firestore
                                Log.e("Firestore", "Error adding device ID to the list of signed-up attendees", e);
                            });
                }
            } else {
                // No limit set, proceed with sign-up without checking limit
                if (signedUpAttendees != null && signedUpAttendees.contains(deviceId)) {
                    // Device ID already exists in the list, show dialog box
                    showDialog("Already Signed Up", "You are already signed up for this event!");
                } else {
                    // Device ID doesn't exist, add it to the list and update Firestore

                    // Update the signedUpAttendees field by appending the new deviceId
                    eventRef.update("signedUpAttendees", FieldValue.arrayUnion(deviceId))
                            .addOnSuccessListener(aVoid -> {
                                // Show success message
                                Toast.makeText(ShowAllEventsAttendees.this, "You have signed up successfully for the event!", Toast.LENGTH_SHORT).show();
                                Log.d("Firestore", "Device signed up successfully for the event!");
                            })
                            .addOnFailureListener(e -> {
                                // Handle failure to update Firestore
                                Log.e("Firestore", "Error adding device ID to the list of signed-up attendees", e);
                            });
                }
            }
        }).addOnFailureListener(e -> {
            // Handle failure to retrieve Firestore document
            Log.e("Firestore", "Error getting document", e);
        });
    }


    /**
     * Shows a dialog with a custom title and message.
     * This method is used to inform the user of sign-up limits, existing sign-ups, or other relevant information.
     *
     * @param title   The title of the dialog.
     * @param message The message to be displayed in the dialog.
     */
    private void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Do nothing or handle the OK button action
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
