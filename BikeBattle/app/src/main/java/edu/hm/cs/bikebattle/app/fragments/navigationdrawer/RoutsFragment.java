package edu.hm.cs.bikebattle.app.fragments.navigationdrawer;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.adapter.RoutsListFragmentAdapter;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.data.DataConnector;
import edu.hm.cs.bikebattle.app.fragments.single.SingleRouteFragment;
import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.User;

;

/**
 * Creats a new Fragment, where the user can find his tracks or routs
 */
public class RoutsFragment extends ListFragment {

  private static final String TAG = "RoutsFragment";
  private User user;
  private List<Route> routs;
  private DataConnector dataConnector;
  private Bundle savedInstanceState;
  private FragmentManager mangager;


  /**
   * This method creates a new Fragment,with the required Informations
   *
   * @param user - is the current User from the App
   * @return new Fragment
   */
  public static final RoutsFragment newInstance(User user, FragmentManager manager) {
    RoutsFragment fragment = new RoutsFragment();
    Bundle args = new Bundle();
    fragment.user = user;
    fragment.mangager = manager;
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    this.savedInstanceState = savedInstanceState;
    View view = inflater.inflate(R.layout.fragment_routs, container, false);
    routs = new ArrayList<Route>();
    BaseActivity activity = (BaseActivity) getActivity();
    dataConnector = activity.getDataConnector();
    getRoutsFromBackEnd();

    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    mangager.beginTransaction().replace(R.id.content_frame, SingleRouteFragment.newInstance(routs.get(position))).commit();

  }

  /**
   * This method gets all Tracks from the backend.
   * true if the user only look for his tracks, flase when the user want to look
   * all tracks.
   */
  private void getRoutsFromBackEnd() {
    dataConnector.getRoutesByUser(user, new Consumer<List<Route>>() {
      @Override
      public void consume(final List<Route> input) {
        setRouts(input);
        refreshRouts();
      }

      @Override
      public void error(int error, Throwable exception) {
        Log.e(TAG, "USER ROUTS UPDATE FAILURE");
      }
    });
  }

  public List<Route> getRouts() {
    return routs;
  }

  public void setRouts(List<Route> routs) {
    this.routs = routs;
  }

  /**
   * Refreshes the Routs Arraylist after loading it out from the backend.
   */
  public void refreshRouts() {

    routs = getRouts();
    setListAdapter(new RoutsListFragmentAdapter(getContext(), routs, user, savedInstanceState
        , getChildFragmentManager()));
  }
}