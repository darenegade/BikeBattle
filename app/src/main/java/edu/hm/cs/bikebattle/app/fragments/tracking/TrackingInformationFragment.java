package edu.hm.cs.bikebattle.app.fragments.tracking;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

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
    /**
     * TextView to display the time of the track.
     */
    private TextView timerView;
    /**
     * TextView to display the difference in altitude.
     */
    private TextView diffAltView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_track_information, container, false);
        speedView = (TextView) view.findViewById(R.id.trackInfo_textView_speed);
        altitudeView = (TextView) view.findViewById(R.id.trackInfo_textView_altitude);
        distanceView = (TextView) view.findViewById(R.id.trackInfo_textView_distance);
        timerView = (TextView) view.findViewById(R.id.trackInfo_textView_time);
        diffAltView = (TextView) view.findViewById(R.id.trackInfo_textView_differenceAlt);
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
                speedView.setText(String.valueOf(String.format(
                        Locale.getDefault(), "%.2f m/s", lastLocation.getSpeed())));

                altitudeView.setText(String.valueOf((String.format(
                        Locale.getDefault(), "%.0f m", lastLocation.getAltitude()))));

                distanceView.setText(String.valueOf((String.format(
                        Locale.getDefault(), "%.2f m", track.getDistanceInM()))));

                diffAltView.setText(String.format(Locale.getDefault(),"%d m",calcDiffAlt(track)));

                long seconds = (track.getTime_in_s()/1000+60)%60;
                long minutes = track.getTime_in_s()/1000/60;
                timerView.setText(String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds));
            }
        });
    }

    /**
     * Calculates the difference in altitude for a track.
     * @param track Track for calculation.
     * @return Difference in altitude.
     */
    private int calcDiffAlt(Track track){
        int result=0;
        for (int i=0;i<track.size()-2;i++){
            result+=Math.abs(track.get(i).getAltitude()-track.get(i+1).getAltitude());
        }
        return result;
    }
}
