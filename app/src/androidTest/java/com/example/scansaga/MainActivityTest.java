package com.example.scansaga;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.mockito.AdditionalMatchers.not;

import android.view.View;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    private FirebaseFirestore db;
    private CollectionReference usernamesRef;
    @Before
    public void setUp() {
        MainActivity.setRunningTest(true);
    }
    @After
    public void tearDown() {
        MainActivity.setRunningTest(false);
    }

    @Test
    public void testAddNewUserWithInvalidFirstName(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.Firstname_editText)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.lastname_editText)).perform(replaceText("AndroidTest"), closeSoftKeyboard());
        onView(withId(R.id.email_editText)).perform(replaceText("user@example.com"), closeSoftKeyboard());
        onView(withId(R.id.phonenumber_editText)).perform(replaceText("1234567890"), closeSoftKeyboard());
        // Click the add user button
        onView(withId(R.id.confirm_button)).perform(click());
        // This checks to make sure the button is still present, simply, that we are still on the same activity
        onView(withId(R.id.Firstname_editText)).check(matches(isDisplayed()));
    }
    @Test
    public void testAddNewUserWithInvalidEmailInput(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.Firstname_editText)).perform(replaceText("AndroidTest"), closeSoftKeyboard());
        onView(withId(R.id.lastname_editText)).perform(replaceText("Ran"), closeSoftKeyboard());
        onView(withId(R.id.phonenumber_editText)).perform(replaceText("0000000000"), closeSoftKeyboard());
        // Click the add user button
        onView(withId(R.id.confirm_button)).perform(click());
        // This checks to make sure the button is still present, simply, that we are still on the same activity
        onView(withId(R.id.Firstname_editText)).check(matches(isDisplayed()));
    }
    @Test
    public void testAddNewUserWithInvalidPhoneNumber(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.Firstname_editText)).perform(replaceText("AndroidTest"), closeSoftKeyboard());
        onView(withId(R.id.lastname_editText)).perform(replaceText("Ran"), closeSoftKeyboard());
        onView(withId(R.id.email_editText)).perform(replaceText("user@example.com"), closeSoftKeyboard());
        onView(withId(R.id.phonenumber_editText)).perform(replaceText(""), closeSoftKeyboard());
        // Click the add user button
        onView(withId(R.id.confirm_button)).perform(click());
        // This checks to make sure the button is still present, simply, that we are still on the same activity
        onView(withId(R.id.Firstname_editText)).check(matches(isDisplayed()));
    }
    @Test
    public void testAddNewUserWithInvalidLastName(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.Firstname_editText)).perform(replaceText("AndroidTest"), closeSoftKeyboard());
        onView(withId(R.id.lastname_editText)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.email_editText)).perform(replaceText("user@example.com"), closeSoftKeyboard());
        onView(withId(R.id.phonenumber_editText)).perform(replaceText("1234567890"), closeSoftKeyboard());
        // Click the add user button
        onView(withId(R.id.confirm_button)).perform(click());
        // This checks to make sure the button is still present, simply, that we are still on the same activity
        onView(withId(R.id.Firstname_editText)).check(matches(isDisplayed()));
    }


    @Test
    public void testUserInDataBase() throws InterruptedException {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.Firstname_editText)).perform(replaceText("Android"), closeSoftKeyboard());
        onView(withId(R.id.lastname_editText)).perform(replaceText("Test"), closeSoftKeyboard());
        onView(withId(R.id.email_editText)).perform(replaceText("user@example.com"), closeSoftKeyboard());
        onView(withId(R.id.phonenumber_editText)).perform(replaceText("1234567890"), closeSoftKeyboard());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        usernamesRef = db.collection("users");
        // Click the add user button
        onView(withId(R.id.confirm_button)).perform(click());
        // Wait to allow Firebase to update
        Thread.sleep(5000);
        usernamesRef.whereEqualTo("Firstname", "Android");
    }
}
