package com.tagalong;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

public class NewEvent extends AppCompatActivity implements View.OnClickListener {

  private MultiAutoCompleteTextView multiAutoComplete;
  private ArrayAdapter<String> adapter;
  private ArrayList<Friend> friendsList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_event);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setIcon(R.drawable.tagalong_icon_small);

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

  private void setupMultiSelect() {
    Intent currentIntent = getIntent();
    friendsList = (ArrayList<Friend>) currentIntent.getSerializableExtra("friendsList");
    System.out.println("friend list: " + friendsList);

    // get the defined string-array
    String[] colors = getResources().getStringArray(R.array.colorList);
    adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,colors);
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
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                              long arg3) {
        Toast.makeText(getBaseContext(), "MultiAutoComplete: " +
          "you add color "+arg0.getItemAtPosition(arg2),
          Toast.LENGTH_LONG).show();
      }
    });

  }
}