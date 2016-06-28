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
 * Fragment to display detailed route information for one route.
 *
 * @author Lukas Brauckmann
 */
public class RouteInformationFragment extends Fragment {
  /**
   * TextView to display the name.
   */
  private TextView nameView;
  /**
   * TextView to display the length.
   */
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

  /**
   * Sets the route to be displayed.
   *
   * @param route New route.
   */
  public void setRoute(Route route) {
    nameView.setText(route.getName());
    String information = String.format("Length: %.2f", route.getDistanceInM() / 1000);
    lengthView.setText(information);
  }
}
