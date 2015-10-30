package gps.fhv.at.gps_hawk.persistence.broker;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import gps.fhv.at.gps_hawk.domain.DomainBase;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.helper.DateHelper;
import gps.fhv.at.gps_hawk.persistence.setup.WaypointDef;

/**
 * Created by Tobias on 25.10.2015.
 */
public class WaypointBroker extends BrokerBase {

    @Override
    public <T extends DomainBase> ContentValues map2db(T domain) {
        Waypoint wp = (Waypoint) domain;

        ContentValues values = new ContentValues();

        // Int
        if (wp.getId() > 0) values.put(WaypointDef._ID, wp.getId());
        values.put(WaypointDef.COLUMN_NAME_NR_OF_SATTELITES, wp.getNrOfSattelites());
        values.put(WaypointDef.COLUMN_NAME_TRACK_ID, wp.getTrackId());
        values.put(WaypointDef.COLUMN_NAME_IS_EXPORTED, wp.getIsExported());

        // Datetime
        values.put(WaypointDef.COLUMN_NAME_DATETIME, DateHelper.toSql(wp.getTimestampCaptured()));

        // Float
        values.put(WaypointDef.COLUMN_ACCURACY, wp.getAccuracy());
        values.put(WaypointDef.COLUMN_BEARING, wp.getBearing());
        values.put(WaypointDef.COLUMN_SPEED, wp.getSpeed());

        // Double
        values.put(WaypointDef.COLUMN_NAME_LAT, wp.getLat());
        values.put(WaypointDef.COLUMN_NAME_LNG, wp.getLng());
        values.put(WaypointDef.COLUMN_NAME_ALTITUDE, wp.getAltitude());

        // Text
        values.put(WaypointDef.COLUMN_PROVIDER, wp.getProvider());

        return values;
    }

    @Override
    public <T extends DomainBase> T map2domain(Cursor cursor) {

        Waypoint wp = new Waypoint();

        // Int
        wp.setId(cursor.getInt(cursor.getColumnIndexOrThrow(WaypointDef._ID)));
        wp.setNrOfSattelites(cursor.getInt(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_NR_OF_SATTELITES)));
        wp.setIsExported(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_IS_EXPORTED));
        wp.setTrackId(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_TRACK_ID));

        // Double
        wp.setLat(cursor.getDouble(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_LAT)));
        wp.setLng(cursor.getDouble(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_LNG)));
        wp.setAltitude(cursor.getDouble(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_ALTITUDE)));

        // Text
        wp.setProvider(cursor.getString(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_PROVIDER)));

        // Float
        wp.setSpeed(cursor.getFloat(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_SPEED)));
        wp.setAccuracy(cursor.getFloat(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_ACCURACY)));
        wp.setBearing(cursor.getFloat(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_BEARING)));

        // Datetime
        wp.setTimestampCaptured(DateHelper.fromSql(cursor.getString(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_DATETIME))));

        return (T) wp;
    }

    @Override
    public String getTblName() {
        return WaypointDef.TABLE_NAME;
    }

}
