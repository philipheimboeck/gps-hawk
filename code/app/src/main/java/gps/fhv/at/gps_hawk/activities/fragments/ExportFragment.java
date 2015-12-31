package gps.fhv.at.gps_hawk.activities.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import gps.fhv.at.gps_hawk.R;
import gps.fhv.at.gps_hawk.helper.ExportStartHelper;
import gps.fhv.at.gps_hawk.helper.IUpdateableView;
import gps.fhv.at.gps_hawk.tasks.ExportMetadataLoaderTask;
import gps.fhv.at.gps_hawk.tasks.UploadWaypointsTask;
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

    private TextView mTextViewAmount;
    private TextView mTextViewAmountExc;
    private TextView mTextViewAmountMotion;
    private TextView mTextViewAmountTracks;

    private View mProgressView;
    private LinearLayout mExpWrapper;
    private ExportStartHelper mExportStartHelper;
    private ExportMetadataLoaderTask mExpDataLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExportStartHelper = new ExportStartHelper(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        PreferenceManager.setDefaultValues(getContext(), R.xml.preferences, true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_export, container, false);

        mProgressView = view.findViewById(R.id.export_progress);
        mExpWrapper = (LinearLayout) view.findViewById(R.id.export_wrapper);

        // Buttons
        mButStartExport = (Button) view.findViewById(R.id.button_do_export);
        mButStartExportExc = (Button) view.findViewById(R.id.button_do_exception_export);
        mButStartExportMotions = (Button) view.findViewById(R.id.button_do_motion_export);
        mButStartExportTracks = (Button) view.findViewById(R.id.button_do_track_export);

        mButStartExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.toast_export, Toast.LENGTH_SHORT).show();
                new UploadWaypointsTask(getContext()).execute();
            }
        });
//        mButStartExportExc.setOnClickListener(mButExportListener);
//        mButStartExportMotions.setOnClickListener(mButExportListener);
//        mButStartExportTracks.setOnClickListener(mButExportListener);

        mTextViewAmount = (TextView) view.findViewById(R.id.tbx_amount_of_waypoints);
        mTextViewAmountExc = (TextView) view.findViewById(R.id.tbx_amount_of_exceptions);
        mTextViewAmountMotion = (TextView) view.findViewById(R.id.tbx_amount_of_motions);
        mTextViewAmountTracks = (TextView) view.findViewById(R.id.tbx_amount_of_tracks);

        doDataLoadingAsnc();

        return view;
    }

    public void doDataLoadingAsnc() {

        // Do loading in in background
        createDataLoadingTask();
        mExpDataLoader.execute();
    }

    private void createDataLoadingTask() {
        mExpDataLoader = new ExportMetadataLoaderTask(this, new IAsyncTaskCaller<Void, int[]>() {

            @Override
            public void onPostExecute(int[] success) {
                updText(success);
                showLoading(false);
            }

            @Override
            public void onCancelled() {

            }

            @Override
            public void onProgressUpdate(Void... progress) {

            }

            @Override
            public void onPreExecute() {
                showLoading(true);
            }

        });
    }

    /**
     * Setting all text-values
     * quite dirty, but should work for us!
     */
    public void updText(int[] amounts) {

        DbFacade db = DbFacade.getInstance(getActivity());
        int i = -1;

        // Waypoints
        mTextViewAmount.setText("" + (amounts[++i] - amounts[i + 1]) + " from " + amounts[i++] + " exported");

        // Exceptions
        mTextViewAmountExc.setText((amounts[++i] - amounts[i + 1]) + " from " + amounts[i++] + " exported");

        // Motions
        mTextViewAmountMotion.setText((amounts[++i] - amounts[i + 1]) + " from " + amounts[i++] + " exported");

        // Tracks
        mTextViewAmountTracks.setText((amounts[++i] - amounts[i + 1]) + " from " + amounts[i++] + " exported");
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

}
