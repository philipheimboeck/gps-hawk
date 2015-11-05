package gps.fhv.at.gps_hawk;

import android.app.Application;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

import gps.fhv.at.gps_hawk.activities.fragments.ExportFragment;
import gps.fhv.at.gps_hawk.domain.Exception2Log;
import gps.fhv.at.gps_hawk.workers.DbFacade;
import gps.fhv.at.gps_hawk.workers.VolatileInstancePool;

/**
 * Created by Tobias on 25.10.2015.
 */
public class GpsHawkApplication extends Application {

    private final Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    public GpsHawkApplication() {
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        // Initialize volatile domain-data
        VolatileInstancePool.getInstance().initialize();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Add some custom exception handler
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                // Do some custom exception handling

                // Save Exception to local db
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));

                Exception2Log exception2Log = new Exception2Log();
                exception2Log.setStackTrace(sw.toString());
                exception2Log.setMessage(ex.getMessage());
                DbFacade db = DbFacade.getInstance();
                db.saveEntity(exception2Log);

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