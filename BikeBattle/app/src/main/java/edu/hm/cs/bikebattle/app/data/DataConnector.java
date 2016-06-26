package edu.hm.cs.bikebattle.app.data;

import android.location.Location;

import java.util.List;

import edu.hm.cs.bikebattle.app.api.domain.TopDriveEntryDto;
import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.modell.User;

import java.util.List;

/**
 * Created by Nils on 26.04.2016.
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
   * @param consumer consumer to call
   */
  void getRoutesByLocation(Location location, float distance, Consumer<List<Route>> consumer);

  /**
   * Returns all Route.
   *
   * @param consumer consumer to call
   */
  void getAllRoutes(Consumer<List<Route>> consumer);

  /**
   * Returns the user with the given id.
   *
   * @param id       user id
   * @param consumer consumer to call
   */
  void getUserById(String id, Consumer<User> consumer);

  /**
   * Login on Backend and returns the current user.
   *
   * @param email    user email
   * @param consumer consumer to call
   */
  void login(String email, Consumer<User> consumer);

  /**
   * Returns the user with the given name.
   *
   * @param name     user name
   * @param consumer consumer to call
   */
  void getUserByName(String name, Consumer<List<User>> consumer);

  /**
   * Returns all tracks of the given user.
   *
   * @param user     User
   * @param consumer consumer to call
   */
  void getTracksByUser(User user, Consumer<List<Track>> consumer);

  /**
   * Returns a Top 20 list of a route.
   *
   * @param route route
   * @param consumer consumer to call
   */
  void getTopTwentyOfRoute(final Route route, final Consumer<List<TopDriveEntryDto>> consumer);

  /**
   * Returns all routes of the given user.
   *
   * @param user     User
   * @param consumer consumer to call
   */
  void getRoutesByUser(User user, Consumer<List<Route>> consumer);

  /**
   * Adds a track to the users database.
   *
   * @param track    new Track
   * @param consumer consumer to call
   * @param owner    owner of the track
   */
  void addTrack(Track track, User owner, Consumer<String> consumer);

  /**
   * Adds a track to the users database. The track is mapped to the route.
   *
   * @param track    new Track
   * @param consumer consumer to call
   * @param owner    owner of the track
   */
  void addTrack(Track track, Route route, User owner, Consumer<Void> consumer);

  /**
   * Sets a route for a specific track.
   *
   * @param track    track
   * @param route    route
   * @param consumer consumer
   */
  void setRouteForTrack(Track track, Route route, Consumer<Void> consumer);

  /**
   * Deletes a track of the user.
   *
   * @param track    to delete
   * @param consumer consumer to call
   */
  void deleteTrack(Track track, Consumer<Void> consumer);

  /**
   * Adds a route to the users database.
   *
   * @param route    new Route
   * @param owner    owner
   * @param consumer consumer to call
   */
  void addRoute(Route route, User owner, Consumer<String> consumer);

  /**
   * Deletes a route of the user.
   *
   * @param route    to delete
   * @param consumer consumer to call
   */
  void deleteRoute(Route route, Consumer<Void> consumer);

  /**
   * Creates a new user.
   *
   * @param user     user
   * @param consumer consumer to call
   */
  void createUser(User user, Consumer<String> consumer);

  /**
   * Updates the user data.
   *
   * @param user     user
   * @param consumer consumer to call
   */
  void changeUserData(User user, Consumer<Void> consumer);

  /**
   * Adds a friend to the users friend list.
   *
   * @param user     user
   * @param friend   friend to add
   * @param consumer consumer to call
   */
  void addFriend(User user, User friend, Consumer<Void> consumer);

  /**
   * Returns all friends of the user.
   *
   * @param user     user
   * @param consumer consumer to call
   */
  void getFriends(User user, Consumer<List<User>> consumer);

  /**
   * Returns all Tracks according to the route to the consumer.
   *
   * @param route    route
   * @param consumer consumer
   */
  public void getTracksByRoute(final Route route, final Consumer<List<Track>> consumer);
}
