package edu.hm.cs.bikebattle.app;

import android.os.Bundle;

import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.OverlayItem;

/**
 * Created by lukas on 26.03.2016.
 */
public class OnMarkerClickListener<T extends OverlayItem> implements OnItemGestureListener<T> {
    OSMActivity activity;

    public OnMarkerClickListener(OSMActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onItemSingleTapUp(int i, T t) {
        OSMRoute route=activity.getRouteByMarker(t);
        Bundle bundle = new Bundle();
        bundle.putString("name",route.getName());
        bundle.putDouble("length",route.getLength());
        new RouteInfoDialog(route).show(activity.getFragmentManager(),"Route information");
        return false;
    }

    @Override
    public boolean onItemLongPress(int i, T t) {
        return false;
    }
}
