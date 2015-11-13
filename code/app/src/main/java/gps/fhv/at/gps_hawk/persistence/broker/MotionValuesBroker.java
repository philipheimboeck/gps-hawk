package gps.fhv.at.gps_hawk.persistence.broker;

import android.content.ContentValues;
import android.database.Cursor;

import gps.fhv.at.gps_hawk.domain.DomainBase;
import gps.fhv.at.gps_hawk.domain.MotionValues;
import gps.fhv.at.gps_hawk.persistence.setup.MotionValuesDef;

/**
 * Created by Tobias on 08.11.2015.
 */
public class MotionValuesBroker extends BrokerBase {
    @Override
    protected <T extends DomainBase> ContentValues map2dbImpl(T domain) {

        MotionValues m = (MotionValues) domain;

        ContentValues values = new ContentValues();

        // Int
        values.put(MotionValuesDef.COLUMN_NAME_MOTION_TYPE, m._motionType);
        values.put(MotionValuesDef.COLUMN_NAME_IS_EXPORTED, m.getIsExported());

        // Datetime
        values.put(MotionValuesDef.COLUMN_NAME_DATETIME_CAPTURED, m._dateTimeCaptured);

        // Float
        values.put(MotionValuesDef.COLUMN_NAME_X, m._x);
        values.put(MotionValuesDef.COLUMN_NAME_Y, m._y);
        values.put(MotionValuesDef.COLUMN_NAME_Z, m._z);

        return values;
    }

    @Override
    protected <T extends DomainBase> T map2domainImpl(Cursor cursor) {

        MotionValues m = new MotionValues();

        // Int
        m._motionType = cursor.getInt(cursor.getColumnIndexOrThrow(MotionValuesDef.COLUMN_NAME_MOTION_TYPE));
        m.setIsExported(cursor.getInt(cursor.getColumnIndexOrThrow(MotionValuesDef.COLUMN_NAME_IS_EXPORTED)));

        // Datetime
        m._dateTimeCaptured = cursor.getLong(cursor.getColumnIndexOrThrow(MotionValuesDef.COLUMN_NAME_DATETIME_CAPTURED));

        // Float
        m._x = cursor.getFloat(cursor.getColumnIndexOrThrow(MotionValuesDef.COLUMN_NAME_X));
        m._y = cursor.getFloat(cursor.getColumnIndexOrThrow(MotionValuesDef.COLUMN_NAME_Y));
        m._z = cursor.getFloat(cursor.getColumnIndexOrThrow(MotionValuesDef.COLUMN_NAME_Z));

        return (T) m;
    }

    @Override
    public String getTblName() {
        return MotionValuesDef.TABLE_NAME;
    }

    @Override
    public String[] getColumns() {
        return new String[]{MotionValuesDef.COLUMN_NAME_DATETIME_CAPTURED,
                MotionValuesDef.COLUMN_NAME_X,
                MotionValuesDef.COLUMN_NAME_Y,
                MotionValuesDef.COLUMN_NAME_Z,
                MotionValuesDef.COLUMN_NAME_MOTION_TYPE,
                MotionValuesDef.COLUMN_NAME_IS_EXPORTED};
    }
}
