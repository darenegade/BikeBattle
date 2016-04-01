package edu.hm.cs.bikebattle.app.modell;

import android.location.Location;
import android.os.Parcelable;

/**
 * Created by Nils on 30.03.2016.
 * This class represents waypoints. The super class is Location from the google maps api.
 * Additional functions can be added here.
 *
 * @author Nils Bernahrdt
 * @version 0.1
 */
public class Waypoint extends Location {
    /**
     * @see #Location#CREATOR
     */
    public static final Parcelable.Creator CREATOR = null;//TODO if needed


    /**
     * @param location location
     * @see #Location#Location(Location)
     */
    public Waypoint(Location location) {
        super(location);
    }
}
