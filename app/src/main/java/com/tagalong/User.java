package com.tagalong;

public class User {
	String fullName, password, email;
	
	
	public User(String fullName, String password, String email) {
		this.fullName = fullName;
		this.password = password;
		this.email = email;
	}
	
	public User(String email, String password) {
		this.password = password;
		this.email = email;
		this.fullName = "";
	}

}
