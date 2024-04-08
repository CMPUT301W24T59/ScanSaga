package com.example.scansaga;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.scansaga.Model.GeoLocationManager;

public class GeoLocationTest {

    // Set up some constants for testing purposes
    private static final double DELTA = 1e-15;
    private static final double TEST_LATITUDE = 45.0;
    private static final double TEST_LONGITUDE = 90.0;

    // This method resets the GeoLocationManager's state before each test
    @BeforeClass
    public static void setUpClass() {
        GeoLocationManager.setPermissions(false); // Ensure permissions are reset
        GeoLocationManager.setLatitude(0.0); // Reset latitude
        GeoLocationManager.setLongitude(0.0); // Reset longitude
    }

    @Test
    public void testLatitude() {
        GeoLocationManager.setLatitude(TEST_LATITUDE);
        assertEquals("Latitude should be set to " + TEST_LATITUDE, TEST_LATITUDE, GeoLocationManager.getLatitude(), DELTA);
    }

    @Test
    public void testLongitude() {
        GeoLocationManager.setLongitude(TEST_LONGITUDE);
        assertEquals("Longitude should be set to " + TEST_LONGITUDE, TEST_LONGITUDE, GeoLocationManager.getLongitude(), DELTA);
    }

    @Test
    public void testPermissions() {
        // Initially, permissions should be false
        GeoLocationManager.setPermissions(false);
        assertFalse("Permissions should initially be false", GeoLocationManager.checkPermissions());

        // Set permissions to true and check
        GeoLocationManager.setPermissions(true);
        assertTrue("Permissions should be set to true", GeoLocationManager.checkPermissions());

        // Reset permissions to false and check
        GeoLocationManager.setPermissions(false);
        assertFalse("Permissions should be reset to false", GeoLocationManager.checkPermissions());
    }
}
