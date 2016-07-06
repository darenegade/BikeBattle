package edu.hm.cs.bikebattle.app.fragments.single;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.api.domain.TopDriveEntryDto;
import edu.hm.cs.bikebattle.app.fragments.GoogleMapHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for Ranking Fragment.
 * @author René Zarwel
 */
public class RankingRecyclerViewAdapter extends RecyclerView.Adapter<RankingRecyclerViewAdapter.RankingEntryViewHolder> {


  /** Context to use**/
  private final Context context;

  /** List of Entries **/
  private List<TopDriveEntryDto> ranking = new ArrayList<TopDriveEntryDto>();

  public RankingRecyclerViewAdapter(Context context) {
    this.context = context;
  }

  @Override
  public RankingEntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_ranking_entry, parent, false);
    return new RankingEntryViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final RankingEntryViewHolder holder, final int position) {

    holder.entry = ranking.get(position);

    Picasso
        .with(context.getApplicationContext())
        .load(holder.entry.getFotoUri())
        .fit()
        .centerCrop()
        .placeholder(R.drawable.world_map)
        .error(R.drawable.world_map)
        .into(holder.mapImage);

    holder.positionView.setText(String.format("%d.", position+1));
    holder.userNameView.setText(holder.entry.getName());
    holder.timeView.setText(GoogleMapHelper.secondsToFormat(
        (long) holder.entry.getTotalTime()));
  }

  @Override
  public int getItemCount() {
    return ranking.size();
  }

  static class RankingEntryViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView positionView;
    public final ImageView mapImage;
    public final TextView userNameView;
    public final TextView timeView;
    public TopDriveEntryDto entry;

    public RankingEntryViewHolder(View view) {
      super(view);
      mView = view;

      mapImage = (ImageView) view.findViewById(R.id.entry_imageView);

      userNameView = (TextView) view.findViewById(R.id.entry_username);
      timeView = (TextView) view.findViewById(R.id.entry_time);
      positionView = (TextView) view.findViewById(R.id.entry_position);

    }
  }

  /**
   * Sets a new user list for this adapter.
   */
  public void setRanking(List<TopDriveEntryDto> ranking){
    this.ranking.clear();
    this.ranking.addAll(ranking);
    notifyDataSetChanged();
  }

}
