package gps.fhv.at.gps_hawk.activities.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.R;
import gps.fhv.at.gps_hawk.helper.MyLocationListener;
import gps.fhv.at.gps_hawk.services.GpsSvc;


public class CaptureFragment extends Fragment {

    private boolean mPermissionsGranted = false;

    private OnFragmentInteractionListener mListener;
    private GpsSvc mGpsService;
    private SupportMapFragment mMapFragment;
    private LocationManager locationManager;
    private MyLocationListener myLocationListener;
    private Button mButStartTracking;

    public CaptureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check for permissions
        mPermissionsGranted =
                PermissionChecker.checkCallingOrSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!mPermissionsGranted) {
            Toast.makeText(getActivity(), "Please enable GPS", Toast.LENGTH_LONG).show();

            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }

            return;
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
        }

    }

    private void handleButStart() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mGpsService = new GpsSvc(locationManager, getActivity());
        boolean gpsRS = mGpsService.initialize();
        if (!gpsRS) {
            showMessageBox(getActivity(), getResources().getString(R.string.enable_gps_button), getResources().getString(R.string.enable_gps_button_positive), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(i);
                }
            });
        }

    }

    protected void showMessageBox(Context context, String message, String positiveText, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(R.string.app_name);
        dlgAlert.setPositiveButton(positiveText, null);
        dlgAlert.setCancelable(true);

        if (listener != null) dlgAlert.setPositiveButton(positiveText, listener);

        dlgAlert.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPermissionsGranted = true;
                    mButStartTracking.setEnabled(true);
                } else {
                    Toast.makeText(getActivity(), R.string.error_permission_location_required, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }


    // Todo: Dirty Hack! Get rid of this static variable
    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Todo: Dirty Hack! Shouldn't use nested fragments. CaptureFragment should therefore be an activity
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_capture, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        mButStartTracking = (Button) view.findViewById(R.id.button_tracking);
        mButStartTracking.setEnabled(mPermissionsGranted);
        mButStartTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButStart();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
