package edu.hm.cs.bikebattle.app.adapter;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.modell.User;

/**
 * Shows for each Element some informations and draws the route into a google map
 */
  public class TrackListFragmentAdapter extends ArrayAdapter<Track> {

  private final List<Track> tracks;
  private final User user;
  private int currentPosition;

    /**
     * Creates a new Adapter for a arraylist of tracks
     * @param context - context
     * @param tracks - list of tracks
     * @param user - current User
     * @param savedInstanceState - saveInstanceState
     * @param manager - manager
     */
  public TrackListFragmentAdapter(Context context, List<Track> tracks, User user,
                                  Bundle savedInstanceState, FragmentManager manager) {
    super(context, -1,tracks);
    this.tracks = tracks;
    this.user = user;
  }
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    currentPosition = position;
    View rowView = LayoutInflater.from(getContext()).inflate(R.layout.item_track_layout,parent,false);
    /**ImageView mapImage = (ImageView)rowView.findViewById(R.id.mapview);

    Picasso
        .with(getContext())
        .load(makeMapString())
        .fit()
        .centerCrop()
        .placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher)
        .into(mapImage);

    TextView textViewName = (TextView) rowView.findViewById(R.id.name_item);
    textViewName.setText(user.getName());


    TextView timeName = (TextView) rowView.findViewById(R.id.time_item);
    timeName.setText(tracks.get(position).getTime_in_s()+" in s");

    TextView textViewInformation = (TextView) rowView.findViewById(R.id.average_speed_name_item);
    textViewInformation.setText(tracks.get(position).getAverageSpeed_in_kmh()+" in kmh");
*/
    return rowView;
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
