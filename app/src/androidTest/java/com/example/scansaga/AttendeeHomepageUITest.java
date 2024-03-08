package com.example.scansaga;

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
    public void signUpEventButton_click_shouldStartSignUpEventActivity() {
        onView(withId(R.id.sign_up_event_button)).perform(click());
        intended(hasComponent(EventSignUp.class.getName()));
    }

    @Test
    public void showAllEventsButton_click_shouldStartShowAllEventsActivity() {
        onView(withId(R.id.show_all_events_button)).perform(click());
        intended(hasComponent(ShowAllEventsAttendees.class.getName()));
    }

    @Test
    public void scanAndGoButton_click_shouldStartScanAndGoActivity() {
        onView(withId(R.id.scan_and_attend_button)).perform(click());
        intended(hasComponent(ScanAndGo.class.getName()));
    }

    @Test
    public void editProfileButton_click_shouldStartEditProfileActivity() {
        User user = new User("Alice", "Wonderland", "testUser@gmail.com", null); // Create a test user
        Intent intent = new Intent();
        intent.putExtra("user", user);
        activityRule.launchActivity(intent);

        onView(withId(R.id.edit_profile_button)).perform(click());
        intended(hasComponent(MyProfile.class.getName()));
    }

    @Test
    public void addEventButton_click_shouldStartAddEventActivity() {
        onView(withId(R.id.add_event_button)).perform(click());
        intended(hasComponent(AddEvent.class.getName()));
    }
}

