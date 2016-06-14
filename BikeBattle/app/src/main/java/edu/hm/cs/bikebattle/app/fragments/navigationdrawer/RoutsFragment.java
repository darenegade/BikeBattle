package edu.hm.cs.bikebattle.app.fragments.navigationdrawer;

;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.adapter.RoutsListFragmentAdapter;
import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.User;

/**
 * Created by Zwen on 13.06.2016.
 */
public class RoutsFragment extends ListFragment {

  private User user;
  private List routs;

  public static final RoutsFragment newInstance(User user) {
    RoutsFragment fragment = new RoutsFragment();
    Bundle args = new Bundle();
    fragment.user = user;

    fragment.setArguments(args);
    return  fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    super.onCreateView(inflater,container,savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_routs, container, false);
    routs = new ArrayList<Route>();
    fillList();
    setListAdapter(new RoutsListFragmentAdapter(getContext(),routs, user));
    setRetainInstance(true);

    return  view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
   // activity.showRouteInfo(position);
  }

  private void fillList(){
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
      routs.add(new Route(wayPoints, "Hochschule", false));

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
      routs.add(new Route(wayPoints, "Englischer Garten", false));
    }


}
