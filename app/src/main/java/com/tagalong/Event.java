package com.tagalong;

/**
 * Created by Kyle on 10/2/15.
 */
public class Event {
  String hostEmail, eventName, eventLocation, eventTime, eventDescription /*, guestList*/;
  int hostEventCount;

  public Event(String hostEmail, int hostEventCount, String name, String location, String time, String description/*String guestList*/) {
    this.hostEmail = hostEmail;
    this.hostEventCount = hostEventCount;
    this.eventName = name;
    this.eventLocation = location;
    this.eventTime = time;
    this.eventDescription = description;
//    this.guestList = guestList;
  }
}
