package com.tagalong;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {
	public static final String SP_NAME = "userDetails";
	SharedPreferences userLocalDB;
	
	public UserLocalStore(Context context) {
		userLocalDB = context.getSharedPreferences(SP_NAME, 0);
	}
	
	public void storeUserData(User user) {
		SharedPreferences.Editor spEditor = userLocalDB.edit();
		spEditor.putString("name", user.fullName);
		spEditor.putString("password", user.password);
		spEditor.putString("email", user.email);
		spEditor.putString("eventCount", Integer.toString(user.eventCount));
		spEditor.commit();
	}
	
	public User getLoggedInUser() {
		String name = userLocalDB.getString("name", "");
		String password = userLocalDB.getString("password", "");
		String email = userLocalDB.getString("email", "");
		int eventCount = Integer.parseInt(userLocalDB.getString("eventCount", "0"));
		
		User storedUser = new User(name, email, password, eventCount);
		return storedUser;
	}
	
	public void setUserLoggedIn(boolean loggedIn) {
		SharedPreferences.Editor spEditor = userLocalDB.edit();
		spEditor.putBoolean("loggedIn", true);
		spEditor.commit();
	}
	
	public boolean getUserLoggedIn() {
		return userLocalDB.getBoolean("loggedIn", false) == true;
	}
	
	public void clearUserData() {
		SharedPreferences.Editor spEditor = userLocalDB.edit();
		spEditor.clear();
		spEditor.commit();
	}
}
