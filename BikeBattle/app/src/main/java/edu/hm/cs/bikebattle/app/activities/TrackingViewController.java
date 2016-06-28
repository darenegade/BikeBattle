package edu.hm.cs.bikebattle.app.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.Locale;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.modell.Track;

/**
 * Created by lukas on 31.05.2016.
 */
public class TrackingViewController {
  private TrackingActivity activity;

  private View bottomSheet;

  private RelativeLayout relativeLayout;

  private FloatingActionButton trackingButton;

  private TextView textViewTime;
  private TextView textViewSpeed;
  private TextView textViewAverageSpeed;
  private TextView textViewDistance;
  private TextView textViewAltitude;

  private boolean init = false;

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
        Log.d("Height:",String.valueOf(v.getLayoutParams().height));
        if (v.getLayoutParams().height > 1000) {
          initRelativeLayout();
        }
      }
    });
  }

  private void initTextViews() {
    textViewTime = (TextView) activity.findViewById(R.id.trackInfo_textView_time);
    textViewSpeed = (TextView) activity.findViewById(R.id.trackInfo_textView_speed);
    textViewAverageSpeed = (TextView) activity.findViewById(R.id.trackInfo_textView_average_speed);
    textViewDistance = (TextView) activity.findViewById(R.id.trackInfo_textView_distance);
    textViewAltitude = (TextView) activity.findViewById(R.id.trackInfo_textView_altitude);
    textViewTime = (TextView) activity.findViewById(R.id.trackInfo_textView_time);
  }

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

  public void changeButtonIcon(boolean tracking){
    if(tracking){
      trackingButton.setImageDrawable(
          ContextCompat.getDrawable(activity, R.drawable.ic_action_stop));
    }else{
      trackingButton.setImageDrawable(
          ContextCompat.getDrawable(activity, R.drawable.ic_action_stop));
    }
  }

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

  private void initRelativeLayout() {
    if (!init) {
      relativeLayout.getLayoutParams().height = bottomSheet.getTop();
      relativeLayout.requestLayout();
      init=true;
    }
  }

  public void updateViews(Track track) {
    long seconds = (track.getTime_in_s()/1000+60)%60;
    long minutes = track.getTime_in_s()/1000/60;
    textViewTime.setText(String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds));

    String param = String.format(Locale.ENGLISH, "%.2f m", track.getDistanceInM());
    textViewDistance.setText(param);
    param = String.format(Locale.ENGLISH, "%.2f m/s", track.get(track.size() - 1).getSpeed());
    textViewSpeed.setText(param);
    param = String.format(Locale.ENGLISH, "%.2f km/h", track.getAverageSpeed_in_kmh());
    textViewAverageSpeed.setText(param);
  }
}
