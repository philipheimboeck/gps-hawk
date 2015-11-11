package gps.fhv.at.gps_hawk;

import android.app.Application;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

import gps.fhv.at.gps_hawk.domain.Exception2Log;
import gps.fhv.at.gps_hawk.workers.DbFacade;
import gps.fhv.at.gps_hawk.workers.LogWorker;
import gps.fhv.at.gps_hawk.workers.VolatileInstancePool;

/**
 * Created by Tobias on 25.10.2015.
 */
public class GpsHawkApplication extends Application {

    private final Thread.UncaughtExceptionHandler mDefaultExceptionHandler;
    private final LogWorker mLogWorker;

    public GpsHawkApplication() {
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        // Initialize volatile domain-data
        VolatileInstancePool.getInstance().initialize();

        // Initialize reading from Logcat
        mLogWorker = new LogWorker();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Be sure to set application context globally (singleton)
        DbFacade.getInstance(getApplicationContext());

        // Start reading Logs
        mLogWorker.initialize();

        // Add some custom exception handler
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {

                try {
                    // Save Exception to local db
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));

                    Exception2Log exception2Log = new Exception2Log();
                    exception2Log.setStackTrace(sw.toString());
                    exception2Log.setMessage(ex.getMessage());
                    exception2Log.setLevel(Log.ASSERT);
                    DbFacade db = DbFacade.getInstance(getApplicationContext());
                    db.saveEntity(exception2Log);


                    ex.printStackTrace();
                    Log.e("UNCAUGHT", ex.getMessage());


                } finally {
                    // Rethrow the exception to the OS!
                    if (mDefaultExceptionHandler != null) {
                        mDefaultExceptionHandler.uncaughtException(thread, ex);
                    }
                }
            }
        });
    }
}