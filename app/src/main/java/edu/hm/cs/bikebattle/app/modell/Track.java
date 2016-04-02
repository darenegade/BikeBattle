package edu.hm.cs.bikebattle.app.modell;

import java.util.Collection;

/**
 * Created by Nils on 30.03.2016.
 * <p/>
 * Class representing a track for tracking a drive.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public class Track extends WaypointList{


    /**
     * owner of the track.
     */
    private User owner;

    /**
     * Initializes the track without calculating the distance and time.
     *
     * @param waypoints     waypoints of the track
     * @param distance_in_m distance of the track
     * @param owner         user who owns the track
     */
    public Track(Collection<? extends Waypoint> waypoints, float distance_in_m, User owner) {
        super(waypoints, distance_in_m);
        if (owner == null) throw new NullPointerException("Owner can not be null!");
        setOwner(owner);
    }

    /**
     * Initializes the track.
     *
     * @param waypoints waypoints of the track
     * @param owner     user who owns the track
     */
    public Track(Collection<? extends Waypoint> waypoints, User owner) {
        super(waypoints);
        if (owner == null) throw new NullPointerException("Owner can not be null!");
        setOwner(owner);
    }

    /**
     * Initializes an empty track.
     *
     * @param owner user who owns the track
     */
    public Track(User owner) {
        super();
        setOwner(owner);
    }

    /**
     * Calculates the time of the track.
     *
     * @param track track
     * @return time
     */
    public static long calculateTime(Track track) {
        if (track == null) throw new NullPointerException("Waypoints can not be null!");
        if (track.size() > 1) {
            return track.get(0).getTime() - track.get(track.size() - 1).getTime();
        }
        return 0;
    }



    /**
     * Returns the time in second of the track.
     *
     * @return time
     */
    public long getTime_in_s() {
        return calculateTime(this);
    }

    /**
     * Returns the average speed in km/h.
     *
     * @return average speed
     */
    public float getAveragespeed_in_kmh() {
        return getDistance_in_m() * 1000 / (getTime_in_s() / 3600.0f);
    }


    /**
     * Changes the owner of the route.
     *
     * @param owner owner
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }
    /**
     * Returns the user who owns this track.
     *
     * @return owner
     */
    public User getOwner() {
        return owner;
    }



}
