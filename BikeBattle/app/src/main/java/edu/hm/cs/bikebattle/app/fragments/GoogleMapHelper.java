package edu.hm.cs.bikebattle.app.fragments;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;


import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.modell.LocationList;

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
    PolylineOptions polyRoute = new PolylineOptions();

    polyRoute.color(color);
    polyRoute.width(6);
    polyRoute.visible(true);

    for (Location wayPoint : track) {
      polyRoute.add(new LatLng(wayPoint.getLatitude(), wayPoint.getLongitude()));
    }
    googleMap.addPolyline(polyRoute);
  }

  /**
   * Displays a position icon on the given position.
   * @param googleMap GoogleMap object.
   * @param position Given position.
   */
  public static void drawPositionIcon(GoogleMap googleMap, LatLng position, float rotation){
    MarkerOptions markerOptions = new MarkerOptions();
    markerOptions.position(position);
    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_navigation));
    markerOptions.rotation(rotation);
    markerOptions.flat(true);
    googleMap.addMarker(markerOptions);
  }

  /**
   * Calculates the outer coordinates of the locationlist.
   * The coordinates are returned in an array in the order:
   * north, east, south, west
   *
   * @param list locationlist
   * @return double array containing outer coordinates {north, east, south, west}
   */
  public static double[] getOutCoordinates(LocationList list) {
    if (list.size() <= 0) {
      return new double[] {0, 0, 0, 0};
    }
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

  /**
   * Zooms to the position of the locationlist in the googlemap object.
   * @param googleMap googlemap object
   * @param list locationlist
   */
  public static void zoomToTrack(GoogleMap googleMap, LocationList list) {
    double width = Math.abs(googleMap.getProjection().getVisibleRegion().farRight.longitude -
        googleMap.getProjection().getVisibleRegion().farLeft.longitude);
    double height = Math.abs(googleMap.getProjection().getVisibleRegion().farRight.latitude -
        googleMap.getProjection().getVisibleRegion().nearRight.latitude);

    double[] coords = GoogleMapHelper.getOutCoordinates(list);

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

  /**
   * Converts seconds to a formatted string.
   * hours:minuets:seconds
   * @param time time
   * @return formatted time
   */
  public static String secondsToFormat(long time) {
    return String.format("%d:%02d:%02d", time / 3600, time / 60 % 60, time % 60);
  }

  /**
   * Converts the distance to a format.
   * If the length is smaller than 1 km the distance is displayed in meter. Otherwise in km.
   * @param distance in m
   * @return formatted distance
   */
  public static String distanceToFormat(float distance) {
    if (distance > 1000) {
      return String.format("%.1f km", distance / 1000);
    } else {
      return String.format("%.0f m", distance);
    }
  }

  /**
   * Returns the line data for the height of a locationlist.
   * @param list locationlost
   * @return linedata
   */
  @NonNull
  public static LineData getLineData(LocationList list) {
    ArrayList<Entry> entries = new ArrayList<Entry>();
    float distance = 0;
    if (list.size() > 0) {
      Location location = list.get(0);
      int lastIndex = (int) distance/10;
      entries.add(new Entry((float) location.getAltitude(), lastIndex));
      for (int i = 1; i < list.size(); i++) {
        Location tmp = list.get(i);
        distance += location.distanceTo(tmp);

        int nextIndex = (int) distance/10;
        if(nextIndex>lastIndex) {
          entries.add(new Entry((float) tmp.getAltitude(), nextIndex));
        }
        lastIndex = nextIndex;
        location = tmp;
      }
    }
    LineDataSet dataset = new LineDataSet(entries, "");

    // creating labels
    ArrayList<String> labels = new ArrayList<String>();
    for (int i = 0; i < list.getDistanceInM() / 10 + 1; i++) {
      labels.add(String.format("%d m", i * 10));
    }
    Log.d("Labels", labels.size()+"");
    return new LineData(labels, dataset);
  }

}
