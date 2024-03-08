package com.example.scansaga;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddEventTest {

    @Rule
    public ActivityScenarioRule<AddEvent> activityRule =
            new ActivityScenarioRule<>(AddEvent.class);

    @Test
    public void testAddNewEvent() {
        activityRule.getScenario().onActivity(activity -> {
            ArrayList<Event> eventDataList = activity.eventDataList;
            int initialSize = eventDataList.size();

            Event newEvent = new Event("Test Event", "Test Venue", "2024-03-06", null);
            activity.addNewEvent(newEvent);

            assertEquals(initialSize + 1, eventDataList.size());
            assertEquals(newEvent, eventDataList.get(eventDataList.size() - 1));
        });
    }

    @Test
    public void testDeleteEvent() {
        // Launch the activity
        activityRule.getScenario().onActivity(activity -> {
            ArrayList<Event> eventDataList = activity.eventDataList;

            Event event1 = new Event("Event 1", "Venue 1", "2024-03-06", null);
            Event event2 = new Event("Event 2", "Venue 2", "2024-03-07", null);

            eventDataList.add(event1);
            eventDataList.add(event2);

            activity.deleteEvent(event1);

            assertEquals(1, eventDataList.size());
            assertFalse(eventDataList.contains(event1));
        });
    }

    @Test
    public void testAddEventButtonDisplayed() {
        onView(ViewMatchers.withId(R.id.add_event_button))
                .check(matches(isDisplayed()));
    }

//    @Test
//    public void testEditEvent() {
//        activityRule.getScenario().onActivity(activity -> {
//            ArrayList<Event> eventDataList = activity.eventDataList;
//
//            // Assuming there's an existing event to edit
//            Event existingEvent = new Event("Existing Event", "Existing Venue", "2024-03-06", null);
//            eventDataList.add(existingEvent);
//
//            // Edit the event
//            Event editedEvent = new Event("Edited Event", "Edited Venue", "2024-03-06", null);
//            activity.editEvent(existingEvent, editedEvent);
//
//            // Check if the event is edited correctly
//            assertEquals(1, eventDataList.size());
//            assertTrue(eventDataList.contains(editedEvent));
//            assertFalse(eventDataList.contains(existingEvent));
//        });
//    }
}

