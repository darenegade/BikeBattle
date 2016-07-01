package edu.hm.cs.bikebattle.app.fragments.tracks;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.fragments.single.SingleRouteFragment;
import edu.hm.cs.bikebattle.app.fragments.single.SingleTrackFragment;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.modell.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for Tracks Fragment.
 *
 * @author René Zarwel
 */
public class TracksRecyclerViewAdapter extends RecyclerView.Adapter<TracksRecyclerViewAdapter.TracksViewHolder> {

  private final static String STATIC_MAP_START_LINK = "http://maps.googleapis.com/maps/api/staticmap?" +
      "size=500x200" +
      "&key=AIzaSyD9-iXLN4t282Q2EW22NCVTUTev4okhZYE" +
      "&path=color:0x0000ff%7Cweight:5";
  private final static String SPLITTER = "%7C";

  /** Context to use**/
  private final Context context;

  /** List of Tracks **/
  private List<Track> tracks = new ArrayList<Track>();

  /** Current User**/
  private User user;

  public TracksRecyclerViewAdapter(BaseActivity activity) {
    this.context = activity.getApplicationContext();
    user = activity.getPrincipal();
  }

  @Override
  public TracksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_track_layout, parent, false);
    return new TracksViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final TracksViewHolder holder, final int position) {

    holder.track = tracks.get(position);

    Picasso
        .with(context)
        .load(makeMapString(tracks.get(position)))
        .fit()
        .centerCrop()
        .placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher)
        .into(holder.mapImage);

    holder.textViewName.setText(user.getName());
    holder.timeName.setText(tracks.get(position).getTime_in_s()+" in s");
    holder.textViewInformation.setText(tracks.get(position).getAverageSpeed_in_kmh()+" in kmh");



    holder.mView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        activity.getSupportFragmentManager()
            .beginTransaction().replace(R.id.content_frame,
            SingleTrackFragment.newInstance(holder.track))
            .addToBackStack("singleTrack")
            .commit();
      }
    });
  }

  @Override
  public int getItemCount() {
    return tracks.size();
  }

  static class TracksViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final ImageView mapImage;
    public final TextView textViewName;
    public final TextView timeName;
    public final TextView textViewInformation;
    public Track track;

    public TracksViewHolder(View view) {
      super(view);
      mView = view;
      mapImage = (ImageView) view.findViewById(R.id.mapview_track);

      LinearLayout nameLayout = (LinearLayout) view.findViewById(R.id.nameLayout_tracks);
      textViewName = (TextView) nameLayout.findViewById(R.id.name_item_track);

      LinearLayout dataLayout = (LinearLayout) view.findViewById(R.id.dataLayout_tracks);

      LinearLayout timeLayout = (LinearLayout) dataLayout.findViewById(R.id.timeLayout_tracks);
      timeName = (TextView) timeLayout.findViewById(R.id.time_item_track);

      LinearLayout speedLayout = (LinearLayout) dataLayout.findViewById(R.id.speedLayout_tracks);
      textViewInformation = (TextView) speedLayout.findViewById(R.id.average_speed_name_item_track);


    }
  }

  /**
   * Sets a new user list for this adapter.
   */
  public void setTracks(List<Track> tracks){
    this.tracks.clear();
    this.tracks.addAll(tracks);
    notifyDataSetChanged();
  }

  private String makeMapString(Track track){

    StringBuilder stringBuilder = new StringBuilder(STATIC_MAP_START_LINK);

    for(Location location: track){
      stringBuilder
          .append(SPLITTER)
          .append(location.getLatitude())
          .append(",")
          .append(location.getLongitude());
    }
    return stringBuilder.toString();
  }

}
