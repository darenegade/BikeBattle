package edu.hm.cs.bikebattle.app.router;

import android.app.Activity;
import android.location.Location;

import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.tracker.AndroidLocationTracker;

/**
 * Created by Nils on 19.04.2016.
 */
public class AndroidLocationRouter extends AndroidLocationTracker implements Router {
  /**
   * Route for navigation.
   */
  private final Route route;

  private int target = 0;

  public final static double TOLERANCE_IN_METER = 40;

  private boolean routing = false;

  private boolean tracking = false;

  private Track result = null;

  public AndroidLocationRouter(Route route, long frequency, Activity activity) {
    super(frequency, activity);
    this.route = route;
  }

  @Override
  public boolean isInStartArea() {
    return getLastLocation().distanceTo(route.get(0)) < TOLERANCE_IN_METER;
  }

  @Override
  public Route getRoute() {
    return route;
  }

  @Override
  public void onLocationChanged(Location location) {
    if (tracking) {
      if (routing) {
        checkTargets(location);
      }
      super.onLocationChanged(location);
    } else if (!routing) {
      if (isInStartArea()) {
        synchronized (this) {
          notifyAll();
        }
      }
    }

  }

  private void checkTargets(Location location) {
    while (location.distanceTo(route.get(target)) < TOLERANCE_IN_METER) {
      target++;
      if (target >= route.size()) {
        //finished
        routing = false;
        tracking = false;
        super.stop();
        result = new Track(getTrack());
        break;
      }
    }
  }

  @Override
  public Location getNextTarget() {
    if (target >= route.size()) {
      return null;
    }
    return route.get(target);
  }

  @Override
  public boolean continueTracking() {
    if (!routing) {
      return super.continueTracking();
    } else {
      return false;
    }
  }

  @Override
  public void stop() {
    if (!routing) {
      //only possible if routing is deactivated
      super.stop();
    }
  }

  @Override
  public boolean isFinished() {
    return target >= route.size();
  }

  @Override
  public boolean start() {
    if (!routing) {
      if (super.start()) {
        routing = true;
        return true;
      }
    }
    return false;
  }

  @Override
  public void stopRouting() {
    routing = false;
  }

  @Override
  public boolean startTracking() {
    if (isInStartArea()) {
      return false;
    }
    tracking = true;
    return true;
  }

  @Override
  public Track getResult() {
    return result;
  }
}
