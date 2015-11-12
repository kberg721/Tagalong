package com.tagalong;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import java.util.ArrayList;


public class Mainpage extends AppCompatActivity {

  TagalongPagerAdapter pagerAdapter;
  ViewPager viewPager;
  UserLocalStore userLocalStore;
  private static final TabsList tabsList = new TabsList();
  ArrayList<Friend> friendsList;
  LoginManager facebookLogin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FacebookSdk.sdkInitialize(getApplicationContext());
    facebookLogin = LoginManager.getInstance();
    setContentView(R.layout.activity_mainpage);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    //getSupportActionBar().setDisplayUseLogoEnabled(true);
    getSupportActionBar().setIcon(R.drawable.tagalong_icon_small);
    TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

    // Get the friends list from login
    Intent currentIntent = getIntent();
    friendsList = (ArrayList<Friend>) currentIntent.getSerializableExtra("friendsList");

    userLocalStore = new UserLocalStore(this);

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

  @Override
  protected void onStart() {
    super.onStart();
    authenticate();
  }

  private boolean authenticate() {
    AccessToken loggedIntoFB = AccessToken.getCurrentAccessToken();
    if (userLocalStore.getLoggedInUser().getFullName().equals("") && loggedIntoFB == null) {
      Intent intent = new Intent(this, LoginActivity.class);
      startActivity(intent);
      return false;
    }
    if (friendsList == null && loggedIntoFB != null) {
      // Get friends list
      new GraphRequest(
        loggedIntoFB,
        "/me/friends",
        null,
        HttpMethod.GET,
        new GraphRequest.Callback() {
          public void onCompleted(GraphResponse response) {
            try {
              friendsList = new FriendsArrayList(
                response.getJSONObject().getJSONArray("data")
              ).getFriendObjects();
            } catch (org.json.JSONException e) {
              System.out.println("Exception: " + e);
            }
          }
        }
      ).executeAsync();
    }
    return true;
  }

  private void setupTabNames(TabLayout tabLayout) {
    String[] tabNames = tabsList.getTabNames();
    int i;
    for (i = 0; i < tabNames.length; i++) {
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
      return super.onOptionsItemSelected(item);
    } else if (id == R.id.action_logout) {
      AccessToken usedFacebook = AccessToken.getCurrentAccessToken();
      if(usedFacebook != null) {
        facebookLogin.logOut();
      } else {
        userLocalStore.clearUserData();
        userLocalStore.setUserLoggedIn(false);
      }
      Intent loginIntent = new Intent(this, LoginActivity.class);
      startActivity(loginIntent);
      return true;
    } else if (id == R.id.action_newActivity) {
      Intent newEventIntent = new Intent(this, NewEvent.class);
      newEventIntent.putExtra("currentUserEmail", userLocalStore.getLoggedInUser().getEmail());
      newEventIntent.putExtra("currentUserEventCount", userLocalStore.getLoggedInUser().getEventCounter());
      newEventIntent.putExtra("friendsList", this.friendsList);
      startActivity(newEventIntent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
