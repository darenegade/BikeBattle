package edu.hm.cs.bikebattle.app.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.modell.LocationList;
import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.router.AndroidLocationRouter;
import edu.hm.cs.bikebattle.app.router.Router;
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
   * Route for routing.
   */
  private Route route;
  /**
   * Tracker for location updates.
   */
  private LocationTracker tracker;
  /**
   * Tracker for location updates.
   */
  private Router router;
  /**
   * Flag whether tracking is turned on.
   */
  private boolean isTracking = false;
  /**
   * Flag whether routing is active.
   */
  private boolean routing;
  /**
   * Id for the route for routing.
   */
  private String routesOid;
  /**
   * The google map which shows the recorded track and the users position.
   */
  private GoogleMap googleMap;
  /**
   * Last location which represents the users position.
   */
  private Location lastLocation;
  /**
   * Controller for the views.
   */
  private TrackingViewController viewController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tracking);

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    if (mapFragment == null) {
      mapFragment = SupportMapFragment.newInstance();
      getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
    }
    mapFragment.getMapAsync(this);

    // Check if routing is enabled.
    Bundle args = getIntent().getExtras();
    try {
      routesOid = args.getString("oid");
    } catch (NullPointerException exception) {
      routesOid = null;
    }
    routing = routesOid != null;
    if (routing) {
      loadRoute();
    }

    // Initialize LocationTracker.
    if (!routing) {
      tracker = new AndroidLocationTracker(1, this);
      //TODO: Auto update to current location.
      lastLocation = tracker.getLastLocation();
    } else {
      router = new AndroidLocationRouter(route, 1, this);
      //TODO: Auto update to current location.
      lastLocation = router.getLastLocation();
    }

    viewController = new TrackingViewController(this);
  }

  /**
   * Loads the route for routing.
   */
  private void loadRoute() {
    final Context context = this;
    getDataConnector().getRouteById(routesOid, new Consumer<Route>() {
      @Override
      public void consume(Route input) {
        route = input;
        drawLocationList(route, Color.RED);
      }

      @Override
      public void error(int error, Throwable exception) {
        //TODO
        Toast.makeText(context, "Error while loading route!", Toast.LENGTH_LONG).show();
      }
    });
  }

  /**
   * Change tracking mode to on or off.
   *
   * @return Is tracking currently turned on.
   */
  public boolean changeTrackingMode() {
    if (isTracking) {
      tracker.stop();
      saveTrack();

      DialogFragment dialog = new CreateRouteDialog();
      dialog.show(getSupportFragmentManager(), "Create new route?");

      isTracking = false;
    } else {
      if (routing && router.isInStartArea()) {
        if (router.start()) {
          startRouting();
          isTracking = true;
        }
      } else if (tracker.start()) {
        startTracking();
        isTracking = true;
      } else {
        Toast.makeText(this, "Please go to start point!", Toast.LENGTH_LONG).show();
      }
    }
    return isTracking;
  }

  /**
   * Saves the recorded track to the backend.
   */

  private void saveTrack() {
    final Context context = this;
    getDataConnector().addTrack(track, getPrincipal(), new Consumer<Void>() {
      @Override
      public void consume(Void input) {
        Toast.makeText(context, "Added new route!", Toast.LENGTH_LONG).show();
      }

      @Override
      public void error(int error, Throwable exception) {
        Toast.makeText(context, "Unable to save track!", Toast.LENGTH_LONG).show();
        //TODO
      }
    });
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
   * Starts a thread for receiving location updates.
   */
  private void startRouting() {
    final Activity context = this;
    new Thread() {
      @Override
      public void run() {
        while (true) {
          try {
            while (locationUpdates == router.getTrack().size()) {
              synchronized (router) {
                router.wait();
              }
            }
            synchronized (router.getTrack()) {
              while (locationUpdates < router.getTrack().size()) {
                context.runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                    updateTrack(router.getTrack(), router.getLastLocation());
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
    drawLocationList(track, Color.BLUE);
    this.lastLocation = lastLocation;
    updateCamera();
    viewController.updateViews(track);
  }

  /**
   * Displays a track in the map.
   *
   * @param track Track that should be displayed.
   */
  private void drawLocationList(LocationList track, int color) {
    googleMap.clear();

    PolylineOptions polyRoute = new PolylineOptions();

    polyRoute.color(color);
    polyRoute.width(6);
    polyRoute.visible(true);

    for (Location wayPoint : track) {
      polyRoute.add(new LatLng(wayPoint.getLatitude(), wayPoint.getLongitude()));
    }
    googleMap.clear();
    googleMap.addPolyline(polyRoute);
  }

  /**
   * Adds a new route to the backend
   *
   * @param name The name of the route.
   */
  public void addRoute(final String name) {
    final Context context = this;
    Route route = new Route(name, track);
    getDataConnector().addRoute(route, getPrincipal(), new Consumer<Void>() {
      @Override
      public void consume(Void input) {
        Toast.makeText(context, "Added new route!", Toast.LENGTH_LONG).show();
      }

      @Override
      public void error(int error, Throwable exception) {
        Toast.makeText(context, "Unable to add new route!", Toast.LENGTH_LONG).show();
        //TODO
      }
    });
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

    LatLng lastPosition = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
    if (routing) {
      googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
          new CameraPosition.Builder().target(lastPosition).zoom(17).tilt(30)
              .bearing(lastLocation.bearingTo(router.getNextTarget())).build()));
      googleMap.addMarker(new MarkerOptions()
          .position(new LatLng(router.getNextTarget().getLatitude(), router.getNextTarget()
              .getLongitude())))
          .setFlat(true);
    } else {
      googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
          new CameraPosition.Builder().target(lastPosition).zoom(17).tilt(30)
              .bearing(lastLocation.getBearing()).build()));
    }
    googleMap.addMarker(new MarkerOptions()
        .position(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_navigation)))
        .setFlat(true);
  }
}