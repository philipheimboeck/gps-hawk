package gps.fhv.at.gps_hawk.activities.navigation;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.R;
import gps.fhv.at.gps_hawk.activities.navigation.adapter.NavigationListAdapter;

/**
 * Author: Philip Heimböck
 * Date: 01.11.15
 */
public class Navigation {

    private AppCompatActivity mActivity;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;

    private ActionBarDrawerToggle mDrawerToggle;

    public Navigation(AppCompatActivity activity, DrawerLayout layout, ListView drawerList) {
        mActivity = activity;
        mDrawerLayout = layout;
        mDrawerListView = drawerList;
    }

    /**
     * Populate the navigation drawer with items
     */
    public void populateNavigation(List<NavigationItem> navigationItems) {
        try {
            NavigationListAdapter adapter = new NavigationListAdapter(mActivity, navigationItems);
            mDrawerListView.setAdapter(adapter);

            // Add Listener
            mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectItem(position);
                }
            });

        } catch (Exception e) {
            Log.e(Constants.PREFERENCES, "Error creating Navigation", e);
        }
    }

    public void enableDrawerIcon() {
        // Enabling action bar app icon and let it behave as toggle button
        ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar != null) {
            mDrawerToggle = new ActionBarDrawerToggle(mActivity, mDrawerLayout, R.string.app_name, R.string.app_name) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
            };
            mDrawerLayout.setDrawerListener(mDrawerToggle);

            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void syncState() {
        mDrawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConf) {
        mDrawerToggle.onConfigurationChanged(newConf);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    /**
     * Select Navigation Item
     *
     * @param position The position of the item in the navigation list
     */
    private void selectItem(int position) {
        NavigationItem item = (NavigationItem) mDrawerListView.getItemAtPosition(position);
        NavigationAction action = item.getAction();

        switch (action.getActionType()) {
            case ACTION_NOTHING:
                // Do nothing
                break;
            case ACTION_FINISH:
                // Finish
                mActivity.finish();
                break;
            case ACTION_CHANGE_ACTIVITY:
                Intent intent = (Intent) action.getObject();
                mActivity.startActivity(intent);
                break;
            case ACTION_CHANGE_FRAGMENT:
                // Replace the fragment
                Fragment fragment = (Fragment) action.getObject();
                FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_placeholder, fragment)
                        .commit();
                break;
        }

        // Highlight the selected item
        mDrawerListView.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerListView);
    }
}
