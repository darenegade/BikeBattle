package edu.hm.cs.bikebattle.app.tracker;

import edu.hm.cs.bikebattle.app.modell.Track;

/**
 * Created by Nils on 16.04.2016.
 */
public interface LocationTracker {

  boolean start();

  boolean restart();

  void stop();

  boolean isReady();

  boolean isCrashed();

  void shutdown();

  Track getTrack();
}
