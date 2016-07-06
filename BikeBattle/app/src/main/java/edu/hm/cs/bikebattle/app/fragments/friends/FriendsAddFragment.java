package edu.hm.cs.bikebattle.app.fragments.friends;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.modell.User;

import java.util.Collections;
import java.util.List;

/**
 * This Fragment allows to search across all users of the Backend and
 * adds new friends to the current principal.
 *
 * @author Rene Zarwel
 */
public class FriendsAddFragment extends Fragment implements SearchView.OnQueryTextListener {
  /** Tag from the current Fragment*/
  public static final String TAG = "FriendsAddFragment";
  /**Current Activity.*/
  private BaseActivity activity;
  /**View Adapter.*/
  private FriendRecyclerViewAdapter adapter;
  /**Search View.*/
  private SearchView searchView;
  /**default View.*/
  private TextView helpText;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public FriendsAddFragment() {
  }

  @SuppressWarnings("unused")
  public static FriendsAddFragment newInstance() {
    return new FriendsAddFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = (BaseActivity) getActivity();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);

    // Place an action bar item for searching.
    MenuItem item = menu.add("Search");
    item.setIcon(android.R.drawable.ic_menu_search);
    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    searchView = new SearchView(getActivity());

    //Configure the ActionBar Item to white icons and text
    SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
    searchAutoComplete.setHintTextColor(getResources().getColor(R.color.icons));
    searchAutoComplete.setTextColor(getResources().getColor(R.color.icons));

    ImageView searchCloseIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
    searchCloseIcon.setImageResource(R.drawable.ic_clear_white_24dp);

    ImageView searchIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
    searchIcon.setImageResource(R.drawable.ic_search_white_24dp);

    //Configure behaviour
    searchView.setOnQueryTextListener(this);
    item.setActionView(searchView);
    searchView.setIconifiedByDefault(false);
    searchView.clearFocus();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_friend_add_list, container, false);

    // We have a menu item to show in action bar.
    setHasOptionsMenu(true);

    // Set the adapter
    if (view instanceof CoordinatorLayout) {
      CoordinatorLayout layout = (CoordinatorLayout) view;
      final Context context = view.getContext();

      helpText = (TextView) view.findViewById(R.id.helpText);

      //Setup user list
      final RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.list);
      recyclerView.setLayoutManager(new LinearLayoutManager(context));

      //Create Adapter to add new friends
      adapter = new FriendRecyclerViewAdapter(activity, new Consumer<User>() {
        @Override
        public void consume(User input) {
          activity.getDataConnector().addFriend(activity.getPrincipal(), input, new Consumer<Void>() {
            @Override
            public void consume(Void input) {
              //Friend added - return to friends list
              activity.getSupportFragmentManager().beginTransaction()
                  .replace(R.id.content_frame, FriendsFragment.newInstance()).commit();
            }

            @Override
            public void error(int error, Throwable exception) {
              Log.e(TAG, "ERROR: " + exception.getMessage());
              Toast.makeText(context, "Error on adding Friend", Toast.LENGTH_LONG).show();
            }
          });
        }

        @Override
        public void error(int error, Throwable exception) {}
      });
      recyclerView.setAdapter(adapter);
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

  @Override
  public boolean onQueryTextSubmit(String query) {

    searchView.clearFocus();

    activity.getDataConnector().getUserByName(query, new Consumer<List<User>>() {
      @Override
      public void consume(List<User> input) {
        //Dont show own user
        input.remove(activity.getPrincipal());
        adapter.setUsers(input);

        if(input.size() > 0)
          helpText.setVisibility(View.GONE);
      }

      @Override
      public void error(int error, Throwable exception) {
        Toast.makeText(activity.getApplicationContext(), "Error on loading friends!!", Toast.LENGTH_LONG)
            .show();
      }
    });

    return true;
  }

  @Override
  public boolean onQueryTextChange(String newText) {

    if(newText.isEmpty()) {
      adapter.setUsers(Collections.<User>emptyList());
      helpText.setEnabled(true);
      helpText.setVisibility(View.VISIBLE);
    } else {
      helpText.setEnabled(false);
    }

    return true;
  }
}
