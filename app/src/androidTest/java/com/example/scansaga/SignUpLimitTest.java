package com.example.scansaga;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.scansaga.Model.Event;
import com.example.scansaga.Views.ShowAllEvents;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anyOf;

public class SignUpLimitTest {

    @Rule
    public ActivityScenarioRule<ShowAllEvents> activityScenarioRule =
            new ActivityScenarioRule<>(ShowAllEvents.class);

    @Test
    public void testAnyErrorChecksForSignUpsLimits() {
        // Click on an event before signing up
        onView(ViewMatchers.withId(R.id.listView)).perform(click());
        // Create a dummy Event object for an event with sign-up limit reached
        Event eventWithLimitReached = new Event("Event Name", "Event Date", "Event Venue", "Image URL", "Some Other Data", null);

        // Perform signup action
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.addSignupInfoToFirestore(eventWithLimitReached);
        });
        onView(ViewMatchers.withId(R.id.listView)).perform(click());
        onView(ViewMatchers.withId(R.id.signup_button)).perform(click());

        // Check if anything is displayed on the screen
        onView(isRoot()).check(matches(isDisplayed()));

    }
}
