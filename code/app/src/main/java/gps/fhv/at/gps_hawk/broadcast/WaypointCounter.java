package gps.fhv.at.gps_hawk.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Author: Philip Heimböck
 * Date: 30.10.15
 */
public class WaypointCounter extends BroadcastReceiver {

    private static int counter = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        counter++;
    }

    public static int count() {
        return counter;
    }

    public static void resetCounter() {
        counter = 0;
    }
}
