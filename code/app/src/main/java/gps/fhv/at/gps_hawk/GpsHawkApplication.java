package gps.fhv.at.gps_hawk;

import android.app.Application;
import android.util.Log;

import gps.fhv.at.gps_hawk.activities.fragments.ExportFragment;

/**
 * Created by Tobias on 25.10.2015.
 */
public class GpsHawkApplication extends Application {

    private final Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    public GpsHawkApplication() {
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Add some custom exception handler
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                // Do some custom exception handling
                ex.printStackTrace();
                Log.e("UNCAUGHT", ex.getMessage());

                // Rethrow the exception to the OS!
                if(mDefaultExceptionHandler != null) {
                    mDefaultExceptionHandler.uncaughtException(thread, ex);
                }
            }
        });
    }
}