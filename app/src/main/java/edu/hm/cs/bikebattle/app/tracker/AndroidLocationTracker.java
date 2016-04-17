package edu.hm.cs.bikebattle.app.tracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import edu.hm.cs.bikebattle.app.modell.Track;

/**
 * Created by Nils on 16.04.2016.
 * <p/>
 * Location tracker which uses the Android library to provide gps locations.
 * Currently implementation with the highest frequency.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public class AndroidLocationTracker implements LocationTracker, LocationListener {
  /**
   * Track.
   */
  private final Track track = new Track();
  /**
   * LocationManager for providing locations.
   */
  private final LocationManager locationManager;
  /**
   * Activity of this tracker.
   */
  private final Activity activity;
  /**
   * Update frequency.
   */
  private final long frequency;

  /**
   * Initializes the tracker.
   *
   * @param frequency update frequency in milliseconds
   * @param activity  activity of the tracker
   */
  public AndroidLocationTracker(long frequency, Activity activity) {
    this.activity = activity;
    this.frequency = frequency;
    locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
  }

  @Override
  public boolean continueTracking() {
    if (isReady()) {
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, frequency, 0, this,
          Looper.getMainLooper());
      return true;
    }
    return false;
  }

  @Override
  public boolean start() {
    synchronized (track) {
      track.clear();
    }
    return continueTracking();
  }

  @Override
  public void stop() {
    locationManager.removeUpdates(this);
  }

  @Override
  public boolean isReady() {
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }

  @Override
  public void shutdown() {
    stop();
  }

  @Override
  public Location getLastLocation() {
    return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
  }

  @Override
  public Track getTrack() {
    return track;
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
    //not used so far.
    String msg;
    switch (status) {
      case LocationProvider.AVAILABLE:
        msg = "Available";
        break;
      case LocationProvider.OUT_OF_SERVICE:
        msg = "out of service";
        break;
      case LocationProvider.TEMPORARILY_UNAVAILABLE:
        msg = "temporarily unavailable";
        break;
      default:
        msg = "unknown status";
        break;
    }
    Log.d("Tracker status", msg);
  }

  @Override
  public void onProviderEnabled(String provider) {
    synchronized (this) {
      notifyAll();
    }
  }

  @Override
  public void onProviderDisabled(String provider) {
    Toast.makeText(activity, "Turn GPS on, please! ",
        Toast.LENGTH_SHORT).show();
    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    activity.startActivity(intent);
    synchronized (this) {
      notifyAll();
    }
  }
}
