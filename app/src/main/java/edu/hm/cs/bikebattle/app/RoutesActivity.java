package edu.hm.cs.bikebattle.app;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class RoutesActivity extends AppCompatActivity {

  private ArrayList<Route> routes = new ArrayList<Route>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_routes);


    // Get the ViewPager and set it's PagerAdapter so that it can display items
    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), this));

    // Give the TabLayout the ViewPager
    TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
    tabLayout.setupWithViewPager(viewPager);
  }

  public ArrayList<Route> getRoutes() {
    return routes;
  }

  public void addRoute(Route route) {
    routes.add(route);
  }
}
