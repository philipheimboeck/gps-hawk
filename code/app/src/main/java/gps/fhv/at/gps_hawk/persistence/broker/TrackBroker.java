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
        values.put(TrackDef.COLUMN_NAME_IS_VALID, t.getIsValid());
        values.put(TrackDef.COLUMN_NAME_IS_EXPORTED, t.getIsExported());
        values.put(TrackDef.COLUMN_NAME_EXTERNAL_ID, t.getExternalId());

        return values;
    }

    @Override
    protected <T extends DomainBase> T map2domainImpl(Cursor cursor) {
        Track t = new Track();

        // Datetime
        t.setStartDateTime(cursor.getInt(cursor.getColumnIndexOrThrow(TrackDef.COLUMN_NAME_DATETIME_START)));
        t.setEndDateTime(cursor.getInt(cursor.getColumnIndexOrThrow(TrackDef.COLUMN_NAME_DATETIME_END)));
        t.setIsValid(cursor.getInt(cursor.getColumnIndexOrThrow(TrackDef.COLUMN_NAME_IS_VALID)));
        t.setIsExported(cursor.getInt(cursor.getColumnIndexOrThrow(TrackDef.COLUMN_NAME_IS_EXPORTED)));
        t.setExternalId(cursor.getString(cursor.getColumnIndexOrThrow(TrackDef.COLUMN_NAME_EXTERNAL_ID)));

        return (T) t;
    }

    @Override
    public String getTblName() {
        return TrackDef.TABLE_NAME;
    }

    @Override
    public String[] getColumns() {
        return new String[]{TrackDef.COLUMN_NAME_DATETIME_END, TrackDef.COLUMN_NAME_DATETIME_START, TrackDef.COLUMN_NAME_EXTERNAL_ID};
    }

    @Override
    public String getExportableWhere() {
        return TrackDef.COLUMN_NAME_EXTERNAL_ID + " IS NOT NULL AND " + TrackDef.COLUMN_NAME_DATETIME_START + " > 0 AND " + TrackDef.COLUMN_NAME_DATETIME_END + " > 0";
    }
}
