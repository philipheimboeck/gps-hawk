package gps.fhv.at.gps_hawk.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.communication.DataClient;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.exceptions.CommunicationException;
import gps.fhv.at.gps_hawk.exceptions.TaskException;
import gps.fhv.at.gps_hawk.workers.DbFacade;

/**
 * Author: Philip Heimböck
 * Date: 31.12.15
 * <p/>
 * The purpose of this task is to retrieve tracks from the server, which then can be used to save waypoints on the server
 */
public class UploadWaypointsTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private IAsyncTaskCaller<Void, Void> mCaller;

    private HashMap<Integer, Track> mTracks = new HashMap<>();

    public UploadWaypointsTask(Context context) {
        this(null, context);
    }

    public UploadWaypointsTask(IAsyncTaskCaller<Void, Void> caller, Context context) {
        mCaller = caller;
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        DataClient client = new DataClient(mContext);
        DbFacade facade = DbFacade.getInstance();

        try {
            int junkSize = Constants.EXPORT_JUNK;

            // Mark unexported entities as "ExportNow" (= flag 2)
            int count = facade.markExportable(0, 2, Waypoint.class);
            while (count > 0) {
                // Retrieve all waypoints
                List<Waypoint> waypoints = facade.getAllEntities2Export(Waypoint.class, junkSize, null);

                if (waypoints.isEmpty()) {
                    // No result set found
                    throw new TaskException("Failed to retrieve exportable entities!");
                }

                // Add the domain objects
                setDomainObjects(facade, waypoints);

                // Filter out waypoints we don't want to export because they belong to old data
                List<Waypoint> filtered = filterObjects(waypoints);

                // Export all waypoints
                client.exportWaypoints(filtered);

                // When successful mark them as exported
                // Do this also for the filtered waypoints!
                facade.markExportableList(waypoints, 1, Waypoint.class);

                // Calculate whats left
                count -= waypoints.size();
            }

        } catch (CommunicationException | TaskException e) {
            Log.e(Constants.PREFERENCES, "Failed to upload waypoints", e);

            // Reset not exported entities
            facade.markExportable(2, 0, Waypoint.class);
        }
        return null;
    }

    private List<Waypoint> filterObjects(List<Waypoint> waypoints) {
        // Remove all waypoints with a track without externalTrackId
        List<Waypoint> filtered = new ArrayList<>();

        for (Waypoint waypoint : waypoints) {
            // Old data without external ID won't be uploaded
            if(waypoint.getTrack().getExternalId() != null) {
                filtered.add(waypoint);
            }
        }

        return filtered;
    }

    private void setDomainObjects(DbFacade facade, List<Waypoint> waypoints) {
        ArrayList<Integer> neededTracks = new ArrayList<>();

        // Find tracks that must be get from the database
        for(Waypoint waypoint : waypoints) {
            if(!mTracks.containsKey(waypoint.getTrackId())) {
                // Add track to map
                neededTracks.add(waypoint.getTrackId());
            }
        }

        // Select all tracks
        List<Track> tracks = facade.select(neededTracks, Track.class);
        for(Track track : tracks) {
            mTracks.put(track.getId(), track);
        }

        // Add the tracks to the waypoints
        for(Waypoint waypoint : waypoints) {
            waypoint.setTrack(mTracks.get(waypoint.getTrackId()));
        }

    }


    @Override
    protected void onPostExecute(Void success) {
        // Return the tracks
        if (mCaller != null) {
            mCaller.onPostExecute(success);
        }
    }
}
