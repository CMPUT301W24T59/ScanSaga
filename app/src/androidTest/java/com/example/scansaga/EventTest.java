package com.example.scansaga;

import android.graphics.Bitmap;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.scansaga.Model.Event;

/**
 * Test class for {@link Event}.
 */
public class EventTest {

    /**
     * Tests the Event constructor for proper instantiation.
     */
    @Test
    public void testEventConstructor() {
        // Given
        String name = "Test Event";
        String date = "2024-03-05";
        String venue = "Test Venue";
        Bitmap qrCodeBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        // When
        Event event = new Event(name, date, venue, null);

        // Then
        assertNotNull(event);
    }

    /**
     * Tests the setter methods of the {@link Event} class.
     */
    @Test
    public void testEventSetters() {
        // Given
        Event event = new Event("Old Event", "3/11/2024", "Old Venue", null);

        // When
        String newName = "New Event";
        String newDate = "3/11/2024";
        String newVenue = "New Venue";
        Bitmap newQrCodeBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        event.setName(newName);
        event.setDate(newDate);
        event.setVenue(newVenue);
        event.setQrCodeBitmap(newQrCodeBitmap);

        // Then
        assertEquals(newName, event.getName());
        assertEquals(newDate, event.getDate());
        assertEquals(newVenue, event.getVenue());
        assertNotNull(event.getQrCodeBitmap());
    }
}
