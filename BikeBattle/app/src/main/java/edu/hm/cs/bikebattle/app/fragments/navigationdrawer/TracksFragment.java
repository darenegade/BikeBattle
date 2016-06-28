package edu.hm.cs.bikebattle.app.fragments.navigationdrawer;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.adapter.TrackListFragmentAdapter;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.data.DataConnector;
import edu.hm.cs.bikebattle.app.fragments.single.SingleTrackFragment;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.modell.User;

;

/**
 * Creats a new Fragment, where the user can find his tracks or routs
 */
public class TracksFragment extends ListFragment {

  private static final String TAG = "TracksFragment";
  private User user;
  private List<Track> tracks;
  private DataConnector dataConnector;
  private Bundle savedInstanceState;
  private FragmentManager mangager;

  /**
   * This method creates a new Fragment,with the required Informations
   *
   * @param user - is the current User from the App
   * @return new Fragment
   */
  public static final TracksFragment newInstance(User user, FragmentManager manager) {
    TracksFragment fragment = new TracksFragment();
    Bundle args = new Bundle();
    fragment.user = user;
    fragment.mangager = manager;
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    this.savedInstanceState = savedInstanceState;
    View view = inflater.inflate(R.layout.fragment_tracks, container, false);
    tracks = new ArrayList<Track>();
    BaseActivity activity = (BaseActivity) getActivity();
    dataConnector = activity.getDataConnector();
    getTracksFromBackEnd();

    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    mangager.beginTransaction().replace(R.id.content_frame, SingleTrackFragment.newInstance(tracks.get(position))).commit();
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
        setAllTracks(input);
        refreshTracks();
      }

      @Override
      public void error(int error, Throwable exception) {
        Log.e(TAG, "ALL TRACKS UPDATE FAILURE");
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
    setListAdapter(new TrackListFragmentAdapter(getContext(), tracks, user, savedInstanceState
        , getChildFragmentManager()));
    setRetainInstance(true);
  }
}
