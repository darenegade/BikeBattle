package edu.hm.cs.bikebattle.app;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lukas on 12.04.2016.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
  final int PAGE_COUNT = 2;
  private RoutesActivity activity;

  public MyFragmentPagerAdapter(FragmentManager fragmentManager, RoutesActivity activity) {
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
        return new RoutesMapFragment(activity);
      case 1: // Fragment # 0 - This will show FirstFragment different title
        return new RoutesListFragment();
      default:
        return null;
    }
  }

  // Returns the page title for the top indicator
  @Override
  public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0:
        return "Map";
      case 1:
        return "List";
      default:
        return null;
    }
  }
}
