package edu.hm.cs.bikebattle.app.tracker;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import edu.hm.cs.bikebattle.app.modell.Track;

/**
 * Created by Nils on 12.04.2016.
 */
public class GoogleAPILocationTracker implements LocationTracker, LocationListener, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

  private Track track = new Track();

  private boolean ready = false;

  private boolean crashed = false;

  private boolean running = false;

  private LocationRequest locationRequest;

  private GoogleApiClient googleApiClient;

  public GoogleAPILocationTracker(Context context, long interval) {
    locationRequest = createLocationRequest(interval);
    googleApiClient = getGoogleApiClient(context);
    googleApiClient.connect();
  }

  @Override
  public boolean isReady() {
    return ready;
  }

  @Override
  public boolean isCrashed() {
    return crashed;
  }

  @Override
  public void shutdown() {
    googleApiClient.disconnect();
  }

  private GoogleApiClient getGoogleApiClient(Context context) {
    return new GoogleApiClient.Builder(context)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
  }

  private LocationRequest createLocationRequest(long interval) {
    LocationRequest locationRequest = new LocationRequest();
    locationRequest.setInterval(interval);
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    return locationRequest;
  }

  @Override
  public boolean continueTracking() {
    if (ready) {
      LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,
          this, Looper.getMainLooper());
    }
    return ready;
  }

  @Override
  public boolean start() {
    if (ready) {
      track.clear();
      return continueTracking();
    }
    return false;
  }

  @Override
  public void stop() {
    LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
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
  public void onConnected(Bundle bundle) {
    ready = true;
    synchronized (this) {
      notifyAll();
    }
  }

  @Override
  public void onConnectionSuspended(int i) {
    //service temporarily unavailable
    ready = false;
    stop();
    synchronized (this) {
      notifyAll();
    }
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    // An unresolvable error has occurred and a connection to Google APIs
    // could not be established. should not happen --> shutdown track if necessary
    ready = false;
    crashed = true;
    synchronized (this) {
      notifyAll();
    }
  }
}
