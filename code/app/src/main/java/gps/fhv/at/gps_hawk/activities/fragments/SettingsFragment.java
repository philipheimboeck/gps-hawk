package gps.fhv.at.gps_hawk.activities.fragments;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Button;

import gps.fhv.at.gps_hawk.R;

/**
 * Created by Tobias on 25.10.2015.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        // Set Default values
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }

}
