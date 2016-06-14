package edu.hm.cs.bikebattle.app.adapter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.User;

/**
 * Created by Zwen on 13.06.2016.
 */
  public class RoutsListFragmentAdapter extends ArrayAdapter<Route> implements OnMapReadyCallback {

  private final Context context;
  private final List<Route> routes;
  private final User user;
  private Location lastLocation;
  private GoogleMap googleMap;

  public RoutsListFragmentAdapter(Context context, List<Route> routes, User user) {
    super(context, 0,routes);
    this.routes = routes;
    this.context = context;
    this.user = user;
  }
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
   /** LayoutInflater inflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    View rowView = inflater.inflate(R.layout.item_routeslist, parent, false);*/

    View rowView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout,parent,false);


    TextView textViewName = (TextView) rowView.findViewById(R.id.name_item);
    textViewName.setText("User: "+user.getName());

    String routTyp;
    if(routes.get(position).getDifficulty()== null)
      routTyp = "Default";

    else routTyp = routes.get(position).getDifficulty().toString();
    TextView routTypName = (TextView) rowView.findViewById(R.id.rout_type_name_item);
    routTypName.setText("Routen Typ: "+routTyp);

    TextView routName = (TextView) rowView.findViewById(R.id.rout_name_item);
    routName.setText("Name Route: "+routes.get(position).getName());

    String difficult;
    if(routes.get(position).getDifficulty()== null)
      difficult = "Default";

    else difficult = routes.get(position).getDifficulty().toString();
    TextView difficultName = (TextView) rowView.findViewById(R.id.difficult_item);
    difficultName.setText("Schwierigkeitsgrad: "+difficult);

    TextView textViewInformation = (TextView) rowView.findViewById(R.id.textView_information_item);
    String information =
        String.format("Length: %.2f km", routes.get(position).getDistanceInM() / 1000);
    textViewInformation.setText(information);

    return rowView;
  }

  private void drawRoute(Route route) {
    PolylineOptions polyRoute = new PolylineOptions();

    polyRoute.color(Color.BLUE);
    polyRoute.width(6);
    polyRoute.visible(true);

    for (Location wayPoint : route) {

      polyRoute.add(new LatLng(wayPoint.getLatitude(), wayPoint.getLongitude()));
    }

    googleMap.addPolyline(polyRoute);

    String information = String.format("%s: %.2f km", "activity.getString(R.string.length)",
        route.getDistanceInM() / 1000);
    googleMap.addMarker(new MarkerOptions()
        .position(new LatLng(route.get(0).getLatitude(), route.get(0).getLongitude()))
        .title(route.getName())
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bike))
        .snippet(information));
  }
  private void updateCamera() {
    LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
        new CameraPosition.Builder().target(latLng).zoom(15).build()));
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      this.googleMap = googleMap;
      this.googleMap.setMyLocationEnabled(true);
      this.googleMap.getUiSettings().setMapToolbarEnabled(false);
      this.googleMap.getUiSettings().setCompassEnabled(true);
      for (Route r : routes) {
        drawRoute(r);
      }
      updateCamera();
    }
  }
}
