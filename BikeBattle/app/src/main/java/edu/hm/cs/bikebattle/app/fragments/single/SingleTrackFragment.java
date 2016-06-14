package edu.hm.cs.bikebattle.app.fragments.single;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.fragments.GoogleMapHelper;
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

  /**
   * Creates a new Fragment for showing a single track.
   *
   * @param track Track
   * @return A new instance of fragment SingleTrackFragment.
   */
  public static SingleTrackFragment newInstance(Track track) {
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
    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
        .findFragmentById(R.id.single_map);
    if (mapFragment == null) {
      mapFragment = SupportMapFragment.newInstance();
      getChildFragmentManager().beginTransaction().replace(R.id.single_map, mapFragment).commit();
    }
    mapFragment.getMapAsync(this);

    View view = inflater.inflate(R.layout.fragment_single_track, container, false);
    if (track != null) {
      drawChart(view);
      fillViews(view);
    }
    return view;
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

    ArrayList<Entry> entries = new ArrayList<Entry>();
    float distance = 0;
    Location location = track.get(0);
    entries.add(new Entry((float) location.getAltitude(), (int) distance / 10));
    for (int i = 1; i < track.size(); i++) {
      Location tmp = track.get(i);
      distance += location.distanceTo(tmp);
      entries.add(new Entry((float) tmp.getAltitude(), (int) distance / 10));
      location = tmp;
    }
    LineDataSet dataset = new LineDataSet(entries, "");

    // creating labels
    ArrayList<String> labels = new ArrayList<String>();
    for (int i = 0; i < track.getDistanceInM() / 10 + 1; i++) {
      labels.add(String.format("%d m", i * 10));
    }
    LineData data = new LineData(labels, dataset);
    ((LineChart) view.findViewById(R.id.chart)).setData(data);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    if (track != null) {
      GoogleMapHelper.drawLocationList(track, Color.RED, googleMap);
      GoogleMapHelper.zoomToTrack(googleMap, track);
    }
  }
}
