package edu.hm.cs.bikebattle.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.adapter.RoutesListAdapter;
import edu.hm.cs.bikebattle.app.modell.Route;

/**
 * Fragment to display a list with routes.
 *
 * @author Lukas Brauckmann
 */
public class RoutesListFragment extends Fragment {
  /**
   * Activity in which the content is displayed.
   */
  private BaseActivity activity;

  private ListView list;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = (BaseActivity) getActivity();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.fragment_routes_list, container, false);
    list = (ListView) view.findViewById(R.id.listView);
    //TODO: Set routes.
    //list.setAdapter(new RoutesListAdapter(getContext(), activity.getRoutes()));
    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, final View view,
                              int position, long id) {
        //TODO
      }
    });

    return view;
  }

  public void updateList(List<Route> routes){
    list.setAdapter(new RoutesListAdapter(getContext(), routes));
  }
}