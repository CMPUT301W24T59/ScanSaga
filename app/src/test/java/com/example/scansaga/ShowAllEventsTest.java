package com.example.scansaga;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

@RunWith(MockitoJUnitRunner.class)
public class ShowAllEventsTest {

    @Mock
    private FirebaseFirestore mockFirestore;

    @Mock
    private CollectionReference mockEventsRef;

    @Mock
    private EventArrayAdapter mockEventAdapter;

    @InjectMocks
    private ShowAllEvents showAllEvents = new ShowAllEvents();

    @Before
    public void setUp() {
        // Assume ShowAllEvents initializes Firestore in onCreate or elsewhere
        when(mockFirestore.collection("events")).thenReturn(mockEventsRef);
    }

    @Test
    public void fetchEventsFromFirestore_VerifiesMethodCalls() {
        // Call the method under test
        showAllEvents.fetchEventsFromFirestore();

        // Verify that the Firestore collection "events" was accessed
        verify(mockFirestore).collection("events");
        // Verify that a snapshot listener was added to the events collection reference
        verify(mockEventsRef).addSnapshotListener(any());
    }

    @Test
    public void deleteEventFromFirestore_VerifiesDeletion() {
        // Prepare a dummy event
        Event dummyEvent = new Event("Sample Event", "01-01-2020", "Sample Venue", "sampleUrl");

        // Call the method under test with the dummy event
        showAllEvents.deleteEventFromFirestore(dummyEvent);

        // Verify that the delete method was called on the correct document reference
        verify(mockEventsRef).document(dummyEvent.getName()).delete();
    }
}
