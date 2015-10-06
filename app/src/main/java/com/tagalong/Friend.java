package com.tagalong;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by christon on 10/2/15.
 */
public class Friend implements Parcelable {
  private String name;
  private String id;
  private boolean selected;

  public Friend(String name, String id) {
    this.name = name;
    this.id = id;
    this.selected = false;
  }

  public String getName() {
    return this.name;
  }

  public String getId() {
    return this.id;
  }

  public boolean isSelected() { return this.selected; }

  public void setSelected(boolean selectedState) {
    this.selected = selectedState;
  }

  protected Friend(Parcel in) {
    name = in.readString();
    id = in.readString();
    boolean[] selectedArray = new boolean[1];
    in.readBooleanArray(selectedArray);
    this.selected = selectedArray[0];
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.name);
    dest.writeString(this.id);
    dest.writeBooleanArray(new boolean[] {this.selected});
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Friend> CREATOR = new Parcelable.Creator<Friend>() {
    @Override
    public Friend createFromParcel(Parcel in) {
      return new Friend(in);
    }

    @Override
    public Friend[] newArray(int size) {
      return new Friend[size];
    }
  };
}