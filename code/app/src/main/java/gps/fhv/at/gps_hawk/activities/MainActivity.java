package gps.fhv.at.gps_hawk.activities;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import gps.fhv.at.gps_hawk.R;
import gps.fhv.at.gps_hawk.activities.navigation.NavigationItem;
import gps.fhv.at.gps_hawk.activities.navigation.adapter.NavigationListAdapter;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find Views
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Create the navigation drawer
        populateNavigation();
    }

    private void populateNavigation() {
        ArrayList<NavigationItem> navigationItems = new ArrayList<>();
        navigationItems.add(new NavigationItem(getString(R.string.navigation_capture), 0));

        NavigationListAdapter adapter = new NavigationListAdapter(this, navigationItems);
        mDrawerList = (ListView) mDrawerLayout.findViewById(R.id.navigation_list);
        mDrawerList.setAdapter(adapter);

        // Enabling action bar app icon and let it behave as toggle button
        if(getActionBar() != null) {
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
}
