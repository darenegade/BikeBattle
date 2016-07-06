package edu.hm.cs.bikebattle.app.fragments.single;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
import edu.hm.cs.bikebattle.app.activities.TrackingActivity;
import edu.hm.cs.bikebattle.app.api.domain.TopDriveEntryDto;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.fragments.GoogleMapHelper;
import edu.hm.cs.bikebattle.app.modell.Route;

import java.util.List;

/**
 * Fragment displaying information of a route.
 * @author Nils Bernhardt
 * @version 1.0
 */
public class SingleRouteFragment extends Fragment implements OnMapReadyCallback {
  /**Route to display.*/
  private Route route;

  /**View.*/
  private View view;

  /**Google Map.*/
  private GoogleMap googleMap;

  /** Ranking Adapter */
  private RankingRecyclerViewAdapter adapter;

  /**
   * Creates a new Fragment for showing a single route.
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
    getActivity().setTitle("");

    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
        .findFragmentById(R.id.single_route_map);
    if (mapFragment == null) {
      mapFragment = SupportMapFragment.newInstance();
      getChildFragmentManager().beginTransaction().replace(R.id.single_route_map,
          mapFragment).commit();
    }
    mapFragment.getMapAsync(this);

    if(view==null) {
      view = inflater.inflate(R.layout.fragment_single_route, container, false);

      if (route != null) {
        drawChart(view);
        fillViews(view);
        getActivity().setTitle(route.getName());
        ((Button)view.findViewById(R.id.single_route_button)).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(view.getContext(), TrackingActivity.class);
            intent.putExtra(TrackingActivity.OID, route.getOid());
            startActivity(intent);
          }
        });
      }
    }

    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.ranking);
    adapter = new RankingRecyclerViewAdapter(
        getActivity().getApplicationContext());

    recyclerView.setAdapter(adapter);

    fillRanking(false);

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    fillRanking(true);
  }

  /**
   * Fills the ranking with tracks from the backend.
   */
  private void fillRanking(boolean refresh) {
    if (getActivity() instanceof BaseActivity) {
      BaseActivity activity = (BaseActivity) getActivity();

      Log.d("SignleRouteFragment", route.getOid());

      activity.getDataConnector().getTopTwentyOfRoute(route,
          new Consumer<List<TopDriveEntryDto>>() {
            @Override
            public void consume(List<TopDriveEntryDto> input) {
              adapter.setRanking(input);
            }

            @Override
            public void error(int error, Throwable exception) {
              Log.e("TOP List", error + "");
              if (error == Consumer.EXCEPTION) {
                Log.e("Top List", exception.getMessage());
              }
            }
          });
    }
  }

  /**
   * Fills all views with the statistics.
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
   * Draws the height chart for the route.
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
    this.googleMap = googleMap;
  }
}
