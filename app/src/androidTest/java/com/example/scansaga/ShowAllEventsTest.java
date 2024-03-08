//package com.example.scansaga;
//
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.espresso.Espresso;
//import androidx.test.espresso.assertion.ViewAssertions;
//import androidx.test.espresso.matcher.ViewMatchers;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.filters.LargeTest;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//import static androidx.test.espresso.Espresso.onData;
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static org.hamcrest.Matchers.instanceOf;
//import static org.hamcrest.Matchers.anything;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//
///**
// * Tests for verifying the functionalities of the ShowAllEvents activity within the ScanSaga application.
// * This includes displaying a list of events and the ability to delete an event from this list.
// */
//@RunWith(AndroidJUnit4.class)
//@LargeTest
//public class ShowAllEventsTest {
//
//    /**
//     * Rule to launch the ShowAllEvents activity before each test execution.
//     */
//    @Rule
//    public ActivityScenarioRule<ShowAllEvents> activityScenarioRule =
//            new ActivityScenarioRule<>(ShowAllEvents.class);
//
//
//
//    @Mock
//    private FirebaseFirestore mockFirestore;
//
//    @Mock
//    private CollectionReference mockEventsRef;
//
//    @Mock
//    private EventArrayAdapter mockEventAdapter;
//
//    @InjectMocks
//    private ShowAllEvents showAllEvents = new ShowAllEvents();
//
//
//    @Before
//    public void setUp() throws Exception {
//        activityScenario = ActivityScenario.launch(ShowAllEvents.class);
//        // Wait for activity to be launched
//        Espresso.onView(ViewMatchers.withId(R.id.listView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
//        // Assume ShowAllEvents initializes Firestore in onCreate or elsewhere
//        when(mockFirestore.collection("events")).thenReturn(mockEventsRef);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        activityScenario.close();
//    }
//
//    @Test
//    public void fetchEventsFromFirestore_VerifiesMethodCalls() {
//        // Call the method under test
//        showAllEvents.fetchEventsFromFirestore();
//
//        // Verify that the Firestore collection "events" was accessed
//        verify(mockFirestore).collection("events");
//        // Verify that a snapshot listener was added to the events collection reference
//        verify(mockEventsRef).addSnapshotListener(any());
//    }
//
//    @Test
//    public void deleteEventFromFirestore_VerifiesDeletion() {
//        // Prepare a dummy event
//        Event dummyEvent = new Event("Sample Event", "01-01-2020", "Sample Venue", "sampleUrl");
//
//        // Call the method under test with the dummy event
//        showAllEvents.deleteEventFromFirestore(dummyEvent);
//
//        // Verify that the delete method was called on the correct document reference
//        verify(mockEventsRef).document(dummyEvent.getName()).delete();
//    }
//
//    /**
//     * Tests if the ListView within the ShowAllEvents activity correctly displays events.
//     */
//    @Test
//    public void testListViewDisplaysEvents() {
//        // Check if the ListView is displayed
//        onView(ViewMatchers.withId(R.id.listView)).check(matches(isDisplayed()));
//
//        // Check if the ListView displays at least one event
//        onData(instanceOf(Event.class))
//                .inAdapterView(withId(R.id.listView))
//                .atPosition(0)
//                .check(matches(isDisplayed()));
//    }
//
//    /**
//     * Tests if the delete button within the ShowAllEvents activity correctly deletes an event from the ListView.
//     */
//
//    @Test
//    public void testDeleteButton() {
//        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());
//        onView(withId(R.id.delete_event_button)).perform(click());
//    }
//
//    @Test
//    public void testListViewDisplayed() {
//        onView(withId(R.id.listView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
//    }
//
//    @Test
//    public void testDeleteButtonDisplayed() {
//        onView(withId(R.id.button_delete)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
//    }
//}
