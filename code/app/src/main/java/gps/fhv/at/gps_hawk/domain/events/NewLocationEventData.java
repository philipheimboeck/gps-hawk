package gps.fhv.at.gps_hawk.domain.events;

import gps.fhv.at.gps_hawk.domain.Track;

/**
 * Created by Tobias on 28.10.2015.
 * Wrapper for additional data
 */
public class NewLocationEventData{
    private int _nrOfSattelites;
    private Track _track;

    public int getNrOfSattelites() {
        return _nrOfSattelites;
    }

    public void setNrOfSattelites(int nrOfSattelites) {
        _nrOfSattelites = nrOfSattelites;
    }

    public Track getTrack() {
        return _track;
    }

    public void setTrack(Track track) {
        _track = track;
    }
}
