package edu.hm.cs.bikebattle.app.fragments;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import edu.hm.cs.bikebattle.app.modell.LocationList;
import edu.hm.cs.bikebattle.app.modell.Track;

/**
 * Created by Nils on 14.06.2016.
 *
 * @author Lukas Brauckmann
 */
public class GoogleMapHelper {
  /**
   * Displays a track in the map.
   *
   * @param track Track that should be displayed.
   */
  public static void drawLocationList(LocationList track, int color, GoogleMap googleMap) {
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

  public static double[] getOutCoordinates(LocationList list) {
    double east = list.get(0).getLongitude();
    double west = east;
    double north = list.get(0).getLatitude();
    double south = north;
    for (Location location : list) {
      if (location.getLongitude() > east) {
        east = location.getLongitude();
      }
      if (location.getLongitude() < west) {
        west = location.getLongitude();
      }
      if (location.getLatitude() > north) {
        north = location.getLatitude();
      }
      if (location.getLatitude() < south) {
        south = location.getLatitude();
      }
    }
    return new double[] {north, east, south, west};
  }

  public static void zoomToTrack(GoogleMap googleMap, Track track) {
    double width = Math.abs(googleMap.getProjection().getVisibleRegion().farRight.longitude -
        googleMap.getProjection().getVisibleRegion().farLeft.longitude);
    double height = Math.abs(googleMap.getProjection().getVisibleRegion().farRight.latitude -
        googleMap.getProjection().getVisibleRegion().nearRight.latitude);

    double[] coords = GoogleMapHelper.getOutCoordinates(track);

    double distHeight = Math.abs(coords[0] - coords[2]);
    double distWidth = Math.abs(coords[1] - coords[3]);
    float zoom;
    if (distHeight > distWidth) {
      zoom = (float) (Math.log(height / distHeight) / Math.log(2));
    } else {
      zoom = (float) (Math.log(width / distWidth) / Math.log(2));
    }
    LatLng latLng = new LatLng((coords[0] + coords[2]) / 2, (coords[1] + coords[3]) / 2);
    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
        new CameraPosition.Builder().target(latLng).zoom(zoom).build()));
  }

  public static String secondsToFormat(long time) {
    return String.format("%d:%02d:%02d", time / 3600, time / 60 % 60, time % 60);
  }

  public static String distanceToFormat(float distance) {
    if (distance > 1000) {
      return String.format("%.1f km", distance / 1000);
    } else {
      return String.format("%.0f m", distance);
    }
  }

}