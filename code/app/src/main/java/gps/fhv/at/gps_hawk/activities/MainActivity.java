package gps.fhv.at.gps_hawk.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import gps.fhv.at.gps_hawk.R;
import gps.fhv.at.gps_hawk.activities.fragments.CaptureFragment;
import gps.fhv.at.gps_hawk.activities.fragments.ExportFragment;
import gps.fhv.at.gps_hawk.activities.fragments.SettingsFragment;
import gps.fhv.at.gps_hawk.activities.navigation.NavigationItem;
import gps.fhv.at.gps_hawk.activities.navigation.adapter.NavigationListAdapter;

public class MainActivity extends AppCompatActivity implements CaptureFragment.OnFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ListView mDrawerList;

    // Fragments
    private CaptureFragment mCaptureFragment;
    private ExportFragment mExportFragment;
    private SettingsFragment mSettingsFragment;

    public MainActivity() {
        mCaptureFragment = new CaptureFragment();
        mExportFragment = new ExportFragment();
        mSettingsFragment = new SettingsFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find Views
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Create the navigation drawer
        populateNavigation();
        enableDrawerIcon();

        // Add Listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
    }

    private void populateNavigation() {
        try {
            ArrayList<NavigationItem> navigationItems = new ArrayList<>();
            navigationItems.add(new NavigationItem(mCaptureFragment, getString(R.string.navigation_capture), 0));
            navigationItems.add(new NavigationItem(mExportFragment, getString(R.string.navigation_export), 0));
            navigationItems.add(new NavigationItem(mSettingsFragment, getString(R.string.navigation_settings), 0));

            NavigationListAdapter adapter = new NavigationListAdapter(this, navigationItems);
            mDrawerList = (ListView) mDrawerLayout.findViewById(R.id.navigation_list);
            mDrawerList.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("FATAL", "Error creating Navigation", e);
        }
    }

    private void enableDrawerIcon() {
        // Enabling action bar app icon and let it behave as toggle button
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);

            mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);

                    invalidateOptionsMenu();
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);

                    invalidateOptionsMenu();
                }
            };
            mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        }
    }

    /**
     * Select Navigation Item
     *
     * @param position The position of the item in the navigation list
     */
    private void selectItem(int position) {
        NavigationItem item = (NavigationItem) mDrawerList.getItemAtPosition(position);
        Fragment fragment = item.getFragment();

        // Replace the fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_placeholder, fragment).commit();

        // Highlight the selected item
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    /*
     * Fragment Interfaces Methods
     */

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
