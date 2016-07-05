package edu.hm.cs.bikebattle.app.fragments.single;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.fragments.GoogleMapHelper;
import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.Track;

/**
 * Fragment displaying information on a single track.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public class SingleTrackFragment extends Fragment implements OnMapReadyCallback {
  /**
   * Track to display.
   */
  private Track track;

  private View view;

  /**
   * Creates a new Fragment for showing a single track.
   *
   * @param track Track
   * @return A new instance of fragment SingleTrackFragment.
   */
  public static SingleTrackFragment newInstance(Track track) {
    Log.d("Track", track.getTime_in_s()+ " " + track.getDistanceInM());
    SingleTrackFragment fragment = new SingleTrackFragment();
    fragment.setTrack(track);
    return fragment;
  }

  /**
   * Sets the track to display.
   *
   * @param track track
   */
  private void setTrack(Track track) {
    this.track = track;
  }

  public SingleTrackFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    getActivity().setTitle("");

    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
        .findFragmentById(R.id.single_map);
    if (mapFragment == null) {
      mapFragment = SupportMapFragment.newInstance();
      getChildFragmentManager().beginTransaction().add(R.id.single_map, mapFragment).commit();
    }
    mapFragment.getMapAsync(this);

    if(view== null) {
      view = inflater.inflate(R.layout.fragment_single_track, container, false);
      if (track != null) {
        drawChart(view);
        fillViews(view);
        addRouteButton(view);
      }
    }
    return view;
  }

  /**
   * Enables the route button if there is an corresponding route in the backend.
   *
   * @param view parent view
   */
  private void addRouteButton(final View view) {
    if (getActivity() instanceof BaseActivity) {
      BaseActivity activity = (BaseActivity) getActivity();
      activity.getDataConnector().getRouteByTrack(track, new Consumer<Route>() {
        @Override
        public void consume(final Route input) {
          if (input != null) {
            Button button = (Button) view.findViewById(R.id.single_track_button);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.content_frame,
                    SingleRouteFragment.newInstance(input)).addToBackStack(null).commit();
              }
            });
          }
        }

        @Override
        public void error(int error, Throwable exception) {

        }
      });
    }
  }

  /**
   * Fills all views with the statistics.
   *
   * @param view inflated view
   */
  private void fillViews(View view) {
    float distance = track.getDistanceInM();
    long time = track.getTime_in_s();
    long paceInSkm = 0;
    if (distance != 0) {
      paceInSkm = (long) (time / (distance / 1000));
    }
    ((TextView) view.findViewById(R.id.single_textView_distance)).setText(
        GoogleMapHelper.distanceToFormat(distance));
    ((TextView) view.findViewById(R.id.single_textView_time)).setText(
        GoogleMapHelper.secondsToFormat(time));
    ((TextView) view.findViewById(R.id.single_textView_average_speed)).setText(
        String.format("%.1f km/h", track.getAverageSpeed_in_kmh()));
    ((TextView) view.findViewById(R.id.single_textView_downward)).setText(
        String.format("%.0f m", track.getDownwardInM()));
    ((TextView) view.findViewById(R.id.single_textView_upward)).setText(
        String.format("%.0f m", track.getUpwardInM()));
    ((TextView) view.findViewById(R.id.single_textView_average_pace)).setText(
        String.format("%d:%02d min/km", paceInSkm / 60, paceInSkm % 60));
  }

  /**
   * Draws the height chart for the track.
   *
   * @param view inflated view
   */
  private void drawChart(View view) {
    ((LineChart) view.findViewById(R.id.chart)).setData(GoogleMapHelper.getLineData(track));
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    if (track != null) {
      GoogleMapHelper.drawLocationList(track, Color.RED, googleMap);
      GoogleMapHelper.zoomToTrack(googleMap, track);
    }
  }
}
