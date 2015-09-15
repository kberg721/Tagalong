package com.tagalong;

import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class Mainpage extends AppCompatActivity {

  TagalongPagerAdapter pagerAdapter;
  ViewPager viewPager;
  private static final TabsList tabsList = new TabsList();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mainpage);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    //getSupportActionBar().setDisplayUseLogoEnabled(true);
    getSupportActionBar().setIcon(R.drawable.tagalong_icon_small);
    TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

    // Set the tab name for each tab
    setupTabNames(tabLayout);


    pagerAdapter = new TagalongPagerAdapter(getSupportFragmentManager());
    viewPager = (ViewPager) findViewById(R.id.pager);
    viewPager.setAdapter(pagerAdapter);
    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {

      }
    });
  }

  private void setupTabNames(TabLayout tabLayout) {
    String[] tabNames = tabsList.getTabNames();
    int i;
    for(i = 0; i < tabNames.length; i++) {
      tabLayout.addTab(tabLayout.newTab().setText(tabNames[i]));
    }
    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_mainpage, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
