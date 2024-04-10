package com.example.scansaga.Model;

/**
 * A utility class for managing the geolocation data of the application.
 * It provides static methods to set and get the latitude, longitude, and permissions
 * status associated with geolocation. This class uses a private constructor to prevent
 * instantiation, functioning solely through static members and methods.
 */
public class GeoLocationManager {

    private static double latitude = 0.0;
    private static double longitude = 0.0;

    // Private constructor to prevent instantiation
    private GeoLocationManager() {}

    // Static method to update the latitude
    public static void setLatitude(double lat) {
        latitude = lat;
    }

    // Static method to update the longitude
    public static void setLongitude(double lon) {
        longitude = lon;
    }

    // Static method to get the current latitude
    public static double getLatitude() {
        return latitude;
    }

    // Static method to get the current longitude
    public static double getLongitude() {
        return longitude;
    }

}
