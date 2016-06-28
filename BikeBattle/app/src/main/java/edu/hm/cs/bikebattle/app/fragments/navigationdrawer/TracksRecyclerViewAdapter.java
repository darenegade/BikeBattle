package edu.hm.cs.bikebattle.app.fragments.navigationdrawer;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.modell.User;


public class TracksRecyclerViewAdapter extends RecyclerView.Adapter<TracksRecyclerViewAdapter.ViewHolder> {

  /** Context to use**/
  private final Context context;

  /**BaseActivity from Parent Activity **/
  private final BaseActivity activity;

  /** List of Tracks **/
  private List<Track> tracks;

  /** Current User**/
  private User user;

  /**Current Position of the view*/
  private int currentPosition;

  public TracksRecyclerViewAdapter(BaseActivity activity, List<Track> tracks, User user) {
    this.activity = activity;
    this.context = activity.getApplicationContext();
    this.tracks = tracks;
    this.user = user;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_track_layout, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {
    this.currentPosition = position;
    holder.mItem = tracks.get(position);

    Picasso
        .with(context)
        .load(makeMapString())
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

      }
    });
  }

  @Override
  public int getItemCount() {
    return tracks.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final ImageView mapImage;
    public final TextView textViewName;
    public final TextView timeName;
    public final TextView textViewInformation;
    public Track mItem;

    public ViewHolder(View view) {
      super(view);
      mView = view;
      mapImage = (ImageView) view.findViewById(R.id.mapview_track);
      textViewName = (TextView) view.findViewById(R.id.name_item_track);
      timeName = (TextView) view.findViewById(R.id.time_item_track);
      textViewInformation = (TextView) view.findViewById(R.id.average_speed_name_item_track);


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

  private String makeMapString(){
    String mapString = "http://maps.googleapis.com/maps/api/staticmap?" +
        "size=500x400" +
        "&key=AIzaSyD9-iXLN4t282Q2EW22NCVTUTev4okhZYE" +
        "&path=color:0x0000ff%7Cweight:5";
    String splitter = "%7C";

    for(int i = 0 ; i < tracks.get(currentPosition).size();i++){
      Location location = tracks.get(currentPosition).get(i);
      mapString = mapString+splitter;
      mapString= mapString+location.getLatitude()+","+location.getLongitude();
    }
    return  mapString;
  }

}
