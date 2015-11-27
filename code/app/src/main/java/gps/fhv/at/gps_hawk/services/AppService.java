package gps.fhv.at.gps_hawk.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import gps.fhv.at.gps_hawk.workers.ExportWorker;
import gps.fhv.at.gps_hawk.workers.IExportWorker;

/**
 * Created by Tobias on 27.11.2015.
 *
 * The purpose of this service is to
 * - capture Network-Status-Changes
 *
 * The service shall be running during whole app running cycle
 */
public class AppService extends Service {

    private IExportWorker mExportWorker;

    public AppService() {

    }

    @Override
    public void onCreate() {
        mExportWorker = new ExportWorker(getApplicationContext());
    }

        @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
