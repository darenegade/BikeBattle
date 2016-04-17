package edu.hm.cs.bikebattle.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.List;

import edu.hm.cs.bikebattle.app.modell.Route;

public class RouteInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);

        /*
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.routeInfo_map);
        mapFragment.getMapAsync(this);
*/
        Intent intent = getIntent();
        RoutesActivity activity = (RoutesActivity) intent.getSerializableExtra("RoutesActivity");
        route = activity.getRoutes().get(intent.getIntExtra("Position", 0));

        setTitle("Route: "+route.getName());
        Log.e("Länge: ",String.valueOf(route.getDistanceInM()));
        //Log.e("Lat: ",String.valueOf(route.get(0).getLatitude()));
        TextView textLength = (TextView) findViewById(R.id.routeInfo_textView_length);
        String information = String.format("%s: %.2f km", getString(R.string.length),
                route.getDistanceInM() / 1000);
        textLength.setText(information);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}
