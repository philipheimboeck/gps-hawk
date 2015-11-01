package gps.fhv.at.gps_hawk.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.R;
import gps.fhv.at.gps_hawk.activities.FragmentsActivity;
import gps.fhv.at.gps_hawk.broadcast.WaypointPersistor;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.workers.GpsWorker;
import gps.fhv.at.gps_hawk.workers.IGpsWorker;

public class LocationService extends Service {

    private IGpsWorker mGpsSvc;
    private BroadcastReceiver mWaypointPersistor;

    public LocationService() {
        mWaypointPersistor = new WaypointPersistor();
    }

    @Override
    public void onCreate() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mGpsSvc = new GpsWorker(locationManager, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mGpsSvc.isGpsAvailable()) {
            // Show notification
            showNotification();

            // Add Waypoint Listeners
            addWaypointListeners();

            // Start GPS tracking
            Track t = (Track) intent.getSerializableExtra(Constants.EXTRA_TRACK);
            mGpsSvc.startGpsTracking(t);
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

        // Stop Waypoint Listeners
        removeWaypointListeners();

        // Stop GPS tracking
        mGpsSvc.stopGpsTracking();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // No binding
        return null;
    }

    private void addWaypointListeners() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(mWaypointPersistor, new IntentFilter(Constants.BROADCAST_NEW_WAYPOINT));
    }

    private void removeWaypointListeners() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(mWaypointPersistor);
    }

    private void showNotification() {
        Intent intent = new Intent(this, FragmentsActivity.class);
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
