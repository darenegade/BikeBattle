package edu.hm.cs.bikebattle.app.fragments.findRoutes;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.data.DataConnector;
import edu.hm.cs.bikebattle.app.fragments.routes.RoutesRecyclerViewAdapter;
import edu.hm.cs.bikebattle.app.modell.Route;

import java.util.List;

/**
 * Fragment to show all own routes.
 *
 * @author sven schulz
 */
public class RoutesListFragment extends Fragment {

  /**
   * Tag from the current Activity.
   */
  private static final String TAG = "RoutesListFragment";
  /**
   * Main Activity.
   */
  private BaseActivity activity;
  /**
   * View Apdapter.
   */
  private RoutesRecyclerViewAdapter adapter;
  /**
   * Default Textview.
   */
  private TextView helpText;
  /**
   * Dataconnector.
   */
  private DataConnector dataConnector;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public RoutesListFragment() {
  }

  /**
   * This method creates a new Fragment,with the required Informations
   *
   * @return new Fragment
   */
  public static final RoutesListFragment newInstance() {
    return new RoutesListFragment();
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = (BaseActivity) getActivity();
    dataConnector = activity.getDataConnector();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_find_routes, container, false);

    if (view instanceof CoordinatorLayout) {
      helpText = (TextView) view.findViewById(R.id.find_helpText_routes);

      //Setup tracks list
      final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.find_list_routes);
      recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
      adapter = new RoutesRecyclerViewAdapter(activity);

      recyclerView.setAdapter(adapter);
    }
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  /**
   * Set list of all routs
   *
   * @param routes - List of routs.
   */
  public void setRoutes(List<Route> routes) {
    adapter.setRoutes(routes);
  }

}
