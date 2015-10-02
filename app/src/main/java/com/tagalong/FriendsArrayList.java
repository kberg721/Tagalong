package com.tagalong;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by christon on 10/2/15.
 */
public class FriendsArrayList implements Parcelable {

  private ArrayList<Friend> friendObjects;

  public FriendsArrayList(JSONArray jsonArray) {
    try {
      friendObjects = new ArrayList<Friend>();
      for(int i=0; i<jsonArray.length(); i++) {
        JSONObject friendObj = jsonArray.getJSONObject(i);
        Friend friend = new Friend(friendObj.getString("name"), friendObj.getString("id"));
        friendObjects.add(friend);
      }
    } catch (org.json.JSONException e) {
      System.out.println("Failed to extract friends list");
      friendObjects = null;
    }
  }

  public Friend get(int index) {
    return friendObjects.get(index);
  }

  public String getNameAt(int index) {
    return friendObjects.get(index).getName();
  }

  public String getIdAt(int index) {
    return friendObjects.get(index).getId();
  }

  public ArrayList<Friend> getFriendObjects() {
    return friendObjects;
  }

  protected FriendsArrayList(Parcel in) {
    if (in.readByte() == 0x01) {
      friendObjects = new ArrayList<Friend>();
      in.readList(friendObjects, Friend.class.getClassLoader());
    } else {
      friendObjects = null;
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    if (friendObjects == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeList(friendObjects);
    }
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<FriendsArrayList> CREATOR = new Parcelable.Creator<FriendsArrayList>() {
    @Override
    public FriendsArrayList createFromParcel(Parcel in) {
      return new FriendsArrayList(in);
    }

    @Override
    public FriendsArrayList[] newArray(int size) {
      return new FriendsArrayList[size];
    }
  };
}