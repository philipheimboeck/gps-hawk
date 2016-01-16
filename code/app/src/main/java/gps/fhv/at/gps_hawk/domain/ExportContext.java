package gps.fhv.at.gps_hawk.domain;

import android.content.Context;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Tobias on 25.10.2015.
 */
public class ExportContext<T extends IExportable> {

    private List<T> _exportList;
    private Context _context;
    private String androidId;
    private Type t;
    private String collectionName;
    private String customWhere;

    public List<T> getExportList() {
        return _exportList;
    }

    public void setExportList(List<T> waypointList) {
        _exportList = waypointList;
    }

    public Context getContext() {
        return _context;
    }

    public void setContext(Context context) {
        _context = context;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public Type getT() {
        return t;
    }

    public void setT(Type t) {
        this.t = t;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCustomWhere() {
        return customWhere;
    }

    public void setCustomWhere(String customWhere) {
        this.customWhere = customWhere;
    }
}
