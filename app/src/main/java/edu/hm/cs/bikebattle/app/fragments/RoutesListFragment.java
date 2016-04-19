package edu.hm.cs.bikebattle.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.RoutesActivity;
import edu.hm.cs.bikebattle.app.RoutesListAdapter;

/**
 * Created by lukas on 12.04.2016.
 */
public class RoutesListFragment extends Fragment {

  private RoutesActivity activity;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = (RoutesActivity) getActivity();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.fragment_routes_list, container, false);
    ListView list = (ListView) view.findViewById(R.id.listView);
    list.setAdapter(new RoutesListAdapter(getContext(), activity.getRoutes()));
    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, final View view,
                              int position, long id) {
        activity.showRouteInfo(position);
      }
    });
    // Inflate the layout for this fragment
    return view;
  }
}
