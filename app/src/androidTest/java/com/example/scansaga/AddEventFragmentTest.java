//package com.example.scansaga;
//
//import androidx.fragment.app.testing.FragmentScenario;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.hamcrest.Matchers;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import android.widget.DatePicker;
//
//@RunWith(AndroidJUnit4.class)
//public class AddEventFragmentTest {
//
//    private FragmentScenario<AddEventFragment> fragmentScenario;
//
//    @Before
//    public void setUp() {
//        // Initialize the FragmentScenario
//        fragmentScenario = FragmentScenario.launchInContainer(AddEventFragment.class);
//        // Initialize AddEventFragment
//        addEventFragment = new AddEventFragment();
//        FragmentScenario.launchInContainer(AddEventFragment.class);
//    }
//
//    @Test
//    public void testEditTextFieldsAreDisplayed() {
//        // Check if EditText fields are displayed
//        onView(withId(R.id.edit_text_event_text)).check(matches(isDisplayed()));
//        onView(withId(R.id.edit_date_text)).check(matches(isDisplayed()));
//        onView(withId(R.id.edit_venue_text)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void testUploadPosterButtonIsDisplayed() {
//        // Check if Upload Poster button is displayed
//        onView(withId(R.id.upload_poster)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void testDatePickerDialogIsDisplayedOnClick() {
//        // Check if DatePickerDialog is displayed when Date EditText is clicked
//        onView(withId(R.id.edit_date_text)).perform(click());
//        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).check(matches(isDisplayed()));
//    }
//    private AddEventFragment addEventFragment;
//
//
//    @Test
//    public void testGenerateQRContent() {
//        // Test generateQRContent method
//        String qrContent = addEventFragment.generateQRContent("Event Name", "2024-03-08", "Event Venue");
//        assertEquals("Event: Event Name\nDate: 2024-03-08\nVenue: Event Venue", qrContent);
//    }
//
//    @Test
//    public void testGenerateUniqueFilename() {
//        // Test generateUniqueFilename method
//        String filename = addEventFragment.generateUniqueFilename();
//        assertNotNull(filename);
//    }
//}
//
