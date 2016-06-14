package edu.hm.cs.bikebattle.app.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.User;

/**
 * Created by Zwen on 13.06.2016.
 */
  public class RoutsListFragmentAdapter extends ArrayAdapter<Route> implements OnMapReadyCallback {

  private final Context context;
  private final List<Route> routes;
  private final User user;
  private GoogleMap googleMap;
  private Bundle savedInstanceState;

  public RoutsListFragmentAdapter(Context context, List<Route> routes, User user, Bundle savedInstanceState) {
    super(context, -1,routes);
    this.routes = routes;
    this.context = context;
    this.savedInstanceState = savedInstanceState;
    this.user = user;
  }
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View rowView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout,parent,false);
    MapView mapView;
    // Gets the MapView from the XML layout and creates it
    mapView = (MapView) rowView.findViewById(R.id.mapview);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);

    TextView textViewName = (TextView) rowView.findViewById(R.id.name_item);
    textViewName.setText(user.getName());

    String routTyp;
    if(routes.get(position).getDifficulty()== null)
      routTyp = "Default";

    else routTyp = routes.get(position).getDifficulty().toString();
    TextView routTypName = (TextView) rowView.findViewById(R.id.rout_type_name_item);
    routTypName.setText(routTyp);

    TextView routName = (TextView) rowView.findViewById(R.id.rout_name_item);
    routName.setText(routes.get(position).getName());

    String difficult;
    if(routes.get(position).getDifficulty()== null)
      difficult = "Default";

    else difficult = routes.get(position).getDifficulty().toString();
    TextView difficultName = (TextView) rowView.findViewById(R.id.difficult_item);
    difficultName.setText(difficult);

    TextView textViewInformation = (TextView) rowView.findViewById(R.id.textView_information_item);
    String information =
        String.format(" %.2f km", routes.get(position).getDistanceInM() / 1000);
    textViewInformation.setText(information);

    return rowView;
  }


  @Override
  public void onMapReady(GoogleMap googleMap) {
    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      this.googleMap = googleMap;
      this.googleMap.setMyLocationEnabled(true);
      this.googleMap.getUiSettings().setMapToolbarEnabled(false);
      //this.googleMap.getUiSettings().setCompassEnabled(true);
     /** for (Route r : routes) {
        drawRoute(r);
      }
      updateCamera();*/
    }
  }
}
