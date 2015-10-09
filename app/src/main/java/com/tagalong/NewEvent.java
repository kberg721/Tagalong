package com.tagalong;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tagalong.fragments.DatePickerFragment;
import com.tagalong.fragments.TimePickerFragment;

import java.util.ArrayList;

public class NewEvent extends AppCompatActivity implements View.OnClickListener,
  DatePickerFragment.OnDateSelectedListener, TimePickerFragment.OnTimeSelectedListener,
  GoogleApiClient.OnConnectionFailedListener {

  private ArrayList<Friend> friendsList;
  Button submitNewEvent;
  EditText new_event_name, new_event_location, new_event_invite;
  TagalongDate eventTime;
  DropdownListAdapter dropdownListAdapter;
  private AutoCompleteTextView mAutocompleteView;
  private GoogleApiClient mGoogleApiClient;
  private PlaceAutoCompleteAdapter mAdapter;
  private TextView mPlaceDetailsText;
  private TextView mPlaceDetailsAttribution;
  private static final String TAG = "TagalongPlaces";
  private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
    new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

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

    mAutocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete_places);
    mGoogleApiClient = new GoogleApiClient
      .Builder(this)
      .addApi(Places.GEO_DATA_API)
      .addOnConnectionFailedListener(this)
      .build();

    mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
    mPlaceDetailsAttribution = (TextView) findViewById(R.id.place_attribution);

    mAdapter = new PlaceAutoCompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
      null);
    mAutocompleteView.setAdapter(mAdapter);

    submitNewEvent = (Button) findViewById(R.id.submitNewEvent);
    new_event_name = (EditText) findViewById(R.id.new_event_name);

    //onClickListener to initiate the dropDown list
    Button inviteButton = (Button)findViewById(R.id.inviteButton);
    inviteButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        initiatePopUp(friendsList);
      }
    });

    mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
    submitNewEvent.setOnClickListener(this);
  }

  @Override
  protected void onStart() {
    super.onStart();
    mGoogleApiClient.connect();
  }

  @Override
  protected void onStop() {
    mGoogleApiClient.disconnect();
    super.onStop();
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
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
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
        String eventGuestList = new_event_invite.getText().toString();
        if(invitedFriends.size() == 0 || eventName == "" || !isTimeSet()) {
          messageResId = R.string.missing_event_field;
        }
        if(messageResId == 0) {
          /*TODO: Parse invitedFriends into eventGuestList
           */
          Event event = new Event(eventName, "", eventTime.toString(), eventGuestList);
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

  private AdapterView.OnItemClickListener mAutocompleteClickListener
    = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
      final AutocompletePrediction item = mAdapter.getItem(position);
      final String placeId = item.getPlaceId();
      final CharSequence primaryText = item.getPrimaryText(null);

      Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
      PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
        .getPlaceById(mGoogleApiClient, placeId);
      placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

      Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
        Toast.LENGTH_SHORT).show();
      Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
    }
  };

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

      // Format details of the place for display and show it in a TextView.
      mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
        place.getId(), place.getAddress(), place.getPhoneNumber(),
        place.getWebsiteUri()));

      // Display the third party attributions if set.
      final CharSequence thirdPartyAttribution = places.getAttributions();
      if (thirdPartyAttribution == null) {
        mPlaceDetailsAttribution.setVisibility(View.GONE);
      } else {
        mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
        mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
      }

      Log.i(TAG, "Place details received: " + place.getName());

      places.release();
    }
  };

  private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                            CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
    Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
      websiteUri));
    return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
      websiteUri));

  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {

    Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
      + connectionResult.getErrorCode());

    Toast.makeText(this,
      "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
      Toast.LENGTH_SHORT).show();
  }
}