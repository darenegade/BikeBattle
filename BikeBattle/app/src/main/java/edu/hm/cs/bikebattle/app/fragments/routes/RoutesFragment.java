package edu.hm.cs.bikebattle.app.fragments.routes;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.data.DataConnector;
import edu.hm.cs.bikebattle.app.modell.Route;

import java.util.Collections;
import java.util.List;

/**
 * Fragment to show all own routes.
 *
 * @author René Zarwel
 */
public class RoutesFragment extends Fragment {

  private static final String TAG = "RoutesListFragment";
  private BaseActivity activity;
  private RoutesRecyclerViewAdapter adapter;
  private SwipeRefreshLayout swipeRefreshLayout;
  private TextView helpText;
  private DataConnector dataConnector;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public RoutesFragment() {
  }

  /**
   * This method creates a new Fragment,with the required Informations
   * @return new Fragment
   */
  public static final RoutesFragment newInstance() {
    return new RoutesFragment();
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

    getActivity().setTitle(getString(R.string.Menu_Own_Routes));

    View view = inflater.inflate(R.layout.fragment_show_all_routes, container, false);

    if (view instanceof CoordinatorLayout) {

      View layout = view.findViewById(R.id.swipeRefreshLayout_routes);
      helpText = (TextView) view.findViewById(R.id.helpText_routes);

      if (layout instanceof SwipeRefreshLayout) {
        swipeRefreshLayout = (SwipeRefreshLayout) layout;
        Context context = layout.getContext();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            // Refresh items
            getRoutesFromBackEnd();
          }
        });

        //Setup tracks list
        final RecyclerView recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.list_routes);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RoutesRecyclerViewAdapter(activity);


        dataConnector.getRoutesByUser(activity.getPrincipal(), new Consumer<List<Route>>() {
          @Override
          public void consume(List<Route> input) {
            Collections.reverse(input);
            adapter.setRoutes(input);
          }

          @Override
          public void error(int error, Throwable exception) {
            Log.e(TAG, "ALL Routes UPDATE FAILURE");
          }
        });

        recyclerView.setAdapter(adapter);


      }
      //Refresh the list
      getRoutesFromBackEnd();
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
   * This method gets all Tracks from the backend.
   * true if the user only look for his tracks, flase when the user want to look
   * all tracks.
   */
  private void getRoutesFromBackEnd() {
    dataConnector.getRoutesByUser(activity.getPrincipal(), new Consumer<List<Route>>() {
      @Override
      public void consume(List<Route> input) {
        Collections.reverse(input);
        adapter.setRoutes(input);

        if(input.size() == 0){
          helpText.setVisibility(View.VISIBLE);
        } else {
          helpText.setVisibility(View.GONE);
        }
        swipeRefreshLayout.setRefreshing(false);
      }

      @Override
      public void error(int error, Throwable exception) {
        Log.e(TAG, "ALL Routes UPDATE FAILURE");
        swipeRefreshLayout.setRefreshing(false);
      }
    }, true);
  }
}
