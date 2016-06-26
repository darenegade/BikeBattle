package edu.hm.cs.bikebattle.app.adapter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.fragments.GoogleMapHelper;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.modell.User;

  public class TrackListFragmentAdapter extends ArrayAdapter<Track> implements OnMapReadyCallback {

  private final List<Track> tracks;
  private final User user;
  private GoogleMap googleMap;
  private Bundle savedInstanceState;
  private  int currentPosition;
  private FragmentManager manager;

  public TrackListFragmentAdapter(Context context, List<Track> tracks, User user,
                                  Bundle savedInstanceState, FragmentManager manager) {
    super(context, -1,tracks);
    this.tracks = tracks;
    this.manager = manager;
    this.savedInstanceState = savedInstanceState;
    this.user = user;
  }
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    currentPosition = position;
    View rowView = LayoutInflater.from(getContext()).inflate(R.layout.item_track_layout,parent,false);

    MapView mapView;
    mapView = (MapView) rowView.findViewById(R.id.mapview);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);

    TextView textViewName = (TextView) rowView.findViewById(R.id.name_item);
    textViewName.setText(user.getName());


    TextView timeName = (TextView) rowView.findViewById(R.id.time_item);
    timeName.setText(tracks.get(position).getTime_in_s()+" in s");

    TextView textViewInformation = (TextView) rowView.findViewById(R.id.average_speed_name_item);
    textViewInformation.setText(tracks.get(position).getAverageSpeed_in_kmh()+" in kmh");

    return rowView;
  }


  @Override
  public void onMapReady(GoogleMap googleMap) {
    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      this.googleMap = googleMap;
      this.googleMap.setMyLocationEnabled(false);
      this.googleMap.getUiSettings().setMapToolbarEnabled(false);

      GoogleMapHelper.drawLocationList(tracks.get(currentPosition), Color.RED, googleMap);
      GoogleMapHelper.zoomToTrack(googleMap, tracks.get(currentPosition));
    }
  }
}
