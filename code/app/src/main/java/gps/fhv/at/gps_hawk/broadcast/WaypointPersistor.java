package gps.fhv.at.gps_hawk.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.tasks.IAsyncTaskCaller;
import gps.fhv.at.gps_hawk.tasks.UploadWaypointsTask;
import gps.fhv.at.gps_hawk.workers.DbFacade;

/**
 * Author: Philip Heimböck
 * Date: 30.10.15
 */
public class WaypointPersistor extends BroadcastReceiver {

    int nrWaypoints = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Waypoint waypoint = (Waypoint) intent.getSerializableExtra(Constants.EXTRA_WAYPOINT);

        // First persist the waypoint
        persist(context, waypoint);

        // Now upload it
        upload(waypoint, context);
    }

    private void persist(Context context, Waypoint waypoint) {
        DbFacade dbFacade = DbFacade.getInstance(context);
        dbFacade.saveEntity(waypoint);
    }

    private void upload(Waypoint waypoint, Context context) {
        // Bulk upload waypoints
        ++nrWaypoints;
        if(nrWaypoints < 30) {
            return;
        }

        // Can the upload be started directly?
        boolean allowed = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.PREF_ALLOW_TRACKING_WITHOUT_WLAN, false);
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connManager.getActiveNetworkInfo();

        if(allowed && activeInfo != null && activeInfo.isConnected()) {
            // Start the upload activity
            new UploadWaypointsTask(context).execute();
        }
    }
}
