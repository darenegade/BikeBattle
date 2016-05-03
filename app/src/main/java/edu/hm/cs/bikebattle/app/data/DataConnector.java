package edu.hm.cs.bikebattle.app.data;

import android.location.Location;

import java.util.List;

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
  List<User> getUserByName(String name);

  /**
   * Returns all tracks of the given user.
   *
   * @param user User
   * @return tracks of the user
   */
  List<Track> getTracksByUser(User user);

  /**
   * Returns all routes of the given user.
   *
   * @param user User
   * @return routes of the user
   */
  List<Route> getRoutesByUser(User user);

  /**
   * Adds a track to the users database.
   *
   * @param track new Track
   */
  void addTrack(Track track);

  /**
   * Deletes a track of the user.
   *
   * @param track to delete
   */
  void deleteTrack(Track track);

  /**
   * Adds a route to the users database.
   *
   * @param route new Route
   */
  void addRoute(Route route);

  /**
   * Deletes a route of the user.
   *
   * @param route to delete
   */
  void deleteRoute(Route route);

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

  /**
   * Adds a friend to the users friend list.
   *
   * @param user   user
   * @param friend friend to add
   */
  void addFriend(User user, User friend);

  /**
   * Returns all friends of the user.
   *
   * @param user user
   * @return list of friends.
   */
  List<User> getFriends(User user);
}
