package edu.hm.cs.bikebattle.app.adapter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.fragments.GoogleMapHelper;
import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.User;

/**
 * Shows for each Element some informations and a google map with the drawed track
 */
  public class RoutsListFragmentAdapter extends ArrayAdapter<Route> implements OnMapReadyCallback {

  private final List<Route> routes;
  private final User user;
  private GoogleMap googleMap;
  private Bundle savedInstanceState;
  private  int currentPosition;
  private FragmentManager manager;

  public RoutsListFragmentAdapter(Context context, List<Route> routes, User user,
                                  Bundle savedInstanceState, FragmentManager manager) {
    super(context, -1,routes);
    this.routes = routes;
    this.manager = manager;
    this.savedInstanceState = savedInstanceState;
    this.user = user;
  }
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    currentPosition = position;

    /**SupportMapFragment mapFragment = (SupportMapFragment) manager
        .findFragmentById(R.id.mapview);
    if (mapFragment == null) {
      mapFragment = SupportMapFragment.newInstance();
      manager.beginTransaction().replace(R.id.mapview, mapFragment).commit();
    }
    mapFragment.getMapAsync(this);*/
    View rowView = LayoutInflater.from(getContext()).inflate(R.layout.item_rout_layout,parent,false);
    MapView mapView;
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
      this.googleMap.setMyLocationEnabled(false);
      this.googleMap.getUiSettings().setMapToolbarEnabled(false);

      GoogleMapHelper.drawLocationList(routes.get(currentPosition), Color.RED, googleMap);
      GoogleMapHelper.zoomToTrack(googleMap, routes.get(currentPosition));
    }
  }
}
