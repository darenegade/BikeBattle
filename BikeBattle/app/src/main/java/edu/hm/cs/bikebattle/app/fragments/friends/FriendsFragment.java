package edu.hm.cs.bikebattle.app.fragments.friends;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.modell.User;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class FriendsFragment extends Fragment {

  private BaseActivity activity;

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
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

    // Set the adapter
    if (view instanceof CoordinatorLayout) {
      CoordinatorLayout layout = (CoordinatorLayout) view;
      Context context = view.getContext();

      //Setup user list
      final RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.list);
      recyclerView.setLayoutManager(new LinearLayoutManager(context));
      final FriendRecyclerViewAdapter adapter = new FriendRecyclerViewAdapter(activity, new Consumer<User>() {
        @Override
        public void consume(User input) {
          //TODO Open Profile
        }

        @Override
        public void error(int error, Throwable exception) {}
      });
      recyclerView.setAdapter(adapter);

      activity.getDataConnector().getFriends(activity.getPrincipal(), new Consumer<List<User>>() {
        @Override
        public void consume(List<User> input) {
          adapter.setUsers(input);
        }

        @Override
        public void error(int error, Throwable exception) {
          Toast.makeText(activity.getApplicationContext(), "Error on loading friends!!", Toast.LENGTH_LONG)
              .show();
        }
      });

      //Setup Floating Button
      FloatingActionButton fab = (FloatingActionButton) layout.findViewById(R.id.fab);
      fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          activity.getSupportFragmentManager().beginTransaction()
              .replace(R.id.conten_frame, FriendsAddFragment.newInstance())
              .addToBackStack("friends")
              .commit();
        }
      });

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

}
