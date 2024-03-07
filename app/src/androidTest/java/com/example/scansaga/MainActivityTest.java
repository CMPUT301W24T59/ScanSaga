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

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalMatchers.not;

import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    private FirebaseFirestore db;
    private CollectionReference usernamesRef;
    private FirestoreTaskIdlingResource idlingResource;

    @Before
    public void setUp() {
        MainActivity.setRunningTest(true);
        idlingResource = new FirestoreTaskIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void tearDown() {
        MainActivity.setRunningTest(false);
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    @Test
    public void testAddNewUserWithInvalidFirstName() {
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
    public void testAddNewUserWithInvalidEmailInput() {
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
    public void testAddNewUserWithInvalidPhoneNumber() {
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
    public void testAddNewUserWithInvalidLastName() {
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
    public void testUserInDataBase() {
        // Launch the Activity
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        // Perform UI actions
        onView(withId(R.id.Firstname_editText)).perform(replaceText("Android"), closeSoftKeyboard());
        onView(withId(R.id.lastname_editText)).perform(replaceText("Test"), closeSoftKeyboard());
        onView(withId(R.id.email_editText)).perform(replaceText("user@example.com"), closeSoftKeyboard());
        onView(withId(R.id.phonenumber_editText)).perform(replaceText("1234567890"), closeSoftKeyboard());
        onView(withId(R.id.confirm_button)).perform(click());

        // Assume that click() triggers a Firestore operation and it is tracked by IdlingResource
        // Espresso will wait for IdlingResource to be idle before moving on

        // Get the Firestore instance
        db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        // Query Firestore for the user
        Task<QuerySnapshot> task = usersRef.whereEqualTo("Firstname", "Android")
                .whereEqualTo("Lastname", "Test")
                .whereEqualTo("Email", "user@example.com")
                .whereEqualTo("PhoneNumber", "1234567890")
                .get();

        idlingResource.increment();

        task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (!querySnapshot.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            // Assert that the user was added and then delete the document
                            assertEquals("Android", documentSnapshot.getString("Firstname"));

                            // Delete the user after verification to clean up
                            documentSnapshot.getReference().delete();
                        }
                    } else {
                        fail("No user found to delete");
                    }
                } else {
                    fail("Failed to query user for deletion");
                }
                // Decrement IdlingResource as operation is finished
                idlingResource.decrement();
            }
        });

        // Unregister the IdlingResource after the test
        IdlingRegistry.getInstance().unregister(idlingResource);
    }
}