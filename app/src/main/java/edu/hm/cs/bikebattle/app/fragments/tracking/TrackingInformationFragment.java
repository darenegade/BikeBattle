package edu.hm.cs.bikebattle.app.fragments.tracking;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.modell.Track;

/**
 * Fragment to display detailed tracking information.
 *
 * @author Lukas Brauckmann
 */
public class TrackingInformationFragment extends Fragment {
  /**
   * TextView to display the speed.
   */
  private TextView speedView;
  /**
   * TextView to display the altitude.
   */
  private TextView altitudeView;
  /**
   * TextView to display the travelled distance.
   */
  private TextView distanceView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.fragment_track_information, container, false);
    speedView = (TextView) view.findViewById(R.id.trackInfo_textView_speed);
    altitudeView = (TextView) view.findViewById(R.id.trackInfo_textView_altitude);
    distanceView = (TextView) view.findViewById(R.id.trackInfo_textView_distance);
    return view;
  }

  /**
   * Sets a new track and last location and displays its information.
   *
   * @param track        New track.
   * @param lastLocation Last location.
   */
  public void updateTrack(final Track track, final Location lastLocation) {
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        float dist = 0;
        for (int i = 0; i < track.size() - 2; i++) {
          dist += track.get(i).distanceTo(track.get(i + 1));
        }

        String speed = String.format("Speed: %.2fm/s", lastLocation.getSpeed());
        String altitude = String.format("Altitude: %.2fm", lastLocation.getAltitude());
        String distance = String.format("Distance: %.2fm", dist);
        speedView.setText(speed);
        altitudeView.setText(altitude);
        distanceView.setText(distance);
      }
    });
  }
}
