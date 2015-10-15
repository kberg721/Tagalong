package com.tagalong;

public class User {
	private String fullName, password, email;
	private int eventCounter;
	
	public User(String fullName, String password, String email) {
		this.fullName = fullName;
		this.password = password;
		this.email = email;
		this.eventCounter = 0;
	}
	
	public User(String email, String password) {
		this.password = password;
		this.email = email;
		this.fullName = "";
		this.eventCounter = 0;
	}

	public void incrementEventCounter() {
		eventCounter++;
	}

	public String getEmail() {
		return this.email;
	}

	public String getFullName() {
		return this.fullName;
	}

	public String getPassword() {return this.password; }

	public int getEventCounter() {
		return this.eventCounter;
	}

}
