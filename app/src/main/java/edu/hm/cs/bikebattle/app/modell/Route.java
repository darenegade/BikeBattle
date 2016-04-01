package edu.hm.cs.bikebattle.app.modell;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nils on 30.03.2016.
 * <p/>
 * This class represents a route. Routes are based on tracks. But have additional attributes like a name.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public class Route extends Track {
    /**
     * name of the route.
     */
    private String name;
    /**
     * Flag whether this route is public.
     */
    private boolean publicRoute;

    /**
     * Initializes the route.
     *
     * @param waypoints   waypoints of the route
     * @param name        name of the route
     * @param publicRoute is the route public
     * @param owner       user who owns the route
     */
    public Route(List<Waypoint> waypoints, String name, boolean publicRoute, User owner) {
        super(waypoints, owner);
        setName(name);
        this.publicRoute = publicRoute;
    }

    /**
     * Initializes this Route without waypoints.
     *
     * @param name        name of the route
     * @param publicRoute is the route public
     * @param owner       user who owns the route
     */
    public Route(String name, boolean publicRoute, User owner) {
        this(new LinkedList<Waypoint>(), name, publicRoute, owner);
    }

    /**
     * Initializes the route with a public flag.
     *
     * @param name      name of the route
     * @param waypoints waypoints of the route
     * @param owner     user who owns the route
     */
    public Route(String name, List<Waypoint> waypoints, User owner) {
        this(waypoints, name, true, owner);
    }

    /**
     * Initializes the route by using the waypoints of the track.
     *
     * @param name        name of the route
     * @param track       track
     * @param publicRoute is the route public
     * @param owner       user who owns the route
     */
    public Route(String name, Track track, boolean publicRoute, User owner) {
        this(track.getWaypoints(), name, publicRoute, owner);
    }

    /**
     * Initializes the Route by using the waypoints of the track. And the route is set to public.
     *
     * @param name  name of the route
     * @param track track
     * @param owner user who owns the route
     */
    public Route(String name, Track track, User owner) {
        this(name, track, true, owner);
    }

    /**
     * Initializes the Route public and empty.
     *
     * @param name  name of the route
     * @param owner user who owns the route
     */
    public Route(String name, User owner) {
        this(name, true, owner);
    }

    /**
     * Returns the name of the route.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the route.
     *
     * @param name name
     */
    public void setName(String name) {
        if (name == null) throw new NullPointerException("Route name can not be null.");
        if (name.length() <= 0)
            throw new IllegalArgumentException("Route name must contain at least one character.");
        this.name = name;
    }

    /**
     * Returns true if the route is public.
     *
     * @return is the route public
     */
    public boolean isPublicRoute() {
        return publicRoute;
    }

    /**
     * Changes whether the route is public.
     *
     * @param publicRoute is the route public
     */
    public void setPublicRoute(boolean publicRoute) {
        this.publicRoute = publicRoute;
    }
}
