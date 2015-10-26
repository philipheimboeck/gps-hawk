package gps.fhv.at.gps_hawk.domain;

import android.content.Context;

import java.util.List;

/**
 * Created by Tobias on 25.10.2015.
 */
public class ExportContext {

    private List<Waypoint> _waypointList;
    private String _url;
    private Context _context;

    public List<Waypoint> getWaypointList() {
        return _waypointList;
    }

    public void setWaypointList(List<Waypoint> waypointList) {
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
}
