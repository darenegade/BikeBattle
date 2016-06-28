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
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.modell.User;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link User}
 *
 * @author: Rene Zarwel
 */
public class FriendRecyclerViewAdapter extends RecyclerView.Adapter<FriendRecyclerViewAdapter.ViewHolder> {

  /** User List of this adapter to show in view**/
  private final List<User> users = new ArrayList<User>();

  /** Context to use**/
  private final Context context;

  /**BaseActivity from Parent Activity **/
  private final BaseActivity activity;

  /** Consumer to use onClick **/
  private final Consumer<User> clickConsumer;

  public FriendRecyclerViewAdapter(BaseActivity activity, Consumer<User> clickConsumer) {
    this.activity = activity;
    this.context = activity.getApplicationContext();
    this.clickConsumer = clickConsumer;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_friend, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {
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
        clickConsumer.consume(holder.mItem);
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

  /**
   * Sets a new user list for this adapter.
   * @param users to show
   */
  public void setUsers(List<User> users){
    this.users.clear();
    this.users.addAll(users);
    notifyDataSetChanged();
  }

}
