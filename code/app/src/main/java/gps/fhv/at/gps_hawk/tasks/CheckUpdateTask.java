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
import gps.fhv.at.gps_hawk.exceptions.RestException;

/**
 * Author: Philip Heimböck
 * Date: 11.11.15
 */
public class CheckUpdateTask extends AsyncTask<Void, Void, Uri> {

    private Context mContext;
    private IAsyncTaskCaller<Void, Uri> mCaller;

    public CheckUpdateTask(Context context, IAsyncTaskCaller<Void, Uri> caller) {
        mContext = context;
        mCaller = caller;
    }

    @Override
    protected Uri doInBackground(Void... params) {
        IAppClient appClient = new AppClient(mContext);

        PackageInfo packageInfo;
        try {
            packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            String version = packageInfo.versionName;

            String url = appClient.getUpdateLink(version);
            if (url != null) {
                return Uri.parse(url);
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(Constants.PREFERENCES, "Retrieval of version failed!", e);
        } catch (RestException e) {
            Log.e(Constants.PREFERENCES, "Failed to get update link!", e);
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        mCaller.onPreExecute();
    }

    @Override
    protected void onPostExecute(Uri url) {
        mCaller.onPostExecute(url);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        mCaller.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Uri url) {
        mCaller.onCancelled();
    }

    @Override
    protected void onCancelled() {
        mCaller.onCancelled();
    }
}
