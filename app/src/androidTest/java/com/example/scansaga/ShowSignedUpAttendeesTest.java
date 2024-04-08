package com.example.scansaga;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scansaga.Controllers.SignedUpAttendeesAdapter;
import com.example.scansaga.Model.User;
import com.example.scansaga.R;
import com.example.scansaga.Views.ShowSignedUpAttendees;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test class for {@link ShowSignedUpAttendees} activity.
 */
@RunWith(AndroidJUnit4.class)
public class ShowSignedUpAttendeesTest {

    @Rule
    public ActivityScenarioRule<ShowSignedUpAttendees> activityScenarioRule =
            new ActivityScenarioRule<>(ShowSignedUpAttendees.class);

    /**
     * Test case to check if the ListView is displayed.
     */
    @Test
    public void testListViewDisplayed() {
        // Start the activity
        try (ActivityScenario<ShowSignedUpAttendees> scenario = ActivityScenario.launch(ShowSignedUpAttendees.class)) {
            // Wait for the activity to be launched and displayed
            scenario.onActivity(activity -> {
                // Check if the ListView is displayed
                ListView listView = activity.findViewById(R.id.listView);
                assertEquals(true, listView.isShown());
            });
        }
    }

    /**
     * Test case to verify fetching and displaying signed-up attendees.
     */
    /**
     * Test case to verify fetching and displaying signed-up attendees.
     */
    /**
     * Test case to verify if the adapter is set correctly with attendees.
     */
    @Test
    public void testAdapterSetWithAttendees() {
        // Start the activity
        try (ActivityScenario<ShowSignedUpAttendees> scenario = ActivityScenario.launch(ShowSignedUpAttendees.class)) {
            // Wait for the activity to be launched and displayed
            scenario.onActivity(activity -> {
                // Get the adapter set on the ListView
                ListView listView = activity.findViewById(R.id.listView);
                //While testing no attendees are signedup as of now
                assertEquals(0, listView.getCount());
            });
        }
    }


}

