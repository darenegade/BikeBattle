package edu.hm.cs.bikebattle.app.fragments.single;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.api.domain.TopDriveEntryDto;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.fragments.GoogleMapHelper;
import edu.hm.cs.bikebattle.app.modell.Route;

/**
 * Fragment displaying information of a route.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public class SingleRouteFragment extends Fragment implements OnMapReadyCallback {
  /**
   * Route to display.
   */
  private Route route;

  /**
   * Creates a new Fragment for showing a single route.
   *
   * @param route Route
   * @return A new instance of fragment SingleTrackFragment.
   */
  public static SingleRouteFragment newInstance(Route route) {
    SingleRouteFragment fragment = new SingleRouteFragment();
    fragment.setRoute(route);
    return fragment;
  }

  /**
   * Sets the route to display.
   *
   * @param route route
   */
  private void setRoute(Route route) {
    this.route = route;
  }

  public SingleRouteFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
        .findFragmentById(R.id.single_route_map);
    if (mapFragment == null) {
      mapFragment = SupportMapFragment.newInstance();
      getChildFragmentManager().beginTransaction().replace(R.id.single_route_map, mapFragment).commit();
    }
    mapFragment.getMapAsync(this);

    View view = inflater.inflate(R.layout.fragment_single_route, container, false);
    if (route != null) {
      drawChart(view);
      fillViews(view);
      fillRanking(view);
    }
    return view;
  }

  private void fillRanking(final View view) {
    if (getActivity() instanceof BaseActivity) {
      BaseActivity activity = (BaseActivity) getActivity();
      activity.getDataConnector().getTopTwentyOfRoute(route, new Consumer<List<TopDriveEntryDto>>() {
        @Override
        public void consume(List<TopDriveEntryDto> input) {
          Log.d("Tracks", input.size() + "");
          GridLayout ranking = (GridLayout) view.findViewById(R.id.ranking_list);
          /*List<TopDriveEntryDto> input = new LinkedList<TopDriveEntryDto>();
          input.add(new TopDriveEntryDto("Nils", null, 10000, 20));
          input.add(new TopDriveEntryDto("Thomas", null, 12000, 18));
          input.add(new TopDriveEntryDto("Peter", null, 8000, 22));
          input.add(new TopDriveEntryDto("Lutz", null, 10000, 20));*/
          for (int index = 0; index < input.size(); index++) {
            TextView rank = new TextView(ranking.getContext());
            rank.setText(index + 1 + "");
            rank.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            ranking.addView(rank);
            TextView name = new TextView(ranking.getContext());
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            name.setText(input.get(index).getName());
            ranking.addView(name);
            TextView time = new TextView(ranking.getContext());
            time.setText(GoogleMapHelper.secondsToFormat((long) input.get(index).getTotalTime()));
            time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            ranking.addView(time);
          }
        }

        @Override
        public void error(int error, Throwable exception) {
          Log.e("Route", error + "");
          if (error == Consumer.EXCEPTION) {
            Log.e("Route", exception.getMessage());
          }
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
    ((TextView) view.findViewById(R.id.single_route_textView_distance)).setText(
        GoogleMapHelper.distanceToFormat(route.getDistanceInM()));
    ((TextView) view.findViewById(R.id.single_route_textView_downward)).setText(
        String.format("%.0f m", route.getDownwardInM()));
    ((TextView) view.findViewById(R.id.single_route_textView_upward)).setText(
        String.format("%.0f m", route.getUpwardInM()));
    ((TextView) view.findViewById(R.id.single_route_textView_difficulty)).setText(
        route.getDifficulty().toString());
    ((TextView) view.findViewById(R.id.single_route_textView_type)).setText(
        route.getRoutetyp().toString());
  }

  /**
   * Draws the height chart for the rpute.
   *
   * @param view inflated view
   */
  private void drawChart(View view) {
    ((LineChart) view.findViewById(R.id.route_chart)).setData(GoogleMapHelper.getLineData(route));
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    if (route != null) {
      GoogleMapHelper.drawLocationList(route, Color.RED, googleMap);
      GoogleMapHelper.zoomToTrack(googleMap, route);
    }
  }
}
