package com.example.scansaga.Model;

public class GeoLocationManager {

    private static boolean permissions = false;
    private static double latitude = 0.0;
    private static double longitude = 0.0;

    // Private constructor to prevent instantiation
    private GeoLocationManager() {}

    // Static method to update the latitude
    public static void setLatitude(double lat) {
        permissions = true;
        latitude = lat;
    }

    // Static method to update the longitude
    public static void setLongitude(double lon) {
        permissions = true;
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
    public static void setPermissions(Boolean state){
        permissions = state;
    }
    public static Boolean checkPermissions(){
        return permissions;
    }
}
