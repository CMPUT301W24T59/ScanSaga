package com.example.scansaga.Model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class GeoLocation extends Context {
    private static Location currentLocation = null;
    private static FusedLocationProviderClient locationClient = null;

    // Check if location permissions are granted
    public static boolean checkPermissions(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Request location permissions
    public static void requestPermissions(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
    }

    // Initialize the location client and start location updates
    public void enableLocation(Context context) {
        if (locationClient == null) {
            locationClient = LocationServices.getFusedLocationProviderClient(context);
        }

        if (checkPermissions(context)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationClient.getLastLocation().addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations, this can be null.
                    if (location != null) {
                        currentLocation = location;
                    }
                }
            });
        }
    }

    // Method to manually stop location updates if needed
    public void disableLocation() {
        // Here you would implement logic to stop updates, for this scenario it might not be strictly necessary
        // since we're only fetching the location once when enabled.
    }

    // Getter for the current location
    public static Location getCurrentLocation() {
        return currentLocation;
    }
}
