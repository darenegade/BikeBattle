package edu.hm.cs.bikebattle.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.modell.Route;

/**
 * Adapter class for the list of routes.
 *
 * @author Lukas Brauckmann
 */
public class RoutesListAdapter extends ArrayAdapter<Route> {
  /**
   * Context.
   */
  private final Context context;
  /**
   * List with routes that should be displayed.
   */
  private final List<Route> routes;

  /**
   * Initialize the adapter.
   *
   * @param context Context.
   * @param routes  List with all routes.
   */
  public RoutesListAdapter(Context context, List<Route> routes) {
    super(context, -1, routes);
    this.context = context;
    this.routes = routes;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.item_routeslist, parent, false);
    TextView textViewName = (TextView) rowView.findViewById(R.id.textView_name);
    TextView textViewInformation = (TextView) rowView.findViewById(R.id.textView_information);

    textViewName.setText(routes.get(position).getName());
    String information =
        String.format("Length: %.2f km", routes.get(position).getDistanceInM() / 1000);
    textViewInformation.setText(information);

    return rowView;
  }
}
