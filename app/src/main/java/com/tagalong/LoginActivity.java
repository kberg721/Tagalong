package com.tagalong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class LoginActivity extends FragmentActivity implements View.OnClickListener {
	
	Button btnLogin;
	EditText etPassword, etEmail;
	TextView registerScreen;
	UserLocalStore userLocalStore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		setContentView(R.layout.activity_login);
		
		btnLogin = (Button) findViewById(R.id.btnLogin);
        registerScreen = (TextView) findViewById(R.id.link_to_register);
        etPassword = (EditText) findViewById(R.id.etPassword);
	    etEmail = (EditText) findViewById(R.id.etEmail);
		
        userLocalStore = new UserLocalStore(this);
        
        btnLogin.setOnClickListener(this);
        registerScreen.setOnClickListener(this);
	}
	
	
	private boolean authenticate() {
		return userLocalStore.getUserLoggedIn();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.link_to_register:
				Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
	            startActivity(i);
	            break;
			case R.id.btnLogin:
				String email = etEmail.getText().toString();
				String password = etPassword.getText().toString();
				
				User user = new User(email, password);
				
				authenticate(user);
				
				userLocalStore.storeUserData(user);
				userLocalStore.setUserLoggedIn(true);
				break;	
		}
		
	}
	
	private void authenticate(User user) {
		ServerRequests serverRequest = new ServerRequests(this);
		serverRequest.fetchUserDataAsyncTask(user, new GetUserCallback() {
			@Override
			public void done(User returnedUser) {
				if(returnedUser == null) {
					showErrorMessage();
				} else {
					logUserIn(returnedUser);
				}
			}
		});
	}
	
	private void showErrorMessage() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
		dialogBuilder.setMessage("Incorrect user details");
		dialogBuilder.setPositiveButton("ok", null);
		dialogBuilder.show();
	}

	private void logUserIn(User returnedUser) {
		userLocalStore.storeUserData(returnedUser);
		userLocalStore.setUserLoggedIn(true);
		
		startActivity(new Intent(this, MainActivity.class));
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Logs 'install' and 'app activate' App Events.
		AppEventsLogger.activateApp(this);
	}
}