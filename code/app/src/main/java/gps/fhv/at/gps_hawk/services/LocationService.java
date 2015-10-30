package gps.fhv.at.gps_hawk.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.R;
import gps.fhv.at.gps_hawk.activities.MainActivity;
import gps.fhv.at.gps_hawk.workers.GpsSvc;
import gps.fhv.at.gps_hawk.workers.IGpsSvc;

public class LocationService extends Service {

    private IGpsSvc mGpsSvc;

    public LocationService() {
    }

    @Override
    public void onCreate() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mGpsSvc = new GpsSvc(locationManager, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mGpsSvc.isGpsAvailable()) {
            // Show notification
            showNotification();

            // Start GPS tracking
            mGpsSvc.startGpsTracking();
        } else {
            // No GPS? Stop the service right away!
            stopSelf();
        }
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel notification
        cancelNotification();

        // Stop GPS tracking
        mGpsSvc.stopGpsTracking();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // No binding
        return null;
    }

    private void showNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_hawk)
                .setContentTitle(this.getString(R.string.notification_tracking_title))
                .setContentText(this.getString(R.string.notification_tracking_text))
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .build();
        // TODO: Add Action to disable tracking from the notification
        // TODO: Show Overview when clicking on notification
        notificationManager.notify(Constants.NOTIFICATION_TRACKING_ID, notification);
    }

    private void cancelNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Constants.NOTIFICATION_TRACKING_ID);
    }
}
