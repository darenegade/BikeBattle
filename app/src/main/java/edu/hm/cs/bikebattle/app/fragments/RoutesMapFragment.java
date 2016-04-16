package edu.hm.cs.bikebattle.app.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.RoutesActivity;
import edu.hm.cs.bikebattle.app.modell.Route;

/**
 * Created by lukas on 12.04.2016.
 */
public class RoutesMapFragment extends Fragment implements OnMapReadyCallback {
  private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

  private GoogleMap googleMap;
  private RoutesActivity activity;

  public RoutesMapFragment(RoutesActivity activity) {
    this.activity = activity;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
        .findFragmentById(R.id.map);
    if (mapFragment == null) {
      mapFragment = SupportMapFragment.newInstance();
      getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
    }
    mapFragment.getMapAsync(this);

    requestPermission();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_routesmap, container, false);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      this.googleMap = googleMap;
      this.googleMap.setMyLocationEnabled(true);
      this.googleMap.getUiSettings().setMapToolbarEnabled(false);
      this.googleMap.getUiSettings().setCompassEnabled(true);
      for (Route r : activity.getRoutes()) {
        drawRoute(r);
      }
    }
  }

  /**
   * Request position permission for Android 6.
   */
  private void requestPermission() {
    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
          MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }
  }

  public void drawRoute(Route route) {
    PolylineOptions polyRoute = new PolylineOptions();

    polyRoute.color(Color.BLUE);
    polyRoute.width(6);
    polyRoute.visible(true);

    for (Location wayPoint : route) {

      polyRoute.add(new LatLng(wayPoint.getLatitude(),wayPoint.getLongitude()));
    }

    googleMap.addPolyline(polyRoute);

    String information = String.format("Length: %.2f km",route.getDistanceInM()/1000);
    googleMap.addMarker(new MarkerOptions()
        .position(new LatLng(route.get(0).getLatitude(),route.get(0).getLongitude()))
        .title(route.getName())
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bike))
        .snippet(information));
  }
}
