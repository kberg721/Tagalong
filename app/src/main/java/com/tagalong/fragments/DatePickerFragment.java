package com.tagalong.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Kberg721 on 10/6/15.
 */
public class DatePickerFragment extends DialogFragment
  implements DatePickerDialog.OnDateSetListener {

  OnDateSelectedListener mCallback;

  public interface OnDateSelectedListener {
    public void onDateSelected(int year, int month, int day);
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the current date as the default date in the picker
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    // Create a new instance of DatePickerDialog and return it
    return new DatePickerDialog(getActivity(), this, year, month, day);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    // This makes sure that the container activity has implemented
    // the callback interface. If not, it throws an exception
    try {
      mCallback = (OnDateSelectedListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
        + " must implement OnDateSelectedListener");
    }
  }

  public void onDateSet(DatePicker view, int year, int month, int day) {
    mCallback.onDateSelected(year, month, day);
  }
}
