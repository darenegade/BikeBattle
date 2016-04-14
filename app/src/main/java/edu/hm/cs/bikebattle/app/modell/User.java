package edu.hm.cs.bikebattle.app.modell;

import java.util.List;

/**
 * Created by Nils Bernhardt on 30.03.2016.
 * This class represents a user and stores his values as well as his routes and tracks.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public class User {
  /**
   * Name of the user.
   */
  private String name;
  /**
   * EMail of user.
   */
  private String email;
  /**
   * Weight of the user.
   */
  private float weightInKg;
  /**
   * Size of the user.
   */
  private float sizeInCm;
  /**
   * Others users which are connected to this user.
   */
  private List<User> friends;
  /**
   * Tracks of this user.
   */
  private List<Track> tracks;
  /**
   * Routes of this user.
   */
  private List<Route> routes;

  /**
   * Initializes the user.
   *
   * @param name       name of the user
   * @param weightInKg weight of the user in kg
   * @param sizeInCm   size of the user in cm
   * @param friends    connected users
   * @param tracks     tracks of the user
   * @param routes     routes of the user
   */
  public User(final String name, final float weightInKg, final float sizeInCm,
              final List<User> friends, final List<Track> tracks, final List<Route> routes) {
    setName(name);
    setWeightInKg(weightInKg);
    setSizeInCm(sizeInCm);
    if (friends == null) {
      throw new NullPointerException("Friends can not be null!");
    }
    this.friends = friends;
    if (tracks == null) {
      throw new NullPointerException("Tracks can not be null!");
    }
    this.tracks = tracks;
    if (routes == null) {
      throw new NullPointerException("Routes can not be null!");
    }
    this.routes = routes;
  }

  /**
   * Initializes the user without relations.
   *
   * @param name       name of the user
   * @param email    email of user
   * @param weightInKg weight of the user in kg
   * @param sizeInCm   size of the user in cm
   */
  public User(final String name,final String email, final float weightInKg, final float sizeInCm) {
    setName(name);
    setWeightInKg(weightInKg);
    setSizeInCm(sizeInCm);
    setEmail(email);
  }
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Returns the size of the user.
   *
   * @return size
   */
  public float getSizeInCm() {
    return sizeInCm;
  }

  /**
   * Returns the name of the user.
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the weight of the user.
   *
   * @return weight
   */
  public float getWeightInKg() {
    return weightInKg;
  }

  /**
   * Returns a list with the friends of the user.
   *
   * @return friends
   */
  public List<User> getFriends() {
    return friends;
  }

  /**
   * Returns a list with the tracks of the user.
   *
   * @return tracks
   */
  public List<Track> getTracks() {
    return tracks;
  }

  /**
   * Returns a list with the routes of the user.
   *
   * @return routes
   */
  public List<Route> getRoutes() {
    return routes;
  }

  /**
   * Changes the name of the user.
   *
   * @param name new name
   */
  public void setName(String name) {
    if (name == null) {
      throw new NullPointerException("Name can not be null.");
    }
    if (name.length() <= 0) {
      throw new IllegalArgumentException("Name must contain at least one character.");
    }
    this.name = name;
  }

  /**
   * Changes the weight of the user.
   *
   * @param weightInKg new weight in kg.
   */
  public void setWeightInKg(float weightInKg) {
    this.weightInKg = weightInKg;
  }

  /**
   * Changes the size of the user.
   *
   * @param sizeInCm new size in cm
   */
  public void setSizeInCm(float sizeInCm) {
    this.sizeInCm = sizeInCm;
  }

  /**
   * Adds a new friend to the friend list.
   *
   * @param friend new user friend
   */
  public void addFriend(User friend) {
    this.friends.add(friend);
  }

  /**
   * Removes a friend from the friend list.
   *
   * @param friend user friend to remove
   */
  public void removeFriend(User friend) {
    friends.remove(friend);
  }

  /**
   * Adds a track to the track list.
   *
   * @param track new track
   */
  public void addTrack(Track track) {
    this.tracks.add(track);
  }

  /**
   * Removes a track from the track list.
   *
   * @param track track to remove
   */
  public void removeTrack(Track track) {
    tracks.remove(track);
  }

  /**
   * Adds a new route to the route list.
   *
   * @param route new route
   */
  public void addRoute(Route route) {
    this.routes.add(route);
  }

  /**
   * Removes a route from the route list.
   *
   * @param route route to remove
   */
  public void removeRoute(Route route) {
    routes.remove(route);
  }

}
