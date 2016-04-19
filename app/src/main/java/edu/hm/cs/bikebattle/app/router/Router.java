package edu.hm.cs.bikebattle.app.router;

import android.location.Location;

import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.tracker.LocationTracker;

/**
 * Created by Nils on 19.04.2016.
 */
public interface Router extends LocationTracker{
  boolean isInStartArea();

  Route getRoute();

  Location getNextTarget();

  boolean isFinished();

  void stopRouting();

  boolean startTracking();

  Track getResult();
}
