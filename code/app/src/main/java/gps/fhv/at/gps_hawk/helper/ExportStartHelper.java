package gps.fhv.at.gps_hawk.helper;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import java.util.Calendar;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.R;
import gps.fhv.at.gps_hawk.domain.Exception2Log;
import gps.fhv.at.gps_hawk.domain.ExportContext;
import gps.fhv.at.gps_hawk.domain.IExportable;
import gps.fhv.at.gps_hawk.domain.MotionValues;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.tasks.ExportTask;
import gps.fhv.at.gps_hawk.tasks.IAsyncTaskCaller;

/**
 * Created by Tobias on 27.11.2015.
 * The aim of this class is to no duplicate the code to start exports
 * (as it is now possible to still do it manually additionally to automatically)
 */
public class ExportStartHelper {

    private Context mContext;
    private ExportTask mExportTask;
    private IUpdateableView mView;
    private static Calendar mLastExportStarted = null;

    public ExportStartHelper(Context ctx, IUpdateableView view) {
        mContext = ctx;
        mView = view;
    }

    public void startExport(int id) {

        // If there already was an export and this is an "automatical export"
        if (mLastExportStarted != null && id < 0) {
            Calendar now = Calendar.getInstance();
            now.add(Calendar.SECOND, -Constants.EXPORT_AUTOMATICALLY_GAP);

            // If last export was within timegap, don't do it again
            if (now.before(mLastExportStarted)) {
                Log.i(Constants.PREFERENCES, "Don't start automatical export, has just done it before!");
                return;
            }
        }

        // Remember if "automatical export"
        if (id < 0) mLastExportStarted = Calendar.getInstance();

        try {
            ExportContext exportContext = new ExportContext();
            exportContext.setContext(mContext);
            exportContext.setAndroidId(Settings.Secure.getString(mContext.getContentResolver(),
                    Settings.Secure.ANDROID_ID));

            setContextSpecificFromButId(id, exportContext);

            mExportTask = new ExportTask(exportContext, new IAsyncTaskCaller<Void, String>() {

                @Override
                public void onPostExecute(String success) {

                    if (mView != null) mView.doDataLoadingAsnc();
//                    if (mView != null) mView.showLoading(false);
                }

                @Override
                public void onCancelled() {
                    if (mView != null) mView.showLoading(true);
                }

                @Override
                public void onProgressUpdate(Void... progress) {

                }

                @Override
                public void onPreExecute() {
                    if (mView != null) mView.showLoading(true);
                }
            });
            mExportTask.execute();

        } catch (Exception e) {
            Log.e(Constants.PREFERENCES, "Cannot start Export", e);
        }

    }

    public static <T extends IExportable> void setContextSpecificFromButId(int id, ExportContext<T> exportContext) {
        switch (id) {
            case R.id.button_do_export:
                exportContext.setT(Waypoint.class);
                exportContext.setCollectionName("waypoints");
                break;
            case R.id.button_do_exception_export:
                exportContext.setT(Exception2Log.class);
                exportContext.setCollectionName("exceptions");
                break;
            case R.id.button_do_motion_export:
                exportContext.setT(MotionValues.class);
                exportContext.setCollectionName("motionValues");
                break;
            case R.id.button_do_track_export:
                exportContext.setT(Track.class);
                exportContext.setCollectionName("tracks");
//                exportContext.setCustomWhere(TrackDef.COLUMN_NAME_DATETIME_END + " > 0");
                break;
            default:
                // export all data - is handled by ExportTask itself
        }

    }

}

