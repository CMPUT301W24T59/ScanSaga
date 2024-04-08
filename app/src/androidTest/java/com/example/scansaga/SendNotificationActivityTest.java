package com.example.scansaga;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.scansaga.Views.SendNotificationActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Instrumented test class to verify the behavior of SendNotificationActivity UI.
 */
@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class SendNotificationActivityTest {

    @Rule
    public ActivityScenarioRule<SendNotificationActivity> activityScenarioRule = new ActivityScenarioRule<>(SendNotificationActivity.class);


    /**
     * Test to verify the behavior of the send notification button.
     * <p>
     * This test checks if notification title and message fields are displayed,
     * enters a test title and message, clicks on the send notification button,
     * and then verifies that the entered title and message are correctly set.
     */
    @Test
    public void testSendNotificationButton() {
        // Check if notification title and message fields are displayed
        Espresso.onView(ViewMatchers.withId(R.id.notification_title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.notification_msg)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Enter notification title and message
        Espresso.onView(ViewMatchers.withId(R.id.notification_title)).perform(ViewActions.replaceText("Test Title"));
        Espresso.onView(ViewMatchers.withId(R.id.notification_msg)).perform(ViewActions.replaceText("Test Message"));

        // Click on send notification button
        Espresso.onView(ViewMatchers.withId(R.id.notification_send_button)).perform(ViewActions.click());

        // Add assertions or verification
        Espresso.onView(ViewMatchers.withId(R.id.notification_title)).check(ViewAssertions.matches(ViewMatchers.withText("Test Title"))); // Ensure title is set correctly
        Espresso.onView(ViewMatchers.withId(R.id.notification_msg)).check(ViewAssertions.matches(ViewMatchers.withText("Test Message"))); // Ensure message is set correctly
    }
}
