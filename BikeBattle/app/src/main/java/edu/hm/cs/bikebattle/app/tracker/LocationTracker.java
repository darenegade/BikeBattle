package edu.hm.cs.bikebattle.app.tracker;

import android.location.Location;

import edu.hm.cs.bikebattle.app.modell.Track;

/**
 * Created by Nils on 16.04.2016.
 * <p/>
 * Interface for Location tracker.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public interface LocationTracker {
  /**
   * Continues tracking. This means previously stored locations are not deleted.
   * Returns false if tracking is not possible. (e.G. GPS is turned off)
   *
   * @return true if tracking is possible
   */
  boolean continueTracking();

  /**
   * Starts tracking with a new track.
   * Returns false if tracking is not possible. (e.G. GPS is turned off)
   *
   * @return true if tracking is possible
   */
  boolean start();

  /**
   * Stops tracking.
   */
  void stop();

  /**
   * Returns true if the tracker is ready. (GPS is turned on)
   *
   * @return true if tracker is ready
   */
  boolean isReady();

  /**
   * Shutdowns system. Not always necessary.
   */
  void shutdown();

  /**
   * Returns the most recent location.
   *
   * @return last location
   */
  Location getLastLocation();

  /**
   * Returns the current track.
   *
   * @return track
   */
  Track getTrack();
}
