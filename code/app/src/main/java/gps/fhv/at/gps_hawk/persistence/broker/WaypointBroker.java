package gps.fhv.at.gps_hawk.persistence.broker;

import android.content.ContentValues;
import android.database.Cursor;

import gps.fhv.at.gps_hawk.domain.DomainBase;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.persistence.setup.WaypointDef;

/**
 * Created by Tobias on 25.10.2015.
 */
public class WaypointBroker extends BrokerBase {

    @Override
    public <T extends DomainBase> ContentValues map2dbImpl(T domain) {
        Waypoint wp = (Waypoint) domain;

        ContentValues values = new ContentValues();

        // Int
        values.put(WaypointDef.COLUMN_NAME_NR_OF_SATTELITES, wp.getNrOfSatellites());
        values.put(WaypointDef.COLUMN_NAME_TRACK_ID, wp.getTrackId());
        values.put(WaypointDef.COLUMN_NAME_IS_EXPORTED, wp.getIsExported());
        values.put(WaypointDef.COLUMN_NAME_VEHICLE_ID, wp.getVehicleId());
        values.put(WaypointDef.COLUMN_NAME_DATETIMESTAMP, wp.getUnixtimestampCaptured());

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
    protected <T extends DomainBase> T map2domainImpl(Cursor cursor) throws IllegalArgumentException {

        Waypoint wp = new Waypoint();

        // Int
        wp.setNrOfSatellites(cursor.getInt(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_NR_OF_SATTELITES)));
        wp.setIsExported(cursor.getInt(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_IS_EXPORTED)));
        wp.setTrackId(cursor.getInt(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_TRACK_ID)));
        wp.setVehicleId(cursor.getInt(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_VEHICLE_ID)));
        wp.setUnixtimestampCaptured(cursor.getLong(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_DATETIMESTAMP)));

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

        return (T) wp;
    }

    @Override
    public String getTblName() {
        return WaypointDef.TABLE_NAME;
    }

    public String[] getColumns() {
        return new String[]{WaypointDef.COLUMN_NAME_NR_OF_SATTELITES,
                WaypointDef.COLUMN_NAME_IS_EXPORTED,
                WaypointDef.COLUMN_NAME_TRACK_ID,
                WaypointDef.COLUMN_NAME_VEHICLE_ID,
                WaypointDef.COLUMN_NAME_LAT,
                WaypointDef.COLUMN_NAME_LNG,
                WaypointDef.COLUMN_NAME_ALTITUDE,
                WaypointDef.COLUMN_PROVIDER,
                WaypointDef.COLUMN_SPEED,
                WaypointDef.COLUMN_ACCURACY,
                WaypointDef.COLUMN_BEARING,
                WaypointDef.COLUMN_NAME_DATETIMESTAMP};
    }

}
