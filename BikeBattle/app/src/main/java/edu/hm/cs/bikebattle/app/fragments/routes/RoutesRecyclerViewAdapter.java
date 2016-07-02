package edu.hm.cs.bikebattle.app.fragments.routes;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import edu.hm.cs.bikebattle.app.modell.LocationList;
import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for Routes Fragment.
 *
 * @author René Zarwel
 */
public class RoutesRecyclerViewAdapter extends RecyclerView.Adapter<RoutesRecyclerViewAdapter.RoutesViewHolder> {

  private final static String STATIC_MAP_START_LINK = "http://maps.googleapis.com/maps/api/staticmap?" +
      "size=500x200" +
      "&key=AIzaSyD9-iXLN4t282Q2EW22NCVTUTev4okhZYE" +
      "&path=color:0x0000ff%7Cweight:5";
  private final static String SPLITTER = "%7C";

  /** Context to use**/
  private final BaseActivity activity;

  /** List of Routes **/
  private List<Route> routes = new ArrayList<Route>();

  /** Current User**/
  private User user;

  public RoutesRecyclerViewAdapter(BaseActivity activity) {
    this.activity = activity;
    user = activity.getPrincipal();
  }

  @Override
  public RoutesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_route_layout, parent, false);
    return new RoutesViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final RoutesViewHolder holder, final int position) {

    Log.d("Routes", routes.get(position).toString());

    holder.route = routes.get(position);

    Picasso
        .with(activity.getApplicationContext())
        .load(makeMapString(routes.get(position)))
        .fit()
        .centerCrop()
        .placeholder(R.drawable.worldmap)
        .error(R.drawable.worldmap)
        .into(holder.mapImage);

    holder.userNameView.setText(user.getName());
    holder.routeNameView.setText(routes.get(position).getName());
    holder.lengthView.setText(routes.get(position).getDistanceInM() + "m");
    holder.typeView.setText(routes.get(position).getRoutetyp().toString());
    holder.difficultyView.setText(routes.get(position).getDifficulty().toString());


    holder.mView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        activity.getSupportFragmentManager()
            .beginTransaction().replace(R.id.content_frame,
            SingleRouteFragment.newInstance(holder.route))
            .addToBackStack("singleRoute")
            .commit();
      }
    });
  }

  @Override
  public int getItemCount() {
    return routes.size();
  }

  static class RoutesViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final ImageView mapImage;
    public final TextView userNameView;
    public final TextView routeNameView;
    public final TextView lengthView;
    public final TextView typeView;
    public final TextView difficultyView;
    public Route route;

    public RoutesViewHolder(View view) {
      super(view);
      mView = view;
      mapImage = (ImageView) view.findViewById(R.id.mapview_routes);

      LinearLayout nameLayout = (LinearLayout) view.findViewById(R.id.nameLayout_routes);
      userNameView = (TextView) nameLayout.findViewById(R.id.name_item_routes);
      routeNameView = (TextView) nameLayout.findViewById(R.id.route_name_item_routes);

      LinearLayout dataLayout = (LinearLayout) view.findViewById(R.id.dataLayout_routes);

      LinearLayout lengthLayout = (LinearLayout) dataLayout.findViewById(R.id.lengthLayout_routes);
      lengthView = (TextView) lengthLayout.findViewById(R.id.length_text_routes);

      LinearLayout typeLayout = (LinearLayout) dataLayout.findViewById(R.id.typeLayout_routes);
      typeView = (TextView) typeLayout.findViewById(R.id.route_type_name_item_routes);

      LinearLayout difficultyLayout = (LinearLayout) dataLayout.findViewById(R.id.difficultyLayout_routes);
      difficultyView = (TextView) difficultyLayout.findViewById(R.id.difficult_item_routes);

    }
  }

  /**
   * Sets a new user list for this adapter.
   */
  public void setRoutes(List<Route> routes){
    this.routes.clear();
    this.routes.addAll(routes);
    notifyDataSetChanged();
  }

  private String makeMapString(LocationList locationList){

    StringBuilder stringBuilder = new StringBuilder(STATIC_MAP_START_LINK);

    for(Location location: locationList){
      stringBuilder
          .append(SPLITTER)
          .append(location.getLatitude())
          .append(",")
          .append(location.getLongitude());
    }
    return stringBuilder.toString();
  }

}
