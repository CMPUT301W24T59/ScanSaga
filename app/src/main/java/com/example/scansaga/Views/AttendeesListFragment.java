package com.example.scansaga.Views;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scansaga.Controllers.CheckedInAttendeesAdapter;
import com.example.scansaga.Model.ShowCheckedInAttendeesActivity;
import com.example.scansaga.Model.User;
import com.example.scansaga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AttendeesListFragment extends Fragment {

    private RecyclerView attendeesRecyclerView;
    private RecyclerView.Adapter attendeesAdapter;
    private ArrayList<User> attendeesList = new ArrayList<>();
    private String eventNameDate;

    public AttendeesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventNameDate = getArguments().getString(ShowCheckedInAttendeesActivity.EXTRA_EVENT_NAME_DATE);
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendees_list, container, false);
        attendeesRecyclerView = view.findViewById(R.id.checked_in_attendee_list);
        attendeesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        attendeesAdapter = new CheckedInAttendeesAdapter(getContext(), attendeesList);
        attendeesRecyclerView.setAdapter(attendeesAdapter);

        // Check if eventNameDate is not null and not empty before using it
        if (eventNameDate != null && !eventNameDate.isEmpty()) {
            fetchCheckedInAttendees(eventNameDate);
        } else {
            // Handle the case where eventNameDate is null or empty
        }
        return view;
    }

    private void fetchCheckedInAttendees(String eventNameDate) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").document(eventNameDate)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<String> checkedInAttendees = (List<String>) document.get("checkedInAttendees");
                            if (checkedInAttendees != null) {
                                for (String deviceId : checkedInAttendees) {
                                    fetchUserDetails(deviceId);
                                }
                            }
                        }
                    } else {
                        Log.e("AttendeesListFragment", "Error fetching event attendees", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.e("AttendeesListFragment", "Error fetching event details", e));
    }

    private void fetchUserDetails(String deviceId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(deviceId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String firstname = document.getString("Firstname");
                            String lastname = document.getString("Lastname");
                            String profileImage = document.getString("ProfilePicture");
                            User user = new User(firstname, lastname, "", "", profileImage);

                            // Ensure thread safety when updating UI components
                            getActivity().runOnUiThread(() -> {
                                attendeesList.add(user);
                                attendeesAdapter.notifyDataSetChanged();
                            });
                        }
                    } else {
                        Log.e("AttendeesListFragment", "Error fetching user details", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.e("AttendeesListFragment", "Error accessing Firestore", e));
    }

}
