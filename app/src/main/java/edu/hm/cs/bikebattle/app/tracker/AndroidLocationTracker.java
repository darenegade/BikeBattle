package edu.hm.cs.bikebattle.app.tracker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import edu.hm.cs.bikebattle.app.modell.Track;

/**
 * Created by Nils on 16.04.2016.
 */
public class AndroidLocationTracker implements LocationTracker, LocationListener {

  private Track track = new Track();

  private final LocationManager locationManager;

  private Context context;

  private final long frequency;

  public AndroidLocationTracker(long frequency, Context context) {
    this.context = context;
    this.frequency = frequency;
    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
  }

  @Override
  public boolean start() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    Activity#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for Activity#requestPermissions for more details.
        return false;
      }
    }
    Looper.prepare();
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, frequency, 0, this);
    return false;
  }

  @Override
  public boolean restart() {
    synchronized (track){
      track.clear();
    }
    return start();
  }

  @Override
  public void stop() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    Activity#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for Activity#requestPermissions for more details.
        return;
      }
    }
    locationManager.removeUpdates(this);
  }

  @Override
  public boolean isReady() {
    return true;
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

  }

  @Override
  public void onProviderEnabled(String provider) {

  }

  @Override
  public void onProviderDisabled(String provider) {

  }
}
