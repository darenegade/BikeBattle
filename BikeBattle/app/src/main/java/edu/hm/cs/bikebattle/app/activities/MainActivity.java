package edu.hm.cs.bikebattle.app.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.fragments.RoutesOverviewFragment;
import edu.hm.cs.bikebattle.app.fragments.friends.FriendsFragment;
import edu.hm.cs.bikebattle.app.fragments.navigationdrawer.MainFragment;
import edu.hm.cs.bikebattle.app.fragments.navigationdrawer.ProfilFragment;
import edu.hm.cs.bikebattle.app.fragments.routes.RoutesFragment;
import edu.hm.cs.bikebattle.app.fragments.tracks.TracksFragment;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.modell.User;

public class MainActivity extends BaseActivity implements NavigationView
    .OnNavigationItemSelectedListener {

  private static final String TAG = "MainActivity";
  private NavigationView navigationView;
  private View headerView;
  private ImageView profilImage;
  private DrawerLayout drawer;
  private FragmentManager fm;
  /**
   * Permission request parameter value.
   */
  private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Reconnect to Google and set Principle from Backend
    reconnect();

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    navigationView = (NavigationView) findViewById(R.id.nav_view);
    setupDrawerContent(navigationView);

    headerView = navigationView.getHeaderView(0);
    //navigationView.setNavigationItemSelectedListener(this);

    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
      @Override
      public void onDrawerClosed(View drawerView) {
        // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
        super.onDrawerClosed(drawerView);
        InputMethodManager inputMethodManager = (InputMethodManager)
            getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
      }

      @Override
      public void onDrawerOpened(View drawerView) {
        // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
        super.onDrawerOpened(drawerView);
        InputMethodManager inputMethodManager = (InputMethodManager)
            getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
      }
    };
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    fm = getSupportFragmentManager();
    fm.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();

    requestPermission();


  }

  /**
   * Debug only
   *
   * @return random track
   */
  private Track getTrackWithRandomTime() {
    long starTime = System.currentTimeMillis();
    long wayPointTime = (long) (Math.random() * 50 + 25);
    Log.d("Starttime", starTime + "");
    Log.d("step", wayPointTime + "");
    ArrayList<Location> wayPoints = new ArrayList<Location>();
    Location loc1 = new Location("");
    loc1.setLatitude(48.154);
    loc1.setLongitude(11.554);
    loc1.setAltitude(500);
    loc1.setTime(starTime + wayPointTime);
    wayPoints.add(loc1);

    Location loc2 = new Location("");
    loc2.setLatitude(48.155);
    loc2.setLongitude(11.556);
    loc2.setAltitude(520);
    loc2.setTime(starTime + 2 * wayPointTime);
    wayPoints.add(loc2);

    Location loc3 = new Location("");
    loc3.setLatitude(48.154);
    loc3.setLongitude(11.557);
    loc3.setTime(starTime + 3 * wayPointTime);
    loc3.setAltitude(480);
    wayPoints.add(loc3);

    Location loc4 = new Location("");
    loc4.setLatitude(48.153);
    loc4.setLongitude(11.561);
    loc4.setAltitude(520);
    loc4.setTime(starTime + 4 * wayPointTime);
    wayPoints.add(loc4);

    Location loc5 = new Location("");
    loc5.setLatitude(48.152);
    loc5.setLongitude(11.56);
    loc5.setAltitude(500);
    loc5.setTime(starTime + 5 * wayPointTime);
    wayPoints.add(loc5);

    Location loc6 = new Location("");
    loc6.setLatitude(48.151);
    loc6.setLongitude(11.558);
    loc6.setAltitude(460);
    loc6.setTime(starTime + 6 * wayPointTime);
    wayPoints.add(loc6);

    return new Track(wayPoints);
  }

  @Override
  public void refreshUserInfo() {

    Log.d("Login", "login");

    final User user = getPrincipal();
    final String name = user.getName();
    final String email = user.getEmail();
    final String foto = getUserPhoto();

    TextView nameField = (TextView) headerView.findViewById(R.id.yournamefield);
    nameField.setText(name);
    profilImage = (ImageView) headerView.findViewById(R.id.imageView);

    Picasso
        .with(getApplicationContext())
        .load(foto)
        .fit()
        .centerCrop()
        .placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher)
        .into(profilImage);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  /**
   * Request position permission for Android 6.
   */
  private void requestPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
          MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  private void setupDrawerContent(NavigationView navigationView) {
    navigationView.setNavigationItemSelectedListener(
        new NavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(MenuItem menuItem) {
            selectDrawerItem(menuItem);
            return true;
          }
        });
  }

  public void selectDrawerItem(MenuItem menuItem) {

    switch (menuItem.getItemId()) {
      case R.id.nav_profil:
        fm.beginTransaction().replace(R.id.content_frame,
            ProfilFragment.newInstance(getPrincipal(), getUserPhoto()))
            .addToBackStack("profile")
            .commit();
        // Set action bar title
        setTitle(menuItem.getTitle());
        break;
      case R.id.nav_routes:
        fm.beginTransaction().replace(R.id.content_frame, RoutesFragment.newInstance())
            .addToBackStack("routes")
            .commit();
        // Set action bar title
        setTitle(menuItem.getTitle());
        break;
      case R.id.nav_tracks:
        fm.beginTransaction().replace(R.id.content_frame, TracksFragment.newInstance())
            .addToBackStack("tracks")
            .commit();
        // Set action bar title
        setTitle(menuItem.getTitle());
        break;
      case R.id.nav_new_track:
        Intent intent = new Intent(this, TrackingActivity.class);
        startActivity(intent);
        break;
      case R.id.nav_find_routes:
        fm.beginTransaction().replace(R.id.content_frame, RoutesOverviewFragment.newInstance())
            .commit();
        // Set action bar title
        setTitle(menuItem.getTitle());
        break;
      case R.id.nav_favorite:
        //fragmentClass = ThirdFragment.class;
        break;
      case R.id.nav_friends:
        fm.beginTransaction().replace(R.id.content_frame,
            FriendsFragment.newInstance())
            .addToBackStack("friends")
            .commit();
        // Set action bar title
        setTitle(menuItem.getTitle());
        break;
    }


    // Highlight the selected item has been done by NavigationView
    menuItem.setChecked(true);
    // Close the navigation drawer
    drawer.closeDrawers();
  }
}
