package gps.fhv.at.gps_hawk.tasks;

import android.os.AsyncTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import gps.fhv.at.gps_hawk.communication.ExportClient;
import gps.fhv.at.gps_hawk.domain.ExportContext;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.domain.Vehicle;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.workers.DbFacade;
import gps.fhv.at.gps_hawk.workers.VolatileInstancePool;

/**
 * Created by Tobias on 25.10.2015.
 */
public class ExportTask extends AsyncTask<Void, Void, String> {

    private ExportContext mExpContext;

    public ExportTask(ExportContext expContext, final IAsyncTaskCaller<Void, Boolean> caller) {
        mExpContext = expContext;
    }

    @Override
    protected String doInBackground(Void... params) {

        DbFacade dbFacade = DbFacade.getInstance(mExpContext.getContext());

        // Mark unexported Waypoints as "ExportNow"
        int count = dbFacade.markWaypoints(0, 2);

        if (count > 0) {

            // Get all Waypoints from DB to export
            mExpContext.setWaypointList(dbFacade.getAllWaypoints2Update());

            // TODO: Test only
//            ArrayList<Track> allTracks = (ArrayList<Track>) dbFacade.selectWhere(null,Track.class);

            // Insert Tracks and Vehicles as Objects
            setDomainObjects(dbFacade,mExpContext);

            // Send via Web
            ExportClient client = new ExportClient(mExpContext.getContext());
            client.exportCollectedWaypoints(mExpContext);

            // Mark "ExportNow" Waypoints as "Exported"
            dbFacade.markWaypoints(2, 1);
        }

        return "";
    }

    private void setDomainObjects(DbFacade dbFacade, ExportContext mExpContext) {
        HashMap<Integer, Vehicle> mapVehicles = new HashMap<>();
        HashMap<Integer, Track> mapTracks = new HashMap<>();

        HashSet<Integer> setTracks = new HashSet<>();
        List<Vehicle> listVehicle = VolatileInstancePool.getInstance().getAllRegistered(Vehicle.class);
        for (Vehicle v : listVehicle) {
            mapVehicles.put(v.getId(), v);
        }

        for (Waypoint w : (ArrayList<Waypoint>) mExpContext.getWaypointList()) {
            if (w.getTrackId() != 0 && !setTracks.contains(w.getTrackId())) {
                setTracks.add(w.getTrackId());
            }
            if (w.getVehicleId() != 0 && mapVehicles.containsKey(w.getVehicleId()))
                w.setVehicle(mapVehicles.get(w.getVehicleId()));
        }

        for (int trackid : setTracks) {
            Track t = dbFacade.select(trackid, Track.class);
            if (t != null) mapTracks.put(t.getId(), t);
        }

        for (Waypoint w : (ArrayList<Waypoint>) mExpContext.getWaypointList()) {
            if (w.getTrackId() != 0) w.setTrack(mapTracks.get(w.getTrackId()));
        }

    }
}
