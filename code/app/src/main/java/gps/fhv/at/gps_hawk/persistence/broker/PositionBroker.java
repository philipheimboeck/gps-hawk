package gps.fhv.at.gps_hawk.persistence.broker;

import android.content.ContentValues;
import android.database.Cursor;

import gps.fhv.at.gps_hawk.domain.DomainBase;
import gps.fhv.at.gps_hawk.domain.Position;
import gps.fhv.at.gps_hawk.helper.DateHelper;
import gps.fhv.at.gps_hawk.persistence.setup.PositionDef;
import gps.fhv.at.gps_hawk.persistence.setup.WaypointDef;

/**
 * Created by Tobias on 25.10.2015.
 */
public class PositionBroker extends BrokerBase {

    @Override
    public <T extends DomainBase> ContentValues map2db(T domain) {
        Position p = (Position) domain;

        ContentValues values = new ContentValues();
        values.put(PositionDef.COLUMN_NAME_LAT, p.getLat());
        values.put(PositionDef.COLUMN_NAME_LNG, p.getLng());

        return values;
    }

    @Override
    public <T extends DomainBase> T map2domain(Cursor cursor) {

        Position p = new Position();

        p.setLat(cursor.getDouble(cursor.getColumnIndexOrThrow(PositionDef.COLUMN_NAME_LAT)));
        p.setLng(cursor.getDouble(cursor.getColumnIndexOrThrow(PositionDef.COLUMN_NAME_LNG)));
        p.setId(cursor.getLong(cursor.getColumnIndexOrThrow(PositionDef._ID)));

        return null;
    }

    @Override
    public String getTblName() {
        return PositionDef.TABLE_NAME;
    }
}
