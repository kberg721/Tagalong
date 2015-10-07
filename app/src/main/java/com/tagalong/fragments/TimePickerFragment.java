package com.tagalong.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Kberg721 on 10/6/15.
 */
public class TimePickerFragment extends DialogFragment
  implements TimePickerDialog.OnTimeSetListener {

  OnTimeSelectedListener mCallback;

  public interface OnTimeSelectedListener {
    public GregorianCalendar onTimeSelected(int hour, int minutes);
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the current time as the default values for the picker
    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);

    // Create a new instance of TimePickerDialog and return it
    return new TimePickerDialog(getActivity(), this, hour, minute,
      DateFormat.is24HourFormat(getActivity()));
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    // This makes sure that the container activity has implemented
    // the callback interface. If not, it throws an exception
    try {
      mCallback = (OnTimeSelectedListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
        + " must implement OnTimeSelectedListener");
    }
  }

  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    mCallback.onTimeSelected(hourOfDay, minute);
  }

}
