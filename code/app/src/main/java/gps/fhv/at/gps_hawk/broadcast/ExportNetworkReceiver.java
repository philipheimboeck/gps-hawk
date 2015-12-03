package gps.fhv.at.gps_hawk.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.helper.ExportStartHelper;
import gps.fhv.at.gps_hawk.workers.IExportWorker;

/**
 * Created by Tobias on 27.11.2015.
 * The purpose of this class is to detect WiFi-Changes and to export data if connected to network
 * Also it tries to export on startup, only if has WiFi connection
 */
public class ExportNetworkReceiver extends BroadcastReceiver implements IExportWorker {

    private Context mContext;
    private ExportStartHelper mExportStartHelper;

    public ExportNetworkReceiver() {

    }

    public ExportNetworkReceiver(Context context) {
        mContext = context;

        // Try to export on each startup
        // Only export if WiFi is connected
        ConnectivityManager connManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi != null && mWifi.isConnected()) {
            startExport();
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;

        NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (networkInfo != null) {

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                //get the different network states
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    Log.v(Constants.PREFERENCES, "Status connected");
                    startExport();
                }

                if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                }

                if (networkInfo.getState() == NetworkInfo.State.CONNECTING) {
                }
            }
        }

    }

    /**
     * as is synchronized, possible subsequent calls are blocking only the first call will lead to
     * export (ExportStartHelper remembers last export)
     */
    private synchronized void startExport() {

        mExportStartHelper = new ExportStartHelper(mContext, null);
        mExportStartHelper.startExport(-1);

    }

}

