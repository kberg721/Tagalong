package com.tagalong;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Chip on 9/7/2015.
 */
public class TagalongPagerAdapter extends FragmentStatePagerAdapter {

  TabsList tabsList;

  public TagalongPagerAdapter(FragmentManager fragmentManager) {
    super(fragmentManager);
    tabsList = new TabsList();
  }

  @Override
  public Fragment getItem(int i) { return tabsList.constructFromPosition(i); }

  @Override
  public int getCount() {
    return tabsList.getTabNames().length;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return tabsList.getTabNames()[position];
  }
}
