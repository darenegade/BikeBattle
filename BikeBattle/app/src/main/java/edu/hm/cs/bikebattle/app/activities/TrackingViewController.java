package edu.hm.cs.bikebattle.app.activities;

import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import edu.hm.cs.bikebattle.app.R;

/**
 * Created by lukas on 31.05.2016.
 */
public class TrackingViewController {
  private TrackingActivity activity;

  private BottomSheetBehavior<View> mBottomSheetBehavior;

  private FloatingActionButton trackingButton;

  private TextView textViewTime;

  public TrackingViewController(TrackingActivity activity) {
    this.activity = activity;

    initTextViews();
    initMap();
    initBottomSheet();
    initButton();
  }

  private void initTextViews() {
    textViewTime=(TextView) activity.findViewById(R.id.trackInfo_textView_time);
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
    View bottomSheet = activity.findViewById(R.id.bottom_sheet);
    mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
    mBottomSheetBehavior.setPeekHeight(textViewTime.getLineHeight()+10);
  }
}
