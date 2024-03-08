package com.example.scansaga;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.RootMatchers;
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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import com.example.scansaga.Views.AddEvent;
import com.example.scansaga.Views.AttendeeHomePage;

import java.util.Calendar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddEventTest {

    @Rule
    public ActivityScenarioRule<AttendeeHomePage.AddEvent> activityScenarioRule =
            new ActivityScenarioRule<>(AttendeeHomePage.AddEvent.class);
    public ActivityScenarioRule<AddEvent> activityRule =
            new ActivityScenarioRule<>(AddEvent.class);

    @Test
    public void testAddEventButtonOpensFragment() {
        // Click the add event button
        Espresso.onView(ViewMatchers.withId(R.id.add_event_button)).perform(ViewActions.click());

        onView(withId(R.id.edit_text_event_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddEventButtonAndQR() {
        // Click the add event button
        Espresso.onView(ViewMatchers.withId(R.id.add_event_button)).perform(ViewActions.click());

        // Enter event details
        String eventName = "Test Event";
        String eventVenue = "Test Venue";
        Espresso.onView(ViewMatchers.withId(R.id.edit_text_event_text)).perform(ViewActions.typeText(eventName));
        Espresso.onView(ViewMatchers.withId(R.id.edit_venue_text)).perform(ViewActions.typeText(eventVenue));

        // Click the button to add the event
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        String currentDate = String.format("%02d/%02d/%d", month, dayOfMonth, year);
        onView(withId(R.id.edit_date_text)).perform(ViewActions.replaceText(currentDate));

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

