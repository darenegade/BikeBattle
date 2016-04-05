package edu.hm.cs.bikebattle.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Activity that displays a google map.
 */
public class GoogleMapActivity extends FragmentActivity
    implements OnMapReadyCallback {

  private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

  private GoogleMap googleMap;
  private ArrayList<Route> routes = new ArrayList<Route>();
  private boolean showMap = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_googlemaps);

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);


    // Debug
    ArrayList<LatLng> wayPoints = new ArrayList<LatLng>();

    wayPoints.add(new LatLng(51.241, 7.887));
    wayPoints.add(new LatLng(51.244, 7.883));
    wayPoints.add(new LatLng(51.243, 7.89));
    wayPoints.add(new LatLng(51.237, 7.892));
    wayPoints.add(new LatLng(51.241, 7.887));
    routes.add(new Route("Route 1", wayPoints, 5.6));

    requestPermission();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_map, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_list) {
      changeView(item);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near Sydney, Australia.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      this.googleMap = googleMap;
      this.googleMap.setMyLocationEnabled(true);
      this.googleMap.getUiSettings().setMapToolbarEnabled(false);
      this.googleMap.getUiSettings().setCompassEnabled(true);
      for (Route r : routes) {
        r.drawOnMap(this.googleMap);
      }
    }
  }

  /**
   * Request position permission for Android 6.
   */
  private void requestPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
          MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }
  }

  private void changeView(MenuItem item) {
    if (showMap) {
      setContentView(R.layout.activity_routelist);
      final ListView listview = (ListView) findViewById(R.id.listView);
      final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.routelist_item, R.id.textView, routes);
      listview.setAdapter(adapter);
      item.setIcon(R.drawable.ic_action_map);
      showMap = false;
    } else {
      setContentView(R.layout.activity_googlemaps);

      // Obtain the SupportMapFragment and get notified when the map is ready to be used.
      SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
          .findFragmentById(R.id.map);
      mapFragment.getMapAsync(this);

      item.setIcon(R.drawable.ic_action_list);
      showMap = true;
    }
  }
}
