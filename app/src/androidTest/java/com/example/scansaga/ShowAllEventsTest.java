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

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

/**
 * Tests for verifying the functionalities of the ShowAllEvents activity within the ScanSaga application.
 * This includes displaying a list of events and the ability to delete an event from this list.
 */
@RunWith(AndroidJUnit4.class)
public class ShowAllEventsTest {

    /**
     * Rule to launch the ShowAllEvents activity before each test execution.
     */
    @Rule
    public ActivityScenarioRule<ShowAllEvents> activityScenarioRule =
            new ActivityScenarioRule<>(ShowAllEvents.class);

    /**
     * Tests if the ListView within the ShowAllEvents activity correctly displays events.
     */
    @Test
    public void testListViewDisplaysEvents() {
        // Check if the ListView is displayed
        onView(ViewMatchers.withId(R.id.listView)).check(matches(isDisplayed()));

        // Check if the ListView displays at least one event
        onData(instanceOf(Event.class))
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .check(matches(isDisplayed()));
    }

    /**
     * Tests if the delete button within the ShowAllEvents activity correctly deletes an event from the ListView.
     */
    @Test
    public void testDeleteButtonDeletesEvent() {
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());
        onView(withId(R.id.delete_event_button)).perform(click());
    }
}
