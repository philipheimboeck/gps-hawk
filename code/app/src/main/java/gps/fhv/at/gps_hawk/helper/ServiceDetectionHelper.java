package gps.fhv.at.gps_hawk.helper;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Author: Philip Heimböck
 * Date: 30.10.15
 */
public class ServiceDetectionHelper {

    /**
     * Checks if the service is running
     *
     * http://stackoverflow.com/questions/600207/how-to-check-if-a-service-is-running-on-android
     *
     * @param context
     * @param serviceClass
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
