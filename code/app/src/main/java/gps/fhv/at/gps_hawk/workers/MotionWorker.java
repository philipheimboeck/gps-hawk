package gps.fhv.at.gps_hawk.workers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.domain.MotionValues;

/**
 * Created by Tobias on 07.11.2015.
 */
public class MotionWorker implements SensorEventListener {

    private Context mContext;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private ArrayList<MotionValues> mMotionValues = new ArrayList<>();

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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values.length >= 3) {
            MotionValues values = new MotionValues();
            values._x = event.values[0];
            values._y = event.values[1];
            values._z = event.values[2];
            values._dateTimeCaptured = event.timestamp;
            values._motionType = MotionValues.MOTION_TYPE_ACCELEROMETER;

//            mMotionValues.add(values);
            Log.d(Constants.PREFERENCES, values.toString());

            // Save 2 DB
            try {
                DbFacade db = DbFacade.getInstance(mContext);
                db.saveEntity(values);
            } catch (Exception e) {
                Log.e(Constants.PREFERENCES, "ERROR at inserting MotionValues", e);
            }
        }

        // Save 2 DB?
        if (mMotionValues.size() > 100) {

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
