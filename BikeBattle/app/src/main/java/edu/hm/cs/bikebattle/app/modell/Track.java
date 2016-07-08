package edu.hm.cs.bikebattle.app.modell;

import android.location.Location;

import java.util.List;

import lombok.ToString;

/**
 * Created by Nils on 30.03.2016.
 * <p/>
 * Class representing a track.
 * A track has a specific time.
 * @author Nils Bernhardt
 * @version 1.0
 */
@ToString(callSuper = true)
public class Track extends LocationList {


  /**
   * Initializes the track.
   * @param locations locations of the track
   */
  public Track(List<? extends Location> locations) {
    super(locations);
  }

  /**
   * Initializes an empty track.
   */
  public Track() {
    super();
  }

  /**
   * Calculates the time of the track.
   * @param track track
   * @return time
   */
  public static long calculateTime(Track track) {
    if (track == null) {
      throw new NullPointerException("Locations can not be null!");
    }
    if (track.size() > 1) {
      return (track.get(track.size() - 1).getTime() - track.get(0).getTime())/1000;
    }
    return 0;
  }


  /**
   * Returns the time in second of the track.
   * @return time
   */
  public long getTime_in_s() {
    return calculateTime(this);
  }

  /**
   * Returns the average speed in km/h.
   * @return average speed
   */
  public float getAverageSpeed_in_kmh() {
    if (getTime_in_s() == 0) {
      return 0;
    }
    return getDistanceInM() / 1000 / (getTime_in_s() / 3600.0f);
  }
}
