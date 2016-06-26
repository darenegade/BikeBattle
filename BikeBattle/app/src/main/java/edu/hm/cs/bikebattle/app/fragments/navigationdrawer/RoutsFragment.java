package edu.hm.cs.bikebattle.app.fragments.navigationdrawer;

;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.adapter.RoutsListFragmentAdapter;
import edu.hm.cs.bikebattle.app.api.domain.Difficulty;
import edu.hm.cs.bikebattle.app.api.domain.Routetyp;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.data.DataConnector;
import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.User;

/**
 * Creats a new Fragment, where the user can find his tracks
 */
public class RoutsFragment extends ListFragment {

  private static final String TAG = "RoutsFragment";
  private User user;
  private List routs;
  private DataConnector dataConnector;
  private Bundle savedInstanceState;

  /**
   * This method creates a new Fragment,with the required Informations
   *
   * @param user     - is the current User from the App
   * @param onlyUser - true if the user only look for his tracks, flase when the user want to look
   *                 all tracks.
   * @return new Fragment
   */
  public static final RoutsFragment newInstance(User user, boolean onlyUser) {
    RoutsFragment fragment = new RoutsFragment();
    Bundle args = new Bundle();
    fragment.user = user;
    args.putBoolean("onlyUser", onlyUser);
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
    getRoutsFromBackEnd(getArguments().getBoolean("onlyUser"));

    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    // activity.showRouteInfo(position);
  }

  private void fillList() {
    Consumer consumer = new Consumer<Void>() {
      @Override
      public void consume(Void input) {
      }

      @Override
      public void error(int error, Throwable exception) {
      }
    };

    ArrayList<Location> wayPoints = new ArrayList<Location>();
    Location loc1 = new Location("");
    loc1.setLatitude(48.154);
    loc1.setLongitude(11.554);
    wayPoints.add(loc1);

    Location loc2 = new Location("");
    loc2.setLatitude(48.155);
    loc2.setLongitude(11.556);
    wayPoints.add(loc2);

    Location loc3 = new Location("");
    loc3.setLatitude(48.154);
    loc3.setLongitude(11.557);
    wayPoints.add(loc3);

    Location loc4 = new Location("");
    loc4.setLatitude(48.153);
    loc4.setLongitude(11.561);
    wayPoints.add(loc4);

    Location loc5 = new Location("");
    loc5.setLatitude(48.152);
    loc5.setLongitude(11.56);
    wayPoints.add(loc5);

    Location loc6 = new Location("");
    loc6.setLatitude(48.151);
    loc6.setLongitude(11.558);
    wayPoints.add(loc6);
    Route test = new Route(wayPoints, "Hochschule", false);
    test.setDifficulty(Difficulty.EASY);
    test.setRoutetyp(Routetyp.ROAD);
    dataConnector.addRoute(test, user, new Consumer<Void>() {
      @Override
      public void consume(Void input) {
      }

      @Override
      public void error(int error, Throwable exception) {
        Log.e(TAG, "ROUT INPUT FAILURE");
      }
    });


    wayPoints = new ArrayList<Location>();
    loc1 = new Location("");
    loc1.setLatitude(48.143);
    loc1.setLongitude(11.588);
    wayPoints.add(loc1);
    loc2 = new Location("");
    loc2.setLatitude(48.144);
    loc2.setLongitude(11.588);
    wayPoints.add(loc2);
    loc3 = new Location("");
    loc3.setLatitude(48.145);
    loc3.setLongitude(11.588);
    wayPoints.add(loc3);
    loc4 = new Location("");
    loc4.setLatitude(48.15);
    loc4.setLongitude(11.588);
    wayPoints.add(loc4);
    loc5 = new Location("");
    loc5.setLatitude(48.148);
    loc5.setLongitude(11.59);
    wayPoints.add(loc5);
    loc6 = new Location("");
    loc6.setLatitude(48.146);
    loc6.setLongitude(11.592);
    wayPoints.add(loc6);
    //routs.add(new Route(wayPoints, "Englischer Garten", false));
  }

  /**
   * This method gets all Tracks from the backend.
   *
   * @param onlyUser true if the user only look for his tracks, flase when the user want to look
   *                 all tracks.
   */
  private void getRoutsFromBackEnd(final boolean onlyUser) {
    if (onlyUser) {
      dataConnector.getRoutesByUser(user, new Consumer<List<Route>>() {
        @Override
        public void consume(final List<Route> input) {
          //routs = input;
          setRouts(input);
          refreshRouts();
        }

        @Override
        public void error(int error, Throwable exception) {
          Log.e(TAG, "USER ROUTS UPDATE FAILURE");
        }
      });
    } else {
      dataConnector.getAllRoutes(new Consumer<List<Route>>() {
        @Override
        public void consume(List<Route> input) {
          setRouts(input);
          refreshRouts();
        }

        @Override
        public void error(int error, Throwable exception) {
          Log.e(TAG, "ALLROUTS UPDATE FAILURE");
        }
      });
    }
  }

  public List getRouts() {
    return routs;
  }

  /**
   *
   * @param routs
   */
  public void setRouts(List routs) {
    this.routs = routs;
  }

  /**
   * Refreshes the Routs Arraylist after loading it out from the backend.
   */
  public void refreshRouts(){

      routs = getRouts();
      setListAdapter(new RoutsListFragmentAdapter(getContext(), routs, user, savedInstanceState
          ,getChildFragmentManager()));
      setRetainInstance(true);
  }
}
