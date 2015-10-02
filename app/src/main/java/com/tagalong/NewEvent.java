package com.tagalong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class NewEvent extends AppCompatActivity implements View.OnClickListener {

  private MultiAutoCompleteTextView multiAutoComplete;
  private ArrayAdapter<Friend> adapter;
  private ArrayList<Friend> friendsList;
  private HashSet<String> addedUserIds;
  HashMap<String, Friend> friendsMap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_event);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setIcon(R.drawable.tagalong_icon_small);
    addedUserIds = new HashSet<String>();
    this.setupMultiSelect();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_new_event, menu);
    return true;
  }

  @Override
  public void onClick(View v) {
    switch(v.getId()) {
      case R.id.submitNewActivity:
        break;
      default:
        break;
    }
  }

  public Context getContext() {
    return (Context) this;
  }

  private HashMap<String, Friend> friendListToMap(ArrayList<Friend> friendsList) {
    HashMap<String, Friend> friendMap = new HashMap<String, Friend>();
    for(Friend friend : friendsList) {
      friendMap.put(friend.getId(), friend);
    }
    return friendMap;
  }

  private void setupMultiSelect() {
    Intent currentIntent = getIntent();
    friendsList = (ArrayList<Friend>) currentIntent.getSerializableExtra("friendsList");
    friendsMap = friendListToMap(friendsList);

    // get the defined string-array
    adapter = new ArrayAdapter<Friend>(this,android.R.layout.simple_list_item_1, friendsList);
    multiAutoComplete = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoComplete);
    // set adapter for the auto complete fields
    multiAutoComplete.setAdapter(adapter);
    // specify the minimum type of characters before drop-down list is shown
    multiAutoComplete.setThreshold(2);
    // comma to separate the different colors
    multiAutoComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    // when the user clicks an item of the drop-down list
    multiAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int friendPos,
                              long id) {
        Friend selectedFriend = (Friend) adapterView.getItemAtPosition(friendPos);
        addedUserIds.add(selectedFriend.getId());
        friendsList.remove(friendsMap.get(selectedFriend.getId()));
        adapter = new ArrayAdapter<Friend>(NewEvent.this, android.R.layout.simple_list_item_1, friendsList);
        multiAutoComplete.setAdapter(adapter);
        Toast.makeText(getBaseContext(), "MultiAutoComplete: " +
          "you added user " + selectedFriend.getName(),
          Toast.LENGTH_LONG).show();
      }
    });

  }
}