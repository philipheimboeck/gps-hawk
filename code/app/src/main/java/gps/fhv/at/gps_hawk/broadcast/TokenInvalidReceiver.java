package gps.fhv.at.gps_hawk.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
        if(TokenHelper.getToken(context) != null) {
            // Remove the token
            TokenHelper.setToken(context, null);

            // Return to login screen
            Intent login = new Intent(context, LoginActivity.class);
            login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
    }
}
