package com.example.scansaga.Model;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.scansaga.Controllers.CheckedInAttendeesAdapter;
import com.example.scansaga.R;
import com.example.scansaga.Views.HomepageActivity;
import com.example.scansaga.Views.OrganizerEvents;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowCheckedInAttendeesActivity extends AppCompatActivity {
    private static final String EXTRA_EVENT_NAME_DATE = "extra_event_name_date"; // use a consistent key
    private Button seeMap;
    private RecyclerView attendeesRecyclerView;
    private CheckedInAttendeesAdapter adapter;
    private ArrayList<User> attendeesList = new ArrayList<>();

    // Firebase Firestore instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_checked_in_attendee_activity);
        seeMap = findViewById(R.id.see_map);

        // Initialize RecyclerView
        attendeesRecyclerView = findViewById(R.id.checked_in_list);
        attendeesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with an empty list
        adapter = new CheckedInAttendeesAdapter(this, attendeesList);
        attendeesRecyclerView.setAdapter(adapter);

        String eventNameDate = getIntent().getStringExtra(EXTRA_EVENT_NAME_DATE);
        if (eventNameDate != null && !eventNameDate.isEmpty()) {
            fetchAttendeesFromFirebase(eventNameDate);
        }
        seeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowCheckedInAttendeesActivity.this, MapsActivity.class);
                intent.putExtra("extra_event_name_date", eventNameDate);
                startActivity(intent);
            }
        });
    }

    private void fetchAttendeesFromFirebase(String eventNameDate) {
        // Get the event's ID from the intent or however you pass it to this activity
        String eventId = eventNameDate; // Replace with actual event ID

        // First, fetch the event document to get the attendee check-in counts
        db.collection("events").document(eventId)
                .get()
                .addOnSuccessListener(eventDocument -> {
                    if (eventDocument.exists()) {
                        Map<String, Long> attendeeCheckInCounts = (Map<String, Long>) eventDocument.get("attendeeCheckInCounts");
                        List<String> checkedInUserIds = new ArrayList<>(attendeeCheckInCounts.keySet());
                        if (!checkedInUserIds.isEmpty()) {
                            for (String userId : checkedInUserIds) {
                                // Fetch each user's details
                                db.collection("users").document(userId)
                                        .get()
                                        .addOnSuccessListener(userDocument -> {
                                            if (userDocument.exists()) {
                                                // Assume checkInCount is initialized to 0 if not present
                                                Long checkInCount = attendeeCheckInCounts.getOrDefault(userId, 0L);

                                                // Create a user object including the check-in count
                                                User user = new User(
                                                        userDocument.getString("Firstname"),
                                                        userDocument.getString("Lastname"),
                                                        userDocument.getString("Email"),
                                                        userDocument.getString("PhoneNumber"),
                                                        userDocument.getString("ProfilePicture")
                                                );
                                                user.setCount(checkInCount.toString());
                                                // Add user to the list
                                                attendeesList.add(user);

                                                // Notify the adapter that the data has changed
                                                adapter.notifyDataSetChanged();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle the error
                                            Log.e("FetchUserError", "Error fetching user details", e);
                                        });
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Log.e("FetchEventError", "Error fetching event", e);
                });
    }

}
