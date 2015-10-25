package gps.fhv.at.gps_hawk.activities.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.R;
import gps.fhv.at.gps_hawk.persistence.setup.WaypointDef;
import gps.fhv.at.gps_hawk.services.DbFacade;

/**
 * Created by Tobias on 25.10.2015.
 */
public class ExportFragment extends Fragment {

    private Button mButStartExport;
    private TextView mTextViewAmount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_export, container, false);

        mButStartExport = (Button) view.findViewById(R.id.button_do_export);
        mButStartExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButExport();
            }
        });

        mTextViewAmount = (TextView) view.findViewById(R.id.tbx_amount_of_waypoints);

        DbFacade db = DbFacade.getInstance(getActivity());
        int amount = db.getCount(WaypointDef.TABLE_NAME);

        mTextViewAmount.setText(""+ amount);

        return view;
    }

    private void handleButExport() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = Constants.PREF_EXPORT_URL;

        if ( prefs.contains(key)) {
            String url = prefs.getString(key,"");
            Toast.makeText(getActivity(),url,Toast.LENGTH_LONG).show();
        }
    }


}
