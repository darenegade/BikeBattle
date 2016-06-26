package edu.hm.cs.bikebattle.app.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.fragments.GoogleMapHelper;
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
public class TrackingActivity extends BaseActivity implements OnMapReadyCallback, LocationListener {
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
  /**
   * LocationManager for providing locations.
   */
  private LocationManager locationManager;

  /**
   * Change tracking mode to on or off.
   *
   * @return Is tracking currently turned on.
   */
  public boolean changeTrackingMode() {
    if (isTracking) {
      if (routing) {
        router.stop();
      } else {
        tracker.stop();
      }
      saveTrack();

      DialogFragment dialog = new CreateRouteDialog();
      dialog.show(getSupportFragmentManager(), "Create new route?");

      isTracking = false;
    } else {
      if (routing) {
        if (router.isInStartArea()) {
          if (router.start()) {
            startRouting();
            isTracking = true;
          }
        } else {
          Toast.makeText(this, "Please go to start point!", Toast.LENGTH_LONG).show();
        }
      } else {
        if (tracker.start()) {
          startTracking();
          isTracking = true;
        }
      }
    }
    return isTracking;
  }

  @Override
  public void onBackPressed() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      locationManager.removeUpdates(this);
    }
    if (routing) {
      router.stop();
    } else {
      tracker.stop();
    }
    super.onBackPressed();
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
      clearMap();
      updateCamera();
    }
  }

  @Override
  public void onLocationChanged(Location location) {
    if (!isTracking) {
      clearMap();
      LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
      googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
          new CameraPosition.Builder().target(latLng).zoom(17).bearing(location.getBearing())
              .build()));
      GoogleMapHelper.drawPositionIcon(googleMap, new LatLng(location.getLatitude(), location
          .getLongitude()));
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

  /**
   * Loads the route for routing.
   */
  private void loadRoute() {
    LocationList list = new LocationList();
    addRoutePoint(list, 48.1229806, 11.5978708);
    addRoutePoint(list, 48.1228938, 11.5974820);
    addRoutePoint(list, 48.1230681, 11.5977203);
    addRoutePoint(list, 48.1234571, 11.5976424);
    addRoutePoint(list, 48.1237490, 11.5973967);
    addRoutePoint(list, 48.1240122, 11.5969924);
    addRoutePoint(list, 48.1243319, 11.5966800);
    addRoutePoint(list, 48.1246915, 11.5966080);
    addRoutePoint(list, 48.1251432, 11.5960900);
    addRoutePoint(list, 48.1254898, 11.5958659);
    addRoutePoint(list, 48.1258059, 11.5956128);
    addRoutePoint(list, 48.1261858, 11.5954958);
    addRoutePoint(list, 48.1264964, 11.5951870);
    addRoutePoint(list, 48.1268341, 11.5950448);
    addRoutePoint(list, 48.1271520, 11.5947710);
    addRoutePoint(list, 48.1275148, 11.5946829);
    addRoutePoint(list, 48.1278858, 11.5945358);
    addRoutePoint(list, 48.1282661, 11.5944073);
    addRoutePoint(list, 48.1283870, 11.5938616);
    addRoutePoint(list, 48.1286249, 11.5933845);
    addRoutePoint(list, 48.1289625, 11.5934323);
    addRoutePoint(list, 48.1291484, 11.5934056);
    addRoutePoint(list, 48.1292529, 11.5931773);
    route = new Route("TestRoute", list);
    /*
    final Context context = this;
    getDataConnector().getRouteById(routesOid, new Consumer<Route>() {
      @Override
      public void consume(Route input) {
        route = input;
      }

      @Override
      public void error(int error, Throwable exception) {
        //TODO
        Toast.makeText(context, "Error while loading route!", Toast.LENGTH_LONG).show();
      }
    });
    */
  }

  @Deprecated
  private void addRoutePoint(LocationList list, double lat, double lng) {
    Location location = new Location("");
    location.setLatitude(lat);
    location.setLongitude(lng);
    list.add(location);
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

  /**
   * Starts a thread for receiving location updates.
   */
  private void startTracking() {
    final Activity context = this;
    new Thread() {
      @Override
      public void run() {
        while (isTracking) {
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
        while (isTracking) {
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
                    if (!router.isFinished()) {
                      updateTrack(router.getTrack(), router.getLastLocation());
                    } else {
                      changeTrackingMode();
                    }
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
    clearMap();
    GoogleMapHelper.drawLocationList(track, Color.BLUE, googleMap);
    this.lastLocation = lastLocation;
    updateCamera();
    viewController.updateViews(track);
  }

  /**
   * Clears the map and draws the route, if routing is enabled.
   */
  private void clearMap() {
    googleMap.clear();
    if (routing) {
      GoogleMapHelper.drawLocationList(route, Color.RED, googleMap);
    }
  }

  /**
   * Moves the camera to the users position.
   */
  private void updateCamera() {
    if (lastLocation != null) {
      clearMap();
      LatLng lastPosition = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
      if (routing) {
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
            new CameraPosition.Builder().target(lastPosition).zoom(17).tilt(30)
                .bearing(lastLocation.bearingTo(router.getNextTarget())).build()));
        googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(router.getNextTarget().getLatitude(), router.getNextTarget()
                .getLongitude())))
            .setFlat(false);
      } else {
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
            new CameraPosition.Builder().target(lastPosition).zoom(17).tilt(30)
                .bearing(lastLocation.getBearing()).build()));
      }
      GoogleMapHelper.drawPositionIcon(googleMap, lastPosition);
    } else {
      Toast.makeText(this, "No last location!", Toast.LENGTH_LONG).show();
    }
  }

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

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, this,
          Looper.getMainLooper());
      lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
    // Check if routing is enabled.
    Bundle args = getIntent().getExtras();
    /*
    try {
      routesOid = args.getString("oid");
    } catch (NullPointerException exception) {
      routesOid = null;
    }
    routing = routesOid != null;
    */
    routing = true;
    if (routing) {
      loadRoute();
      router = new AndroidLocationRouter(route, 1, this);
    } else {
      tracker = new AndroidLocationTracker(1, this);
    }

    viewController = new TrackingViewController(this);
  }
}