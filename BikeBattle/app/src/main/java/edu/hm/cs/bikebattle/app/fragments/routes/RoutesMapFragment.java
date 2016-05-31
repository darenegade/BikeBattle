package edu.hm.cs.bikebattle.app.fragments.routes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import edu.hm.cs.bikebattle.app.activities.RoutesActivity;
import edu.hm.cs.bikebattle.app.modell.Route;

/**
 * Fragment to display a google map which shows routes.
 *
 * @author Lukas Brauckmann
 */
public class RoutesMapFragment extends Fragment implements OnMapReadyCallback {
  /**
   * The google map in which routes can be displayed.
   */
  private GoogleMap googleMap;
  /**
   * Activity in which the content is displayed.
   */
  private RoutesActivity activity;
  /**
   * Last location.
   */
  private Location lastLocation;
  private boolean mapReady = false;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    activity = (RoutesActivity) getActivity();

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
        .findFragmentById(R.id.map);
    if (mapFragment == null) {
      mapFragment = SupportMapFragment.newInstance();
      getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
    }
    mapFragment.getMapAsync(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_map, container, false);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      this.googleMap = googleMap;
      this.googleMap.setMyLocationEnabled(true);
      this.googleMap.getUiSettings().setMapToolbarEnabled(false);
      this.googleMap.getUiSettings().setCompassEnabled(true);
      mapReady=true;
      showRoutes();
      updateCamera();
    }
  }

  public void showRoutes() {
    for (Route r : activity.getRoutes()) {
      drawRoute(r);
    }
  }

  /**
   * Displays a route in the map.
   *
   * @param route Route that should be displayed.
   */
  private void drawRoute(Route route) {
    if (mapReady) {
      PolylineOptions polyRoute = new PolylineOptions();

      polyRoute.color(Color.BLUE);
      polyRoute.width(6);
      polyRoute.visible(true);

      for (Location wayPoint : route) {

        polyRoute.add(new LatLng(wayPoint.getLatitude(), wayPoint.getLongitude()));
      }

      googleMap.addPolyline(polyRoute);

      String information = String.format("%s: %.2f km", activity.getString(R.string.length),
          route.getDistanceInM() / 1000);
      googleMap.addMarker(new MarkerOptions()
          .position(new LatLng(route.get(0).getLatitude(), route.get(0).getLongitude()))
          .title(route.getName())
          .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bike))
          .snippet(information));
    }
  }

  public void setLastLocation(Location lastLocation) {
    this.lastLocation = lastLocation;
  }

  /**
   * Moves the camera to the users position.
   */
  private void updateCamera() {
    LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
        new CameraPosition.Builder().target(latLng).zoom(15).build()));
  }
}