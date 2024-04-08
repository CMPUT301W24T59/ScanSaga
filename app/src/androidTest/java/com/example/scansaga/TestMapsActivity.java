package com.example.scansaga;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.scansaga.Model.MapsActivity;
import com.example.scansaga.R;
import com.google.android.gms.maps.SupportMapFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * Espresso test for {@link MapsActivity} to ensure the Google Map is displayed.
 */
@RunWith(AndroidJUnit4.class)
public class TestMapsActivity {

    @Rule
    public ActivityScenarioRule<MapsActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MapsActivity.class);

    /**
     * Verifies that the map fragment is loaded and displayed on the screen.
     */
    @Test
    public void testMapIsDisplayed() {
        onView(ViewMatchers.withId(R.id.map)).check(matches(isDisplayed()));
    }
}
