package edu.hm.cs.bikebattle.app.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.api.domain.Difficulty;
import edu.hm.cs.bikebattle.app.api.domain.Routetyp;
import edu.hm.cs.bikebattle.app.fragments.navigationdrawer.MainFragment;
import edu.hm.cs.bikebattle.app.fragments.navigationdrawer.ProfilFragment;
import edu.hm.cs.bikebattle.app.fragments.single.SingleRouteFragment;
import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.User;

public class MainActivity extends BaseActivity
    implements NavigationView.OnNavigationItemSelectedListener {

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


    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });

    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    fm = getSupportFragmentManager();
    fm.beginTransaction().replace(R.id.conten_frame, new MainFragment()).commit();

    requestPermission();
  }

  @Override
  public void refreshUserInfo() {

    final User user = getPrincipal();
    final String name = user.getName();
    final String email = user.getEmail();
    final Uri foto = getUserPhoto();

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

    //Debug
    ArrayList<Location> wayPoints = new ArrayList<Location>();
    Location loc1 = new Location("");
    loc1.setLatitude(48.154);
    loc1.setLongitude(11.554);
    loc1.setAltitude(500);
    wayPoints.add(loc1);
    Location loc2 = new Location("");
    loc2.setLatitude(48.155);
    loc2.setLongitude(11.556);
    loc2.setAltitude(520);
    wayPoints.add(loc2);
    Location loc3 = new Location("");
    loc3.setLatitude(48.154);
    loc3.setLongitude(11.557);
    loc3.setAltitude(480);
    wayPoints.add(loc3);
    Location loc4 = new Location("");
    loc4.setLatitude(48.153);
    loc4.setLongitude(11.561);
    loc4.setAltitude(520);
    wayPoints.add(loc4);
    Location loc5 = new Location("");
    loc5.setLatitude(48.152);
    loc5.setLongitude(11.56);
    loc5.setAltitude(500);
    wayPoints.add(loc5);
    Location loc6 = new Location("");
    loc6.setLatitude(48.151);
    loc6.setLongitude(11.558);
    loc6.setAltitude(460);
    wayPoints.add(loc6);

    Route route = new Route("Test", wayPoints);
    route.setRoutetyp(Routetyp.CITY);
    route.setDifficulty(Difficulty.EASY);
    fm.beginTransaction().replace(R.id.conten_frame, SingleRouteFragment.newInstance(route)).commit();
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

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

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
        fm.beginTransaction().replace(R.id.conten_frame,
            ProfilFragment.newInstance(null, null)).commit();
        break;
      case R.id.nav_tracks:
        break;
      case R.id.nav_routes:
        //fragmentClass = ThirdFragment.class;
        break;
      case R.id.nav_favorite:
        //fragmentClass = ThirdFragment.class;
        break;
      case R.id.nav_friends:
        //fragmentClass = ThirdFragment.class;
        break;
      default:
        fm.beginTransaction().replace(R.id.conten_frame,
            ProfilFragment.newInstance(null, null)).commit();
    }


    // Highlight the selected item has been done by NavigationView
    menuItem.setChecked(true);
    // Set action bar title
    setTitle(menuItem.getTitle());
    // Close the navigation drawer
    drawer.closeDrawers();
  }
}
