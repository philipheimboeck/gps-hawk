package gps.fhv.at.gps_hawk.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import gps.fhv.at.gps_hawk.R;
import gps.fhv.at.gps_hawk.activities.fragments.ExportFragment;
import gps.fhv.at.gps_hawk.activities.fragments.SettingsFragment;
import gps.fhv.at.gps_hawk.activities.navigation.NavigationAction;
import gps.fhv.at.gps_hawk.activities.navigation.Navigation;
import gps.fhv.at.gps_hawk.activities.navigation.NavigationItem;

public class FragmentsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    // Fragments
    private ExportFragment mExportFragment;
    private SettingsFragment mSettingsFragment;
    private Navigation mNavigation;

    public FragmentsActivity() {
        mExportFragment = new ExportFragment();
        mSettingsFragment = new SettingsFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show first fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_placeholder, mExportFragment).commit();

        // Find Views
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navigation_list);

        // Create the navigation drawer
        ArrayList<NavigationItem> navigationItems = new ArrayList<>();
        navigationItems.add(new NavigationItem(new NavigationAction().setActionType(NavigationAction.NavigationActionType.ACTION_FINISH),
                getString(R.string.navigation_capture), R.drawable.ic_hawk_white));
        navigationItems.add(new NavigationItem(new NavigationAction(mExportFragment),
                getString(R.string.navigation_export), 0));
        navigationItems.add(new NavigationItem(new NavigationAction(mSettingsFragment),
                getString(R.string.navigation_settings), R.drawable.ic_setting_dark));

        mNavigation = new Navigation(this, mDrawerLayout, mDrawerList);
        mNavigation.populateNavigation(navigationItems);
        mNavigation.enableDrawerIcon();
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
