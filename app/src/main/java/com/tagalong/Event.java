package com.tagalong;

/**
 * Created by Kyle on 10/2/15.
 */
public class Event {
  String eventName, eventLocation, eventTime, guestList;


  public Event(String name, String location, String time, String guestList) {
    this.eventName = name;
    this.eventLocation = location;
    this.eventTime = time;
    this.guestList = guestList;
  }

}
