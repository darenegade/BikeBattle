package edu.hm.cs.bikebattle.app.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.TrackingActivity;

/**
 * Fragment adapter for the tracking activity.
 *
 * @author Lukas Brauckmann
 */
public class TrackingFragmentPagerAdapter extends FragmentPagerAdapter {
  /**
   * Number of tabs.
   */
  private static final int PAGE_COUNT = 2;
  /**
   * Activity in which the content is displayed.
   */
  private TrackingActivity activity;

  /**
   * Initialize the adapter.
   *
   * @param fragmentManager FragmentManager.
   * @param activity        Activity in which the content is displayed.
   */
  public TrackingFragmentPagerAdapter(FragmentManager fragmentManager, TrackingActivity activity) {
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
        return activity.getInformationFragment();
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
        return "Track information";
      default:
        return null;
    }
  }
}
