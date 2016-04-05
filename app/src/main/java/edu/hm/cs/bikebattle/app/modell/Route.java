package edu.hm.cs.bikebattle.app.modell;

import android.location.Location;

import java.util.List;

/**
 * Created by Nils on 30.03.2016.
 * <p/>
 * This class represents a route. Routes are based on tracks.
 * But have additional attributes like a name.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public class Route extends LocationList {
  /**
   * name of the route.
   */
  private String name;
  /**
   * Flag whether this route is public.
   */
  private boolean privateRoute;

  private User owner;

  /**
   * Initializes the route.
   *
   * @param locations    locations of the route
   * @param name         name of the route
   * @param privateRoute is the route private
   * @param owner        user who owns the route
   */
  public Route(List<? extends Location> locations, String name,
               boolean privateRoute, User owner) {
    super(locations);
    setName(name);
    setPrivateRoute(privateRoute);
    setOwner(owner);
  }

  /**
   * Initializes this Route without locations.
   *
   * @param name         name of the route
   * @param privateRoute is the route public
   * @param owner        user who owns the route
   */
  public Route(String name, boolean privateRoute, User owner) {
    super();
    setName(name);
    setPrivateRoute(privateRoute);
    setOwner(owner);
  }

  /**
   * Initializes the route with a public flag.
   *
   * @param name      name of the route
   * @param locations locations of the route
   * @param owner     user who owns the route
   */
  public Route(String name, List<? extends Location> locations, User owner) {
    this(locations, name, false, owner);
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
    if (name == null) {
      throw new NullPointerException("Route name can not be null.");
    }
    if (name.length() <= 0) {
      throw new IllegalArgumentException("Route name must contain at least one character.");
    }
    this.name = name;
  }

  /**
   * Returns true if the route is private.
   *
   * @return is the route private
   */
  public boolean isPrivateRoute() {
    return privateRoute;
  }

  /**
   * Changes whether the route is private.
   *
   * @param privateRoute is the route private
   */
  public void setPrivateRoute(boolean privateRoute) {
    this.privateRoute = privateRoute;
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