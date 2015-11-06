package gps.fhv.at.gps_hawk.workers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import gps.fhv.at.gps_hawk.domain.DomainBase;
import gps.fhv.at.gps_hawk.domain.Vehicle;

/**
 * The aim of this class is to hold instances of domain-objects that are not present in database
 * Most objects are filled at application startup
 *
 * Created by Tobias on 05.11.2015.
 */
public class VolatileInstancePool {

    private static VolatileInstancePool mInstance;
    private HashMap<Type,ArrayList<DomainBase>> mObjects;

    public static VolatileInstancePool getInstance() {
        if ( mInstance == null) mInstance = new VolatileInstancePool();
        return mInstance;
    }

    public void initialize() {

        // Only do it once
        if ( mObjects == null ) {
            mObjects = new HashMap<>();

            registerObject(Vehicle.class,new Vehicle(1));
            registerObject(Vehicle.class,new Vehicle(2));
            registerObject(Vehicle.class,new Vehicle(3));
            registerObject(Vehicle.class,new Vehicle(4));
            registerObject(Vehicle.class,new Vehicle(5));
        }
    }

    public void registerObject(Type t, DomainBase obj) {
        if ( !mObjects.containsKey(t)) {
            mObjects.put(t,new ArrayList<DomainBase>());
        }
        mObjects.get(t).add(obj);
    }

    public <T extends DomainBase> ArrayList<T> getAllRegistered(Type t) {
        if ( mObjects.containsKey(t)) {
            return (ArrayList) mObjects.get(t);
        }
        return new ArrayList<>();
    }


}
