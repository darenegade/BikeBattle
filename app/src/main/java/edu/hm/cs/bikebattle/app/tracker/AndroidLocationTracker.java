package edu.hm.cs.bikebattle.app.tracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
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
 */
public class AndroidLocationTracker implements LocationTracker, LocationListener, GpsStatus.Listener {

  private Track track = new Track();

  private final LocationManager locationManager;

  private Activity activity;

  private final long frequency;

  private boolean ready = false;


  public AndroidLocationTracker(long frequency, Activity activity) {
    this.activity = activity;
    this.frequency = frequency;
    locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

    locationManager.addGpsStatusListener(this);

  }

  @Override
  public boolean continueTracking() {

    Log.d("Tracker", "started");

    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, frequency, 0, this, Looper.getMainLooper());

    return true;
  }

  private AndroidLocationTracker getThis() {
    return this;
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
    return ready;
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

    String msg = "";
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
    }
    Log.d("Tracker location", msg);
  }

  @Override
  public void onProviderEnabled(String provider) {
    Log.d("Tracker on", provider);
    Toast.makeText(activity, "Gps is turned on!! ",
        Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onProviderDisabled(String provider) {
    Log.d("Tracker off", provider);
    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    activity.startActivity(intent);
    Toast.makeText(activity, "Gps is turned off!! ",
        Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onGpsStatusChanged(int event) {
    String msg = "";
    switch (event) {
      case GpsStatus.GPS_EVENT_FIRST_FIX:
        ready = true;
        synchronized (this) {
          notifyAll();
        }
        msg = "first fix";
        break;
      case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
        msg = "satellite status";
        break;
      case GpsStatus.GPS_EVENT_STARTED:
        msg = "gps started";
        break;
      case GpsStatus.GPS_EVENT_STOPPED:
        msg = "gps stoped";
        break;
    }
    Log.d("Tracker gps status", msg);
  }
}
