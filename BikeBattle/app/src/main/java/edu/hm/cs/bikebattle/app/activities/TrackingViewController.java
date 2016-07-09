package edu.hm.cs.bikebattle.app.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.maps.SupportMapFragment;
import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.fragments.GoogleMapHelper;
import edu.hm.cs.bikebattle.app.modell.Track;

import java.util.Locale;

/**
 * Helper class for tracking activity. Controls the views and updates track information.
 *
 * @author Lukas Brauckmann
 */
public class TrackingViewController {
  /**
   * TrackingActivity.
   */
  private TrackingActivity activity;
  /**
   * BottomSheet.
   */
  private View bottomSheet;
  /**
   * Layout for the map.
   */
  private RelativeLayout relativeLayout;
  /**
   * Button to start and stop tracking.
   */
  private FloatingActionButton trackingButton;
  /**
   * TextView for the time.
   */
  private TextView textViewTime;
  /**
   * TextView for the speed.
   */
  private TextView textViewSpeed;
  /**
   * TextView for the average speed.
   */
  private TextView textViewAverageSpeed;
  /**
   * TextView for the distance.
   */
  private TextView textViewDistance;
  /**
   * TextView for the altitude.
   */
  private TextView textViewAltitude;
  /**
   * TextView for difference.
   */
  private TextView textViewDifferenceAlt;
  /**
   * Initialize boolean.
   */
  private boolean init = false;

  /**
   * Initializes the class.
   *
   * @param activity - TrackingActivity.
   */
  public TrackingViewController(TrackingActivity activity) {
    this.activity = activity;
    relativeLayout = (RelativeLayout) activity.findViewById(R.id.relative_layout);

    initTextViews();
    initMap();
    initBottomSheet();
    initButton();

    relativeLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
      @Override
      public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
                                 int oldTop, int oldRight, int oldBottom) {
        if (v.getLayoutParams().height > 1000) {
          initRelativeLayout();
        }
      }
    });
  }

  /**
   * Toggles the icon of the button.
   *
   * @param tracking Flag for tracking.
   */
  public void changeButtonIcon(boolean tracking) {
    if (tracking) {
      trackingButton.setImageDrawable(
          ContextCompat.getDrawable(activity, R.drawable.ic_action_start));
    } else {
      trackingButton.setImageDrawable(
          ContextCompat.getDrawable(activity, R.drawable.ic_action_stop));
    }
  }

  /**
   * Updates the text views information.
   *
   * @param track - Current track.
   */
  public void updateViews(Track track) {
    textViewTime.setText(GoogleMapHelper.secondsToFormat(track.getTime_in_s()));
    textViewDistance.setText(GoogleMapHelper.distanceToFormat(track.getDistanceInM()));
    String param = String.format(Locale.ENGLISH, "%.2f km/h", track.get(track.size() - 1)
        .getSpeed() * 3.6);
    textViewSpeed.setText(param);
    param = String.format(Locale.ENGLISH, "%.2f km/h", track.getAverageSpeed_in_kmh());
    textViewAverageSpeed.setText(param);
    param = String.format(Locale.ENGLISH, "%.2f m", track.get(track.size() - 1).getAltitude());
    textViewAltitude.setText(param);
    param = String.format(Locale.ENGLISH, "%.2f m", track.getUpwardInM());
    textViewDifferenceAlt.setText(param);
  }

  /**
   * Initializes the text views.
   */
  private void initTextViews() {
    textViewTime = (TextView) activity.findViewById(R.id.trackInfo_textView_time);
    textViewSpeed = (TextView) activity.findViewById(R.id.trackInfo_textView_speed);
    textViewAverageSpeed = (TextView) activity.findViewById(R.id.trackInfo_textView_average_speed);
    textViewDistance = (TextView) activity.findViewById(R.id.trackInfo_textView_distance);
    textViewAltitude = (TextView) activity.findViewById(R.id.trackInfo_textView_altitude);
    textViewTime = (TextView) activity.findViewById(R.id.trackInfo_textView_time);
    textViewDifferenceAlt = (TextView) activity.findViewById(R.id.trackInfo_textView_differenceAlt);
  }

  /**
   * Initializes the floating button.
   */
  private void initButton() {
    trackingButton = (FloatingActionButton) activity.findViewById(R.id.tracking_button);
    trackingButton.setImageDrawable(
        ContextCompat.getDrawable(activity, R.drawable.ic_action_start));
    final TrackingActivity content = activity;
    trackingButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Click action
        if (content.changeTrackingMode()) {
          trackingButton.setImageDrawable(
              ContextCompat.getDrawable(content, R.drawable.ic_action_stop));
        } else {
          trackingButton.setImageDrawable(
              ContextCompat.getDrawable(content, R.drawable.ic_action_start));
        }
      }
    });
  }

  /**
   * Initializes the map.
   */
  private void initMap() {
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) activity.getSupportFragmentManager()
        .findFragmentById(R.id.map);
    if (mapFragment == null) {
      mapFragment = SupportMapFragment.newInstance();
      activity.getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment)
          .commit();
    }
    mapFragment.getMapAsync(activity);
  }

  /**
   * Initializes the bottom sheet.
   */
  private void initBottomSheet() {
    bottomSheet = activity.findViewById(R.id.bottom_sheet);
    BottomSheetBehavior<View> mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
    mBottomSheetBehavior.setPeekHeight(textViewTime.getLineHeight() + 10);
    mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
      @Override
      public void onStateChanged(@NonNull View bottomSheet, int newState) {

      }

      @Override
      public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        relativeLayout.getLayoutParams().height = bottomSheet.getTop();
        relativeLayout.requestLayout();
      }
    });
  }

  /**
   * Initializes the relative layout.
   */
  private void initRelativeLayout() {
    if (!init) {
      relativeLayout.getLayoutParams().height = bottomSheet.getTop();
      relativeLayout.requestLayout();
      init = true;
    }
  }
}
