package edu.hm.cs.bikebattle.app;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by lukas on 24.03.2016.
 */
public class Route {
    /**
     * The name of the route.
     */
    private final String name;
    /**
     * A list which includes all way points.
     */
    private ArrayList<LatLng> wayPoints;
    /**
     * Marker that represents the route on the map.
     */
    private Marker positionMarker;
    /**
     * Length of the route.
     */
    private double length;

    /**
     * Initialize the route.
     *
     * @param name      Name of the route.
     * @param wayPoints List of waypoints describing the route.
     * @param length    Length of the route.
     */
    public Route(String name, ArrayList<LatLng> wayPoints, double length) {
        this.name = name;
        this.wayPoints = wayPoints;
        this.length = length;
    }

    /**
     * Draw the route on the map and display the marker.
     *
     * @param map Map to draw on.
     */
    public void drawOnMap(GoogleMap map) {
        PolylineOptions polyRoute = new PolylineOptions();

        polyRoute.color(Color.BLUE);
        polyRoute.width(6);
        polyRoute.visible(true);

        for (LatLng wayPoint : wayPoints) {
            polyRoute.add(wayPoint);
        }

        map.addPolyline(polyRoute);
        positionMarker = map.addMarker(new MarkerOptions().position(wayPoints.get(0)).title(name)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bike))
            .snippet("Length: " + length + "km"));
    }

    @Override
    public String toString() {
        return name + "\n" + length + " km";
    }
}
