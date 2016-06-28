package edu.hm.cs.bikebattle.app.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.Locale;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.modell.Route;

/**
 * Fragment to display a google map which shows routes.
 *
 * @author Lukas Brauckmann
 */
public class RoutesMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap
    .OnMyLocationButtonClickListener {
  /**
   * The google map in which routes can be displayed.
   */
  private GoogleMap googleMap;
  /**
   * Activity in which the content is displayed.
   */
  private BaseActivity activity;
  /**
   * Fragment to display the routes in a list
   */
  private RoutesListFragment listFragment;
  /**
   * List with all routes.
   */
  private List<Route> routes;

  /**
   * Factory for a new fragment.
   *
   * @param listFragment List fragment.
   * @return New RoutesMapFragment.
   */
  public static final RoutesMapFragment newInstance(RoutesListFragment listFragment) {
    RoutesMapFragment fragment = new RoutesMapFragment();
    fragment.listFragment = listFragment;
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    activity = (BaseActivity) getActivity();

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
    return inflater.inflate(R.layout.fragment_routes_map, container, false);
  }

  @Override
  public void onMapReady(final GoogleMap googleMap) {
    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      this.googleMap = googleMap;
      this.googleMap.setMyLocationEnabled(true);
      this.googleMap.getUiSettings().setMapToolbarEnabled(false);
      this.googleMap.getUiSettings().setCompassEnabled(true);
      this.googleMap.getUiSettings().setTiltGesturesEnabled(false);
      this.googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
          LatLng topLeft = googleMap.getProjection().getVisibleRegion().farLeft;
          LatLng bottomRight = googleMap.getProjection().getVisibleRegion().nearRight;
          Location center = new Location("Center");
          center.setLatitude((topLeft.latitude + bottomRight.latitude) / 2);
          center.setLongitude((topLeft.longitude + bottomRight.longitude) / 2);
          float distanceLat = (float) Math.abs(topLeft.latitude - bottomRight.latitude);
          float distanceLong = (float) Math.abs(topLeft.longitude - bottomRight.longitude);
          loadRoutes(center, Math.max(distanceLat, distanceLong));
        }
      });
    }
  }

  @Override
  public boolean onMyLocationButtonClick() {
    return false;
  }

  private void loadRoutes(Location location, float distance) {
    Log.d("Data", location.toString() + "\n" + distance);
    activity.getDataConnector().getRoutesByLocation(location, distance, new Consumer<List<Route>>
        () {
      @Override
      public void consume(List<Route> input) {
        routes = input;
        showRoutes();
        listFragment.updateList(input);
        updateCamera();
        Log.d("Loaded routes:", String.valueOf(input.size()));
      }

      @Override
      public void error(int error, Throwable exception) {
        Log.e("Error", "Unable to load routes! " + error);
        if (exception != null) {
          Log.e("Exception", exception.toString());
        }
      }
    });
  }

  private void showRoutes() {
    for (Route route : routes) {
      drawRoute(route);
    }
  }

  /**
   * Displays a route in the map.
   *
   * @param route Route that should be displayed.
   */
  private void drawRoute(Route route) {
    PolylineOptions polyRoute = new PolylineOptions();

    polyRoute.color(Color.BLUE);
    polyRoute.width(6);
    polyRoute.visible(true);

    for (Location wayPoint : route) {

      polyRoute.add(new LatLng(wayPoint.getLatitude(), wayPoint.getLongitude()));
    }

    googleMap.addPolyline(polyRoute);

    String information = String.format(Locale.ENGLISH, "%s: %.2f km", activity.getString(R
            .string.length),
        route.getDistanceInM() / 1000);
    googleMap.addMarker(new MarkerOptions()
        .position(new LatLng(route.get(0).getLatitude(), route.get(0).getLongitude()))
        .title(route.getName())
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bike))
        .snippet(information));
  }

  /**
   * Moves the camera to the users position.
   */
  private void updateCamera() {
    //TODO: Last location
    /*
    LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
        new CameraPosition.Builder().target(latLng).zoom(15).build()));
  */
  }
}