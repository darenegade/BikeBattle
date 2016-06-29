package edu.hm.cs.bikebattle.app.adapter;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.squareup.picasso.Picasso;

import java.util.List;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.User;

/**
 * Shows for each Element some informations and draws the route into a google map
 */
  public class RoutsListFragmentAdapter extends ArrayAdapter<Route> {

  private final List<Route> routes;
  private final User user;
  private GoogleMap googleMap;
  private Bundle savedInstanceState;
  private  int currentPosition;
  private FragmentManager manager;

  /**
   * Creates a new Adapter for a arraylist of tracks
   * @param context - context
   * @param routes - list of routes
   * @param user - current User
   * @param savedInstanceState - saveInstanceState
   * @param manager - manager
   */
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


    View rowView = LayoutInflater.from(getContext()).inflate(R.layout.item_route_layout,parent,false);

    ImageView mapImage = (ImageView)rowView.findViewById(R.id.mapview_routes);

    Picasso
        .with(getContext())
        .load(makeMapString())
        .fit()
        .centerCrop()
        .placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher)
        .into(mapImage);

    TextView textViewName = (TextView) rowView.findViewById(R.id.name_item_routes);
    textViewName.setText(user.getName());

    String routTyp;
    if(routes.get(position).getDifficulty()== null)
      routTyp = "Default";

    else routTyp = routes.get(position).getDifficulty().toString();
    TextView routTypName = (TextView) rowView.findViewById(R.id.route_type_name_item_routes);
    routTypName.setText(routTyp);

    TextView routName = (TextView) rowView.findViewById(R.id.route_name_item_routes);
    routName.setText(routes.get(position).getName());

    String difficult;
    if(routes.get(position).getDifficulty()== null)
      difficult = "Default";

    else difficult = routes.get(position).getDifficulty().toString();
    TextView difficultName = (TextView) rowView.findViewById(R.id.difficult_item_routes);
    difficultName.setText(difficult);

    TextView textViewInformation = (TextView) rowView.findViewById(R.id.length_text_routes);
    String information =
        String.format(" %.2f km", routes.get(position).getDistanceInM() / 1000);
    textViewInformation.setText(information);

    return rowView;
  }

  private String makeMapString(){
    String mapString = "http://maps.googleapis.com/maps/api/staticmap?" +
        "size=500x400" +
        "&key=AIzaSyD9-iXLN4t282Q2EW22NCVTUTev4okhZYE" +
        "&path=color:0x0000ff%7Cweight:5";
    String splitter = "%7C";

    for(int i = 0 ; i < routes.get(currentPosition).size();i++){
      Location location = routes.get(currentPosition).get(i);
      mapString = mapString+splitter;
      mapString= mapString+location.getLatitude()+","+location.getLongitude();
    }
    return  mapString;
  }
}
