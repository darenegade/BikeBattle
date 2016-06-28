package edu.hm.cs.bikebattle.app.fragments.navigationdrawer;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.data.DataConnector;
import edu.hm.cs.bikebattle.app.fragments.friends.FriendRecyclerViewAdapter;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.modell.User;

public class ShowTracksFragment extends Fragment {

  private static final String TAG = "TracksFragment";
  private BaseActivity activity;
  private TracksRecyclerViewAdapter adapter;
  private SwipeRefreshLayout swipeRefreshLayout;
  private TextView helpText;
  private User user;
  private List<Track>tracks;
  private DataConnector dataConnector;

  /**
   * This method creates a new Fragment,with the required Informations
   * @param user - is the current User from the App
   * @return new Fragment
   */
  public static final ShowTracksFragment newInstance(User user) {
    ShowTracksFragment fragment = new ShowTracksFragment();
    Bundle args = new Bundle();
    fragment.user = user;
    fragment.setArguments(args);
    return fragment;
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
    tracks = new ArrayList<Track>();
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
        getTracksFromBackEnd();
        adapter = new TracksRecyclerViewAdapter(activity,tracks, user);
        recyclerView.setAdapter(adapter);
      }
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
    dataConnector.getTracksByUser(user, new Consumer<List<Track>>() {
      @Override
      public void consume(List<Track> input) {
        adapter.setTracks(input);
        setAllTracks(input);
        refreshTracks();
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
    });
  }

  public List<Track> getAllTracks() {
    return tracks;
  }

  public void setAllTracks(List<Track> tracks) {
    this.tracks = tracks;
  }

  /**
   * Refreshes the Tracks Arraylist after loading it out from the backend.
   */
  public void refreshTracks() {
    tracks = getAllTracks();
  }
}
