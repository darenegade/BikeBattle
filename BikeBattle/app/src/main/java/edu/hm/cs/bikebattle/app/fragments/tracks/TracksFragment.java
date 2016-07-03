package edu.hm.cs.bikebattle.app.fragments.tracks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.activities.TrackingActivity;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.data.DataConnector;
import edu.hm.cs.bikebattle.app.modell.Track;

import java.util.Collections;
import java.util.List;

/**
 * Fragment to show all own drives/tracks.
 *
 * @author René Zarwel
 */
public class TracksFragment extends Fragment {

  private static final String TAG = "TracksFragment";
  private BaseActivity activity;
  private TracksRecyclerViewAdapter adapter;
  private SwipeRefreshLayout swipeRefreshLayout;
  private TextView helpText;
  private DataConnector dataConnector;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public TracksFragment() {
  }

  /**
   * This method creates a new Fragment,with the required Informations
   * @return new Fragment
   */
  public static final TracksFragment newInstance() {
    return new TracksFragment();
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = (BaseActivity) getActivity();
    dataConnector = activity.getDataConnector();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_show_all_tracks, container, false);

    if (view instanceof CoordinatorLayout) {

      View layout = view.findViewById(R.id.swipeRefreshLayout_tracks);
      helpText = (TextView) view.findViewById(R.id.helpText_tracks);

      if (layout instanceof SwipeRefreshLayout) {
        swipeRefreshLayout = (SwipeRefreshLayout) layout;
        Context context = layout.getContext();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            // Refresh items
            getTracksFromBackEnd();
          }
        });

        //Setup tracks list
        final RecyclerView recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.list_tracks);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new TracksRecyclerViewAdapter(activity);

        //Setup Floating Button to start addFriends Fragment
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_start_track);
        fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(activity, TrackingActivity.class);
            startActivity(intent);
          }
        });


        dataConnector.getTracksByUser(activity.getPrincipal(), new Consumer<List<Track>>() {
          @Override
          public void consume(List<Track> input) {
            Collections.reverse(input);
            adapter.setTracks(input);
          }

          @Override
          public void error(int error, Throwable exception) {
            Log.e(TAG, "ALL TRACKS UPDATE FAILURE");
          }
        });

        recyclerView.setAdapter(adapter);


      }
      //Refresh the list
      getTracksFromBackEnd();
    }
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  /**
   * This method gets all Tracks from the backend.
   * true if the user only look for his tracks, flase when the user want to look
   * all tracks.
   */
  private void getTracksFromBackEnd() {
    dataConnector.getTracksByUser(activity.getPrincipal(), new Consumer<List<Track>>() {
      @Override
      public void consume(List<Track> input) {
        Collections.reverse(input);
        adapter.setTracks(input);

        if(input.size() == 0){
          helpText.setVisibility(View.VISIBLE);
        } else {
          helpText.setVisibility(View.GONE);
        }
        swipeRefreshLayout.setRefreshing(false);
      }

      @Override
      public void error(int error, Throwable exception) {
        Log.e(TAG, "ALL TRACKS UPDATE FAILURE");
        swipeRefreshLayout.setRefreshing(false);
      }
    }, true);
  }
}
