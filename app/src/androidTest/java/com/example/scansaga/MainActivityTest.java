package com.example.scansaga;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.mockito.AdditionalMatchers.not;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.scansaga.Model.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    /*
    private View decorView;
    @Before
    public void setUp() {
        activityRule.getScenario().onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                decorView = activity.getWindow().getDecorView();
            }
        });
    }

    @Test
    public void testAddNewUserWithInvalidEmail() {
        // Simulate user input with an invalid email
        onView(withId(R.id.Firstname_editText)).perform(replaceText("Rosy"), closeSoftKeyboard());
        onView(withId(R.id.lastname_editText)).perform(replaceText("Budhathoki"), closeSoftKeyboard());
        onView(withId(R.id.email_editText)).perform(replaceText("INVALID"), closeSoftKeyboard());
        onView(withId(R.id.phonenumber_editText)).perform(replaceText("0000000000"), closeSoftKeyboard());
        // Click the add user button
        onView(withId(R.id.confirm_button)).perform(click());
    }

    @Test
    public void testAddNewUserWithInvalidPhoneNumber() {
        // Simulate user input with an invalid phone number
        onView(withId(R.id.Firstname_editText)).perform(replaceText("Rosy"), closeSoftKeyboard());
        onView(withId(R.id.lastname_editText)).perform(replaceText("Budhathoki"), closeSoftKeyboard());
        onView(withId(R.id.email_editText)).perform(replaceText("rosybudhathoki@example.com"), closeSoftKeyboard());
        onView(withId(R.id.phonenumber_editText)).perform(replaceText("INVALID"), closeSoftKeyboard());

        // Click the add user button
        onView(withId(R.id.confirm_button)).perform(click());
        // Verify that the welcome_textview is not displayed
        onView(withId(R.id.Firstname_editText)).check(matches((isDisplayed())));

    }
    */
    @Test
    public void testAddNewUserWithValidInput() {
        // Simulate user input
        onView(withId(R.id.Firstname_editText)).perform(replaceText("Rosy"), closeSoftKeyboard());
        onView(withId(R.id.lastname_editText)).perform(replaceText("Budhathoki"), closeSoftKeyboard());
        onView(withId(R.id.email_editText)).perform(replaceText("rosybudhathoki@example.com"), closeSoftKeyboard());
        onView(withId(R.id.phonenumber_editText)).perform(replaceText("0000000000"), closeSoftKeyboard());

        // Click the add user button
        onView(withId(R.id.confirm_button)).perform(click());

        // Verify that the correct screen is displayed after navigation
        // For example, you can verify the presence of a specific UI element on the new screen
        onView(withId(R.id.welcome_textview)).check(matches(isDisplayed()));

    }


}
