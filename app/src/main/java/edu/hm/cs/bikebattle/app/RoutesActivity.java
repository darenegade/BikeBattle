package edu.hm.cs.bikebattle.app;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

import edu.hm.cs.bikebattle.app.fragments.RouteInformationFragment;
import edu.hm.cs.bikebattle.app.fragments.RoutesOverviewFragment;
import edu.hm.cs.bikebattle.app.modell.Route;

public class RoutesActivity extends FragmentActivity {

  private RoutesOverviewFragment overviewFragment;
  private RouteInformationFragment informationFragment;
  private ArrayList<Route> routes = new ArrayList<Route>();
  private boolean showRouteInfo = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_routes);

    createTestRoute();

    overviewFragment = new RoutesOverviewFragment();
    informationFragment = new RouteInformationFragment();

    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
    ft.add(R.id.fragment_container, overviewFragment);
    ft.add(R.id.fragment_container, informationFragment);
    ft.hide(informationFragment);
    ft.commit();
  }

  public ArrayList<Route> getRoutes() {
    return routes;
  }

  public void addRoute(Route route) {
    routes.add(route);
  }

  public void showRouteInfo(int routeId) {
    showRouteInfo = true;
    informationFragment.setRoute(routes.get(routeId));

    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
    ft.hide(overviewFragment);
    ft.show(informationFragment);
    ft.commit();
  }

  @Override
  public void onBackPressed() {
    if (showRouteInfo) {
      showRouteInfo = false;
      FragmentManager fm = getSupportFragmentManager();
      FragmentTransaction ft = fm.beginTransaction();
      ft.hide(informationFragment);
      ft.show(overviewFragment);
      ft.commit();
    } else {
      super.onBackPressed();
    }
  }

  private void createTestRoute() {
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
    addRoute(new Route("Hochschule", wayPoints, null));

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
    addRoute(new Route("Englischer Garten", wayPoints, null));
  }
}
