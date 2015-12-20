package gps.fhv.at.gps_hawk.activities.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.R;
import gps.fhv.at.gps_hawk.domain.Exception2Log;
import gps.fhv.at.gps_hawk.domain.ExportContext;
import gps.fhv.at.gps_hawk.domain.MotionValues;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.helper.ExportStartHelper;
import gps.fhv.at.gps_hawk.helper.IUpdateableView;
import gps.fhv.at.gps_hawk.persistence.setup.Exception2LogDef;
import gps.fhv.at.gps_hawk.persistence.setup.MotionValuesDef;
import gps.fhv.at.gps_hawk.persistence.setup.TrackDef;
import gps.fhv.at.gps_hawk.persistence.setup.WaypointDef;
import gps.fhv.at.gps_hawk.workers.DbFacade;
import gps.fhv.at.gps_hawk.tasks.ExportTask;
import gps.fhv.at.gps_hawk.tasks.IAsyncTaskCaller;

/**
 * Created by Tobias on 25.10.2015.
 */
public class ExportFragment extends Fragment implements IUpdateableView {

    private Button mButStartExport;
    private Button mButStartExportExc;
    private Button mButStartExportMotions;
    private Button mButStartExportTracks;

    private View.OnClickListener mButExportListener;

    private TextView mTextViewAmount;
    private TextView mTextViewAmountExc;
    private TextView mTextViewAmountMotion;
    private TextView mTextViewAmountTracks;

    private View mProgressView;
    private ExportTask mExportTask;
    private LinearLayout mExpWrapper;
    private ExportStartHelper mExportStartHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExportStartHelper = new ExportStartHelper(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        PreferenceManager.setDefaultValues(getContext(), R.xml.preferences, true);

        // TODO: remove before deployment
        // And also remove entries in server-db between 08:42 and ... on 13.11.2015 from my deviceid!
        // And also remove entries in server-db between 08:00 and ... on 28.11.2015 from my deviceid!
        DbFacade db = DbFacade.getInstance(getContext());
        db.markExportable(2, 0, Waypoint.class);
        db.markExportable(2, 0, Exception2Log.class);
        db.markExportable(2, 0, MotionValues.class);
        db.markExportable(2, 0, Track.class);
//        db.markExportable(0, 1, Waypoint.class);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_export, container, false);

        mProgressView = view.findViewById(R.id.export_progress);
        mExpWrapper = (LinearLayout) view.findViewById(R.id.export_wrapper);

        // Buttons
        mButStartExport = (Button) view.findViewById(R.id.button_do_export);
        mButStartExportExc = (Button) view.findViewById(R.id.button_do_exception_export);
        mButStartExportMotions = (Button) view.findViewById(R.id.button_do_motion_export);
        mButStartExportTracks = (Button) view.findViewById(R.id.button_do_track_export);

        mButExportListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButExport(v.getId());
            }
        };
        mButStartExport.setOnClickListener(mButExportListener);
        mButStartExportExc.setOnClickListener(mButExportListener);
        mButStartExportMotions.setOnClickListener(mButExportListener);
        mButStartExportTracks.setOnClickListener(mButExportListener);

        mTextViewAmount = (TextView) view.findViewById(R.id.tbx_amount_of_waypoints);
        mTextViewAmountExc = (TextView) view.findViewById(R.id.tbx_amount_of_exceptions);
        mTextViewAmountMotion = (TextView) view.findViewById(R.id.tbx_amount_of_motions);
        mTextViewAmountTracks = (TextView) view.findViewById(R.id.tbx_amount_of_tracks);

        updText();

        return view;
    }

    /**
     * Setting all text-values
     * quite dirty, but should work for us!
     */
    private void updText() {

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

        // Tracks
        amount = db.getCount(TrackDef.TABLE_NAME, null);
        amountExported = db.getCount(TrackDef.TABLE_NAME, TrackDef.COLUMN_NAME_IS_EXPORTED + " = 0");

        mTextViewAmountTracks.setText((amount - amountExported) + " from " + amount + " exported");
    }

    public void showLoading(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

        disableRek(mExpWrapper, !show);

        // Update text when buttons are enabled again
        if (!show) {
            updText();
        }

    }

    private void disableRek(ViewGroup view, boolean isEnabled) {
        int i = 0;
        while (i < view.getChildCount()) {
            View v = view.getChildAt(i);
            if (v instanceof ViewGroup) {
                disableRek((ViewGroup) v, isEnabled);
            } else if (v instanceof Button) {
                v.setEnabled(isEnabled);
            }
            ++i;
        }
    }

    private void handleButExport(int id) {

        mExportStartHelper.startExport(id);

    }


}
