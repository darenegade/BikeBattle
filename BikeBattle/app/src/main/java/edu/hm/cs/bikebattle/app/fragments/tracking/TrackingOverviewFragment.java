package edu.hm.cs.bikebattle.app.fragments.tracking;


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
import edu.hm.cs.bikebattle.app.activities.TrackingActivity;
import edu.hm.cs.bikebattle.app.adapter.TrackingFragmentPagerAdapter;


/**
 * Fragment to display the navigation tabs.
 *
 * @author Lukas Brauckmann
 */
public class TrackingOverviewFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.fragment_tab_overview, container, false);

    try {
      TrackingActivity activity = (TrackingActivity) getActivity();

      // Get the ViewPager and set it's PagerAdapter so that it can display items
      ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
      viewPager.setAdapter(
          new TrackingFragmentPagerAdapter(activity.getSupportFragmentManager(), activity));

      // Give the TabLayout the ViewPager
      TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
      tabLayout.setupWithViewPager(viewPager);
    } catch (Exception exception) {
      Log.e("Error!!", exception.getMessage());
    }

    return view;
  }
}