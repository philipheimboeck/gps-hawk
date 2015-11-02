package gps.fhv.at.gps_hawk.domain;

import android.content.Context;

import java.util.List;

/**
 * Created by Tobias on 25.10.2015.
 */
public class ExportContext<T extends IExportable> {

    private List<T> _waypointList;
    private String _url;
    private Context _context;
    private String androidId;

    public List<T> getWaypointList() {
        return _waypointList;
    }

    public void setWaypointList(List<T> waypointList) {
        _waypointList = waypointList;
    }

    public String getUrl() {
        return _url;
    }

    public void setUrl(String url) {
        _url = url;
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
}
