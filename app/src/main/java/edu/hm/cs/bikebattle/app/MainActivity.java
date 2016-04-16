package edu.hm.cs.bikebattle.app;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import edu.hm.cs.bikebattle.app.tracker.AndroidLocationTracker;
import edu.hm.cs.bikebattle.app.tracker.LocationTracker;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final Context context = this;
    new Thread() {
      @Override
      public void run() {
        try {

          LocationTracker tracker;
          //tracker = new GoogleAPILocationTracker(context, 0);
          tracker = new AndroidLocationTracker(0, context);
          synchronized (tracker) {
            while (!tracker.isReady())
              tracker.wait();
          }
          tracker.start();
          synchronized (this) {
            wait(60000);
          }
          tracker.stop();
          tracker.shutdown();
          Log.d("Track", tracker.getTrack().size()+"");
          for(Location l: tracker.getTrack())
            Log.d("Location", l.toString());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

      }
    }.start();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
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
}
