package com.example.scansaga;

import android.content.Intent;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class AttendeeHomePageTest {

    @Rule
    public ActivityScenarioRule<AttendeeHomePage> activityScenarioRule =
            new ActivityScenarioRule<>(AttendeeHomePage.class);

    @Test
    public void testShowAllEventsButton() {
        // Click the show all events button
        onView(withId(R.id.show_all_events_button)).perform(click());

        // Ensure ShowAllEventsAttendeesActivity is launched
        ActivityScenario<ShowAllEventsAttendees> showAllEventsActivityScenario = ActivityScenario.launch(ShowAllEventsAttendees.class);
        assertNotNull(showAllEventsActivityScenario);
    }

    @Test
    public void testEditProfileButton() {
        // Click the edit profile button
        onView(withId(R.id.edit_profile_button)).perform(click());

        // Ensure MyProfileActivity is launched
        ActivityScenario<MyProfile> myProfileActivityScenario = ActivityScenario.launch(MyProfile.class);
        assertNotNull(myProfileActivityScenario);
    }

    @Test
    public void testAddEventButton() {
        // Click the add event button
        onView(withId(R.id.add_event_button)).perform(click());

        // Ensure AddEventActivity is launched
        ActivityScenario<AddEvent> addEventActivityScenario = ActivityScenario.launch(AddEvent.class);
        assertNotNull(addEventActivityScenario);
    }
}
