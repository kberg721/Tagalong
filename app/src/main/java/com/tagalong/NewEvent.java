package com.tagalong;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.tagalong.fragments.DatePickerFragment;
import com.tagalong.fragments.TimePickerFragment;

import java.util.ArrayList;

public class NewEvent extends AppCompatActivity implements View.OnClickListener,
  DatePickerFragment.OnDateSelectedListener, TimePickerFragment.OnTimeSelectedListener,
  GoogleApiClient.OnConnectionFailedListener {

  private static final String TAG = "Tagalong";
  private EditText new_event_name;
  private TagalongDate eventTime;
  private TextView eventDate;
  private Button submitNewEvent;

  //Used for Event Guest List
  private ArrayList<Friend> friendsList;
  private DropdownListAdapter dropdownListAdapter;

  //Used for Event Location
  private PlaceAutocompleteFragment mAutoCompleteFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_event);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    //getSupportActionBar().setDisplayUseLogoEnabled(true);
    getSupportActionBar().setIcon(R.drawable.tagalong_icon_small);

    //Event Guest List Initializations
    Intent currentIntent = getIntent();
    friendsList = (ArrayList<Friend>) currentIntent.getSerializableExtra("friendsList");
    System.out.println("friend list: " + friendsList);

    eventTime = new TagalongDate();
    mAutoCompleteFragment = (PlaceAutocompleteFragment)
      getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
    new_event_name = (EditText) findViewById(R.id.new_event_name);

    //onClickListener to initiate the dropDown list
    Button inviteButton = (Button)findViewById(R.id.inviteButton);
    inviteButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        initiatePopUp(friendsList);
      }
    });
    //Event Location
    mAutoCompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
        @Override
        public void onPlaceSelected(Place place) { // Handle the selected Place
        }
        @Override
        public void onError(Status status) { // Handle the error
        }
    });

    eventTime = new TagalongDate();
    eventDate = (TextView)findViewById(R.id.eventDate);
    new_event_name = (EditText) findViewById(R.id.new_event_name);
    submitNewEvent = (Button) findViewById(R.id.submitNewEvent);
    submitNewEvent.setOnClickListener(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_new_event, menu);
    return true;
  }

  /* This section deals with the code that will initialize the drop down menu
     containing all of the user's friends.
   */
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

  /*  The following code supports the Time and Date widgets
      to enable the user to set the event time.
   */
  public void showTimePickerDialog(View v) {
    DialogFragment newFragment = new TimePickerFragment();
    newFragment.show(getFragmentManager(), "timePicker");
    showDatePickerDialog(v);
  }

  public void onTimeSelected(int hours, int minutes) {
    eventTime.setmHour(hours);
    eventTime.setmMinute(minutes);
    setEventTimeText();
  }

  public void showDatePickerDialog(View v) {
    DialogFragment newFragment = new DatePickerFragment();
    newFragment.show(getFragmentManager(), "datePicker");
  }

  public void onDateSelected(int year, int month, int day) {
    eventTime.setmYear(year);
    eventTime.setmMonth(month + 1);
    eventTime.setmDay(day);
  }

  public void setEventTimeText() {
    int notMilitaryTime = eventTime.getmHour() % 12;
    String amOrPm = (notMilitaryTime != 0) ? "pm" : "am";
    if (notMilitaryTime == 0){notMilitaryTime = 12;}
    eventDate.setText("Time: " + notMilitaryTime + ":" + eventTime.getmMinute() +
      amOrPm + " on " + eventTime.getmMonth() + "/" + eventTime.getmDay() +
      "/" + eventTime.getmYear());
  }

  public void clearEventTimeText() {
    eventDate.setText("Time");
  }

  //Determines whether the event time has been set
  public boolean isEventTimeSet() {
    return !(eventTime.getmDay() == 0 || eventTime.getmMonth() == 0 || eventTime.getmYear() == 0 ||
      eventTime.getmHour() == 0 || eventTime.getmMinute() == 0);
  }

  /* The following methods and declarations support the Event Location functionality,
     making calls to the Google Places API with the use of a PlaceAutoCompleteAdapter
   */
  @Override
  protected void onStart() {
    super.onStart();
    //mGoogleApiClient.connect();
  }

  @Override
  protected void onStop() {
    //mGoogleApiClient.disconnect();
    super.onStop();
  }


  private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
    = new ResultCallback<PlaceBuffer>() {
    @Override
    public void onResult(PlaceBuffer places) {
      if (!places.getStatus().isSuccess()) {
        // Request did not complete successfully
        Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
        places.release();
        return;
      }
      // Get the Place object from the buffer.
      final Place place = places.get(0);

      Log.i(TAG, "Place details received: " + place.getName());

      places.release();
    }
  };

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {

    Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
      + connectionResult.getErrorCode());

    Toast.makeText(this,
      "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
      Toast.LENGTH_SHORT).show();
  }

  /* the onClick listener will validate all of the event data fields
   * and then will pass on the data to the submitEvent method
   */
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
        //String eventLocation = mAutocompleteView.getText().toString();
        if(invitedFriends.size() == 0 || eventName == ""
          //|| eventLocation == ""
          || !isEventTimeSet()) {
          messageResId = R.string.missing_event_field;
        }
        if(messageResId == 0) {
          /*TODO: Parse invitedFriends into eventGuestList
           *TODO: get email to friends class
           */
          Intent currentIntent = this.getIntent();
          String hostEmail = currentIntent.getStringExtra("currentUserEmail");
          int hostEventCounter = currentIntent.getIntExtra("currentUserEventCount", 0);
          Event event = new Event(eventName, null, eventTime.toString(), "", hostEmail, hostEventCounter);
          submitEvent(event);
        } else {
          Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        }
        break;
      default:
        break;
    }
  }

  //submitEvent will send the event object to the server and store it.
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