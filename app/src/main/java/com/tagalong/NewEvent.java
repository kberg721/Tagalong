package com.tagalong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewEvent extends Activity implements View.OnClickListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_event);
  }


  @Override
  public void onClick(View v) {
    switch(v.getId()) {
      case R.id.link_to_register:
        break;
      case R.id.btnLogin:
        break;
      default:
        break;
    }
  }
}