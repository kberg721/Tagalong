package com.tagalong.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.tagalong.FriendsArrayList;
import com.tagalong.GetUserCallback;
import com.tagalong.Mainpage;
import com.tagalong.R;
import com.tagalong.ServerRequests;
import com.tagalong.User;
import com.tagalong.UserLocalStore;

import org.json.JSONObject;


public class FacebookLoginFragment extends Fragment {

  private LoginButton loginButton;
  private CallbackManager callbackManager;
  private ServerRequests serverRequest;
  private UserLocalStore userLocalStore;
  private User toBeRegistered;
  private Intent mainpageIntent;

  public static FacebookLoginFragment newInstance() {
    return new FacebookLoginFragment();
  }

  @Override
  public View onCreateView(
    LayoutInflater inflater,
    ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_facebook_login, container, false);

    userLocalStore = new UserLocalStore(getContext());
    serverRequest = new ServerRequests(getContext());
    loginButton = (LoginButton) view.findViewById(R.id.facebook_login_btn);
    callbackManager = CallbackManager.Factory.create();

    loginButton.setReadPermissions("user_friends", "email", "public_profile");
    // If using in a fragment
    loginButton.setFragment(this);
    // Other app specific specialization

    // Callback registration
    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override
      public void onSuccess(LoginResult loginResult) {
        Bundle params = new Bundle();
        params.putString("fields", "id,name,email,picture,friends");
        new GraphRequest(
          loginResult.getAccessToken(),
          "/me",
          params,
          HttpMethod.GET,
          new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
              JSONObject jsonObject = response.getJSONObject();
              try {
                String email = jsonObject.getString("email");
                String name = jsonObject.getString("name");
                User FBUser = new User(name, email, "", userLocalStore.getLoggedInUser().getEventCount());
                mainpageIntent = new Intent(getActivity(), Mainpage.class);
                mainpageIntent.putExtra("friendsList", new FriendsArrayList(
                  jsonObject.getJSONObject("friends").getJSONArray("data")
                ).getFriendObjects());
                mainpageIntent.putExtra("loginStatus", true);
                mainpageIntent.putExtra("name", jsonObject.getString("name"));
                mainpageIntent.putExtra("email", jsonObject.getString("email"));
                /*TODO: fix using Parcelable at future juncture.
                  mainpageIntent.putExtra("picture", jsonObject.getJSONObject("picture"));
                 */
                authenticate(FBUser);
              } catch (org.json.JSONException e) {
                System.out.println("Exception: " + e);
              }
            }
          }
        ).executeAsync();
      }

      private void authenticate(User user) {
        toBeRegistered = user;
        serverRequest.fetchUserDataAsyncTask(user, new GetUserCallback() {
          @Override
          public void done(User returnedUser) {
            if (returnedUser == null) {
              registerUser(toBeRegistered);
            } else {
              logUserIn(returnedUser);
            }
          }
        });
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

  private void logUserIn(User returnedUser) {
    userLocalStore.storeUserData(returnedUser);
    userLocalStore.setUserLoggedIn(true);

    startActivity(mainpageIntent);
  }

  private void registerUser(User user) {
    ServerRequests serverRequest = new ServerRequests(getContext());
    serverRequest.storeUserDataInBackground(user, new GetUserCallback() {
      @Override
      public void done(User returnedUser) {
        startActivity(mainpageIntent);
      }
    });
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }

}
