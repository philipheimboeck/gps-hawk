package gps.fhv.at.gps_hawk.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.workers.DbFacade;

/**
 * Author: Philip Heimböck
 * Date: 30.10.15
 */
public class WaypointPersistor extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Waypoint waypoint = (Waypoint) intent.getSerializableExtra(Constants.EXTRA_WAYPOINT);

        // First persist the waypoint
        persist(context, waypoint);

        // Now upload it
        upload(waypoint);
    }

    private void persist(Context context, Waypoint waypoint) {
        DbFacade dbFacade = DbFacade.getInstance(context);
        dbFacade.saveEntity(waypoint);
    }

    private void upload(Waypoint waypoint) {
        // Todo: Upload multiple waypoints at once
    }
}
