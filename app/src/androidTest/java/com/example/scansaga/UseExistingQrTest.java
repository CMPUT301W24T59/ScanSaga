package com.example.scansaga;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.scansaga.R;
import com.example.scansaga.Views.UseExistingQr;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UseExistingQrTest {

    @Rule
    public ActivityScenarioRule<UseExistingQr> activityRule =
            new ActivityScenarioRule<>(UseExistingQr.class);

    @Test
    public void testQrDisplays() {
        // Check that the RecyclerView is visible
        onView(withId(R.id.recyclerview)).check(matches(isDisplayed()));
    }
}
