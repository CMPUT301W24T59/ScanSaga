package com.example.scansaga;

import static androidx.test.espresso.Espresso.onIdle;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.scansaga.Views.HomepageActivity;
import com.example.scansaga.Views.ShowAllEvents;
import com.example.scansaga.Views.ShowAllUsers;
import com.example.scansaga.Views.AddEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Espresso tests for {@link HomepageActivity} button clicks and intent verifications.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomepageActivityTest {
    @Rule
    public ActivityScenarioRule<HomepageActivity> activityScenarioRule = new ActivityScenarioRule<>(HomepageActivity.class);
    private IdlingResource idlingResource;

    /**
     * Sets up the necessary components before each test.
     */
    @Before
    public void setUp() {
        Intents.init();
    }

    /**
     * Cleans up after each test.
     */
    @After
    public void tearDown() {
        Intents.release();
    }

    /**
     * Verifies that clicking the "Show All Events" button correctly launches the {@link ShowAllEvents} activity.
     */
    @Test
    public void testShowAllEventsButtonClick() {
        // Get the activity scenario
        ActivityScenario<HomepageActivity> scenario = activityScenarioRule.getScenario();
        scenario.onActivity(activity -> {
            // Find the "Show All Events" button
            Button showAllEventsButton = activity.findViewById(R.id.show_all_events_button);
            assertNotNull(showAllEventsButton);

            // Perform click on the button
            showAllEventsButton.performClick();
        });

        // Wait for Espresso to be idle before verifying the launched activity
        onIdle();

        // Verify that the correct activity is launched
        Intent actualIntent = Intents.getIntents().get(0);
        Intent expectedIntent = new Intent(ApplicationProvider.getApplicationContext(), ShowAllEvents.class);
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }

    /**
     * Verifies that clicking the "Add Event" button correctly launches the {@link AddEvent} activity.
     */
    @Test
    public void testAddEventButtonClick() {
        // Get the activity scenario
        ActivityScenario<HomepageActivity> scenario = activityScenarioRule.getScenario();
        scenario.onActivity(activity -> {
            // Find the "Add Event" button
            Button addEventButton = activity.findViewById(R.id.add_event_button);
            assertNotNull(addEventButton);

            // Perform click on the button
            addEventButton.performClick();
        });
    }

    /**
     * Verifies that clicking the "Show All Users" button correctly launches the {@link ShowAllUsers} activity.
     */
    @Test
    public void testShowAllUsersButtonClick() {
        // Get the activity scenario
        ActivityScenario<HomepageActivity> scenario = activityScenarioRule.getScenario();
        scenario.onActivity(activity -> {
            // Find the "Show All Users" button
            Button showAllUsersButton = activity.findViewById(R.id.show_all_users_button);
            assertNotNull(showAllUsersButton);

            // Perform click on the button
            showAllUsersButton.performClick();
        });

        // Wait for Espresso to be idle before verifying the launched activity
        onIdle();

        // Verify that the correct activity is launched
        Intent actualIntent = Intents.getIntents().get(0);
        Intent expectedIntent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ShowAllUsers.class);
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }

}
