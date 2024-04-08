package com.example.scansaga;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.scansaga.Model.Event;

/**
 * Unit tests for the Event class.
 */
public class EventTest {

    private static final String INITIAL_NAME = "Event Name";
    private static final String INITIAL_DATE = "2021-01-01";
    private static final String INITIAL_VENUE = "Event Venue";
    private static final String INITIAL_IMAGE_URL = "https://example.com/image.jpg";
    private static final String INITIAL_LIMIT = "22";
    private static final String INITIAL_QR_URL = "ASDFGHJKL";

    private Event event;

    /**
     * Sets up the test fixture before each test method.
     */
    @Before
    public void setUp() {
        event = new Event(INITIAL_NAME, INITIAL_DATE, INITIAL_VENUE, INITIAL_IMAGE_URL, INITIAL_QR_URL);
    }

    /**
     * Test for verifying the correct retrieval of the event name.
     */
    @Test
    public void testNameGetter() {
        assertEquals("Event name should be retrieved correctly", INITIAL_NAME, event.getName());
    }

    /**
     * Test for verifying the correct update of the event name.
     */
    @Test
    public void testNameSetter() {
        String newName = "New Event Name";
        event.setName(newName);
        assertEquals("Event name should be updated correctly", newName, event.getName());
    }

    /**
     * Test for verifying the correct retrieval of the event date.
     */
    @Test
    public void testDateGetter() {
        assertEquals("Event date should be retrieved correctly", INITIAL_DATE, event.getDate());
    }

    /**
     * Test for verifying the correct update of the event date.
     */
    @Test
    public void testDateSetter() {
        String newDate = "2022-02-02";
        event.setDate(newDate);
        assertEquals("Event date should be updated correctly", newDate, event.getDate());
    }

    /**
     * Test for verifying the correct retrieval of the event venue.
     */
    @Test
    public void testVenueGetter() {
        assertEquals("Event venue should be retrieved correctly", INITIAL_VENUE, event.getVenue());
    }

    /**
     * Test for verifying the correct update of the event venue.
     */
    @Test
    public void testVenueSetter() {
        String newVenue = "New Event Venue";
        event.setVenue(newVenue);
        assertEquals("Event venue should be updated correctly", newVenue, event.getVenue());
    }

    /**
     * Test for verifying the correct retrieval of the event's image URL.
     */
    @Test
    public void testImageUrlGetter() {
        assertEquals("Image URL should be retrieved correctly", INITIAL_IMAGE_URL, event.getImageUrl());
    }

    /**
     * Test for verifying the correct update of the event's image URL.
     */
    @Test
    public void testImageUrlSetter() {
        String newImageUrl = "https://example.com/newimage.jpg";
        event.setImageUrl(newImageUrl);
        assertEquals("Image URL should be updated correctly", newImageUrl, event.getImageUrl());
    }
    @Test
    public void testSetterLimit() {
        String newName = "20";
        event.setLimit(newName);
        assertEquals("Event name should be updated correctly", newName, event.getLimit());
    }
    @Test
    public void testNameQrUrl() {
        String newName = "qwerty";
        event.setQrCodeUrl(newName);
        assertEquals("Event name should be updated correctly", newName, event.getQrUrl());
    }
}