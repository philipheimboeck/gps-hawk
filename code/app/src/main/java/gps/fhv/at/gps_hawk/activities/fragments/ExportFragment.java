package gps.fhv.at.gps_hawk.activities.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import gps.fhv.at.gps_hawk.domain.Exception2Log;
import gps.fhv.at.gps_hawk.domain.ExportContext;
import gps.fhv.at.gps_hawk.domain.MotionValues;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.persistence.setup.Exception2LogDef;
import gps.fhv.at.gps_hawk.persistence.setup.MotionValuesDef;
import gps.fhv.at.gps_hawk.persistence.setup.WaypointDef;
import gps.fhv.at.gps_hawk.workers.DbFacade;
import gps.fhv.at.gps_hawk.tasks.ExportTask;
import gps.fhv.at.gps_hawk.tasks.IAsyncTaskCaller;

/**
 * Created by Tobias on 25.10.2015.
 */
public class ExportFragment extends Fragment {

    private Button mButStartExport;
    private Button mButStartExportExc;
    private Button mButStartExportMotions;

    private View.OnClickListener mButExportListener;

    private TextView mTextViewAmount;
    private TextView mTextViewAmountExc;
    private TextView mTextViewAmountMotion;

    private View mProgressView;
    private ExportTask mExportTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        PreferenceManager.setDefaultValues(getContext(), R.xml.preferences, true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_export, container, false);

        mProgressView = view.findViewById(R.id.export_progress);

        // Buttons
        mButStartExport = (Button) view.findViewById(R.id.button_do_export);
        mButStartExportExc = (Button) view.findViewById(R.id.button_do_exception_export);
        mButStartExportMotions = (Button) view.findViewById(R.id.button_do_motion_export);

        mButExportListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButExport(v.getId());
            }
        };
        mButStartExport.setOnClickListener(mButExportListener);
        mButStartExportExc.setOnClickListener(mButExportListener);
        mButStartExportMotions.setOnClickListener(mButExportListener);

        mTextViewAmount = (TextView) view.findViewById(R.id.tbx_amount_of_waypoints);
        mTextViewAmountExc = (TextView) view.findViewById(R.id.tbx_amount_of_exceptions);
        mTextViewAmountMotion = (TextView) view.findViewById(R.id.tbx_amount_of_motions);

        DbFacade db = DbFacade.getInstance(getActivity());

        // Waypoints
        int amount = db.getCount(WaypointDef.TABLE_NAME, null);
        int amountExported = db.getCount(WaypointDef.TABLE_NAME, WaypointDef.COLUMN_NAME_IS_EXPORTED + " = 0");

        mTextViewAmount.setText("" + (amount - amountExported) + " from " + amount + " exported");

        // Exceptions
        amount = db.getCount(Exception2LogDef.TABLE_NAME, null);
        amountExported = db.getCount(Exception2LogDef.TABLE_NAME, Exception2LogDef.COLUMN_NAME_IS_EXPORTED + " = 0");

        mTextViewAmountExc.setText((amount - amountExported) + " from " + amount + " exported");

        // Motions
        amount = db.getCount(MotionValuesDef.TABLE_NAME, null);
        amountExported = db.getCount(MotionValuesDef.TABLE_NAME, MotionValuesDef.COLUMN_NAME_IS_EXPORTED + " = 0");

        mTextViewAmountMotion.setText((amount - amountExported) + " from " + amount + " exported");

        return view;
    }

    private void showLoading(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }

    private void handleButExport(int id) {
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

            switch (id) {
                case R.id.button_do_export:
                    exportContext.setT(Waypoint.class);
                    exportContext.setCollectionName("waypoints");
                    break;
                case R.id.button_do_exception_export:
                    exportContext.setT(Exception2Log.class);
                    exportContext.setCollectionName("exceptions");
                    break;
                case R.id.button_do_motion_export:
                    exportContext.setT(MotionValues.class);
                    exportContext.setCollectionName("motionValues");
                    break;
                default:
                    throw new RuntimeException("Unallowed button invokd handleButExport");
            }

            mExportTask = new ExportTask(exportContext, new IAsyncTaskCaller<Void, String>() {

                @Override
                public void onPostExecute(String success) {

                    if ("ERROR".equals(success)) {
                        Toast.makeText(getContext(), getString(R.string.error_export_failed), Toast.LENGTH_LONG).show();
                    }

                    showLoading(false);
                }

                @Override
                public void onCancelled() {
                    showLoading(true);
                }

                @Override
                public void onProgressUpdate(Void... progress) {

                }

                @Override
                public void onPreExecute() {
                    showLoading(true);
                }
            });
            mExportTask.execute();
        }
    }


}
