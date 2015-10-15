package com.tagalong;

/**
 * Created by Kyle on 10/2/15.
 */
public class Event {
  String eventName, eventLocation, eventTime, guestList, hostEmail;
  int hostEventCount;


  public Event(String name, String location, String time, String guestList, String hostEmail,
               int hostEventCount) {
    this.eventName = name;
    this.eventLocation = location;
    this.eventTime = time;
    this.guestList = guestList;
    this.hostEmail = hostEmail;
    this.hostEventCount = hostEventCount;
  }

}
