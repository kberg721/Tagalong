package com.tagalong;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.tagalong.fragments.DatePickerFragment;
import com.tagalong.fragments.TimePickerFragment;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class NewEvent extends AppCompatActivity implements View.OnClickListener,
  DatePickerFragment.OnDateSelectedListener, TimePickerFragment.OnTimeSelectedListener {

  private ArrayList<Friend> friendsList;

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
      case R.id.btnLogin:
        break;
      default:
        break;
    }
  }

  public void showTimePickerDialog(View v) {
    DialogFragment newFragment = new TimePickerFragment();
    newFragment.show(getFragmentManager(),"timePicker");
  }

  public GregorianCalendar onTimeSelected(int hours, int minutes) {
    GregorianCalendar timeSelected = new GregorianCalendar(0, 0, 0, hours, minutes);
    Log.d("time created", timeSelected.toString());
    return timeSelected;
  }

  public void showDatePickerDialog(View v) {
    DialogFragment newFragment = new DatePickerFragment();
    newFragment.show(getFragmentManager(), "datePicker");
  }

  public GregorianCalendar onDateSelected(int year, int month, int day) {
    GregorianCalendar dateSelected = new GregorianCalendar(year, month, day);
    Log.d("date created", dateSelected.toString());
    return dateSelected;
  }

}