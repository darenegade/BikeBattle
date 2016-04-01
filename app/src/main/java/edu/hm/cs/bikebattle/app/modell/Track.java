package edu.hm.cs.bikebattle.app.modell;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nils on 30.03.2016.
 * <p/>
 * Class representing a track for tracking a drive.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public class Track {
    /**
     * list of the waypoints.
     */
    private List<Waypoint> waypoints;
    /**
     * time used for the track.
     */
    private long time_in_s;
    /**
     * distance of the the track.
     */
    private float distance_in_m;
    /**
     * owner of the track.
     */
    private final User owner;

    /**
     * Initializes the track without calculating the distance and time.
     *
     * @param waypoints     waypoints of the track
     * @param time_in_s     time used for the track
     * @param distance_in_m distance of the track
     * @param owner         user who owns the track
     */
    public Track(List<Waypoint> waypoints, long time_in_s, float distance_in_m, User owner) {
        if (waypoints == null) throw new NullPointerException("Waypoints can not be null!");
        this.waypoints = waypoints;
        this.time_in_s = time_in_s;
        this.distance_in_m = distance_in_m;
        if (owner == null) throw new NullPointerException("Owner can not be null!");
        this.owner = owner;
    }

    /**
     * Initializes the track.
     *
     * @param waypoints waypoints of the track
     * @param owner     user who owns the track
     */
    public Track(List<Waypoint> waypoints, User owner) {
        this(waypoints, calculateTime(waypoints), calculateDistance(waypoints), owner);
    }

    /**
     * Initializes an empty track.
     *
     * @param owner user who owns the track
     */
    public Track(User owner) {
        this(new LinkedList<Waypoint>(), owner);
    }

    /**
     * Calculates the distance between all waypoints in the list.
     *
     * @param waypoints list of waypoints
     * @return distance
     */
    public static float calculateDistance(List<Waypoint> waypoints) {
        if (waypoints == null) throw new NullPointerException("Waypoints can not be null!");
        if (waypoints.size() < 2) {
            return 0;
        }
        float distance = 0;
        for (int index = 0; index < waypoints.size() - 1; index++) {
            distance += waypoints.get(index).distanceTo(waypoints.get(index + 1));
        }
        return distance;
    }

    /**
     * Calculates the time of the track.
     *
     * @param waypoints list of waypoints
     * @return time
     */
    public static long calculateTime(List<Waypoint> waypoints) {
        if (waypoints == null) throw new NullPointerException("Waypoints can not be null!");
        if (waypoints.size() > 0) {
            return waypoints.get(0).getTime() - waypoints.get(waypoints.size() - 1).getTime();
        }
        return 0;
    }

    /**
     * Returns the list of waypoints.
     *
     * @return waypoints
     */
    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    /**
     * Appends a waypoint at the end of the track.
     *
     * @param waypoint waypoint to add
     */
    public void addWaypoint(Waypoint waypoint) {
        this.waypoints.add(waypoint);
        if (waypoints.size() > 1) {
            time_in_s = calculateTime(this.waypoints);
            distance_in_m += waypoint.distanceTo(waypoints.get(waypoints.size() - 2));
        }
    }

    /**
     * Removes a waypoint.
     *
     * @param waypoint waypoint to remove
     */
    public void removeWaypoint(Waypoint waypoint) {
        this.waypoints.remove(waypoint);

        time_in_s = calculateTime(this.waypoints);
        distance_in_m = calculateDistance(waypoints);
    }

    /**
     * Returns the time in second of the track.
     *
     * @return time
     */
    public long getTime_in_s() {
        return time_in_s;
    }

    /**
     * Returns the average speed in km/h.
     *
     * @return average speed
     */
    public float getAveragespeed_in_kmh() {
        return distance_in_m * 1000 / (time_in_s / 3600.0f);
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
