package gps.fhv.at.gps_hawk.persistence.broker;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.domain.DomainBase;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.helper.DateHelper;
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
        if (t.getStartDateTime() != null)
            values.put(TrackDef.COLUMN_NAME_DATETIME_START, DateHelper.toSql(t.getStartDateTime()));
        if (t.getEndDateTime() != null)
            values.put(TrackDef.COLUMN_NAME_DATETIME_END, DateHelper.toSql(t.getEndDateTime()));

        return values;
    }

    @Override
    protected <T extends DomainBase> T map2domainImpl(Cursor cursor) {
        Track t = new Track();

        // Datetime
        String toConvert = cursor.getString(cursor.getColumnIndexOrThrow(TrackDef.COLUMN_NAME_DATETIME_START));
        if (toConvert != null) {
            try {
                t.setStartDateTime(DateHelper.fromSql(toConvert));
            } catch (Exception e) {
                Log.e(Constants.PREFERENCES, "error converting to Calendar form SQL", e);
            }
        }

        toConvert = cursor.getString(cursor.getColumnIndexOrThrow(TrackDef.COLUMN_NAME_DATETIME_END));
        if (toConvert != null) {
            try {
                t.setEndDateTime(DateHelper.fromSql(toConvert));
            } catch (Exception e) {
                Log.e(Constants.PREFERENCES, "error converting to Calendar form SQL", e);

            }
        }

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
