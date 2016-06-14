package edu.hm.cs.bikebattle.app.fragments.single;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SingleTrackFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SingleTrackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleTrackFragment extends Fragment implements OnMapReadyCallback {
  private Track track;

  private OnFragmentInteractionListener mListener;

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param track Track
   * @return A new instance of fragment SingleTrackFragment.
   */
  public static SingleTrackFragment newInstance(Track track) {
    SingleTrackFragment fragment = new SingleTrackFragment();
    fragment.setTrack(track);
    return fragment;
  }

  private void setTrack(Track track) {
    this.track = track;
  }

  public SingleTrackFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
      float distance = track.getDistanceInM();
      long time = track.getTime_in_s();
      float speed = track.getAverageSpeed_in_kmh();
      ((TextView) view.findViewById(R.id.single_textView_distance)).setText(GoogleMapHelper.distanceToFormat(distance));
      ((TextView) view.findViewById(R.id.single_textView_time)).setText(GoogleMapHelper.secondsToFormat(time));
      ((TextView) view.findViewById(R.id.single_textView_average_speed)).setText(String.format("%.1f km/h", speed));
    }
    return view;
  }

  private void drawChart(View view) {
    LineChart chart = (LineChart) view.findViewById(R.id.chart);
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
    chart.setData(data); // set the data and list of lables into chart
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    Log.d("GooglMap read track ist", track + "");
    if (track != null) {
      GoogleMapHelper.drawLocationList(track, Color.RED, googleMap);
      GoogleMapHelper.zoomToTrack(googleMap, track);
    }
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p/>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }
}
