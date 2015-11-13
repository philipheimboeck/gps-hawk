package gps.fhv.at.gps_hawk.persistence.broker;

import android.content.ContentValues;
import android.database.Cursor;

import gps.fhv.at.gps_hawk.domain.DomainBase;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.persistence.setup.TrackDef;

/**
 * Created by Tobias on 30.10.2015.
 */
public class TrackBroker extends BrokerBase {
    @Override
    public <T extends DomainBase> ContentValues map2dbImpl(T domain) {

        Track t = (Track) domain;

        ContentValues values = new ContentValues();

        // Datetime
        values.put(TrackDef.COLUMN_NAME_DATETIME_START, t.getStartDateTime());
        values.put(TrackDef.COLUMN_NAME_DATETIME_END, t.getEndDateTime());

        return values;
    }

    @Override
    protected <T extends DomainBase> T map2domainImpl(Cursor cursor) {
        Track t = new Track();

        // Datetime
        t.setStartDateTime(cursor.getInt(cursor.getColumnIndexOrThrow(TrackDef.COLUMN_NAME_DATETIME_START)));
        t.setEndDateTime(cursor.getInt(cursor.getColumnIndexOrThrow(TrackDef.COLUMN_NAME_DATETIME_END)));

        return (T) t;
    }

    @Override
    public String getTblName() {
        return TrackDef.TABLE_NAME;
    }

    @Override
    public String[] getColumns() {
        return new String[]{TrackDef.COLUMN_NAME_DATETIME_END, TrackDef.COLUMN_NAME_DATETIME_START};
    }
}
