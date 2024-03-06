package com.example.scansaga;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomepageActivityTest {
    @Rule
    public ActivityScenarioRule<HomepageActivity> activityScenarioRule = new ActivityScenarioRule<>(HomepageActivity.class);
    private IdlingResource idlingResource;
    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

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
        Espresso.onIdle();

        // Verify that the correct activity is launched
        Intent actualIntent = Intents.getIntents().get(0);
        Intent expectedIntent = new Intent(ApplicationProvider.getApplicationContext(), ShowAllEvents.class);
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }


//    @Test
//    public void testSignUpEventButtonClick() {
//        ActivityScenario<HomepageActivity> scenario = activityScenarioRule.getScenario();
//        scenario.onActivity(activity -> {
//            Button signUpEventButton = activity.findViewById(R.id.sign_up_event_button);
//            assertNotNull(signUpEventButton);
//
//            signUpEventButton.performClick();
//
//            Intent expectedIntent = new Intent(activity, EventSignUp.class);
//            Intent actualIntent = ApplicationProvider.getApplicationContext().getPackageManager().getLaunchIntentForPackage(expectedIntent.getComponent().getPackageName());
//
//            assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
//        });
//    }
//
//    @Test
//    public void testScanAndGoButtonClick() {
//        ActivityScenario<HomepageActivity> scenario = activityScenarioRule.getScenario();
//        scenario.onActivity(activity -> {
//            Button scanAndGoButton = activity.findViewById(R.id.scan_and_attend_button);
//            assertNotNull(scanAndGoButton);
//
//            scanAndGoButton.performClick();
//
//            Intent expectedIntent = new Intent(activity, ScanAndGo.class);
//            Intent actualIntent = ApplicationProvider.getApplicationContext().getPackageManager().getLaunchIntentForPackage(expectedIntent.getComponent().getPackageName());
//
//            assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
//        });
//    }
//
//    @Test
//    public void testEditProfileButtonClick() {
//        ActivityScenario<HomepageActivity> scenario = activityScenarioRule.getScenario();
//        scenario.onActivity(activity -> {
//            Button editProfileButton = activity.findViewById(R.id.edit_profile_button);
//            assertNotNull(editProfileButton);
//
//            editProfileButton.performClick();
//
//            Intent expectedIntent = new Intent(activity, MyProfile.class);
//            Intent actualIntent = ApplicationProvider.getApplicationContext().getPackageManager().getLaunchIntentForPackage(expectedIntent.getComponent().getPackageName());
//
//            assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
//        });
//    }
//
//    @Test
//    public void testAddEventButtonClick() {
//        ActivityScenario<HomepageActivity> scenario = activityScenarioRule.getScenario();
//        scenario.onActivity(activity -> {
//            Button addEventButton = activity.findViewById(R.id.add_event_button);
//            assertNotNull(addEventButton);
//
//            addEventButton.performClick();
//
//            Intent expectedIntent = new Intent(activity, AddEvent.class);
//            Intent actualIntent = ApplicationProvider.getApplicationContext().getPackageManager().getLaunchIntentForPackage(expectedIntent.getComponent().getPackageName());
//
//            assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
//        });
//    }
//
//    @Test
//    public void testShowAllUsersButtonClick() {
//        ActivityScenario<HomepageActivity> scenario = activityScenarioRule.getScenario();
//        scenario.onActivity(activity -> {
//            Button showAllUsersButton = activity.findViewById(R.id.show_all_users_button);
//            assertNotNull(showAllUsersButton);
//
//            showAllUsersButton.performClick();
//
//            Intent expectedIntent = new Intent(activity, ShowAllUsers.class);
//            Intent actualIntent = ApplicationProvider.getApplicationContext().getPackageManager().getLaunchIntentForPackage(expectedIntent.getComponent().getPackageName());
//
//            assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
//        });
//    }
}
