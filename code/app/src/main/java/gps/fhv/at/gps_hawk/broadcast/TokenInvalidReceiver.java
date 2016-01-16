package gps.fhv.at.gps_hawk.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.activities.LoginActivity;
import gps.fhv.at.gps_hawk.helper.TokenHelper;

/**
 * Author: Philip Heimböck
 * Date: 13.11.15
 */
public class TokenInvalidReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // Do only when there is already a token (because when logging in it is not necessary)
        if (TokenHelper.getToken(context) != null) {
            Log.i(Constants.PREFERENCES, "Removing token because it is invalid");

            // Remove the token
            TokenHelper.setToken(context, null);

            // Restart app
            Intent i = context.getApplicationContext().getPackageManager()
                    .getLaunchIntentForPackage(context.getApplicationContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.getApplicationContext().startActivity(i);
        }
    }
}
