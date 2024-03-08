package com.example.scansaga;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertNotNull;

import android.content.Intent;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AttendeeHomepageUITest {

    @Rule
    public ActivityScenarioRule<AttendeeHomePage> activityRule = new ActivityScenarioRule<AttendeeHomePage>(AttendeeHomePage.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testShowAllEventsButton() {
        // Click the show all events button
        onView(withId(R.id.show_all_events_button)).perform(click());

        // Ensure ShowAllEventsAttendeesActivity is launched
        ActivityScenario<ShowAllEventsAttendees> showAllEventsActivityScenario = ActivityScenario.launch(ShowAllEventsAttendees.class);
        assertNotNull(showAllEventsActivityScenario);
    }

    @Test
    public void testScanAndGoButton() {
        // Click the scan and go button
        onView(withId(R.id.scan_and_attend_button)).perform(click());

        // Ensure ScanAndGoActivity is launched
        ActivityScenario<ScanAndGo> scanAndGoActivityScenario = ActivityScenario.launch(ScanAndGo.class);
        assertNotNull(scanAndGoActivityScenario);
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

