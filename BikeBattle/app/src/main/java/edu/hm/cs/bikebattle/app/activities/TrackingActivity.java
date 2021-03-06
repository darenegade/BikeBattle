package edu.hm.cs.bikebattle.app.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.api.domain.Difficulty;
import edu.hm.cs.bikebattle.app.api.domain.Routetyp;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.fragments.GoogleMapHelper;
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
   * String for intent argument.
   */
  public static final String OID = "oid";
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
   * Changes tracking mode to on or off.
   *
   * @return Is tracking currently turned on.
   */
  public boolean changeTrackingMode() {
    if (isTracking) {
      isTracking = false;
      if (routing) {
        router.stop();
        saveRouting();
      } else {
        tracker.stop();
        saveTracking();
      }
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
   * Initializes the GoogleMap, when the object is ready.
   *
   * @param googleMap GoogleMap object
   */
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

  /**
   * Called when the LocationManager received a new location.
   *
   * @param location New location
   */
  @Override
  public void onLocationChanged(Location location) {
    if (!isTracking) {
      clearMap();
      LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
      if (location.getAccuracy() > 10) {
        // Zoom into the google map.
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
            new CameraPosition.Builder().target(latLng).zoom(17).bearing(location.getBearing())
                .build()));
      }
      GoogleMapHelper.drawPositionIcon(googleMap, new LatLng(location.getLatitude(), location
          .getLongitude()), location.getBearing());
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
   * Creates a dialog for adding a new route.
   */
  private void showRouteDialog() {
    final Dialog dialog = new Dialog(this);
    dialog.setContentView(R.layout.dialog_create_route);
    dialog.setTitle("Add new route?");

    final EditText nameTextField = (EditText) dialog.findViewById(R.id.editText);

    final RadioGroup radioType = (RadioGroup) dialog.findViewById(R.id.radioType);
    radioType.check(R.id.radioButton_type_off);
    final RadioGroup radioDiff = (RadioGroup) dialog.findViewById(R.id.radioDiff);
    radioDiff.check(R.id.radioButton_diff_easy);

    Button saveButton = (Button) dialog.findViewById(R.id.button_ok);
    Button cancelButton = (Button) dialog.findViewById(R.id.button_cancel);

    // Set button listener.
    final Context context = this;
    saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String name = String.valueOf(nameTextField.getText());
        if (name.equals("")) {
          Toast.makeText(context, "Empty name!", Toast.LENGTH_LONG).show();
        } else {
          RadioButton selectedType = (RadioButton) dialog.findViewById(radioType
              .getCheckedRadioButtonId());
          RadioButton selectedDiff = (RadioButton) dialog.findViewById(radioDiff
              .getCheckedRadioButtonId());
          addRoute(name, selectedType.getText(), selectedDiff.getText());
          dialog.dismiss();
        }
      }
    });

    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        getDataConnector().addTrack(track, new Consumer<String>() {
          @Override
          public void consume(String input) {
            Toast.makeText(context, "Added new track!", Toast.LENGTH_LONG).show();
          }

          @Override
          public void error(int error, Throwable exception) {
            Toast.makeText(context, "Unable to save track!", Toast.LENGTH_LONG).show();
          }
        });

        dialog.dismiss();
        setResult(MainActivity.SINGLE_ROUTE, new Intent());
        finish();
      }
    });

    dialog.show();
  }

  /**
   * Adds a new route to the backend
   *
   * @param name         The name of the route.
   * @param selectedType Route type.
   * @param selectedDiff Route difficulty.
   */
  private void addRoute(final String name, CharSequence selectedType, CharSequence selectedDiff) {
    final Context context = this;
    final Route route = new Route(name, track);

    // Set route type.
    if (selectedType.equals("City")) {
      route.setRoutetyp(Routetyp.CITY);
    } else if (selectedType.equals("Offroad")) {
      route.setRoutetyp(Routetyp.OFFROAD);
    } else if (selectedType.equals("Road")) {
      route.setRoutetyp(Routetyp.ROAD);
    } else {
      Toast.makeText(context, "Wrong route type!", Toast.LENGTH_LONG).show();
      return;
    }

    // Set route difficulty.
    if (selectedDiff.equals("Easy")) {
      route.setDifficulty(Difficulty.EASY);
    } else if (selectedDiff.equals("Normal")) {
      route.setDifficulty(Difficulty.NORMAL);
    } else if (selectedDiff.equals("Hard")) {
      route.setDifficulty(Difficulty.HARD);
    } else {
      Toast.makeText(context, "Wrong route difficulty!", Toast.LENGTH_LONG).show();
      return;
    }

    // Add the new route.
    getDataConnector().addRoute(route, new Consumer<String>() {
      @Override
      public void consume(String input) {
        Toast.makeText(context, "Added new route!", Toast.LENGTH_LONG).show();

        Intent intent = new Intent();
        intent.putExtra(MainActivity.ROUTE_ID_EXTRA, input);
        setResult(MainActivity.SINGLE_ROUTE, intent);

        route.setOid(input);

        getDataConnector().addTrack(track, route, new Consumer<Void>() {
          @Override
          public void consume(Void input) {
            Toast.makeText(context, "Added new track!", Toast.LENGTH_LONG).show();
          }

          @Override
          public void error(int error, Throwable exception) {
            Toast.makeText(context, "Unable to save track!", Toast.LENGTH_LONG).show();
          }
        });

        finish();
      }

      @Override
      public void error(int error, Throwable exception) {
        Toast.makeText(context, "Unable to add new route!", Toast.LENGTH_LONG).show();
      }
    });
  }

  /**
   * Loads the route for routing.
   */
  private void loadRoute() {
    final TrackingActivity context = this;
    getDataConnector().getRouteById(routesOid, new Consumer<Route>() {
      @Override
      public void consume(Route input) {
        route = input;
        router = new AndroidLocationRouter(route, 1, context);
        clearMap();
        updateCamera();
      }

      @Override
      public void error(int error, Throwable exception) {
        Toast.makeText(context, "Error while loading route!", Toast.LENGTH_LONG).show();
      }
    });
  }

  /**
   * Adds a route into the backend, if it is finished.
   */
  private void saveRouting() {
    if (track != null && track.size() > 0) {
      final Context context = this;
      if (router.isFinished()) {
        getDataConnector().addTrack(track, route, new Consumer<Void>() {
          @Override
          public void consume(Void input) {
            Toast.makeText(context, "Added track to route!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent();
            intent.putExtra(MainActivity.ROUTE_ID_EXTRA, route.getOid());
            setResult(MainActivity.SINGLE_ROUTE, intent);
            finish();
          }

          @Override
          public void error(int error, Throwable exception) {
            Toast.makeText(context, "Unable to save track!", Toast.LENGTH_LONG).show();
          }
        });
      } else {
        getDataConnector().addTrack(track, new Consumer<String>() {
          @Override
          public void consume(String input) {
            Toast.makeText(context, "Added new track!", Toast.LENGTH_LONG).show();
            setResult(MainActivity.SINGLE_ROUTE, new Intent());
            finish();
          }

          @Override
          public void error(int error, Throwable exception) {
            Toast.makeText(context, "Unable to save track!", Toast.LENGTH_LONG).show();
          }
        });
      }
    } else {
      Toast.makeText(this, "Empty track! No track was saved", Toast.LENGTH_LONG).show();
    }
  }

  /**
   * Saves the recorded track to the backend.
   */
  private void saveTracking() {
    if (track != null && track.size() > 0) {
      showRouteDialog();
    } else {
      Toast.makeText(this, "Empty track! No track was saved", Toast.LENGTH_LONG).show();
    }
  }

  /**
   * Starts a thread for receiving location updates for tracking.
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
   * Starts a thread for receiving location updates for routing.
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
                      viewController.changeButtonIcon(false);
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
   * Updates the track and updates the tracking information views.
   *
   * @param track        Updated track.
   * @param lastLocation Last location.
   */
  private void updateTrack(Track track, Location lastLocation) {
    if (isTracking) {
      this.track = track;
      clearMap();
      this.lastLocation = lastLocation;
      updateCamera();
      viewController.updateViews(track);
    }
  }

  /**
   * Clears the map and draws the route, if routing is enabled.
   */
  private void clearMap() {
    googleMap.clear();
    if (route != null && !router.isFinished()) {
      GoogleMapHelper.drawLocationList(route, Color.RED, googleMap);

      if (router.getNextTarget() != null) {
        googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(router.getNextTarget().getLatitude(), router.getNextTarget()
                .getLongitude())))
            .setFlat(false);
      }
    }
    if (track != null) {
      GoogleMapHelper.drawLocationList(track, Color.BLUE, googleMap);
    }
  }

  /**
   * Moves the camera to the users position.
   */
  private void updateCamera() {
    if (lastLocation != null) {
      LatLng lastPosition = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
      if (track != null && track.size() >= 2) {
        if (routing && route != null) {
          googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
              new CameraPosition.Builder().target(lastPosition).zoom(17).tilt(30)
                  .bearing(lastLocation.bearingTo(router.getNextTarget())).build()));
        } else {
          googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
              new CameraPosition.Builder().target(lastPosition).zoom(17).tilt(30)
                  .bearing(track.get(track.size() - 2).bearingTo(lastLocation)).build()));
        }
        GoogleMapHelper.drawPositionIcon(googleMap, lastPosition, track.get(track.size() - 2)
            .bearingTo(lastLocation));
      } else {
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
            new CameraPosition.Builder().target(lastPosition).zoom(17).tilt(30).build()));
        GoogleMapHelper.drawPositionIcon(googleMap, lastPosition, lastLocation.getBearing());
      }
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

    // Create location manager.
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, this,
          Looper.getMainLooper());
      lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
    // Check if routing is enabled.
    try {
      routesOid = getIntent().getStringExtra(OID);
    } catch (NullPointerException exception) {
      routesOid = null;
    }
    routing = routesOid != null;
    if (routing) {
      loadRoute();
    } else {
      tracker = new AndroidLocationTracker(1, this);
    }
    viewController = new TrackingViewController(this);
  }
}