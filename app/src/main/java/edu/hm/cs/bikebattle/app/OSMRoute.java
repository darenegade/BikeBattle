package edu.hm.cs.bikebattle.app;

import android.graphics.Color;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import java.util.ArrayList;

import edu.hm.cs.bikebattle.app.OSMActivity;

/**
 * Created by lukas on 24.03.2016.
 */
public class OSMRoute {
    /**
     * The name of the route.
     */
    private final String name;
    /**
     * A list which includes all way points.
     */
    private ArrayList<GeoPoint> wayPoints;
    /**
     * Marker that represents the route on the map.
     */
    private OverlayItem positionMarker;
    /**
     * Length of the route in meters.
     */
    private final double length;

    private final OSMActivity activity;

    public OSMRoute(String name, ArrayList<GeoPoint> wayPoints, OSMActivity activity) {
        this.name = name;
        this.wayPoints = wayPoints;
        this.length = calcLength();
        this.activity = activity;
        positionMarker = new OverlayItem(this.name, String.valueOf(this.length),
                this.wayPoints.get(0));
        positionMarker.setMarker(activity.getResources().getDrawable(R.drawable.ic_bike));
    }

    public String getName() {
        return name;
    }

    public double getLength(){
        return length;
    }

    public void drawOnMap(MapView map) {
        PathOverlay path = new PathOverlay(Color.BLUE,7,new DefaultResourceProxyImpl(activity));
        for (GeoPoint g : wayPoints) {
            path.addPoint(g);
        }
        map.getOverlays().add(path);
    }

    @Override
    public String toString(){
        return String.format("%s:\n%.2f km",name,length/1000);
    }

    public OverlayItem getMarker() {
        return positionMarker;
    }

    private double calcLength(){
        double result=0;
        for (int i=0;i<wayPoints.size()-1;i++) {
            result+=wayPoints.get(i).distanceTo(wayPoints.get(i+1));
        }
        return result;
    }
}
