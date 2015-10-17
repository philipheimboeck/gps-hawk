package gps.fhv.at.gps_hawk.helper;

import android.content.Context;
import android.content.SharedPreferences;

import gps.fhv.at.gps_hawk.Constants;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public class TokenHelper {

    private final Context mContext;

    public TokenHelper(Context context) {
        this.mContext = context;
    }

    public String getToken() {
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(Constants.PREF_DEVICE_TOKEN, null);
    }

    public void setToken(String token) {
        // Save token to settings
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.PREF_DEVICE_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.PREF_DEVICE_TOKEN, token);
        editor.apply();
    }
}
