package gps.fhv.at.gps_hawk.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.R;
import gps.fhv.at.gps_hawk.activities.navigation.Navigation;
import gps.fhv.at.gps_hawk.activities.navigation.NavigationAction;
import gps.fhv.at.gps_hawk.activities.navigation.NavigationItem;
import gps.fhv.at.gps_hawk.broadcast.WaypointCounter;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.helper.ServiceDetectionHelper;
import gps.fhv.at.gps_hawk.services.LocationService;
import gps.fhv.at.gps_hawk.workers.DbFacade;
import gps.fhv.at.gps_hawk.workers.GpsWorker;


public class CaptureActivity extends AppCompatActivity {

    private static GpsWorker mGpsService;

    private LocationManager locationManager;
    private boolean mPermissionsGranted = false;

    private SupportMapFragment mMapFragment;
    private TextView mWaypointCounterView;
    private Button mStartTrackingButton;

    private Navigation mNavigation;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ImageButton mNowFoot;
    private ImageButton mNowBicycle;
    private ImageButton mNowBus;
    private ImageButton mNowTrain;
    private ImageButton mNowCar;

    private Polyline mPolyline;

    private Track mCurrentTrack;

    /**
     * State of the waypoint listeners (Registered/Not Registered)
     */
    private boolean mRegistered;

    /**
     * Updates the number of waypoints
     */
    private WaypointCounter mWaypointCounter = new WaypointCounter() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);

            mWaypointCounterView.setText(getString(R.string.number_of_waypoints, WaypointCounter.count()));
        }
    };

    /**
     * Updates the map
     */
    private BroadcastReceiver mWaypointMapUpdater = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Add the waypoint to the map
            Waypoint waypoint = (Waypoint) intent.getSerializableExtra(Constants.EXTRA_WAYPOINT);
            if (waypoint != null) {
                LatLng point = new LatLng(waypoint.getLat(), waypoint.getLng());

                if(mMapFragment != null && mMapFragment.getMap() != null) {
                    addMapPoint(point);

                    // Show position
                    mMapFragment.getMap().moveCamera(CameraUpdateFactory.newLatLng(point));

                    // Zoom in the Google Map
                    mMapFragment.getMap().animateCamera(CameraUpdateFactory.zoomTo(15));
                }

            }
        }
    };



    public CaptureActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        // Check for permissions
        mPermissionsGranted =
                PermissionChecker.checkCallingOrSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!mPermissionsGranted) {
            Toast.makeText(this, "Please enable GPS", Toast.LENGTH_LONG).show();

            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }

        } else {
            initializeView();
        }

    }

    /**
     * Initialize the view
     */
    public void initializeView() {

        // Find Views
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navigation_list);

        // Create the navigation drawer
        Intent exportIntent = new Intent(this, FragmentsActivity.class);
        exportIntent.putExtra(Constants.EXTRA_FRAGMENT, Constants.FRAGMENT_EXPORT);
        Intent settingsIntent = new Intent(this, FragmentsActivity.class);
        settingsIntent.putExtra(Constants.EXTRA_FRAGMENT, Constants.FRAGMENT_SETTINGS);

        ArrayList<NavigationItem> navigationItems = new ArrayList<>();
        navigationItems.add(new NavigationItem(new NavigationAction(), getString(R.string.navigation_capture), R.drawable.ic_hawk_white));
        navigationItems.add(new NavigationItem(new NavigationAction(exportIntent), getString(R.string.navigation_export), 0));
        navigationItems.add(new NavigationItem(new NavigationAction(settingsIntent), getString(R.string.navigation_settings), R.drawable.ic_setting_dark));

        mNavigation = new Navigation(this, mDrawerLayout, mDrawerList);
        mNavigation.populateNavigation(navigationItems);
        mNavigation.enableDrawerIcon();

        // Button
        mStartTrackingButton = (Button) findViewById(R.id.button_tracking);
        mStartTrackingButton.setEnabled(mPermissionsGranted);
        mStartTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleStartButton();
            }
        });

        mWaypointCounterView = (TextView) findViewById(R.id.text_waypoint_counter);
        mWaypointCounterView.setText(getString(R.string.number_of_waypoints, WaypointCounter.count()));

        // ImageButtons
        mNowFoot = (ImageButton) findViewById(R.id.butNowFoot);
        mNowBicycle = (ImageButton) findViewById(R.id.butNowBicycle);
        mNowBus = (ImageButton) findViewById(R.id.butNowBus);
        mNowTrain = (ImageButton) findViewById(R.id.butNowTrain);
        mNowCar = (ImageButton) findViewById(R.id.butNowCar);

        // Initialize the map
        initializeMapFragment();

        // When already tracking, show some other elements
        initializeViewInTrackingMode();
    }

    private void initializeMapFragment() {
        // Map options
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if(mMapFragment == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mMapFragment).commit();
        }

        initializeMap();
    }

    private void initializeMap() {
        if(mMapFragment != null) {
            // Map options
            mMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMap.setMyLocationEnabled(true);

                    // Set polyline
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.width(5);
                    polylineOptions.visible(true);
                    polylineOptions.color(Color.parseColor("#CC0000FF"));
                    mPolyline = googleMap.addPolyline(polylineOptions);
                }
            });
        }
    }

    /**
     * When the view initializes but is in tracking mode, some changes needs to be made
     */
    private void initializeViewInTrackingMode() {
        // Tracking?
        if (ServiceDetectionHelper.isServiceRunning(getApplicationContext(), LocationService.class)) {

            // Change the button text
            mStartTrackingButton.setText(R.string.stop_tracking);

            // Add route when currently tracking
            // TODO Add all points from current track to list
            // for(LatLng point : getAllPoints) addMapPoint(point);

            // Add the waypoint listeners
            addWaypointListener();
        }
    }

    private void addMapPoint(LatLng point) {
        if(mPolyline != null) {
            List<LatLng> points = mPolyline.getPoints();
            points.add(point);
            mPolyline.setPoints(points); // Set again so that it will redraw
        }
    }

    /**
     * Start or stop the tracking
     */
    private void handleStartButton() {
        // Check if service is running
        if (!ServiceDetectionHelper.isServiceRunning(getApplicationContext(), LocationService.class)) {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mGpsService = new GpsWorker(locationManager, getApplicationContext());
            mCurrentTrack = new Track();
            DbFacade db = DbFacade.getInstance(this);
            int trackID =  (int) db.saveEntity(mCurrentTrack);
            mCurrentTrack.setId(trackID);

            if (mGpsService.isGpsAvailable()) {
                // Start the service
                Intent intent = new Intent(this, LocationService.class);
                intent.putExtra(Constants.EXTRA_TRACK, mCurrentTrack);
                this.startService(intent);
                mStartTrackingButton.setText(R.string.stop_tracking);

                // Add the listener
                addWaypointListener();
            } else {
                // Show settings to enable GPS
                showMessageBox(this, getResources().getString(R.string.enable_gps_button), getResources().getString(R.string.enable_gps_button_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(i);
                    }
                });
            }

        } else {
            // Stop the service
            Intent intent = new Intent(this, LocationService.class);
            this.stopService(intent);
            mStartTrackingButton.setText(R.string.start_tracking);

            // Remove the listener
            removeWaypointListener();
        }
    }

    /**
     * Add waypoint listeners
     */
    private void addWaypointListener() {
        if (!mRegistered) {
            mRegistered = true;

            LocalBroadcastManager.getInstance(this).registerReceiver(mWaypointCounter, new IntentFilter(Constants.BROADCAST_NEW_WAYPOINT));
            LocalBroadcastManager.getInstance(this).registerReceiver(mWaypointMapUpdater, new IntentFilter(Constants.BROADCAST_NEW_WAYPOINT));
        }
    }

    /**
     * Removes all listeners
     */
    private void removeWaypointListener() {
        if (mRegistered) {
            mRegistered = false;

            LocalBroadcastManager.getInstance(this).unregisterReceiver(mWaypointCounter);
            WaypointCounter.resetCounter();

            LocalBroadcastManager.getInstance(this).unregisterReceiver(mWaypointMapUpdater);
        }
    }

    protected void showMessageBox(Context context, String message, String positiveText, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(R.string.app_name);
        dlgAlert.setPositiveButton(positiveText, null);
        dlgAlert.setCancelable(true);

        if (listener != null) dlgAlert.setPositiveButton(positiveText, listener);

        dlgAlert.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPermissionsGranted = true;
                    mStartTrackingButton.setEnabled(true);

                    initializeView();
                } else {
                    Toast.makeText(this, R.string.error_permission_location_required, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mNavigation.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mNavigation.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mNavigation.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
}
