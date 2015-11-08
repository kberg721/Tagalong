package com.tagalong;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
  DropdownListAdapter dropdownListAdapter;

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

    btnCreateEvent = (Button) findViewById(R.id.submitNewEvent);
    new_event_name = (EditText) findViewById(R.id.new_event_name);
    new_event_location = (EditText) findViewById(R.id.new_event_location);

    //onClickListener to initiate the dropDown list
    Button inviteButton = (Button)findViewById(R.id.inviteButton);
    inviteButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        initiatePopUp(friendsList);
      }
    });
  }

  private void initiatePopUp(ArrayList<Friend> friendsList) {
    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context
      .LAYOUT_INFLATER_SERVICE);
    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.popup_window,
      (ViewGroup) findViewById(R.id.popupView));

    final PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams
        .WRAP_CONTENT, true);

    //background cannot be null if we want the touch event to be active outside the pop-up window
    pw.setBackgroundDrawable(new BitmapDrawable());
    pw.setTouchable(true);
    //inform pop-up the touch event outside its window
    pw.setOutsideTouchable(true);
    //the pop-up will be dismissed if touch event occurs anywhere outside its window
    pw.setTouchInterceptor(new View.OnTouchListener() {
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
          pw.dismiss();
          return true;
        }
        return false;
      }
    });

    pw.setContentView(layout);
    final RelativeLayout inviteLayout = (RelativeLayout) findViewById(R.id.inviteLayout);

    pw.showAsDropDown(inviteLayout);


    final ListView list = (ListView) layout.findViewById(R.id.dropdownList);
    TextView selectedValues = (TextView) findViewById(R.id.selectedValues);

    dropdownListAdapter = new DropdownListAdapter(this, friendsList, selectedValues);
    //’items’ is the values’ list and ‘selectedValues’ is the textview where the selected values
    // are displayed
    list.setAdapter(dropdownListAdapter);

  }

    @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_new_event, menu);
    return true;
  }

  public void submitEvent() {
    // TODO: extract other fields from the form
    String eventName = null;
    String eventLocation = null;
    String eventTime = null;
    ArrayList<Friend> invitedFriends = dropdownListAdapter.getSelectedFriends();
  }

  @Override
  public void onClick(View v) {
    switch(v.getId()) {
      case R.id.submitNewEvent:
        /* TODO: add validation for event form
         *
         */
        int messageResId = 0; //will be used to make toast
        ArrayList<Friend> invitedFriends = dropdownListAdapter.getSelectedFriends();
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