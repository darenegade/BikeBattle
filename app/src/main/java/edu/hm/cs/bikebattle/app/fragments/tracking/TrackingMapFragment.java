package edu.hm.cs.bikebattle.app.fragments.tracking;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import edu.hm.cs.bikebattle.app.activities.TrackingActivity;
import edu.hm.cs.bikebattle.app.modell.Track;

/**
 * Fragment to display a google map for tracking a new track.
 *
 * @author Lukas Brauckmann
 */
public class TrackingMapFragment extends Fragment implements OnMapReadyCallback {
  /**
   * The google map which shows the recorded track and the users position.
   */
  private GoogleMap googleMap;
  /**
   * Last location which represents the users position.
   */
  private Location lastLocation;

  private FloatingActionButton trackingButton;

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
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_map, container, false);
    trackingButton = (FloatingActionButton) view.findViewById(R.id.tracking_button);
    trackingButton.setImageDrawable(
        ContextCompat.getDrawable(getContext(), R.drawable.ic_action_start));
    trackingButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Click action
        TrackingActivity activity = (TrackingActivity) getActivity();
        if (activity.changeTrackingMode()) {
          trackingButton.setImageDrawable(
              ContextCompat.getDrawable(getContext(), R.drawable.ic_action_stop));
        } else {
          trackingButton.setImageDrawable(
              ContextCompat.getDrawable(getContext(), R.drawable.ic_action_start));
        }
      }
    });
    return view;
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      this.googleMap = googleMap;
      this.googleMap.getUiSettings().setMapToolbarEnabled(false);
      this.googleMap.getUiSettings().setCompassEnabled(true);
      updateCamera();
    }
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

  /**
   * Sets a new track and updates the GUI.
   *
   * @param track New track.
   */
  public void updateTrack(final Track track) {
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        drawTrack(track);
        updateCamera();
      }
    });
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

  /**
   * Sets the last location.
   *
   * @param lastLocation Last location.
   */
  public void setLastLocation(Location lastLocation) {
    this.lastLocation = lastLocation;
  }
}
