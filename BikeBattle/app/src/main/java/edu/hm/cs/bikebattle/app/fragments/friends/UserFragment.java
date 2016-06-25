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
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class UserFragment extends Fragment {

  private OnListFragmentInteractionListener mListener;

  private BaseActivity activity;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public UserFragment() {
  }

  @SuppressWarnings("unused")
  public static UserFragment newInstance() {
    return new UserFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = (BaseActivity) getActivity();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_user_list, container, false);

    // Set the adapter
    if (view instanceof CoordinatorLayout) {
      CoordinatorLayout layout = (CoordinatorLayout) view;
      Context context = view.getContext();

      //Setup user list
      final RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.list);
      recyclerView.setLayoutManager(new LinearLayoutManager(context));

      activity.getDataConnector().getFriends(activity.getPrincipal(), new Consumer<List<User>>() {
        @Override
        public void consume(List<User> input) {
          recyclerView.setAdapter(new MyUserRecyclerViewAdapter(input, activity, mListener));
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
              .replace(R.id.conten_frame, UserFragment.newInstance()).commit();
        }
      });

    }
    return view;
  }


  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnListFragmentInteractionListener) {
      mListener = (OnListFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnListFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
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
  public interface OnListFragmentInteractionListener {
    void onListFragmentInteraction(User user);
  }
}
