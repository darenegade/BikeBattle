package edu.hm.cs.bikebattle.app.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.RoutesActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class RoutesOverviewFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.fragment_routes_overview, container, false);

    try {
      RoutesActivity activity = (RoutesActivity) getActivity();

      // Get the ViewPager and set it's PagerAdapter so that it can display items
      ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
      viewPager.setAdapter(new RoutesFragmentPagerAdapter(activity.getSupportFragmentManager(), activity));

      // Give the TabLayout the ViewPager
      TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
      tabLayout.setupWithViewPager(viewPager);
    }catch(Exception e){
      Log.e("Error!!",e.getMessage());
    }

    return view;
  }

}