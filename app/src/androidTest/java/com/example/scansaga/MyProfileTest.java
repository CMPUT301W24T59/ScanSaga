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

@RunWith(AndroidJUnit4.class)
public class MyProfileTest {

    @Rule
    public ActivityScenarioRule<MyProfile> activityScenarioRule =
            new ActivityScenarioRule<>(MyProfile.class);

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