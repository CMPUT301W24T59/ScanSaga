package com.example.scansaga.Views;

import android.content.Context;
import android.provider.Settings;

public class Utils {

    public static String getDeviceId(Context context) {
        // Get the Android ID
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }
}
