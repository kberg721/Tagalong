package com.tagalong;

public class User {
	String fullName, email, password;
	int eventCount;

	public User(String fullName, String email, String password) {
		this(fullName, email, password, 0);
	}
	
	public User(String email, String password) {
		this("", email, password, 0);
	}

	public User(String email, String password, int eventCount) {
		this("", email, password, eventCount);
	}

	public int getEventCount () {
		return eventCount;
	}

	public User(String fullName, String email, String password, int eventCount) {
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.eventCount = eventCount;
	}
}
