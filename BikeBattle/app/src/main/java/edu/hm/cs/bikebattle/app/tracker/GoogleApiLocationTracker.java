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
import edu.hm.cs.bikebattle.app.modell.User;

/**
 * Created by Nils B. on 12.04.2016.
 * <p>
 * Location tracker using the GoogleAPI Client. Has usually no higher frequency than 5s.
 * Combines network and gps.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public class GoogleApiLocationTracker implements LocationTracker, LocationListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
  /** Current track.*/
  private final Track track;
  /**Flag if tracker is ready.*/
  private boolean ready = false;
  /**Flag if client has an unresolvable error.*/
  private boolean crashed = false;
  /**Location request for API.*/
  private final LocationRequest locationRequest;
  /**Client for the Google API.*/
  private final GoogleApiClient googleApiClient;

  /**
   * Initializes the tracker and connects to the google API.
   * @param interval - Update frequency.
   * @param context - Context of the activity.
   */
  public GoogleApiLocationTracker(long interval, Context context) {
    track = new Track();
    locationRequest = createLocationRequest(interval);
    googleApiClient = getGoogleApiClient(context);
    googleApiClient.connect();
  }

  @Override
  public boolean isReady() {
    return ready;
  }

  /**
   * Returns true if the tracker is unresolvable crashed.
   * @return true if crashed
   */
  public boolean isCrashed() {
    return crashed;
  }

  @Override
  public void shutdown() {
    googleApiClient.disconnect();
  }

  @Override
  public Location getLastLocation() {
    return LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
  }

  /**
   * Builds a GoogleAPI Client for the location services.
   * @param context - context of the activity.
   * @return Google API client
   */
  private GoogleApiClient getGoogleApiClient(Context context) {
    return new GoogleApiClient.Builder(context)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
  }

  /**
   * Creates a location request.
   * @param interval - update frequency.
   * @return location request
   */
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
  public void onConnectionSuspended(int index) {
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
