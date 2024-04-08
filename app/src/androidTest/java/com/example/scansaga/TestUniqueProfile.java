package com.example.scansaga;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.example.scansaga.Model.MyProfile;

/**
 * Espresso test for {@link MyProfile} activity to ensure that the unique profile
 * image is displayed correctly.
 */
@RunWith(AndroidJUnit4.class)
public class TestUniqueProfile {

    @Rule
    public ActivityScenarioRule<MyProfile> activityScenarioRule =
            new ActivityScenarioRule<>(MyProfile.class);

    /**
     * Verifies that the unique profile image is displayed correctly on the screen.
     */
    @Test
    public void testProfileImageDisplay() {
        // Check if the unique profile image is displayed correctly
        Espresso.onView(ViewMatchers.withId(R.id.profile_image_view))
                .check(matches(ViewMatchers.isDisplayed()));
    }
}
