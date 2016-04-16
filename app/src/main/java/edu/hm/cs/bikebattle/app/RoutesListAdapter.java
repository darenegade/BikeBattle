package edu.hm.cs.bikebattle.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.hm.cs.bikebattle.app.modell.Route;

/**
 * Created by lukas on 16.04.2016.
 */
public class RoutesListAdapter extends ArrayAdapter<Route> {
  private final Context context;
  private final List<Route> routes;

  public RoutesListAdapter(Context context, List<Route> objects) {
    super(context, -1, objects);
    this.context=context;
    this.routes=objects;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.item_routeslist,parent,false);
    TextView textViewName = (TextView) rowView.findViewById(R.id.textView_name);
    TextView textViewInformation = (TextView) rowView.findViewById(R.id.textView_information);

    textViewName.setText(routes.get(position).getName());
    String information = String.format("Length: %.2f km",routes.get(position).getDistanceInM()/1000);
    textViewInformation.setText(information);

    return rowView;
  }
}
