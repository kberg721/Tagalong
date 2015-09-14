package com.example.tagalong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {
	
	Button btnLogout;
	EditText etName, etPassword, etEmail;
	UserLocalStore userLocalStore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnLogout = (Button) findViewById(R.id.btnLogout);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
		
        userLocalStore = new UserLocalStore(this);
        
        btnLogout.setOnClickListener(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(authenticate() == true) {
			displayUserDetails();
		}
	}
	
	private boolean authenticate() {
		if(userLocalStore.getLoggedInUser().fullName.equals("")) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			return false;
		}
		return true;
	}
	
	private void displayUserDetails() {
		User user = userLocalStore.getLoggedInUser();
		etName.setText(user.fullName);
		etEmail.setText(user.email);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnLogout:
				userLocalStore.clearUserData();
				userLocalStore.setUserLoggedIn(false);
				Intent loginIntent = new Intent(this, LoginActivity.class);
				startActivity(loginIntent);
				break;
		}
		
	}
}
