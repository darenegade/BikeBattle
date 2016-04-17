package edu.hm.cs.bikebattle.app;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

import edu.hm.cs.bikebattle.app.fragments.RoutesFragmentPagerAdapter;
import edu.hm.cs.bikebattle.app.modell.Route;

public class RoutesActivity extends AppCompatActivity implements Serializable {

  private ArrayList<Route> routes = new ArrayList<Route>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_routes);


    // Get the ViewPager and set it's PagerAdapter so that it can display items
    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setAdapter(new RoutesFragmentPagerAdapter(getSupportFragmentManager(), this));

    // Give the TabLayout the ViewPager
    TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
    tabLayout.setupWithViewPager(viewPager);

    createTestRoute();
  }

  public ArrayList<Route> getRoutes() {
    return routes;
  }

  public void addRoute(Route route) {
    routes.add(route);
  }

  private void createTestRoute(){
    ArrayList<Location> wayPoints = new ArrayList<Location>();
    Location loc1=new Location("");
    loc1.setLatitude(48.154);
    loc1.setLongitude(11.554);
    wayPoints.add(loc1);
    Location loc2=new Location("");
    loc2.setLatitude(48.155);
    loc2.setLongitude(11.556);
    wayPoints.add(loc2);
    Location loc3=new Location("");
    loc3.setLatitude(48.154);
    loc3.setLongitude(11.557);
    wayPoints.add(loc3);
    Location loc4=new Location("");
    loc4.setLatitude(48.153);
    loc4.setLongitude(11.561);
    wayPoints.add(loc4);
    Location loc5=new Location("");
    loc5.setLatitude(48.152);
    loc5.setLongitude(11.56);
    wayPoints.add(loc5);
    Location loc6=new Location("");
    loc6.setLatitude(48.151);
    loc6.setLongitude(11.558);
    wayPoints.add(loc6);
    addRoute(new Route("Hochschule", wayPoints, null));

    wayPoints = new ArrayList<Location>();
    loc1=new Location("");
    loc1.setLatitude(48.143);
    loc1.setLongitude(11.588);
    wayPoints.add(loc1);
    loc2=new Location("");
    loc2.setLatitude(48.144);
    loc2.setLongitude(11.588);
    wayPoints.add(loc2);
    loc3=new Location("");
    loc3.setLatitude(48.145);
    loc3.setLongitude(11.588);
    wayPoints.add(loc3);
    loc4=new Location("");
    loc4.setLatitude(48.15);
    loc4.setLongitude(11.588);
    wayPoints.add(loc4);
    loc5=new Location("");
    loc5.setLatitude(48.148);
    loc5.setLongitude(11.59);
    wayPoints.add(loc5);
    loc6=new Location("");
    loc6.setLatitude(48.146);
    loc6.setLongitude(11.592);
    wayPoints.add(loc6);
    addRoute(new Route("Englischer Garten", wayPoints, null));
  }
}
