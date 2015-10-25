package gps.fhv.at.gps_hawk.persistence.broker;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        values.put(WaypointDef.COLUMN_NAME_NR_OF_SATTELITES, wp.getNrOfSattelites());
        values.put(WaypointDef.COLUMN_NAME_DATETIME, DateHelper.toSql(wp.getTimestampCaptured()));
        values.put(WaypointDef.COLUMN_NAME_POSITION_ID, wp.getPositionId());

        return values;
    }

    @Override
    public <T extends DomainBase> T map2domain(Cursor cursor) {

        Waypoint wp = new Waypoint();

        wp.setNrOfSattelites(cursor.getInt(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_NR_OF_SATTELITES)));
        wp.setPositionId(cursor.getInt(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_POSITION_ID)));
        wp.setId(cursor.getLong(cursor.getColumnIndexOrThrow(WaypointDef._ID)));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            cal.setTime(sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_POSITION_ID))));
            wp.setTimestampCaptured(cal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (T) wp;
    }

    @Override
    public String getTblName() {
        return WaypointDef.TABLE_NAME;
    }

}
