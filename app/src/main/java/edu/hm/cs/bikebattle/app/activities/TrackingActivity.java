package edu.hm.cs.bikebattle.app.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.fragments.tracking.TrackingInformationFragment;
import edu.hm.cs.bikebattle.app.fragments.tracking.TrackingMapFragment;
import edu.hm.cs.bikebattle.app.fragments.tracking.TrackingOverviewFragment;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.tracker.AndroidLocationTracker;
import edu.hm.cs.bikebattle.app.tracker.LocationTracker;

/**
 * Activity for tracking a new track.
 *
 * @author Lukas Brauckmann
 */
public class TrackingActivity extends AppCompatActivity {
  /**
   * Counter for received location updates.
   */
  private int locationUpdates = 0;
  /**
   * Fragment for showing the map.
   */
  private final TrackingMapFragment mapFragment = new TrackingMapFragment();
  /**
   * Fragment for showing tracking information.
   */
  private final TrackingInformationFragment informationFragment = new TrackingInformationFragment();
  /**
   * Track that is recorded so far.
   */
  private Track track;
  /**
   * Tracker for location updates.
   */
  private LocationTracker tracker;
  /**
   * Flag whether tracking is turned on.
   */
  private boolean isTracking = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_routes);

    final TrackingOverviewFragment overviewFragment = new TrackingOverviewFragment();

    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
    ft.add(R.id.fragment_container, overviewFragment);
    ft.commit();

    //tracker = new GoogleApiLocationTracker(this, 0);
    tracker = new AndroidLocationTracker(1, this);
    mapFragment.setLastLocation(tracker.getLastLocation());
  }

  /**
   * Change tracking mode to on or off.
   * @return Is tracking currently turned on.
   */
  public boolean changeTrackingMode() {
    if (isTracking) {
      tracker.stop();
      isTracking = false;
    } else {
      if (tracker.start()) {
        startTracking();
        isTracking = true;
      }
    }
    return isTracking;
  }

  @Override
  public void onBackPressed() {
    tracker.stop();
    super.onBackPressed();
  }

  /**
   * Starts a thread for receiving location updates.
   */
  private void startTracking() {
    new Thread() {
      @Override
      public void run() {
        while (true) {
          try {
            while (locationUpdates == tracker.getTrack().size()) {
              synchronized (tracker) {
                tracker.wait();
              }
            }
            synchronized (tracker.getTrack()) {
              while (locationUpdates < tracker.getTrack().size()) {
                updateTrack(tracker.getTrack(), tracker.getLastLocation());
                locationUpdates++;
              }
            }
          } catch (InterruptedException exception) {
            exception.printStackTrace();
          }

        }
      }
    }.start();
  }

  /**
   * Updates the track and updates the fragments.
   *
   * @param track        Updated track.
   * @param lastLocation Last location.
   */
  private void updateTrack(Track track, Location lastLocation) {
    this.track = track;
    mapFragment.updateTrack(track);
    mapFragment.setLastLocation(lastLocation);
    informationFragment.updateTrack(track, lastLocation);
  }

  /**
   * Returns the map fragment.
   *
   * @return Map fragment.
   */
  public TrackingMapFragment getMapFragment() {
    return mapFragment;
  }

  /**
   * Returns the information fragment.
   *
   * @return Infomration fragment.
   */
  public TrackingInformationFragment getInformationFragment() {
    return informationFragment;
  }
}