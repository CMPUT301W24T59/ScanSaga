package com.example.scansaga;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

import androidx.test.espresso.IdlingPolicies;
import org.junit.Before;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShowAllImagesTest {

    @Rule
    public ActivityScenarioRule<ShowAllImagesFromStorage> mActivityScenarioRule = new ActivityScenarioRule<>(ShowAllImagesFromStorage.class);

//    @Before
//    public void setUp() {
//        // Set the idle timeout for IdlingPolicies
//        IdlingPolicies.setMasterPolicyTimeout(10, TimeUnit.SECONDS);
//        IdlingPolicies.setIdlingResourceTimeout(10, TimeUnit.SECONDS);
//    }
//
//    @Test
//    public void testProgressBarVisibility() {
//        IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.SECONDS);
//        Espresso.onView(withId(R.id.progress)).check(matches(isDisplayed()));
//    }

    @Test
    public void testImageListNotNull() {
        ActivityScenario<ShowAllImagesFromStorage> scenario = mActivityScenarioRule.getScenario();
        scenario.onActivity(activity -> {
            assertNotNull(activity.imagelist);
        });
    }

    @Test
    public void testAdapterNotNull() {
        ActivityScenario<ShowAllImagesFromStorage> scenario = mActivityScenarioRule.getScenario();
        scenario.onActivity(activity -> {
            assertNotNull(activity.adapter);
        });
    }

    @Test
    public void testRecyclerViewNotNull() {
        ActivityScenario<ShowAllImagesFromStorage> scenario = mActivityScenarioRule.getScenario();
        scenario.onActivity(activity -> {
            assertNotNull(activity.recyclerView);
        });
    }

    @Test
    public void testProgressBarNotNull() {
        ActivityScenario<ShowAllImagesFromStorage> scenario = mActivityScenarioRule.getScenario();
        scenario.onActivity(activity -> {
            assertNotNull(activity.progressBar);
        });
    }

    @Test
    public void testRecyclerViewVisibility() {
        onView(withId(R.id.recyclerview))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testRecyclerViewItems() {
        // Wait for some time for data to load
        try {
            Thread.sleep(5000); // Adjust this time based on actual loading time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recyclerview))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(RecyclerViewActions.scrollToPosition(5));
    }
}


