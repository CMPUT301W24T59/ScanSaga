package com.example.scansaga;

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


/**
 * Tests for the MyProfile activity of the ScanSaga application.
 * This class verifies that the profile information is displayed correctly to the user.
 */

@RunWith(AndroidJUnit4.class)
public class MyProfileTest {

    /**
     * Rule to launch the MyProfile activity before each test execution.
     */

    @Rule
    public ActivityScenarioRule<MyProfile> activityScenarioRule =
            new ActivityScenarioRule<>(MyProfile.class);

    /**
     * Verifies that the user's first name, last name, email, and phone number are displayed correctly
     * in the MyProfile activity.
     */

    @Test
    public void testProfileDisplay() {
        // Check if the first name, last name, email, and phone number are displayed correctly
        Espresso.onView(ViewMatchers.withId(R.id.first_name_text_view))
                .check(matches(withText("Rosy")));
        Espresso.onView(ViewMatchers.withId(R.id.last_name_text_view))
                .check(matches(withText("Budhathoki")));
        Espresso.onView(ViewMatchers.withId(R.id.email_text_view))
                .check(matches(withText("rosybudhathoki@example.com")));
        Espresso.onView(ViewMatchers.withId(R.id.phone_number_text_view))
                .check(matches(withText("0000000000")));
    }
}