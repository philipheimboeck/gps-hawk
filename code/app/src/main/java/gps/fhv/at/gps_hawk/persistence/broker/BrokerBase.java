package gps.fhv.at.gps_hawk.persistence.broker;

import android.content.ContentValues;
import android.database.Cursor;

import gps.fhv.at.gps_hawk.domain.DomainBase;

/**
 * Created by Tobias on 25.10.2015.
 */
public abstract class BrokerBase {

    public abstract  <T extends DomainBase> ContentValues map2db(T domain);
    public abstract <T extends DomainBase> T map2domain(Cursor cursor);
    public abstract String getTblName();

}
