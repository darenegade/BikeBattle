package edu.hm.cs.bikebattle.app.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import edu.hm.cs.bikebattle.app.R;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  /**
   * Permission request parameter value.
   */
  private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.routes_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), RoutesActivity.class);
        startActivity(intent);
                /*Intent intent = new Intent(getApplicationContext(), TrackingTestActivity.class);
                startActivity(intent);*/
      }
    });
    findViewById(R.id.track_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), TrackingActivity.class);
        startActivity(intent);
      }
    });

    /**final DataConnector connector = new BasicDataConnector();
    connector.getUserByName("Nils", new Consumer<List<User>>() {
      @Override
      public void consume(List<User> input) {
        if(input.size()>0){
          Log.d(TAG, "got user " + input.get(0).getName() + " - OID: " + input.get(0).getOid());
          Route route = new Route("Test");
          Location location;
          location = new Location("");
          location.setLongitude(0);
          location.setLatitude(0);
          route.add(location);
          location = new Location("");
          location.setLongitude(1);
          location.setLatitude(0);
          route.add(location);
          location = new Location("");
          location.setLongitude(2);
          location.setLatitude(1);
          route.add(location);
          location = new Location("");
          location.setLongitude(0);
          location.setLatitude(0);
          route.add(location);
          route.setRoutetyp(Routetyp.CITY);
          route.setDifficulty(Difficulty.EASY);
          connector.addRoute(route, input.get(0), new Consumer<Void>() {
            @Override
            public void consume(Void input) {
              Log.d(TAG, "Route added");
              ((Button)findViewById(R.id.track_button)).setText("Test");
            }

            @Override
            public void error(int error, Throwable exception) {
              Log.e(TAG,"Error2: " + error+"");
            }
          });
        }
      }

      @Override
      public void error(int error, Throwable exception) {
        Log.e(TAG,"Error1: " + error+"");
      }
    });**/

    requestPermission();
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
}
