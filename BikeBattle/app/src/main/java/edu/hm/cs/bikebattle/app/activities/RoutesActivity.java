package edu.hm.cs.bikebattle.app.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.fragments.RouteInformationFragment;
import edu.hm.cs.bikebattle.app.fragments.RoutesListFragment;
import edu.hm.cs.bikebattle.app.fragments.RoutesMapFragment;
import edu.hm.cs.bikebattle.app.fragments.RoutesOverviewFragment;
import edu.hm.cs.bikebattle.app.modell.Route;

/**
 * Activity for showing routes near to the user.
 *
 * @author Lukas Brauckmann
 */
public class RoutesActivity extends BaseActivity {
  /**
   * Fragment for navigation tabs.
   */
  private RoutesOverviewFragment overviewFragment = new RoutesOverviewFragment();
  /**
   * Fragment for detailed route information.
   */
  private RouteInformationFragment informationFragment = new RouteInformationFragment();
  /**
   * Fragment for displaying the map.
   */
  private final RoutesMapFragment mapFragment = new RoutesMapFragment();
  /**
   * Fragment for displaying the routes in a list.
   */
  private final RoutesListFragment listFragment = new RoutesListFragment();
  /**
   * LocationManager for providing locations.
   */
  private LocationManager locationManager;
  /**
   * ArrayList for all routes that should be shown.
   */
  private List<Route> routes = new ArrayList<Route>();
  /**
   * Flag whether the information fragment is displayed.
   */
  private boolean showRouteInfo = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_routes);

    //loadRoutes();
    createTestRoute();

    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    mapFragment.setLastLocation(getLastLocation());

    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
    ft.add(R.id.fragment_container, overviewFragment);
    ft.add(R.id.fragment_container, informationFragment);
    ft.hide(informationFragment);
    ft.commit();
  }

  /**
   * Returns the List with all routes.
   *
   * @return List with all routes.
   */
  public List<Route> getRoutes() {
    return routes;
  }

  /**
   * Show detailed information fragment for one route.
   *
   * @param routeId Id of the route, that should be displayed.
   */
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

  /**
   * Gets the last location from the location manager.
   *
   * @return Last location.
   */
  private Location getLastLocation() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }
    return null;
  }

  /**
   * Gets the fragment that displays the map.
   *
   * @return Map fragment.
   */
  public RoutesMapFragment getMapFragment() {
    return mapFragment;
  }

  /**
   * Gets the fragment that displays the list.
   *
   * @return List fragment.
   */
  public RoutesListFragment getListFragment() {
    return listFragment;
  }

  private void loadRoutes() {
    getDataConnector().getRoutesByLocation(getLastLocation(), 10, new Consumer<List<Route>>() {
      @Override
      public void consume(List<Route> input) {
        Log.d("Data", "Loaded routes!");
        routes = input;
        Log.d("Routes size", String.valueOf(routes.size()));
        mapFragment.showRoutes();
        listFragment.updateList();
      }

      @Override
      public void error(int error, Throwable exception) {

      }
    });
  }

  /**
   * Only for testing purpose.
   */
  private void createTestRoute() {
    ArrayList<Location> wayPoints = new ArrayList<Location>();
    Location loc1 = new Location("");
    loc1.setLatitude(48.143);
    loc1.setLongitude(11.588);
    wayPoints.add(loc1);
    Location loc2 = new Location("");
    loc2.setLatitude(48.144);
    loc2.setLongitude(11.588);
    wayPoints.add(loc2);
    Location loc3 = new Location("");
    loc3.setLatitude(48.145);
    loc3.setLongitude(11.588);
    wayPoints.add(loc3);
    Location loc4 = new Location("");
    loc4.setLatitude(48.15);
    loc4.setLongitude(11.588);
    wayPoints.add(loc4);
    Location loc5 = new Location("");
    loc5.setLatitude(48.148);
    loc5.setLongitude(11.59);
    wayPoints.add(loc5);
    Location loc6 = new Location("");
    loc6.setLatitude(48.146);
    loc6.setLongitude(11.592);
    wayPoints.add(loc6);
    routes.add(new Route(wayPoints, "Englischer Garten", false));
  }
}