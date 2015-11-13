package gps.fhv.at.gps_hawk.persistence.broker;

import android.content.ContentValues;
import android.database.Cursor;

import gps.fhv.at.gps_hawk.domain.DomainBase;
import gps.fhv.at.gps_hawk.domain.Exception2Log;
import gps.fhv.at.gps_hawk.persistence.setup.Exception2LogDef;

/**
 * Created by Tobias on 02.11.2015.
 */
public class Exception2LogBroker extends BrokerBase {

    @Override
    protected <T extends DomainBase> ContentValues map2dbImpl(T domain) {

        Exception2Log e = (Exception2Log) domain;

        ContentValues values = new ContentValues();

        // Datetime
        values.put(Exception2LogDef.COLUMN_NAME_DATETIME, e.getDateTime());

        // String
        values.put(Exception2LogDef.COLUMN_NAME_MSG, e.getMessage());
        values.put(Exception2LogDef.COLUMN_NAME_STACK_TRACE, e.getStackTrace());

        // int
        values.put(Exception2LogDef.COLUMN_NAME_IS_EXPORTED, e.getIsExported());
        values.put(Exception2LogDef.COLUMN_NAME_LEVEL, e.getLevel());

        return values;
    }

    @Override
    protected <T extends DomainBase> T map2domainImpl(Cursor cursor) {

        Exception2Log e = new Exception2Log();

        // Datetime
        e.setDateTime(cursor.getInt(cursor.getColumnIndexOrThrow(Exception2LogDef.COLUMN_NAME_DATETIME)));

        // String
        e.setStackTrace(cursor.getString(cursor.getColumnIndexOrThrow(Exception2LogDef.COLUMN_NAME_STACK_TRACE)));
        e.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(Exception2LogDef.COLUMN_NAME_MSG)));

        // Int
        e.setIsExported(cursor.getInt(cursor.getColumnIndexOrThrow(Exception2LogDef.COLUMN_NAME_IS_EXPORTED)));
        e.setLevel(cursor.getInt(cursor.getColumnIndexOrThrow(Exception2LogDef.COLUMN_NAME_LEVEL)));

        return (T) e;
    }

    @Override
    public String getTblName() {
        return Exception2LogDef.TABLE_NAME;
    }

    @Override
    public String[] getColumns() {
        return new String[]{Exception2LogDef.COLUMN_NAME_DATETIME,
                Exception2LogDef.COLUMN_NAME_MSG,
                Exception2LogDef.COLUMN_NAME_STACK_TRACE,
                Exception2LogDef.COLUMN_NAME_LEVEL,
                Exception2LogDef.COLUMN_NAME_IS_EXPORTED};
    }

}
