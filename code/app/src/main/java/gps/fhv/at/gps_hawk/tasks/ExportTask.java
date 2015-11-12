package gps.fhv.at.gps_hawk.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.communication.ExportClient;
import gps.fhv.at.gps_hawk.domain.ExportContext;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.domain.Vehicle;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.exceptions.UnExpectedResultException;
import gps.fhv.at.gps_hawk.workers.DbFacade;
import gps.fhv.at.gps_hawk.workers.VolatileInstancePool;

/**
 * Created by Tobias on 25.10.2015.
 */
public class ExportTask extends AsyncTask<Void, Void, String> {

    private ExportContext mExpContext;
    private IAsyncTaskCaller<Void, String> mCaller;

    public ExportTask(ExportContext expContext, final IAsyncTaskCaller<Void, String> caller) {
        mExpContext = expContext;
        mCaller = caller;
    }

    protected void onPreExecute() {
        mCaller.onPreExecute();
    }

    protected void onPostExecute(final String result) {
        mCaller.onPostExecute(result);
    }

    @Override
    protected String doInBackground(Void... params) {

        DbFacade dbFacade = DbFacade.getInstance(mExpContext.getContext());

        // Mark unexported Waypoints as "ExportNow"
        int count = dbFacade.markExportable(0, 2, mExpContext.getT());

        if (count > 0) {

            // Get all Waypoints from DB to export
            mExpContext.setExportList(dbFacade.getAllEntities2Export(mExpContext.getT()));

            // Insert Tracks and Vehicles as Objects
            if (mExpContext.getT().equals(Waypoint.class)) {
                setDomainObjects(dbFacade, mExpContext);
            }

            // Send via Web
            ExportClient client = new ExportClient(mExpContext.getContext());
            try {

                boolean result = client.exportCollectedWaypoints(mExpContext);

                // If no successful - be sure to reset flags
                if (!result) {
                    throw new UnExpectedResultException("An unexpected result occured while exporting data");
                }

            } catch (UnExpectedResultException e) {
                Log.e(Constants.PREFERENCES, "Unexpected result", e);

                // Reset not exported waypoints
                dbFacade.markExportable(2, 0, mExpContext.getT());
                return "ERROR";
            }

            // Mark "ExportNow" Waypoints as "Exported"
            dbFacade.markExportable(2, 1, mExpContext.getT());
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

        for (Waypoint w : (ArrayList<Waypoint>) mExpContext.getExportList()) {
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

        for (Waypoint w : (ArrayList<Waypoint>) mExpContext.getExportList()) {
            if (w.getTrackId() != 0) w.setTrack(mapTracks.get(w.getTrackId()));
        }

    }
}
