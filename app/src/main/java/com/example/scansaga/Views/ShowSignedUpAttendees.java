package com.example.scansaga.Views;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scansaga.Controllers.SignedUpAttendeesAdapter;
import com.example.scansaga.Model.User;
import com.example.scansaga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * An activity that displays a list of attendees who have signed up for an event.
 * It retrieves the list of signed-up attendee IDs from Firestore based on the event details
 * passed through intent extras and then fetches each attendee's details to display their names in a ListView.
 */
public class ShowSignedUpAttendees extends AppCompatActivity {

    private static final String TAG = "ShowSignedUpAttendees";
    private FirebaseFirestore db;
    public SignedUpAttendeesAdapter adapter;

    /**
     * Initializes the activity, sets up the layout, and initiates the process to fetch and display
     * signed-up attendees for the specified event from Firestore.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_signed_up_attendees);

        db = FirebaseFirestore.getInstance();

        // Retrieve the event details from extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String eventName = extras.getString("Name");
            String eventDate = extras.getString("Date");

            // Construct the document name based on your naming convention
            String documentName = eventName + "_" + eventDate; // Replace "_date" with the actual date

            // Retrieve signed-up attendee IDs from Firestore
            db.collection("events").document(documentName)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                ArrayList<String> attendeeIDs = (ArrayList<String>) documentSnapshot.get("signedUpAttendees");
                                if (attendeeIDs != null && !attendeeIDs.isEmpty()) {
                                    fetchAttendeeDetails(attendeeIDs);
                                } else {
                                    // Handle the case when there are no signed-up attendees
                                    Log.d(TAG, "No signed-up attendees for this event");
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error getting document", e);
                        }
                    });
        }
    }

    /**
     * Fetches attendee details such as first and last name from Firestore using their IDs.
     * Once all attendee details are fetched, it calls {@link #displayAttendeeNames(ArrayList)} to update the UI.
     *
     * @param attendeeIDs A list of attendee IDs to fetch details for.
     */
    private void fetchAttendeeDetails(ArrayList<String> attendeeIDs) {
        // Retrieve attendee details from Firestore based on their IDs
        ArrayList<User> users = new ArrayList<>();
        for (String userID : attendeeIDs) {
            db.collection("users").document(userID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String firstname = documentSnapshot.getString("Firstname");
                                String lastname = documentSnapshot.getString("Lastname");
                                User user = new User(firstname, lastname, "" ,"","");
                                users.add(user);
                                // Notify the adapter when all user details are fetched
                                if (users.size() == attendeeIDs.size()) {
                                    displayAttendeeNames(users);
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error getting document", e);
                        }
                    });
        }
    }

    /**
     * Populates a ListView with the names of attendees using a custom adapter.
     * This method is called after all attendee details have been successfully fetched and compiled into a list.
     *
     * @param attendees A list of User objects representing the attendees to be displayed.
     */
    private void displayAttendeeNames(ArrayList<User> attendees) {
        // Populate ListView with attendee names using SignedUpAttendeesAdapter
        ListView listView = findViewById(R.id.listView);
        adapter = new SignedUpAttendeesAdapter(this, attendees);
        listView.setAdapter(adapter);
    }
}
