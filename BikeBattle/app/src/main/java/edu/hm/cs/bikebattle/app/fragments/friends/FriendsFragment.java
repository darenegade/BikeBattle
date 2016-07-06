package edu.hm.cs.bikebattle.app.fragments.friends;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.fragments.navigationdrawer.ProfilFragment;
import edu.hm.cs.bikebattle.app.modell.User;

import java.util.List;

/**
 * This Fragment show the friends of the current principal.
 *
 * @author Rene Zarwel
 */
public class FriendsFragment extends Fragment {
  /**Current Activity.*/
  private BaseActivity activity;
  /**View Adapter.*/
  private FriendRecyclerViewAdapter adapter;
  /** Current Layout.*/
  private SwipeRefreshLayout swipeRefreshLayout;
  /**default View.*/
  private TextView helpText;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public FriendsFragment() {
  }

  @SuppressWarnings("unused")
  public static FriendsFragment newInstance() {
    return new FriendsFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = (BaseActivity) getActivity();
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    getActivity().setTitle(getString(R.string.Menu_Your_Friends));

    View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

    // Set the adapter
    if (view instanceof CoordinatorLayout) {

      View layout = view.findViewById(R.id.swipeRefreshLayout);
      helpText = (TextView) view.findViewById(R.id.helpText);

      if (layout instanceof SwipeRefreshLayout) {
        swipeRefreshLayout = (SwipeRefreshLayout) layout;
        Context context = layout.getContext();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            // Refresh items
            refreshItems();
          }
        });

        //Setup user list
        final RecyclerView recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new FriendRecyclerViewAdapter(activity, new Consumer<User>() {
          @Override
          public void consume(User input) {
            activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, ProfilFragment.newInstance(input, input.getFotoUri(), false))
                .addToBackStack("FriendProfile")
                .commit();
          }

          @Override
          public void error(int error, Throwable exception) {
          }
        });
        recyclerView.setAdapter(adapter);

        //Get data from Backend and put into adapter
        activity.getDataConnector().getFriends(activity.getPrincipal(), new Consumer<List<User>>() {
          @Override
          public void consume(List<User> input) {
            adapter.setUsers(input);
            if(input.size() == 0){
              helpText.setVisibility(View.VISIBLE);
            } else {
              helpText.setVisibility(View.GONE);
            }
          }

          @Override
          public void error(int error, Throwable exception) {
            Toast.makeText(activity.getApplicationContext(), "Error on loading friends!!", Toast.LENGTH_LONG)
                .show();
          }
        });
        refreshItems();

        //Setup Floating Button to start addFriends Fragment
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, FriendsAddFragment.newInstance())
                .addToBackStack("friends")
                .commit();
          }
        });
      }
    }
    return view;
  }

  /**
   * Refreshes the friends in the list.
   */
  private void refreshItems() {
    //Get data from Backend and put into adapter
    activity.getDataConnector().getFriends(activity.getPrincipal(), new Consumer<List<User>>() {
      @Override
      public void consume(List<User> input) {
        adapter.setUsers(input);
        if(input.size() == 0){
          helpText.setVisibility(View.VISIBLE);
        } else {
          helpText.setVisibility(View.GONE);
        }
        swipeRefreshLayout.setRefreshing(false);
      }

      @Override
      public void error(int error, Throwable exception) {
        Toast.makeText(activity.getApplicationContext(), "Error on loading friends!!", Toast.LENGTH_LONG)
            .show();
        swipeRefreshLayout.setRefreshing(false);
      }
    }, true);
  }


  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

}
