package gps.fhv.at.gps_hawk.persistence.broker;

import android.content.ContentValues;
import android.database.Cursor;

import gps.fhv.at.gps_hawk.domain.DomainBase;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.helper.DateHelper;
import gps.fhv.at.gps_hawk.persistence.setup.TrackDef;
import gps.fhv.at.gps_hawk.persistence.setup.WaypointDef;

/**
 * Created by Tobias on 30.10.2015.
 */
public class TrackBroker extends BrokerBase {
    @Override
    public <T extends DomainBase> ContentValues map2db(T domain) {

        Track t = (Track) domain;

        ContentValues values = new ContentValues();

        // Int
        values.put(TrackDef._ID,t.getId());

        // Datetime
        values.put(TrackDef.COLUMN_NAME_DATETIME_START,DateHelper.toSql(t.getStartDateTime()));
        values.put(TrackDef.COLUMN_NAME_DATETIME_END,DateHelper.toSql(t.getEndDateTime()));

        return values;
    }

    @Override
    protected  <T extends DomainBase> T map2domainImpl(Cursor cursor) {
        Track t = new Track();

        // Int
        t.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TrackDef._ID)));

        // Datetime
        t.setStartDateTime(DateHelper.fromSql(cursor.getString(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_DATETIME))));
        t.setEndDateTime(DateHelper.fromSql(cursor.getString(cursor.getColumnIndexOrThrow(WaypointDef.COLUMN_NAME_DATETIME))));

        return (T)t;
    }

    @Override
    public String getTblName() {
        return TrackDef.TABLE_NAME;
    }
}
