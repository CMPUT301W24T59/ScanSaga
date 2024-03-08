//package com.example.scansaga;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.clearText;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.typeText;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//
//import android.os.Bundle;
//import android.widget.TextView;
//
//import androidx.fragment.app.testing.FragmentScenario;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.filters.LargeTest;
//import androidx.test.rule.ActivityTestRule;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//@LargeTest
//public class EditUserFragmentTest {
//
//    @Rule
//    public ActivityScenarioRule<EditUserFragment> activityRule = new ActivityScenarioRule<>(EditUserFragment.class);
//
//    @Test
//    public void testEditUserFragmentInitialization() {
//        User testUser = new User("John", "Doe", "john.doe@example.com", "1234567890");
//        String testDeviceId = "testDevice123";
//        Bundle args = new Bundle();
//        args.putString("deviceId", testDeviceId);
//        args.putSerializable("currentUser", testUser);
//
//        FragmentScenario<EditUserFragment> scenario = FragmentScenario.launchInContainer(EditUserFragment.class, args);
//        scenario.onFragment(fragment -> {
//            assertNotNull(fragment);
//            assertEquals(testUser, fragment.userToEdit);
//            assertEquals(testDeviceId, fragment.deviceId);
//            assertNull(fragment.listener);
//
//            TextView titleTextView = fragment.getView().findViewById(R.id.title_text_view);
//            assertEquals("Edit User", titleTextView.getText().toString());
//        });
//
//    @Test
//    public void testEmailValidation() {
//        EditUserFragment fragment = new EditUserFragment();
//        assertTrue(fragment.isValidEmail("test@example.com"));
//        assertFalse(fragment.isValidEmail("invalid-email"));
//    }
//
//    @Test
//    public void testPhoneNumberValidation() {
//        EditUserFragment fragment = new EditUserFragment();
//        assertTrue(fragment.isValidPhoneNumber("1234567890"));
//        assertFalse(fragment.isValidPhoneNumber("123")); // Invalid length
//        assertFalse(fragment.isValidPhoneNumber("abcdefg")); // Non-numeric characters
//    }
//
//    @Test
//    public void testEditUserFragmentSaveButton() {
//        User testUser = new User("John", "Doe", "john.doe@example.com", "1234567890");
//        String testDeviceId = "testDevice123";
//        EditUserFragment fragment = EditUserFragment.newInstance(testDeviceId, testUser);
//        fragment.show(activityRule.getActivity().getSupportFragmentManager(), "edit_user_fragment");
//        onView(withId(R.id.edit_text_firstname)).perform(clearText(), typeText("Jane"));
//        onView(withId(R.id.edit_text_lastname)).perform(clearText(), typeText("Doe"));
//        onView(withId(R.id.edit_text_email)).perform(clearText(), typeText("jane.doe@example.com"));
//        onView(withId(R.id.edit_text_phone)).perform(clearText(), typeText("9876543210"));
//        onView(withText("Save")).perform(click());
//        // Check if the user information is updated correctly
//        // Assert the expected behavior based on the update operation
//    }
//
//    @Test
//    public void testUpdateUserInFirestore() {
//        // This test assumes Firestore is mocked or running in a test environment
//        // Mock Firestore setup or use Firebase Test Lab for Firebase related tests
//        // This is an example of how to test the Firestore update operation
//        EditUserFragment fragment = new EditUserFragment();
//        fragment.updateUserInFirestore("John", "Doe", "test@example.com", "1234567890");
//        // Check if Firestore update was successful
//        // Assert the expected behavior based on Firestore setup
//    }
//}
//
