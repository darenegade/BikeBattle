package edu.hm.cs.bikebattle.app;

import android.graphics.Color;
import android.location.Location;

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
    private String name;
    private ArrayList<LatLng> wayPoints;
    private Marker positionMarker;

    public Route(String name, ArrayList<LatLng> wayPoints) {
        this.name = name;
        this.wayPoints = wayPoints;
    }

    public void drawOnMap(GoogleMap map){
        PolylineOptions polyRoute = new PolylineOptions();

        polyRoute.color(Color.BLUE);
        polyRoute.width(5);
        polyRoute.visible(true);

        for ( LatLng wayPoint : wayPoints)
        {
            polyRoute.add(wayPoint);
        }

        map.addPolyline( polyRoute );
        positionMarker = map.addMarker(new MarkerOptions().position(wayPoints.get(0)).title(name)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bike)));
    }

    public LatLng getStart(){
        return wayPoints.get(0);
    }
}
