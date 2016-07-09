package edu.hm.cs.bikebattle.app.fragments.findRoutes;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.fragments.findRoutes.RoutesListFragment;
import edu.hm.cs.bikebattle.app.fragments.findRoutes.RoutesMapFragment;

/**
 * Fragment adapter for the routes fragment.
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
  private BaseActivity activity;
  /**
   * Fragment in which the map is displayed.
   */
  private RoutesMapFragment mapFragment;
  /**
   * Fragment in which all Routes are displayed.
   */
  private RoutesListFragment listFragment;

  /**
   * Initializes the adapter.
   *
   * @param fragmentManager - FragmentManager.
   * @param activity        - Activity in which the content is displayed.
   */
  public RoutesFragmentPagerAdapter(FragmentManager fragmentManager, BaseActivity activity) {
    super(fragmentManager);
    this.activity = activity;
    listFragment = new RoutesListFragment();
    mapFragment = RoutesMapFragment.newInstance(listFragment);
  }

  @Override
  public int getCount() {
    return PAGE_COUNT;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0: // Fragment # 0 - This will show the mapFragment.
        return mapFragment;
      case 1: // Fragment # 0 - This will show the listFragment.
        return listFragment;
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
