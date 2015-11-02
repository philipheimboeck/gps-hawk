package gps.fhv.at.gps_hawk.activities.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.R;
import gps.fhv.at.gps_hawk.broadcast.WaypointCounter;
import gps.fhv.at.gps_hawk.domain.ExportContext;
import gps.fhv.at.gps_hawk.persistence.setup.Exception2LogDef;
import gps.fhv.at.gps_hawk.persistence.setup.WaypointDef;
import gps.fhv.at.gps_hawk.workers.DbFacade;
import gps.fhv.at.gps_hawk.tasks.ExportTask;
import gps.fhv.at.gps_hawk.tasks.IAsyncTaskCaller;

/**
 * Created by Tobias on 25.10.2015.
 */
public class ExportFragment extends Fragment {

    private Button mButStartExport;
    private TextView mTextViewAmount;
    private TextView mTextViewAmountExc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        PreferenceManager.setDefaultValues(getContext(), R.xml.preferences, true);

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
        mTextViewAmountExc = (TextView) view.findViewById(R.id.tbx_amount_of_exceptions);

        DbFacade db = DbFacade.getInstance(getActivity());
        int amount = db.getCount(WaypointDef.TABLE_NAME, null);
        int amountExported = db.getCount(WaypointDef.TABLE_NAME, WaypointDef.COLUMN_NAME_IS_EXPORTED + " = 0");

        mTextViewAmount.setText("" + (amount - amountExported) + " from " + amount + " exported");

        amount = db.getCount(Exception2LogDef.TABLE_NAME, null);
        amountExported = db.getCount(Exception2LogDef.TABLE_NAME, Exception2LogDef.COLUMN_NAME_IS_EXPORTED);

        mTextViewAmountExc.setText((amount - amountExported) + " from " + amount + " exported");

        return view;
    }

    private void handleButExport() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = Constants.PREF_EXPORT_URL;


        if (prefs.contains(key)) {
            String url = prefs.getString(key, "");
            Toast.makeText(getActivity(), url, Toast.LENGTH_LONG).show();

            ExportContext exportContext = new ExportContext();
            exportContext.setUrl(url);
            exportContext.setContext(getActivity());
            exportContext.setAndroidId(Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID));

            ExportTask exportTask = new ExportTask(exportContext, new IAsyncTaskCaller<Void, Boolean>() {

                @Override
                public void onPostExecute(Boolean success) {

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
            exportTask.execute();
        }
    }


}
