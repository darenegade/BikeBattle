package edu.hm.cs.bikebattle.app.tracker;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import edu.hm.cs.bikebattle.app.modell.Track;

/**
 * Created by Nils on 12.04.2016.
 */
public class LocationTracker implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

  private Track track = new Track(null);

  private boolean ready = false;

  private boolean running = false;

  private LocationRequest locationRequest;

  private GoogleApiClient googleApiClient;

  public LocationTracker(Context context, long interval, long fastestInterval) {
    locationRequest = createLocationRequest(interval, fastestInterval);
    googleApiClient = getGoogleApiClient(context);
    googleApiClient.connect();
  }

  public boolean isReady() {
    return ready;
  }

  public void shutdown() {
    //TODO
    googleApiClient.disconnect();
  }

  private GoogleApiClient getGoogleApiClient(Context context) {
    return new GoogleApiClient.Builder(context)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
  }

  private LocationRequest createLocationRequest(long interval, long fastestInterval) {
    LocationRequest locationRequest = new LocationRequest();
    locationRequest.setInterval(interval);
    locationRequest.setFastestInterval(fastestInterval);
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    return locationRequest;
  }

  public boolean start() {
    if (ready) {
      Looper.prepare();
      LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }
    return ready;
  }

  public

  public void stop() {
    LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
  }

  public Track getTrack() {
    return track;
  }

  @Override
  public void onLocationChanged(Location location) {
    synchronized (track) {
      track.add(location);
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

  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {

  }
}
