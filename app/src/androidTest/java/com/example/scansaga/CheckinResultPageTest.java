package com.example.scansaga;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.scansaga.R;
import com.example.scansaga.Views.CheckinResultPage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.content.Intent;

@RunWith(AndroidJUnit4.class)
public class CheckinResultPageTest {
    private Intent successIntent;
    private Intent failIntent;

    @Before
    public void createIntents() {
        // Create an intent with extras indicating check-in success
        successIntent = new Intent(ApplicationProvider.getApplicationContext(), CheckinResultPage.class);
        successIntent.putExtra("checkInMessage", "Check-in Successful!");
        successIntent.putExtra("checkInSuccess", true);

        // Create an intent with extras indicating check-in failure
        failIntent = new Intent(ApplicationProvider.getApplicationContext(), CheckinResultPage.class);
        failIntent.putExtra("checkInMessage", "Check-in Failed!");
        failIntent.putExtra("checkInSuccess", false);
    }

    @Test
    public void whenCheckInSuccess_DisplaySuccessMessageAndImage() {
        // Launch activity with success intent
        try (ActivityScenario<CheckinResultPage> scenario = ActivityScenario.launch(successIntent)) {
            // Check if the success message is displayed
            onView(withId(R.id.check_in_result_textview))
                    .check(matches(withText("Check-in Successful!")));

            // Check if the success image is visible
            onView(withId(R.id.check_in_sucess_image))
                    .check(matches(isDisplayed()));
        }
    }

}
