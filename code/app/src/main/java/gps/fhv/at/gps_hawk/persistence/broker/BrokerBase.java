package gps.fhv.at.gps_hawk.persistence.broker;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import gps.fhv.at.gps_hawk.domain.DomainBase;

/**
 * Created by Tobias on 25.10.2015.
 */
public abstract class BrokerBase {

    public <T extends DomainBase> ContentValues map2db(T domain) {
        ContentValues contentValues = map2dbImpl(domain);

        // Only map id, if already known (auto_increment issue)
        if (domain.getId() > 0) contentValues.put(BaseColumns._ID, domain.getId());

        return contentValues;
    }
    protected abstract  <T extends DomainBase> ContentValues map2dbImpl(T domain);

    public <T extends DomainBase> T map2domain(Cursor cursor) {
        try {

            T domain = map2domainImpl(cursor);

            domain.setId(cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)));

            return domain;
        } catch (IllegalArgumentException e) {
            Log.e("FATAL","Tried to access unfetched column in cursor",e);
            throw e;
        }
    }
    protected abstract <T extends DomainBase> T map2domainImpl(Cursor cursor);

    public abstract String getTblName();

    public abstract String[] getColumns();

}
