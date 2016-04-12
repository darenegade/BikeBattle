package edu.hm.cs.bikebattle.app;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by lukas on 12.04.2016.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
  final int PAGE_COUNT = 3;
  private String tabTitles[] = new String[] {"Map", "List"};
  private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
  private Context context;

  public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
    super(fm);
    this.context = context;
    //fragments.add(new RoutesMapFragment());
    //fragments.add(new RoutesListFragment());
  }

  @Override
  public int getCount() {
    return PAGE_COUNT;
  }

  @Override
  public Fragment getItem(int position) {
    return fragments.get(position);
  }

  @Override
  public CharSequence getPageTitle(int position) {
    // Generate title based on item position
    return tabTitles[position];
  }
}
