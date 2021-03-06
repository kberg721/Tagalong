package com.example.tagalong;


import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity implements OnClickListener {
	
	Button btnRegister;
	EditText etName, etPassword, etEmail;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        //Set View to register.xml
        setContentView(R.layout.activity_register);
        
        btnRegister = (Button) findViewById(R.id.btnRegister);
        etName = (EditText) findViewById(R.id.etName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etEmail = (EditText) findViewById(R.id.etEmail);
        
 
        TextView loginScreen = (TextView) findViewById(R.id.login_link);
 
        // Listening to Login Screen link
        loginScreen.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()) {
			case R.id.login_link:
				// Closing registration screen
		        // Switching to Login Screen/closing register screen
		        finish();
			case R.id.btnRegister:
				String name = etName.getText().toString();
				String password = etPassword.getText().toString();
				String email = etEmail.getText().toString();
				
				User user = new User(name, email, password);
				
				registerUser(user);
				break;
		}
    }
	
	private void registerUser(User user) {
		ServerRequests serverRequest = new ServerRequests(this);
		serverRequest.storeUserDataInBackground(user, new GetUserCallback() {
			@Override
			public void done(User returnedUser) {
				startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
			}
		});
	}

}
