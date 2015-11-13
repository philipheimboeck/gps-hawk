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
    private long mLastCapturedAt = -1;

    private AsyncTask<Integer, Void, String> mTaskSave2Db;
    private int mMinTimeGapInMillis = 0;

    private AsyncTask<Integer, Void, String> getTaskSave2Db() {
        return new AsyncTask<Integer, Void, String>() {

            @Override
            protected String doInBackground(Integer... params) {
                try {
                    DbFacade db = DbFacade.getInstance(mContext);
                    db.saveEntities(mMotionValues, params[0]);
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

            mMinTimeGapInMillis = (int) SettingsWorker.getInstance().getSetting(Constants.SETTING_ACCELERATION_MIN_TIME_GAP);

        } catch (Exception e) {
            Log.e(Constants.PREFERENCES, "Error requesting MotionSensors", e);
        }
    }

    public void stop() {
        mSensorManager.unregisterListener(this);

        // Save currently unsaved MotionValues
        // ignore the few values in buffy
        if (!mIsThreadWorking) {
            insert2Db();
        }
    }

    private static MotionValues createMV(SensorEvent event) {
        MotionValues values = new MotionValues();
        values._x = event.values[0];
        values._y = event.values[1];
        values._z = event.values[2];
        values._motionType = MotionValues.MOTION_TYPE_ACCELEROMETER;

        // convert to millis
        values._dateTimeCaptured = (new Date()).getTime() + (event.timestamp - System.nanoTime()) / 1000000L;

        return values;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values.length >= 3) {

            // Create Motion-Value
            MotionValues values = createMV(event);

            // Use already converted millis to decide whether to save
            if (!isValidEvent(values)) return;

            // Else: use MotionValues and reset
            mLastCapturedAt = values._dateTimeCaptured;

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

                // Log if buffy contains more than 90% of Array-Capacity
                if (mBuffy.size() > 0 && ((double) mBuffy.size() / mMotionValues.length) > 0.9)
                    Log.i(Constants.PREFERENCES, "Buffy-Size: " + mBuffy.size());

                // Always clear whole buffy
                mBuffy.clear();

                ++mCurrentMV;
            }

            if (mCurrentMV % Constants.MOTION_TO_DB_THRESHOLD == 0 && !mIsThreadWorking) {
                insert2Db();
            }

            mCurrentMV %= Constants.MOTION_TO_DB_THRESHOLD;

        }

    }

    private synchronized void insert2Db() {

        mIsThreadWorking = true;
        Log.d(Constants.PREFERENCES, "Enter saving MotionValues");

        // start new thread saving all valid MotionValues
        mTaskSave2Db = getTaskSave2Db();
        mTaskSave2Db.execute(mCurrentMV);

    }

    private boolean isValidEvent(MotionValues mv) {
        if (mv._dateTimeCaptured - mLastCapturedAt > mMinTimeGapInMillis) {
            return true;
        }
        return false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
