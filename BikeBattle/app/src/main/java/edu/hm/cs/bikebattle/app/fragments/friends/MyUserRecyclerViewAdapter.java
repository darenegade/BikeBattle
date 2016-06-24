package edu.hm.cs.bikebattle.app.fragments.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.modell.User;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link User} and makes a call to the
 * specified {@link UserFragment.OnListFragmentInteractionListener}.
 */
public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

  private final List<User> users;
  private final Context context;
  private final UserFragment.OnListFragmentInteractionListener mListener;

  public MyUserRecyclerViewAdapter(List<User> users, BaseActivity activity, UserFragment.OnListFragmentInteractionListener listener) {
    this.context = activity.getApplicationContext();
    mListener = listener;

    this.users = users;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_user, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.mItem = users.get(position);
    holder.name.setText(users.get(position).getName());

    Picasso.with(context)
        .load(users.get(position).getFotoUri())
        .fit()
        .centerCrop()
        .placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher)
        .into(holder.foto);

    holder.mView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (null != mListener) {
          // Notify the active callbacks interface (the activity, if the
          // fragment is attached to one) that an item has been selected.
          mListener.onListFragmentInteraction(holder.mItem);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return users.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView name;
    public final ImageView foto;
    public User mItem;

    public ViewHolder(View view) {
      super(view);
      mView = view;
      name = (TextView) view.findViewById(R.id.name);
      foto = (ImageView) view.findViewById(R.id.imageView);
    }

    @Override
    public String toString() {
      return super.toString() + " '" + name.getText() + "'";
    }
  }
}
