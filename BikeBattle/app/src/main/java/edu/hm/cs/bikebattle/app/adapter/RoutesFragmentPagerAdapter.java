package edu.hm.cs.bikebattle.app.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.RoutesActivity;
import edu.hm.cs.bikebattle.app.fragments.routes.RoutesListFragment;

/**
 * Fragment adapter for the routes activity.
 *
 * @author Lukas Brauckmann
 */
public class RoutesFragmentPagerAdapter extends FragmentPagerAdapter {
  /**
   * Number of tabs.
   */
  private static final int PAGE_COUNT = 2;
  /**
   * Activity in which the content is displayed.
   */
  private RoutesActivity activity;

  /**
   * Initialize the adapter.
   *
   * @param fragmentManager FragmentManager.
   * @param activity        Activity in which the content is displayed.
   */
  public RoutesFragmentPagerAdapter(FragmentManager fragmentManager, RoutesActivity activity) {
    super(fragmentManager);
    this.activity = activity;
  }

  @Override
  public int getCount() {
    return PAGE_COUNT;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0: // Fragment # 0 - This will show FirstFragment
        return activity.getMapFragment();
      case 1: // Fragment # 0 - This will show FirstFragment different title
        return new RoutesListFragment();
      default:
        return null;
    }
  }

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
