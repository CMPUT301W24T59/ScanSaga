package com.example.scansaga.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scansaga.Controllers.EventArrayAdapter;
import com.example.scansaga.Model.Event;
import com.example.scansaga.Model.MyProfile;
import com.example.scansaga.Model.User;
import com.example.scansaga.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Activity for the homepage of the attendee.
 */
public class AttendeeHomePage extends AppCompatActivity {

    /**
     * Called when the activity is created. Initializes UI elements, retrieves user data
     * passed from the previous activity, and sets up click listeners for buttons.
     *
     * @param savedInstanceState  If the activity is being re-initialized after previously
     *                            being shut down then this Bundle contains the data it most
     *                            recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_homepage);

        // Initialize buttons
        Button scanAndGoButton = findViewById(R.id.scan_and_attend_button);
        Button editProfileButton = findViewById(R.id.edit_profile_button);
        Button showAllEventsButton = findViewById(R.id.show_all_events_button);
        Button addEventButton = findViewById(R.id.add_event_button);

        // Retrieve the user details passed from MainActivity
        User user = (User) getIntent().getSerializableExtra("user");


        // Set click listener for show all events button
        showAllEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ShowAllEventsActivity
                Intent intent = new Intent(AttendeeHomePage.this, ShowAllEventsAttendees.class);
                startActivity(intent);
            }
        });


        // Set click listener for edit profile button
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start EditProfileActivity
                Intent intent = new Intent(AttendeeHomePage.this, MyProfile.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        // Set click listener for add event button
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddEventActivity
                Intent intent = new Intent(AttendeeHomePage.this, AddEvent.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Nested activity within AttendeeHomePage responsible for handling the display
     * and addition of events. Implements the AddEventFragment.AddEventDialogListener
     * to receive data from an AddEventFragment.
     */
    public static class AddEvent extends AppCompatActivity implements AddEventFragment.AddEventDialogListener {
        ListView eventList;
        ArrayList<Event> eventDataList;
        EventArrayAdapter eventArrayAdapter;
        private FirebaseFirestore db;
        private CollectionReference eventsRef;

        /**
         *  Callback method triggered when a new event is to be added from the AddEventFragment.
         *  Updates the UI to reflect the added event.
         *
         * @param event The new Event object to be added to the list
         */
        @Override
        public void addNewEvent(Event event) {
            eventArrayAdapter.notifyDataSetChanged();
        }
        /**
         *  Callback method triggered when an event is deleted from the AddEventFragment.
         *  Removes the event from the list and updates the UI.
         *
         * @param event The Event object to be deleted from the list.
         */

        @Override
        public void deleteEvent(Event event) {
            eventDataList.remove(event);
            eventArrayAdapter.notifyDataSetChanged();
        }

        /**
         * Callback method triggered when an event is edited from the AddEventFragment.
         * Updates the UI to reflect changes to the edited event.
         *
         * @param event The edited Event object.
         */
        @Override
        public void editEvent(Event event) {
            eventArrayAdapter.notifyDataSetChanged();
        }
        /**
         * Called when the activity is created. Initializes UI elements, sets up Firestore
         * references, creates the adapter for the event list, and sets an OnClickListener
         * for the FloatingActionButton to trigger the AddEventFragment.
         *
         * @param savedInstanceState  If the activity is being re-initialized after previously
         *                            being shut down then this Bundle contains the data it most
         *                            recently supplied in onSaveInstanceState(Bundle).
         */

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

        }


    }
}
