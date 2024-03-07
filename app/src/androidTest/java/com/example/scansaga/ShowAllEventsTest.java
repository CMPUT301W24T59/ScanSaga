//package com.example.scansaga;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//import static java.util.regex.Pattern.matches;
//
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.filters.LargeTest;
//
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RunWith(AndroidJUnit4.class)
//@LargeTest
//public class ShowAllEventsTest {
//    @Rule
//    public ActivityScenarioRule<ShowAllEvents> activityScenarioRule =
//            new ActivityScenarioRule<>(ShowAllEvents.class);
//
//    private ActivityScenario<ShowAllEvents> activityScenario;
//
//    @Before
//    public void setUp() {
//        activityScenario = activityScenarioRule.getScenario();
//    }
//
//    @Test
//    public void testListViewDisplayed() {
//        activityScenario.onActivity(activity -> {
//            onView(withId(R.id.listView)).check(matches(isDisplayed()));
//        });
//    }
//
//    @Test
//    public void testListViewItemClick() {
//        activityScenario.onActivity(activity -> {
//            onView(withId(R.id.listView)).perform(click());
//        });
//    }
//
//    @Test
//    public void testDeleteButtonDisplayed() {
//        activityScenario.onActivity(activity -> {
//            onView(withId(R.id.listView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
//
//            // Now check if the delete button is displayed
//            onView(withId(R.id.button_delete)).check(matches(isDisplayed()));
//        });
//    }
//
//    @Test
//    public void testDeleteButtonClick() {
//        activityScenario.onActivity(activity -> {
//            onView(withId(R.id.button_delete)).perform(click());
//        });
//    }
//
//    @Test
//    public void testFetchAndDeleteEventsFromFirestore() {
//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//        CollectionReference eventsRef = firestore.collection("events");
//
//        // Add a sample event to Firestore
//        Map<String, Object> event = new HashMap<>();
//        event.put("Date", "2024-03-07");
//        event.put("Venue", "Test Venue");
//        // Add a placeholder bitmap (you can use a real QR code image)
//        byte[] dummyImageBytes = new byte[0]; // Dummy image bytes
//        event.put("QR", dummyImageBytes);
//        eventsRef.document("TestEvent").set(event);
//
//        // Wait for Firestore to sync
//        try {
//            Thread.sleep(2000); // Wait for 2 seconds to ensure data is synced
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // Verify that the event is fetched and displayed in the ListView
//        activityScenario.onActivity(activity -> {
//            onView(withId(R.id.listView)).check(matches(isDisplayed()));
//            // Check if the event is fetched and displayed
//            boolean eventFound = false;
//            for (QueryDocumentSnapshot document : activity.eventList) {
//                if (document.getId().equals("TestEvent")) {
//                    eventFound = true;
//                    break;
//                }
//            }
//            assertTrue(eventFound);
//        });
//
//        // Click on the delete button to delete the event
//        onView(withId(R.id.button_delete)).perform(click());
//
//        // Wait for Firestore to sync after deletion
//        try {
//            Thread.sleep(2000); // Wait for 2 seconds to ensure deletion is synced
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // Verify that the event is deleted from Firestore and no longer displayed in the ListView
//        activityScenario.onActivity(activity -> {
//            // Check if the event is removed from the list
//            boolean eventFound = false;
//            for (QueryDocumentSnapshot document : activity.eventList) {
//                if (document.getId().equals("TestEvent")) {
//                    eventFound = true;
//                    break;
//                }
//            }
//            assertFalse(eventFound);
//        });
//    }
//}
