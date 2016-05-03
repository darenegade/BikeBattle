package edu.hm.cs.bikebattle.app.modell;

import android.location.Location;

import java.util.List;

/**
 * Created by Nils on 30.03.2016.
 * <p/>
 * Class representing a track for tracking a drive.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public class Track extends LocationList {

  /**
   * Initializes the track without calculating the distance and time.
   *
   * @param locations   locations of the track
   * @param distanceInM distance of the track
   */
  public Track(List<? extends Location> locations, float distanceInM, User owner) {
    super(locations, owner, distanceInM);
  }

  /**
   * Initializes the track.
   *
   * @param locations locations of the track
   */
  public Track(List<? extends Location> locations, User owner) {
    super(locations, owner);
  }

  /**
   * Initializes an empty track.
   */
  public Track(User owner) {
    super(owner);
  }

  /**
   * Calculates the time of the track.
   *
   * @param track track
   * @return time
   */
  public static long calculateTime(Track track) {
    if (track == null) {
      throw new NullPointerException("Locations can not be null!");
    }
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
  public float getAverageSpeed_in_kmh() {
    return getDistanceInM() * 1000 / (getTime_in_s() / 3600.0f);
  }


}
