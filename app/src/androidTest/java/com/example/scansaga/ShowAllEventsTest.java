package com.example.scansaga;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class ShowAllEventsTest {

    @Rule
    public ActivityScenarioRule<ShowAllEvents> activityScenarioRule =
            new ActivityScenarioRule<>(ShowAllEvents.class);

    @Test
    public void testListViewDisplaysEvents() {
        // Check if the ListView is displayed
        Espresso.onView(ViewMatchers.withId(R.id.listView)).check(matches(isDisplayed()));

        // Check if the ListView displays at least one event
        Espresso.onData(instanceOf(Event.class))
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .check(matches(isDisplayed()));
    }

    @Test
    public void testDeleteButtonDeletesEvent() {
        // Click the delete button for the first event in the list
        Espresso.onView(ViewMatchers.withId(R.id.button_delete)).perform(ViewActions.click());

        // Verify that the event is removed from the ListView
        Espresso.onView(withText("Event Name")) // Replace with the name of the event you expect to be deleted
                .check(matches(not(isDisplayed())));
    }
}
