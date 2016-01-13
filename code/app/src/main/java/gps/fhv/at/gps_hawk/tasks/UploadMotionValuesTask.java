package gps.fhv.at.gps_hawk.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.communication.DataClient;
import gps.fhv.at.gps_hawk.domain.MotionValues;
import gps.fhv.at.gps_hawk.exceptions.CommunicationException;
import gps.fhv.at.gps_hawk.exceptions.TaskException;
import gps.fhv.at.gps_hawk.workers.DbFacade;

/**
 * Author: Philip Heimböck
 * Date: 06.01.16
 */
public class UploadMotionValuesTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private IAsyncTaskCaller<Void, Void> mCaller;

    public UploadMotionValuesTask(Context context) {
        mContext = context;
    }

    public UploadMotionValuesTask(IAsyncTaskCaller<Void, Void> caller, Context context) {
        mContext = context;
        mCaller = caller;
    }

    @Override
    protected Void doInBackground(Void... params) {
        DataClient client = new DataClient(mContext);
        DbFacade facade = DbFacade.getInstance();

        try {
            int junkSize = Constants.EXPORT_JUNK * 3;

            // Mark unexported entities as "ExportNow" (= flag 2)
            int count = facade.markExportable(0, 2, MotionValues.class);
            while (count > 0) {
                // Retrieve the motion values
                List<MotionValues> motionValues = facade.getAllEntities2Export(MotionValues.class, junkSize, null);

                if (motionValues.isEmpty()) {
                    // No result set found
                    throw new TaskException("Failed to retrieve exportable entities!");
                }

                // Send them to the server
                client.exportMotionValues(motionValues);

                // Mark motion values as exported
                facade.markExportableList(motionValues, 1, MotionValues.class);

                // Calculate whats left
                count -= motionValues.size();
            }

        } catch (CommunicationException | TaskException e) {
            Log.e(Constants.PREFERENCES, "Failed to export motion values!", e);

            // Reset not exported entities
            facade.markExportable(2, 0, MotionValues.class);
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
