package gps.fhv.at.gps_hawk.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.communication.DataClient;
import gps.fhv.at.gps_hawk.domain.Exception2Log;
import gps.fhv.at.gps_hawk.exceptions.CommunicationException;
import gps.fhv.at.gps_hawk.exceptions.TaskException;
import gps.fhv.at.gps_hawk.workers.DbFacade;

/**
 * Author: Philip Heimböck
 * Date: 31.12.15
 * <p/>
 * The purpose of this task is to retrieve tracks from the server, which then can be used to save waypoints on the server
 */
public class UploadLogTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private IAsyncTaskCaller<Void, Void> mCaller;

    public UploadLogTask(Context context) {
        this(null, context);
    }

    public UploadLogTask(IAsyncTaskCaller<Void, Void> caller, Context context) {
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
            int count = facade.markExportable(0, 2, Exception2Log.class);
            while (count > 0) {

                // Retrieve all logs
                List<Exception2Log> logs = facade.getAllEntities2Export(Exception2Log.class, junkSize, null);

                if (logs.isEmpty()) {
                    // No result set found
                    throw new TaskException("Failed to retrieve exportable entities!");
                }

                // Export all logs
                client.exportLogs(logs);

                // When successful mark them as exported
                // Do this also for the filtered logs!
                facade.markExportableList(logs, 1, Exception2Log.class);

                // Calculate whats left
                count -= logs.size();
            }

        } catch (CommunicationException | TaskException e) {
            Log.e(Constants.PREFERENCES, "Failed to upload logs", e);

            // Reset not exported entities
            facade.markExportable(2, 0, Exception2Log.class);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void success) {
        // Return the tracks
        if (mCaller != null) {
            mCaller.onPostExecute(success);
        }
    }
}
