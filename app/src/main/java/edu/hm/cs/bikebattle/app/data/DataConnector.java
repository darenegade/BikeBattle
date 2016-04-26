package edu.hm.cs.bikebattle.app.data;

import android.location.Location;

import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.modell.User;

/**
 * Created by Nils on 26.04.2016.
 * <p/>
 * Interface for the connection to the data local and on the server.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public interface DataConnector {
  /**
   * Returns all Route which are within the distance to the given location.
   *
   * @param location Location
   * @param distance maximum distance
   * @return all Routes
   */
  Route[] getRoutesByLocation(Location location, float distance);

  /**
   * Returns the user with the given id.
   *
   * @param id user id
   * @return user
   */
  User getUserById(int id);

  /**
   * Returns the user with the given name.
   *
   * @param name user name
   * @return user
   */
  User getUserByName(String name);

  /**
   * Returns all tracks of the given user.
   *
   * @param user User
   * @return tracks of the user
   */
  Track[] getTracksByUser(User user);

  /**
   * Returns all routes of the given user.
   *
   * @param user User
   * @return routes of the user
   */
  Route[] getRoutesByUser(User user);

  /**
   * Adds a track to the users database.
   *
   * @param track new Track
   * @param user  owner
   */
  void addTrack(Track track, User user);

  /**
   * Deletes a track of the user.
   *
   * @param track to delete
   * @param user  owner
   */
  void deleteTrack(Track track, User user);

  /**
   * Adds a route to the users database.
   *
   * @param route new Route
   * @param user  owner
   */
  void addRoute(Route route, User user);

  /**
   * Deletes a route of the user.
   *
   * @param route to delete
   * @param user  owner
   */
  void deleteRoute(Route route, User user);

  /**
   * Creates a new user.
   *
   * @param user user
   */
  void createUser(User user);

  /**
   * Updates the user data.
   *
   * @param user user
   */
  void changeUserData(User user);

}
