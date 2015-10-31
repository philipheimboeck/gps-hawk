package gps.fhv.at.gps_hawk.persistence.broker;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import gps.fhv.at.gps_hawk.domain.DomainBase;

/**
 * Created by Tobias on 25.10.2015.
 */
public abstract class BrokerBase {

    public abstract  <T extends DomainBase> ContentValues map2db(T domain);

    public <T extends DomainBase> T map2domain(Cursor cursor) {
        try {
            return map2domainImpl(cursor);
        } catch (IllegalArgumentException e) {
            Log.e("FATAL","Tried to access unfetched column in cursor",e);
            throw e;
        }
    }
    protected abstract <T extends DomainBase> T map2domainImpl(Cursor cursor);

    public abstract String getTblName();

}
