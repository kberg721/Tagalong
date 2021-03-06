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
  private String email;

  public Friend(String name, String id, String email) {
    this.name = name;
    this.id = id;
    this.email = email;
    this.selected = false;
  }

  public String getName() {
    return this.name;
  }

  public String getId() {
    return this.id;
  }

  public String getEmail() { return this.email; }

  public boolean isSelected() { return this.selected; }

  public void setSelected(boolean selectedState) {
    this.selected = selectedState;
  }

  protected Friend(Parcel in) {
    name = in.readString();
    // May need to change this to read the string and find out if it is an email or an id
    id = in.readString();
    email = in.readString();
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
    dest.writeString(this.email);
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