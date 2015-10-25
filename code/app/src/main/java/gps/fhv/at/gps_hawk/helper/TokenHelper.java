package gps.fhv.at.gps_hawk.helper;

import android.content.Context;
import android.content.SharedPreferences;

import gps.fhv.at.gps_hawk.Constants;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public abstract class TokenHelper {

    public static String getToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(Constants.PREF_DEVICE_TOKEN, null);
    }

    public static void setToken(Context context, String token) {
        // Save token to settings
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.PREF_DEVICE_TOKEN, token);
        editor.apply();
    }
}
