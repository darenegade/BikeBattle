package edu.hm.cs.bikebattle.app;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;


public class OSMActivity extends Activity {

    private static final int MY_PERMISSIONS_REQUEST = 0;
    private MapView mapView;
    private GeoPoint location;
    private MyLocationOverlay locationMarker;
    private ArrayList<OSMRoute> routes = new ArrayList<OSMRoute>();
    private ItemizedIconOverlay<OverlayItem> itemizedIconOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_osmmap);
        mapView = (MapView) findViewById(R.id.osmmap);

        requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapView.setTileSource(TileSourceFactory.CYCLEMAP);
            mapView.setMultiTouchControls(true);
            mapView.setMaxZoomLevel(20);
            itemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(),
                    new OnMarkerClickListener<OverlayItem>(this));

            mapView.getOverlays().add(itemizedIconOverlay);

            MapController mapController = (MapController) mapView.getController();
            mapController.setZoom(15);

            locationMarker = new MyLocationOverlay(this, mapView);
            mapView.getOverlays().add(locationMarker);
            locationMarker.enableMyLocation();
            locationMarker.enableFollowLocation();
            location = locationMarker.getMyLocation();

            initTestRoute();

            drawMarkers();
        }
    }

    /**
     * Request position permission for Android 6.
     */
    private void requestPermissions(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, MY_PERMISSIONS_REQUEST);
        }
    }

    private void initTestRoute() {
        ArrayList<GeoPoint> wayPoints = new ArrayList<GeoPoint>();

        wayPoints.add(new GeoPoint(51.241, 7.887));
        wayPoints.add(new GeoPoint(51.244, 7.883));
        wayPoints.add(new GeoPoint(51.243, 7.89));
        wayPoints.add(new GeoPoint(51.237, 7.892));
        wayPoints.add(new GeoPoint(51.241, 7.887));
        routes.add(new OSMRoute("Route 1", wayPoints,this));

        routes.get(0).drawOnMap(mapView);
    }

    private void drawMarkers() {
        itemizedIconOverlay.removeAllItems();
        // Create som init objects
        OverlayItem position = new OverlayItem("Current Position", "You",
                location);
        /*
        Drawable marker = getResources().getDrawable(R.drawable.ic_location);
        position.setMarker(marker);
        */

        // Add the init objects to the ArrayList overlayItemArray
        itemizedIconOverlay.addItem(position);

        for (OSMRoute route : routes) {
            itemizedIconOverlay.addItem(route.getMarker());
        }
    }

    public OSMRoute getRouteByMarker(OverlayItem marker){
        for (OSMRoute r:routes){
            if(marker.equals(r.getMarker()))
                return r;
        }
        return null;
    }
}
