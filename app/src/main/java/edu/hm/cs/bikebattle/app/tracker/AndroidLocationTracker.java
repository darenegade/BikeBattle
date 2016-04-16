package edu.hm.cs.bikebattle.app.tracker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import edu.hm.cs.bikebattle.app.modell.Track;

/**
 * Created by Nils on 16.04.2016.
 */
public class AndroidLocationTracker implements LocationTracker, LocationListener {

  private Track track;

  @Override
  public boolean start() {
    return false;
  }

  @Override
  public boolean restart() {
    return false;
  }

  @Override
  public void stop() {

  }

  @Override
  public boolean isReady() {
    return false;
  }

  @Override
  public boolean isCrashed() {
    return false;
  }

  @Override
  public void shutdown() {

  }

  @Override
  public Track getTrack() {
    return null;
  }

  @Override
  public void onLocationChanged(Location location) {
    Log.d("Tracker", location.toString());
    synchronized (track) {
      track.add(location);
      synchronized (this) {
        notifyAll();
      }
    }
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {

  }

  @Override
  public void onProviderEnabled(String provider) {

  }

  @Override
  public void onProviderDisabled(String provider) {

  }
}
