package gps.fhv.at.gps_hawk.domain.events;

/**
 * Created by Tobias on 28.10.2015.
 * Wrapper for additional data
 */
public class NewLocationEventData{
    private int _nrOfSattelites;

    public int getNrOfSattelites() {
        return _nrOfSattelites;
    }

    public void setNrOfSattelites(int nrOfSattelites) {
        _nrOfSattelites = nrOfSattelites;
    }
}
