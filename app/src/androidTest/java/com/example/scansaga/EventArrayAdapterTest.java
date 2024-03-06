package com.example.scansaga;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class EventArrayAdapterTest {
    private Context context;
    private EventArrayAdapter adapter;
    private ArrayList<Event> events;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        events = new ArrayList<>();
        // Create sample Event objects
        Event event1 = new Event("Event 1", "2024-03-06", "Venue 1", null);
        Event event2 = new Event("Event 2", "2024-03-07", "Venue 2", null);
        events.add(event1);
        events.add(event2);
        adapter = new EventArrayAdapter(context, events);
    }

    @After
    public void tearDown() {
        context = null;
        adapter = null;
        events.clear();
        events = null;
    }

    @Test
    public void testGetView() {
        // Test when convertView is null
        View view = adapter.getView(0, null, null);
        assertNotNull(view);

        // Verify that the correct data is displayed
        TextView eventNameTextView = view.findViewById(R.id.event_text);
        TextView eventTimeTextView = view.findViewById(R.id.time_text);
        TextView eventVenueTextView = view.findViewById(R.id.venue_text);

        assertNotNull(eventNameTextView);
        assertNotNull(eventTimeTextView);
        assertNotNull(eventVenueTextView);

        assertEquals("Event 1", eventNameTextView.getText().toString());
        assertEquals("2024-03-06", eventTimeTextView.getText().toString());
        assertEquals("Venue 1", eventVenueTextView.getText().toString());

        // Test when convertView is not null
        View recycledView = adapter.getView(1, view, null);
        assertNotNull(recycledView);

        // Verify that the recycled view is the same as the original view
        assertEquals(view, recycledView);

        // Verify that the correct data is displayed for the second event
        TextView recycledEventNameTextView = recycledView.findViewById(R.id.event_text);
        TextView recycledEventTimeTextView = recycledView.findViewById(R.id.time_text);
        TextView recycledEventVenueTextView = recycledView.findViewById(R.id.venue_text);

        assertNotNull(recycledEventNameTextView);
        assertNotNull(recycledEventTimeTextView);
        assertNotNull(recycledEventVenueTextView);

        assertEquals("Event 2", recycledEventNameTextView.getText().toString());
        assertEquals("2024-03-07", recycledEventTimeTextView.getText().toString());
        assertEquals("Venue 2", recycledEventVenueTextView.getText().toString());
    }
}

