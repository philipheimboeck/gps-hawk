package gps.fhv.at.gps_hawk.activities.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Button;

import gps.fhv.at.gps_hawk.R;

/**
 * Created by Tobias on 25.10.2015.
 */
public class SettingsFragment extends PreferenceFragment {

    private Button mButStartExport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        // Set Default values
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
    }

}
