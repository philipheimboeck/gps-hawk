package gps.fhv.at.gps_hawk.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.R;
import gps.fhv.at.gps_hawk.communication.DataClient;
import gps.fhv.at.gps_hawk.domain.Exception2Log;
import gps.fhv.at.gps_hawk.domain.ExportContext;
import gps.fhv.at.gps_hawk.domain.MotionValues;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.domain.Vehicle;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.exceptions.UnExpectedResultException;
import gps.fhv.at.gps_hawk.helper.ExportStartHelper;
import gps.fhv.at.gps_hawk.workers.DbFacade;
import gps.fhv.at.gps_hawk.workers.VolatileInstancePool;

/**
 * Created by Tobias on 25.10.2015.
 */
public class ExportTask extends AsyncTask<Void, Void, String> {

    private ExportContext mExpContext;
    private IAsyncTaskCaller<Void, String> mCaller;
    public static int isCurrentlyRunning = 0;
    private static int[] mExpIds = {R.id.button_do_export, R.id.button_do_motion_export, R.id.button_do_exception_export, R.id.button_do_track_export};


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

        if (isCurrentlyRunning == 1) {
            Log.i(Constants.PREFERENCES, "ExportTask currently running - return");
            return "";
        }

        ++isCurrentlyRunning;

        try {

            // Set default values if no specific data to export was set - but also then export ALL data!
            // If all data exported - return
            while (trySetDefaultExport()) {

                DbFacade dbFacade = DbFacade.getInstance(mExpContext.getContext());

                // Mark unexported entities as "ExportNow" (=flag 2)
                int count = dbFacade.markExportable(0, 2, mExpContext.getT());

                while (count > 0) {

                    int junkSize = Constants.EXPORT_JUNK;
                    if (mExpContext.getT().equals(MotionValues.class))
                        junkSize *= 3; // 3 times more MotionValues than Waypoints
                    if (mExpContext.getT().equals(Exception2Log.class))
                        junkSize /= 2; // 2 times less Exceptions than Waypoints

                    Log.d(Constants.PREFERENCES, "Found " + count + " " + mExpContext.getCollectionName() + " 2 export - Start chunk with limit: " + junkSize);

                    // Get all Waypoints from DB to export
//                    mExpContext.setExportList(dbFacade.getAllEntities2Export(mExpContext.getT(), junkSize, mExpContext.getCustomWhere()));

                    // Insert Tracks and Vehicles as Objects
                    if (mExpContext.getT().equals(Waypoint.class)) {
                        setDomainObjects(dbFacade, mExpContext);
                    }

                    // Send via Web
                    DataClient client = new DataClient(mExpContext.getContext());
                    try {

//                        boolean result = client.exportWaypoints();

//                         If no successful - be sure to reset flags
                        if (!true) {
                            throw new UnExpectedResultException("An unexpected result occurred while exporting data");
                        }

                    } catch (UnExpectedResultException e) {
                        Log.e(Constants.PREFERENCES, "Unexpected result", e);

                        // Reset not exported entities
                        dbFacade.markExportable(2, 0, mExpContext.getT());
                        return "ERROR";
                    }

                    // Mark "ExportNow" Entities as "Exported"
                    dbFacade.markExportableList(mExpContext.getExportList(), 1, mExpContext.getT());

                    count = (count > junkSize) ? count - junkSize : 0;
                }

                // if was a default export - delete it again to continue exporting
                if (i >= 0) mExpContext.setCollectionName(null);
            }
        } catch (Exception e) {
            Log.e(Constants.PREFERENCES, "Problem in exporting - rethrow", e);
        } finally {
            --isCurrentlyRunning;
            i = -1;
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

    private static int i = -1;
    private static boolean hasDoneFirstExport = false;

    /**
     * if is a "all-data-Export", set Type and collectionName in order of their priority to export
     * Returns, if there should be done an export according to the number of just done exports
     *
     * @return
     */
    private boolean trySetDefaultExport() {

        // If no collectionName defined --> use default
        if (mExpContext.getCollectionName() == null) {

            // start with first index
            if (i < 0) i = 0;

            // Stop exporting, all data exported!
            if (i >= mExpIds.length) return false;

            ExportStartHelper.setContextSpecificFromButId(mExpIds[i], mExpContext);
            ++i;

            return true;

        }

        // -1, if is first button-export, 0 if is not -> reset and return
        if (hasDoneFirstExport) {
            hasDoneFirstExport = false;
            return false;
        }
        hasDoneFirstExport = true;
        return true;

    }

}
