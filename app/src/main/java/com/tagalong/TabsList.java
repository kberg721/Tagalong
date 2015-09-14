package com.tagalong;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.tagalong.fragments.*;

/**
 * Created by Chip on 9/1/2015.
 */
public class TabsList {
  private static final String[] TAB_NAME_STRINGS
    = new String[] {"All", "Owned", "Going", "Invited"};
  private static final Class[] TAB_CLASSES
    = new Class[] {
    AllTabFragment.class, OwnedTabFragment.class,
    GoingTabFragment.class, InvitedTabFragment.class};

  public String[] getTabNames() {
    return TAB_NAME_STRINGS;
  }

  public Class[] getTabClasses() {
    return TAB_CLASSES;
  }

  public Fragment constructFromId(String id){
    int matchIndex = -1;
    String currentId;
    for(int i=0; i<TAB_NAME_STRINGS.length; i++) {
      currentId = TAB_NAME_STRINGS[i].toLowerCase();
      if (id.equals(currentId)) {
        matchIndex = i;
        i = TAB_NAME_STRINGS.length;
      }
    }
    if (matchIndex == -1) {
      throw new IllegalArgumentException("Invalid tab id");
    } else {
      try {
        return (Fragment) TAB_CLASSES[matchIndex].getConstructors()[0].newInstance();
      } catch (Exception e) {
        Log.d("TabsList", "Failed to create new fragment", e);
        return null;
      }
    }
  }

  public Fragment constructFromPosition(int pos) {
    if (pos < 0 || pos >= TAB_CLASSES.length) {
      throw new IllegalArgumentException("Invalid tab position");
    } else {
      try {
        return (Fragment) TAB_CLASSES[pos].getConstructors()[0].newInstance();
      } catch (Exception e) {
        Log.d("TabsList", "Failed to create new fragment", e);
        return null;
      }
    }
  }
}
