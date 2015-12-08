package gps.fhv.at.gps_hawk.workers;

import android.location.Location;

import gps.fhv.at.gps_hawk.domain.POI;
import gps.fhv.at.gps_hawk.domain.Vehicle;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.domain.events.NewLocationEventData;

/**
 * Author: Philip Heimböck
 * Date: 30.10.15
 */
public class WaypointFactory {

    private static WaypointFactory instance;

    public static WaypointFactory getInstance() {
        if (instance == null) {
            instance = new WaypointFactory();
        }
        return instance;
    }


    private Vehicle mVehicle;
    private POI mPOI;

    private WaypointFactory() {
    }

    /**
     * Creates a new waypoint instance
     *
     * @param location
     * @param data
     * @return
     */
    public Waypoint createWaypoint(Location location, NewLocationEventData data) {

        // Add all data to the new waypoint and return it
        return setVehicleData(
                setPOIData(
                        setLocationData(new Waypoint(), location, data)
                )
        );
    }

    private Waypoint setLocationData(Waypoint waypoint, Location location, NewLocationEventData data) {

        // Double
        waypoint.setLat(location.getLatitude());
        waypoint.setLng(location.getLongitude());
        waypoint.setAltitude(location.getAltitude());
        // Float
        waypoint.setAccuracy(location.getAccuracy());
        waypoint.setSpeed(location.getSpeed());
        waypoint.setBearing(location.getBearing());
        // Text
        waypoint.setProvider(location.getProvider());
        // Int
        waypoint.setNrOfSatellites(data.getNrOfSattelites());
        waypoint.setTrackId(data.getTrack().getId());
        waypoint.setUnixtimestampCaptured((int) (location.getTime() / 1000));

        return waypoint;
    }

    private Waypoint setPOIData(Waypoint waypoint) {
        POI poi = getPOI();
        if (poi != null) {
            // Todo: Add POI to waypoint
            setPOI(null); // No other waypoint will get this data
        }
        return waypoint;
    }

    private Waypoint setVehicleData(Waypoint waypoint) {
        Vehicle vehicle = getVehicle();
        if (vehicle != null) {
            waypoint.setVehicle(vehicle);
            waypoint.setVehicleId(vehicle.getId());
            setVehicle(null); // Reset vehicle
        }
        return waypoint;
    }

    public POI getPOI() {
        return mPOI;
    }

    public void setPOI(POI POI) {
        mPOI = POI;
    }

    public Vehicle getVehicle() {
        return mVehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        mVehicle = vehicle;
    }
}
