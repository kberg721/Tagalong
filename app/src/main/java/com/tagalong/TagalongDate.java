package com.tagalong;

/**
 * Created by Kberg721 on 10/7/15.
 */
public class TagalongDate {
  private int mYear, mMonth, mDay, mHour, mMinute;

  public TagalongDate(int year, int month, int day, int hour, int minutes) {
    this.mYear = year;
    this.mMonth = month;
    this.mDay = day;
    this.mHour = hour;
    this.mMinute = minutes;
  }

  public TagalongDate() {
    this(0,0,0,0,0);
  }

  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(Integer.toString(mYear) + ",");
    result.append(Integer.toString(mMonth) + ",");
    result.append(Integer.toString(mDay) + ",");
    result.append(Integer.toString(mHour) + ",");
    result.append(Integer.toString(mMinute));
    return result.toString();
  }

  public int getmYear() {
    return mYear;
  }

  public void setmYear(int mYear) {
    this.mYear = mYear;
  }

  public int getmMonth() {
    return mMonth;
  }

  public void setmMonth(int mMonth) {
    this.mMonth = mMonth;
  }

  public int getmDay() {
    return mDay;
  }

  public void setmDay(int mDay) {
    this.mDay = mDay;
  }

  public int getmHour() {
    return mHour;
  }

  public void setmHour(int mHour) {
    this.mHour = mHour;
  }

  public int getmMinute() {
    return mMinute;
  }

  public void setmMinute(int mMinute) {
    this.mMinute = mMinute;
  }
}
