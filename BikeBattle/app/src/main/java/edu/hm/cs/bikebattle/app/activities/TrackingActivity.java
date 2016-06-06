package edu.hm.cs.bikebattle.app.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.tracker.AndroidLocationTracker;
import edu.hm.cs.bikebattle.app.tracker.LocationTracker;

/**
 * Activity for tracking a new track.
 *
 * @author Lukas Brauckmann
 */
public class TrackingActivity extends BaseActivity implements OnMapReadyCallback {
  /**
   * Counter for received location updates.
   */
  private int locationUpdates = 0;
  /**
   * Track that is recorded so far.
   */
  private Track track;
  /**
   * Tracker for location updates.
   */
  private LocationTracker tracker;
  /**
   * Flag whether tracking is turned on.
   */
  private boolean isTracking = false;
  /**
   * Flag whether routing is active.
   */
  private boolean routing;

  private String routesOid;
  /**
   * The google map which shows the recorded track and the users position.
   */
  private GoogleMap googleMap;
  /**
   * Last location which represents the users position.
   */
  private Location lastLocation;

  private TrackingViewController viewController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tracking);

    Bundle args = getIntent().getExtras();
    try {
      routesOid = args.getString("oid");
    } catch (NullPointerException exception) {
      routesOid = null;
    }
    routing = routesOid != null;
    if(routing){
      loadTrack();
    }

    //tracker = new GoogleApiLocationTracker(this, 0);
    tracker = new AndroidLocationTracker(1, this);
    //TODO: Auto update to current location.
    this.lastLocation=tracker.getLastLocation();

    viewController=new TrackingViewController(this);
  }

  private void loadTrack() {
    //TODO: Load route for routing.
  }

  /**
   * Change tracking mode to on or off.
   *
   * @return Is tracking currently turned on.
   */
  public boolean changeTrackingMode() {
    if (isTracking) {
      tracker.stop();
      //TODO: Backend implementation
      /*
      new Thread() {
        @Override
        public void run() {
          dataConnector.addTrack(track,new User("Lukas",80,190));
          Log.e("Data:","Saved track!");
        }
      }.start();
      */

      DialogFragment dialog = new CreateRouteDialog();
      dialog.show(getSupportFragmentManager(), "Create new route?");

      isTracking = false;
    } else {
      if (tracker.start()) {
        startTracking();
        isTracking = true;
      }
    }
    return isTracking;
  }

  @Override
  public void onBackPressed() {
    tracker.stop();
    super.onBackPressed();
  }

  /**
   * Starts a thread for receiving location updates.
   */
  private void startTracking() {
    final Activity context = this;
    new Thread() {
      @Override
      public void run() {
        while (true) {
          try {
            while (locationUpdates == tracker.getTrack().size()) {
              synchronized (tracker) {
                tracker.wait();
              }
            }
            synchronized (tracker.getTrack()) {
              while (locationUpdates < tracker.getTrack().size()) {
                context.runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                    updateTrack(tracker.getTrack(), tracker.getLastLocation());
                  }
                });
                locationUpdates++;
              }
            }
          } catch (InterruptedException exception) {
            exception.printStackTrace();
          }

        }
      }
    }.start();
  }

  /**
   * Updates the track and updates the fragments.
   *
   * @param track        Updated track.
   * @param lastLocation Last location.
   */
  private void updateTrack(Track track, Location lastLocation) {
    this.track = track;
    drawTrack(track);
    this.lastLocation=lastLocation;
    updateCamera();
    viewController.updateViews(track);
  }

  /**
   * Displays a track in the map.
   *
   * @param track Track that should be displayed.
   */
  private void drawTrack(Track track) {
    googleMap.clear();

    PolylineOptions polyRoute = new PolylineOptions();

    polyRoute.color(Color.BLUE);
    polyRoute.width(6);
    polyRoute.visible(true);

    for (Location wayPoint : track) {
      polyRoute.add(new LatLng(wayPoint.getLatitude(), wayPoint.getLongitude()));
    }
    polyRoute.add(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
    googleMap.clear();
    googleMap.addPolyline(polyRoute);
  }

  public void addRoute(final String name) {
    //TODO: Backend implementation
    /*
    new Thread() {
      @Override
      public void run() {
        dataConnector.addRoute(new Route(name,track),new User("Lukas",80,190));
        Log.e("Data:","Saved route!");
      }
    }.start();
    */
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      this.googleMap = googleMap;
      this.googleMap.getUiSettings().setMapToolbarEnabled(false);
      this.googleMap.getUiSettings().setCompassEnabled(true);
      updateCamera();
    }
  }

  /**
   * Moves the camera to the users position.
   */
  private void updateCamera() {
    LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
        new CameraPosition.Builder().target(latLng).zoom(17).tilt(30)
            .bearing(lastLocation.getBearing()).build()));
    googleMap.addMarker(new MarkerOptions()
        .position(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_navigation)))
        .setFlat(true);
  }
}