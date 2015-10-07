package com.tagalong;


import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tagalong.fragments.DatePickerFragment;
import com.tagalong.fragments.TimePickerFragment;

import java.util.ArrayList;

public class NewEvent extends AppCompatActivity implements View.OnClickListener,
  DatePickerFragment.OnDateSelectedListener, TimePickerFragment.OnTimeSelectedListener {

  private ArrayList<Friend> friendsList;
  Button btnCreateEvent;
  EditText new_event_name, new_event_location, new_event_invite;
  TagalongDate eventTime;

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
    eventTime = new TagalongDate();

    btnCreateEvent = (Button) findViewById(R.id.btnCreateEvent);
    new_event_invite = (EditText) findViewById(R.id.new_event_invite);
    new_event_name = (EditText) findViewById(R.id.new_event_name);
    new_event_location = (EditText) findViewById(R.id.new_event_location);
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
      case R.id.btnCreateEvent:
        /* TODO: add validation for event form
         *
         */
        int messageResId = 0; //will be used to make toast
        //create the event

        String eventName = new_event_name.getText().toString();
        String eventLocation = new_event_location.getText().toString();
        String eventGuestList = new_event_invite.getText().toString();
        if(eventGuestList == "" || eventName == "" || eventLocation == "" || !isTimeSet()) {
          messageResId = R.string.missing_event_field;
        }
        if(messageResId == 0) {
          Event event = new Event(eventName, eventLocation, eventTime.toString(), eventGuestList);
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
      
  public void showTimePickerDialog(View v) {
    DialogFragment newFragment = new TimePickerFragment();
    newFragment.show(getFragmentManager(), "timePicker");
  }

  public void onTimeSelected(int hours, int minutes) {
    eventTime.setmHour(hours);
    eventTime.setmMinute(minutes);
  }

  public void showDatePickerDialog(View v) {
    DialogFragment newFragment = new DatePickerFragment();
    newFragment.show(getFragmentManager(), "datePicker");
  }

  public void onDateSelected(int year, int month, int day) {
    eventTime.setmYear(year);
    eventTime.setmMonth(month);
    eventTime.setmDay(day);
  }

  //Determines whether the event time has been set
  public boolean isTimeSet() {
    return !(eventTime.getmDay() == 0 || eventTime.getmMonth() == 0 || eventTime.getmYear() == 0 ||
      eventTime.getmHour() == 0 || eventTime.getmMinute() == 0);
  }
}