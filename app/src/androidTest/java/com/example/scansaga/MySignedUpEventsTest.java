package com.example.scansaga;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import static org.junit.Assert.assertEquals;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.example.scansaga.Views.MySignedUpEvents;

/**
 * Instrumented test class for {@link MySignedUpEvents} activity.
 */
@RunWith(AndroidJUnit4.class)
public class MySignedUpEventsTest {

    @Rule
    public ActivityScenarioRule<MySignedUpEvents> activityScenarioRule =
            new ActivityScenarioRule<>(MySignedUpEvents.class);

    /**
     * Test case to check if the ListView is displayed.
     */
    @Test
    public void testListViewDisplayed() {
        // Check if the ListView is displayed
        onView(withId(R.id.listView)).check(matches(isDisplayed()));
    }

    /**
     * Test case to verify the count of events in the event list.
     */
    @Test
    public void testEventListCount() {
        // Start the activity
        try (ActivityScenario<MySignedUpEvents> scenario = ActivityScenario.launch(MySignedUpEvents.class)) {
            // Wait for the activity to be launched and displayed
            scenario.onActivity(activity -> {
                // Count the number of items displayed in the ListView
                int eventCount = activity.eventList.size();
                // Assert that the count is equal to 1 as when testing the device only had signed up for 1 event
                assertEquals(1, eventCount);
            });
        }
    }

    /**
     * Test case to fetch events from Firestore and verify if the event list is not empty.
     */
    @Test
    public void testFetchEventsFromFirestore() {
        // Start the activity
        try (ActivityScenario<MySignedUpEvents> scenario = ActivityScenario.launch(MySignedUpEvents.class)) {
            // Wait for the activity to be launched and displayed
            scenario.onActivity(activity -> {
                // Fetch events from Firestore
                activity.fetchEventsFromFirestore();
                // Assert that eventList contains at least one event
                assertEquals(true, !activity.eventList.isEmpty());
            });
        }
    }
}
