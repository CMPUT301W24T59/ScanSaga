package com.example.scansaga.Views;

import android.content.Context;
import android.provider.Settings;

/**
 * A utility class providing a set of static methods for common operations across the application.
 */
public class Utils {

    /**
     * Retrieves the unique Android device ID. This ID is used for identifying the device
     * and can be useful in scenarios where a unique identifier for the device is required,
     * such as tracking device-specific data or settings.
     *
     * @param context The context of the caller, used to access the ContentResolver for querying device settings.
     * @return A string representing the unique Android device ID.
     */
    public static String getDeviceId(Context context) {
        // Get the Android ID
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }
}
