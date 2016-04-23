package edu.hm.cs.bikebattle.app.router;

import android.location.Location;

import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.tracker.LocationTracker;

/**
 * Created by Nils on 19.04.2016.
 * <p/>
 * Interface for a router. The router tracks the ride and navigates over a given route.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public interface Router extends LocationTracker {
  /**
   * Checks if the user is currently in the start area.
   *
   * @return true if in start area.
   */
  boolean isInStartArea();

  /**
   * Returns the route.
   *
   * @return route
   */
  Route getRoute();

  /**
   * Returns the next target or null if the route is finished.
   *
   * @return the next target
   */
  Location getNextTarget();

  /**
   * Returns true if all targets are reached.
   *
   * @return true if finished
   */
  boolean isFinished();

  /**
   * Stops routing. Tracking is continued.
   */
  void stopRouting();

  /**
   * Starts tracking, but only if the user is within the start area.
   *
   * @return true if tracking is started
   */
  boolean startTracking();

  /**
   * Returns the result track.
   *
   * @return result
   */
  Track getResult();
}
