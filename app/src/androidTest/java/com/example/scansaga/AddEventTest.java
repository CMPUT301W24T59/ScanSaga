package com.example.scansaga;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;

@RunWith(AndroidJUnit4.class)
public class AddEventTest {

    @Rule
    public ActivityScenarioRule<AddEvent> activityScenarioRule =
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

        // Click the "Add" button in the dialog
        Espresso.onView(withText("Add"))
                .inRoot(RootMatchers.isDialog())
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());

        // Check if the event is added to the ListView
        Espresso.onView(ViewMatchers.withId(R.id.event_list)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.event_text)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.time_text)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.venue_text)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.qr_code_image)).check(matches(isDisplayed()));

    }

}

