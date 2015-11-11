package gps.fhv.at.gps_hawk.workers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.domain.MotionValues;

/**
 * Created by Tobias on 07.11.2015.
 */
public class MotionWorker implements IMotionWorker, SensorEventListener {

    private Context mContext;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    /**
     * Indicates whether a background thread is running saving MotionValues to database
     */
    private static boolean mIsThreadWorking = false;

    private ArrayList<MotionValues> mBuffy = new ArrayList<>();
    private MotionValues[] mMotionValues = new MotionValues[Constants.MOTION_TO_DB_THRESHOLD];
    private int mCurrentMV = 0;

    private AsyncTask<Integer, Void, String> mTaskSave2Db;

    private AsyncTask<Integer, Void, String> getTaskSave2Db() {
        return new AsyncTask<Integer, Void, String>() {

            @Override
            protected String doInBackground(Integer... params) {
                try {
                    DbFacade db = DbFacade.getInstance(mContext);
                    db.saveEntities(mMotionValues, params[0]);
                    Log.d(Constants.PREFERENCES, "Leave saving MotionValues");
                } catch (Exception e) {
                    Log.e(Constants.PREFERENCES, "ERROR at inserting MotionValues", e);
                }
                mIsThreadWorking = false;

                return "";
            }
        };
    }

    public MotionWorker(Context mContext) {
        this.mContext = mContext;
    }

    public void initialize() {

        try {
            mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        } catch (Exception e) {
            Log.e(Constants.PREFERENCES, "Error requesting MotionSensors", e);
        }
    }

    public void stop() {
        mSensorManager.unregisterListener(this);

        // Save currently unsaved MotionValues
        // ignore the few values in buffy
        if (!mIsThreadWorking) {
            mTaskSave2Db = getTaskSave2Db();
            mTaskSave2Db.execute(mCurrentMV + 1);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values.length >= 3) {
            MotionValues values = new MotionValues();
            values._x = event.values[0];
            values._y = event.values[1];
            values._z = event.values[2];

            // convert to millis
            values._dateTimeCaptured = (new Date()).getTime() + (event.timestamp - System.nanoTime()) / 1000000L;

            values._motionType = MotionValues.MOTION_TYPE_ACCELEROMETER;

            // Add to arr (or buffy in case of currently saving to db)
            if (mIsThreadWorking) {
                mBuffy.add(values);
            } else {
                mMotionValues[mCurrentMV] = values;
                for (MotionValues mv : mBuffy) {
                    // cannot add more values from buffy because array is already full
                    if (mCurrentMV >= mMotionValues.length) break;
                    mMotionValues[mCurrentMV++] = mv;
                }
                if (mBuffy.size() > 0) Log.i(Constants.PREFERENCES, "Buffy-Size: " + mBuffy.size());
                mBuffy.clear();

                ++mCurrentMV;
            }

            if (mCurrentMV % Constants.MOTION_TO_DB_THRESHOLD == 0 && !mIsThreadWorking) {

                mIsThreadWorking = true;
                Log.d(Constants.PREFERENCES, "Enter saving MotionValues");

                // start new thread saving all 100 MotionValues
                mTaskSave2Db = getTaskSave2Db();
                mTaskSave2Db.execute(mCurrentMV);

            }

            mCurrentMV %= Constants.MOTION_TO_DB_THRESHOLD;

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
