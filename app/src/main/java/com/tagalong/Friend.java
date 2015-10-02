package com.tagalong;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by christon on 10/2/15.
 */
public class Friend implements Parcelable {
  private String name;
  private String id;

  public Friend(String name, String id) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public String getId() {
    return this.id;
  }

  protected Friend(Parcel in) {
    name = in.readString();
    id = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeString(id);
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