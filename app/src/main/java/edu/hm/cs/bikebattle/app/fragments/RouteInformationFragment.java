package edu.hm.cs.bikebattle.app.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.modell.Route;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteInformationFragment extends Fragment {
  private TextView nameView;
  private TextView lengthView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.fragment_route_information, container, false);
    nameView = (TextView) view.findViewById(R.id.routeInfo_textView_name);
    lengthView = (TextView) view.findViewById(R.id.routeInfo_textView_length);
    return view;
  }

  public void setRoute(Route route){
    nameView.setText(route.getName());
    lengthView.setText(String.valueOf(route.getDistanceInM()));
  }
}
