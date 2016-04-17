package edu.hm.cs.bikebattle.app.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.RoutesActivity;

/**
 * Created by lukas on 12.04.2016.
 */
public class RoutesFragmentPagerAdapter extends FragmentPagerAdapter {
  final int PAGE_COUNT = 2;
  private RoutesActivity activity;

  public RoutesFragmentPagerAdapter(FragmentManager fragmentManager, RoutesActivity activity) {
    super(fragmentManager);
    this.activity = activity;
  }

  // Returns total number of pages
  @Override
  public int getCount() {
    return PAGE_COUNT;
  }

  // Returns the fragment to display for that page
  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0: // Fragment # 0 - This will show FirstFragment
        Fragment fragment = new RoutesMapFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("RoutesActivity", activity);
        fragment.setArguments(bundle);
        return fragment;
      case 1: // Fragment # 0 - This will show FirstFragment different title
        fragment = new RoutesListFragment();
        bundle = new Bundle();
        bundle.putSerializable("RoutesActivity", activity);
        fragment.setArguments(bundle);
        return fragment;
      default:
        return null;
    }
  }

  // Returns the page title for the top indicator
  @Override
  public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0:
        return activity.getString(R.string.map);
      case 1:
        return activity.getString(R.string.list);
      default:
        return null;
    }
  }
}
