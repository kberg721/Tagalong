package com.tagalong.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import android.support.v4.app.Fragment;
import com.facebook.login.widget.LoginButton;
import com.tagalong.R;


public class FacebookLoginFragment extends Fragment {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    public static FacebookLoginFragment newInstance() {
        return new FacebookLoginFragment();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook_login, container, false);

        loginButton = (LoginButton) view.findViewById(R.id.facebook_login_btn);
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("user_friends");
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("success!");
            }

            @Override
            public void onCancel() {
                System.out.println("cancelled.");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("error :(");
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
