package com.example.scansaga;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;

import com.example.scansaga.Model.ShowCheckedInAttendeesActivity;
import com.example.scansaga.Model.MapsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class ShowCheckedInAttendeesActivityTest {

    @Rule
    public ActivityScenarioRule<ShowCheckedInAttendeesActivity> activityRule =
            new ActivityScenarioRule<>(ShowCheckedInAttendeesActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @Test
    public void checkIfRecyclerViewExists() {
        onView(withId(R.id.checked_in_list)).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfSeeMapButtonExists() {
        onView(withId(R.id.see_map)).check(matches(isDisplayed()));
    }

    @Test
    public void clickSeeMapButton_NavigatesToMapsActivity() {
        onView(withId(R.id.see_map)).perform(click());
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }
}
