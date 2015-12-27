package com.tagalong;

/**
 * Created by christon on 12/26/15.
 */
public enum EventResponse {
  NOT_ATTENDING(0), ATTENDING(1), NO_RESPONSE(2);
  private int value;
  private EventResponse(int value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return Integer.toString(value);
  }
}
