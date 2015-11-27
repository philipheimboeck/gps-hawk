package gps.fhv.at.gps_hawk.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import gps.fhv.at.gps_hawk.domain.Vehicle;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.helper.ServiceDetectionHelper;
import gps.fhv.at.gps_hawk.persistence.setup.WaypointDef;
import gps.fhv.at.gps_hawk.services.AppService;
import gps.fhv.at.gps_hawk.services.LocationService;
import gps.fhv.at.gps_hawk.tasks.CheckUpdateTask;
import gps.fhv.at.gps_hawk.tasks.IAsyncTaskCaller;
import gps.fhv.at.gps_hawk.workers.DbFacade;
import gps.fhv.at.gps_hawk.workers.GpsWorker;
import gps.fhv.at.gps_hawk.workers.VolatileInstancePool;
import gps.fhv.at.gps_hawk.workers.WaypointFactory;


public class CaptureActivity extends AppCompatActivity {

    public static final int MAP_ZOOM = 15;

    /*
     * States
     */
    public static final String STATE_WAYPOINT_LISTENER = "waypointListener";
    private static final String STATE_VEHICLE = "activeVehicle";
    private static final String STATE_TRACK = "currentTrack";

    private boolean mPermissionsGranted = false;

    /**
     * Temporary save the update path
     */
    private Uri mDownloadUri;

    private SupportMapFragment mMapFragment;
    private TextView mWaypointCounterView;
    private Button mStartTrackingButton;
    private Button mTaskValidButtonYes;
    private Button mTaskValidButtonNo;
    private View.OnClickListener mButTaskValidClickListener;
    private TextView mTxtValidTrackQuest;

    private Navigation mNavigation;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ImageButton[] mImgVehicleButtons;

    private Polyline mPolyline;

    private Track mCurrentTrack;
    private boolean mZoomed = false;

    /**
     * State of the waypoint listeners (Registered/Not Registered)
     */
    private boolean mWaypointListenerRegistered;

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
                LatLng point = waypoint.createLatLng();

                if (mMapFragment != null && mMapFragment.getMap() != null) {
                    addMapPoint(point);

                    // Show position
                    mMapFragment.getMap().moveCamera(CameraUpdateFactory.newLatLng(point));

                    // Zoom in the Google Map
                    if (!mZoomed) {
                        mMapFragment.getMap().animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM));
                        mZoomed = true;
                    }

                }

            }
        }
    };

    /**
     * Saves the current vehicle id
     */
    private int mActiveVehicleId;


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

        // TODO: Testing motion sensors
//        mMotionWorker = new MotionWorker(getApplicationContext());
//        mMotionWorker.initialize();

        initializeView();

        if (!mPermissionsGranted) {
            Toast.makeText(this, "Please enable GPS", Toast.LENGTH_LONG).show();

            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_CODE_PERMISSION_GPS);
            }

        }

        checkForUpdate();
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
//        navigationItems.add(new NavigationItem(new NavigationAction(settingsIntent), getString(R.string.navigation_settings), R.drawable.ic_setting_dark));

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

        mButTaskValidClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = false;
                if (v.getId() == R.id.but_valid_track_yes) isValid = true;

                handleTaksValidButtons(isValid);
            }
        };
        mTaskValidButtonYes = (Button) findViewById(R.id.but_valid_track_yes);
        mTaskValidButtonYes.setOnClickListener(mButTaskValidClickListener);
        mTaskValidButtonNo = (Button) findViewById(R.id.but_valid_track_no);
        mTaskValidButtonNo.setOnClickListener(mButTaskValidClickListener);
        mTxtValidTrackQuest = (TextView) findViewById(R.id.txt_valid_track_quest);

        mWaypointCounterView = (TextView) findViewById(R.id.text_waypoint_counter);
        mWaypointCounterView.setText(getString(R.string.number_of_waypoints, WaypointCounter.count()));

        // ImageButtons
        int i = 0;
        ArrayList<Vehicle> vList = VolatileInstancePool.getInstance().getAllRegistered(Vehicle.class);
        mImgVehicleButtons = new ImageButton[5]; // TODO: Constant for nr of vehicles
        mImgVehicleButtons[i++] = (ImageButton) findViewById(R.id.butNowFoot);
        mImgVehicleButtons[i++] = (ImageButton) findViewById(R.id.butNowBicycle);
        mImgVehicleButtons[i++] = (ImageButton) findViewById(R.id.butNowBus);
        mImgVehicleButtons[i++] = (ImageButton) findViewById(R.id.butNowTrain);
        mImgVehicleButtons[i++] = (ImageButton) findViewById(R.id.butNowCar);

        mVehicleClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVehicle(v.getId());
            }
        };

        // Connect registered vehicles with the button on the ui
        i = 0;
        for (Vehicle vehicle : vList) {
            vehicle.setUiId(mImgVehicleButtons[i].getId());
            mImgVehicleButtons[i].setOnClickListener(mVehicleClickListener);
            ++i;
        }

        // Initialize the map
        initializeMapFragment();
    }

    /**
     * Change the vehicle
     *
     * @param id
     */
    private void changeVehicle(int id) {
        ArrayList<Vehicle> vList = VolatileInstancePool.getInstance().getAllRegistered(Vehicle.class);
        Vehicle ourVehicle = null;
        int i = -1;
        for (Vehicle vehicle : vList) {
            ++i;
            if (id == vehicle.getUiId()) {
                ourVehicle = vehicle;
                mImgVehicleButtons[i].setBackgroundResource(R.drawable.current_vehicle);
                Log.i(Constants.PREFERENCES, "Vehicle changed: " + ourVehicle.getId());
                continue;
            }
            mImgVehicleButtons[i].setBackgroundResource(R.drawable.inactive_vehicle);
        }
        WaypointFactory.getInstance().setVehicle(ourVehicle);

        // Save active vehicle
        mActiveVehicleId = id;
    }

    /**
     * Basically the same as changeVehicle, but doesn't set the vehicle in the WayPointFactory
     *
     * @param id
     */
    private void changeVehicleView(int id) {
        ArrayList<Vehicle> vList = VolatileInstancePool.getInstance().getAllRegistered(Vehicle.class);
        for (int i = 0; i < vList.size(); i++) {
            if (id == vList.get(i).getUiId()) {
                mImgVehicleButtons[i].setBackgroundResource(R.drawable.current_vehicle);
            }
        }
    }

    private View.OnClickListener mVehicleClickListener;

    private void initializeMapFragment() {
        // Map options
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mMapFragment == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mMapFragment).commit();
        }

        initializeMap();
    }

    private void initializeMap() {

        if (mMapFragment != null) {
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
            if (mCurrentTrack != null) {

                // Remove old waypoints
                clearWaypoints();

                // Add all points from current track to list
                for (Waypoint waypoint : getAllPoints(mCurrentTrack)) {
                    addMapPoint(waypoint.createLatLng());
                }
            }

            // Add the waypoint listeners
            addWaypointListener();
        }
    }

    private void clearWaypoints() {
        if (mPolyline != null) {
            mPolyline.setPoints(new ArrayList<LatLng>());
        }
    }

    private Iterable<Waypoint> getAllPoints(Track currentTrack) {
        return DbFacade.getInstance(this).selectWhere(WaypointDef.COLUMN_NAME_TRACK_ID + " = " + currentTrack.getId(), Waypoint.class);
    }

    private void addMapPoint(LatLng point) {
        if (mPolyline != null) {
            List<LatLng> points = mPolyline.getPoints();
            points.add(point);
            mPolyline.setPoints(points); // Set again so that it will redraw
        }
    }

    private void handleTaksValidButtons(boolean isValid) {

        Log.i(Constants.PREFERENCES, "Is Valid: " + isValid);

        // Visibility of buttons/textView
        toggleButtons(false);

        // Stop the service
        Intent intent = new Intent(this, LocationService.class);
        intent.putExtra("isValid", isValid ? 1 : 0);
        intent.putExtra("terminate", true);
        this.startService(intent);

        // Remove the listener
        removeWaypointListener();

    }

    private void toggleButtons(boolean showValid) {

        // 'valid' meaning for elements checking whether the track is valid
        int valid = showValid ? View.VISIBLE : View.GONE;
        int tracking = showValid ? View.GONE : View.VISIBLE;

        mTaskValidButtonNo.setVisibility(valid);
        mTaskValidButtonYes.setVisibility(valid);
        mTxtValidTrackQuest.setVisibility(valid);
        mStartTrackingButton.setVisibility(tracking);

    }

    /**
     * Start or stop the tracking
     */
    private void handleStartButton() {
        // Check if service is running
        if (!ServiceDetectionHelper.isServiceRunning(getApplicationContext(), LocationService.class)) {

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            GpsWorker gpsService = new GpsWorker(locationManager, getApplicationContext());
            mCurrentTrack = new Track();
            DbFacade db = DbFacade.getInstance(this);
            int trackID = (int) db.saveEntity(mCurrentTrack);
            mCurrentTrack.setId(trackID);

            if (gpsService.isGpsAvailable()) {
                // Start the service
                Intent intent = new Intent(this, LocationService.class);
                intent.putExtra(Constants.EXTRA_TRACK, mCurrentTrack);
                this.startService(intent);
                mStartTrackingButton.setText(R.string.stop_tracking);

                // Remove old waypoint
                clearWaypoints();

                // Reset zoom
                mZoomed = false;

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
            mStartTrackingButton.setText(R.string.start_tracking);

            // Visibility of Buttons/Views
            toggleButtons(true);

        }
    }

    /**
     * Add waypoint listeners
     */
    private void addWaypointListener() {
        Log.d(Constants.PREFERENCES, "addingWaypoint: " + !mWaypointListenerRegistered);
        if (!mWaypointListenerRegistered) {
            mWaypointListenerRegistered = true;

            LocalBroadcastManager.getInstance(this).registerReceiver(mWaypointCounter, new IntentFilter(Constants.BROADCAST_NEW_WAYPOINT));
            LocalBroadcastManager.getInstance(this).registerReceiver(mWaypointMapUpdater, new IntentFilter(Constants.BROADCAST_NEW_WAYPOINT));
        }
    }

    /**
     * Removes all listeners
     */
    private void removeWaypointListener() {
        if (mWaypointListenerRegistered) {
            mWaypointListenerRegistered = false;

            LocalBroadcastManager.getInstance(this).unregisterReceiver(mWaypointCounter);
            WaypointCounter.resetCounter();

            LocalBroadcastManager.getInstance(this).unregisterReceiver(mWaypointMapUpdater);
        }
    }

    private void showMessageBox(Context context, String message, String positiveText, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(R.string.app_name);
        dlgAlert.setPositiveButton(positiveText, null);
        dlgAlert.setCancelable(true);

        if (listener != null) dlgAlert.setPositiveButton(positiveText, listener);

        dlgAlert.create().show();
    }


    /**
     * Checks for an update
     */
    private void checkForUpdate() {

        // Start update task
        final Context context = this;
        CheckUpdateTask updateTask = new CheckUpdateTask(this, new IAsyncTaskCaller<Void, Uri>() {
            @Override
            public void onPostExecute(final Uri updateUrl) {
                if (updateUrl != null) {
                    // New update
                    android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                    dialogBuilder.setMessage(R.string.update_available)
                            .setTitle(R.string.update_available_title)
                            .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                        downloadUpdate(updateUrl);
                                    } else if (Build.VERSION.SDK_INT >= 23) {
                                        // Temporary save the download url
                                        mDownloadUri = updateUrl;
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_PERMISSION_STORAGE);
                                    }
                                }
                            })
                            .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Todo
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            }

            @Override
            public void onCancelled() {

            }

            @Override
            public void onProgressUpdate(Void... progress) {

            }

            @Override
            public void onPreExecute() {

            }
        });
        updateTask.execute();
    }

    /**
     * Downloads the file
     *
     * @param uri
     */
    private void downloadUpdate(Uri uri) {
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "GPSHawk.apk");
        final DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        // Register download receiver
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Cursor c = null;
                try {
                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    c = downloadManager.query(new DownloadManager.Query().setFilterById(id));

                    if (c.moveToFirst()) {
                        // Check if status was successful
                        int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            Intent installIntent = new Intent(Intent.ACTION_VIEW).setDataAndType(
                                    Uri.parse(c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))), "application/vnd.android.package-archive"
                            );
                            startActivity(installIntent);
                        } else {
                            String reason = c.getString(c.getColumnIndex(DownloadManager.COLUMN_REASON));
                            Log.e(Constants.PREFERENCES, "Update failed" + reason);
                        }

                        // Deregister
                        unregisterReceiver(this);
                    }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constants.REQUEST_CODE_PERMISSION_GPS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPermissionsGranted = true;
                    mStartTrackingButton.setEnabled(mPermissionsGranted);

                    initializeView();
                } else {
                    Toast.makeText(this, R.string.error_permission_location_required, Toast.LENGTH_LONG).show();
                }
                break;
            case Constants.REQUEST_CODE_PERMISSION_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadUpdate(mDownloadUri);
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (mNavigation != null) { // Is maybe null if the permissions are not set
            mNavigation.syncState();
        }

//        // Start AppService
//        Intent intent = new Intent(this, AppService.class);
//        this.startService(intent);
//        ServiceDetectionHelper.isServiceRunning(getApplicationContext(), AppService.class);


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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Store custom data
        outState.putBoolean(STATE_WAYPOINT_LISTENER, mWaypointListenerRegistered);
        outState.putInt(STATE_VEHICLE, mActiveVehicleId);
        outState.putSerializable(STATE_TRACK, mCurrentTrack);

        // Always call this
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always
        super.onRestoreInstanceState(savedInstanceState);

        // Restore custom data
        mWaypointListenerRegistered = savedInstanceState.getBoolean(STATE_WAYPOINT_LISTENER);

        mActiveVehicleId = savedInstanceState.getInt(STATE_VEHICLE);
        if (mActiveVehicleId > 0) {
            changeVehicleView(mActiveVehicleId);
        }

        mCurrentTrack = (Track) savedInstanceState.getSerializable(STATE_TRACK);

        // When already tracking, show some other elements
        initializeViewInTrackingMode();
    }
}
