package edu.hm.cs.bikebattle.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.hm.cs.bikebattle.app.modell.User;
import edu.hm.cs.bikebattle.app.tracker.AndroidLocationTracker;
import edu.hm.cs.bikebattle.app.tracker.LocationTracker;

/**
 * Activity for testing tracker.
 * Can stop, start or continue tracking and shows a list the tracked locations.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public class TrackingTestActivity extends Activity {
  /**
   * amount of locations currently listed.
   */
  private static int views = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tracking_test);
    final LocationTracker tracker;
    //tracker = new GoogleApiLocationTracker(this, 0);
    tracker = new AndroidLocationTracker(0, this, new User("test", 60 ,170)); //TODO
    final TextView status = (TextView) findViewById(R.id.statusText);
    status.setText("Stoped");
    final LinearLayout locations = (LinearLayout) findViewById(R.id.locations);
    findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (tracker.start()) {
          status.setText("Running");
          locations.removeAllViews();
          views = 0;
        }
      }
    });

    findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (tracker.continueTracking()) {
          status.setText("Running");
        }
      }
    });

    findViewById(R.id.stopButton).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        tracker.stop();
        status.setText("Stoped");
      }
    });

    final Activity context = this;

    new Thread() {
      @Override
      public void run() {
        while (true) {
          try {
            while (views == tracker.getTrack().size()) {
              synchronized (tracker) {
                tracker.wait();
              }
            }
            synchronized (tracker.getTrack()) {
              while (views < tracker.getTrack().size()) {
                Log.e("Activity", "New Location");
                final TextView tv = new TextView(context);
                tv.setText(tracker.getTrack().get(views).toString());
                context.runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                    locations.addView(tv);
                  }
                });
                views++;
              }
            }
          } catch (InterruptedException exception) {
            exception.printStackTrace();
          }

        }
      }
    }.start();
  }

}
