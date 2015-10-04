package com.tagalong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class NewEvent extends AppCompatActivity implements View.OnClickListener {

  private ArrayList<Friend> friendsList;
  Button btnCreateEvent;
  EditText new_event_name, new_event_time, new_event_location, new_event_invite;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_event);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    //getSupportActionBar().setDisplayUseLogoEnabled(true);
    getSupportActionBar().setIcon(R.drawable.tagalong_icon_small);
    Intent currentIntent = getIntent();
    friendsList = (ArrayList<Friend>) currentIntent.getSerializableExtra("friendsList");
    System.out.println("friend list: " + friendsList);

    btnCreateEvent = (Button) findViewById(R.id.btnCreateEvent);
    new_event_invite = (EditText) findViewById(R.id.new_event_invite);
    new_event_name = (EditText) findViewById(R.id.new_event_name);
    new_event_location = (EditText) findViewById(R.id.new_event_location);
    new_event_time = (EditText) findViewById(R.id.new_event_time);
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
      case R.id.link_to_register:
        break;
      case R.id.btnCreateEvent:
        /* TODO: add validation for event form
         *
         */
        int messageResId = 0; //will be used to make toast
        String eventName = new_event_name.getText().toString();
        String eventTime = new_event_time.getText().toString();
        String eventLocation = new_event_location.getText().toString();
        String eventGuestList = new_event_invite.getText().toString();
        if(eventGuestList == "" || eventTime == "" || eventName == "" || eventLocation == "") {
          messageResId = R.string.missing_event_field;
        }
        if(messageResId == 0) {
          Event event = new Event(eventName, eventLocation, eventTime, eventGuestList);
          submitEvent(event);
        } else {
          Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        }
        break;
      default:
        break;
    }
  }

  private void submitEvent(Event event) {
    ServerRequests serverRequest = new ServerRequests(this);
    serverRequest.storeEventDataInBackground(event, new GetEventCallback() {
      @Override
      public void done(Event returnedEvent) {
        finish();
      }
    });
  }
}