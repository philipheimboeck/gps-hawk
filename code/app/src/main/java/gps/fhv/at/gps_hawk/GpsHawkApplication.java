package gps.fhv.at.gps_hawk;

import android.app.Application;
import android.util.Log;

import gps.fhv.at.gps_hawk.activities.fragments.ExportFragment;

/**
 * Created by Tobias on 25.10.2015.
 */
public class GpsHawkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e("GpsHawkApplication", ex.getMessage());
            }
        });
    }
}