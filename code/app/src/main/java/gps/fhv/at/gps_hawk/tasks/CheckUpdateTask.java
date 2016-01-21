package gps.fhv.at.gps_hawk.tasks;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.communication.AppClient;
import gps.fhv.at.gps_hawk.communication.IAppClient;
import gps.fhv.at.gps_hawk.exceptions.CommunicationException;

/**
 * Author: Philip Heimböck
 * Date: 11.11.15
 */
public class CheckUpdateTask extends AsyncTask<Void, Void, CheckUpdateTask.UpdateTaskResult> {

    private Context mContext;
    private IAsyncTaskCaller<Void, UpdateTaskResult> mCaller;

    public CheckUpdateTask(Context context, IAsyncTaskCaller<Void, UpdateTaskResult> caller) {
        mContext = context;
        mCaller = caller;
    }

    @Override
    protected UpdateTaskResult doInBackground(Void... params) {
        IAppClient appClient = new AppClient(mContext);

        PackageInfo packageInfo;
        try {
            packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            String version = packageInfo.versionName;

            Log.i(Constants.PREFERENCES, "Checking for update; current version is " + version);

            String url = appClient.getUpdateLink(version);
            if (url != null) {
                Log.i(Constants.PREFERENCES, "Update found: " + url);
                return new UpdateTaskResult(Uri.parse(url));
            }

            Log.i(Constants.PREFERENCES, "No update available");
            return new UpdateTaskResult(false);

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(Constants.PREFERENCES, "Retrieval of version failed!", e);
        } catch (CommunicationException e) {
            Log.e(Constants.PREFERENCES, "Failed to get update link!", e);
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        mCaller.onPreExecute();
    }

    @Override
    protected void onPostExecute(UpdateTaskResult url) {
        mCaller.onPostExecute(url);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        mCaller.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(UpdateTaskResult url) {
        mCaller.onCancelled();
    }

    @Override
    protected void onCancelled() {
        mCaller.onCancelled();
    }

    public class UpdateTaskResult {
        public boolean updateAvailable;
        public Uri updateLink;

        public UpdateTaskResult(boolean updateAvailable) {
            this.updateAvailable = updateAvailable;
        }

        public UpdateTaskResult(Uri updateLink) {
            this.updateAvailable = true;
            this.updateLink = updateLink;
        }
    }
}
