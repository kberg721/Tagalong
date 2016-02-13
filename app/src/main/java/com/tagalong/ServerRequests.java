package com.tagalong;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.facebook.AccessToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServerRequests extends Admins {
	
	ProgressDialog progressDialog;
	
	public static final int CONNECTION_TIMEOUT = 1000 * 15;
	
	public ServerRequests(Context context) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.setTitle("Processing");
		progressDialog.setMessage("Please wait...");
	}
	
	public void storeUserDataInBackground(User user, GetUserCallback callback) {
		progressDialog.show();
		new StoreUserDataAsyncTask(user, callback).execute();
	}

	public void updateUserEventCountInBackground(User user, GetUserCallback callback) {
		progressDialog.show();
		new UpdateUserEventCountAsyncTask(user, callback).execute();
	}

	public void storeEventDataInBackground(Event event, GetEventCallback callback) {
		progressDialog.show();
		new StoreEventDataAsyncTask(event, callback).execute();
	}

  public void storeInviteeDataInBackground(User host, Friend invitee, GetInviteeCallback callback, EventResponse isAttending) {
    progressDialog.show();
    new StoreInviteeDataAsyncTask(host, invitee, callback, isAttending).execute();
  }
	
	public void fetchUserDataAsyncTask(User user, GetUserCallback callback) {
		//progressDialog.show();
		new FetchUserDataAsyncTask(user, callback).execute();
	}

	public void fetchEventDataAsyncTask(Event event, GetEventCallback callback) {
		progressDialog.show();
		new FetchEventDataAsyncTask(event, callback).execute();
	}
	
	public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
		User user;
		GetUserCallback userCallback;
		
		public StoreUserDataAsyncTask(User user, GetUserCallback callBack) {
			this.user = user;
			this.userCallback = callBack;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			ArrayList<NameValuePair> dataToSend = new ArrayList<>();
			dataToSend.add(new BasicNameValuePair("name", user.fullName));
			dataToSend.add(new BasicNameValuePair("email", user.email));
			dataToSend.add(new BasicNameValuePair("password", user.password));
			dataToSend.add(new BasicNameValuePair("eventCount", Integer.toString(user.eventCount)));

			
			HttpParams httpRequestParam = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);
			
			HttpClient client = new DefaultHttpClient(httpRequestParam);
			HttpPost post = new HttpPost(getRegisterFile());
			
			try {
				post.setEntity(new UrlEncodedFormEntity(dataToSend));
				client.execute(post);
			} catch(Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void aVoid) {
			progressDialog.dismiss();
			userCallback.done(null);
			super.onPostExecute(aVoid);
			
		}
	}

	public class UpdateUserEventCountAsyncTask extends AsyncTask<Void, Void, Void> {
		User user;
		GetUserCallback userCallback;

		public UpdateUserEventCountAsyncTask(User user, GetUserCallback callBack) {
			this.user = user;
			this.userCallback = callBack;
		}

		@Override
		protected Void doInBackground(Void... params) {
			ArrayList<NameValuePair> dataToSend = new ArrayList<>();
			dataToSend.add(new BasicNameValuePair("name", user.fullName));
			dataToSend.add(new BasicNameValuePair("email", user.email));
			dataToSend.add(new BasicNameValuePair("password", user.password));
			dataToSend.add(new BasicNameValuePair("eventCount", Integer.toString(user.eventCount)));

			HttpParams httpRequestParam = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

			HttpClient client = new DefaultHttpClient(httpRequestParam);
			HttpPost post = new HttpPost(getUpdateEventFile());

			try {
				post.setEntity(new UrlEncodedFormEntity(dataToSend));
				client.execute(post);
			} catch(Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public class StoreInviteeDataAsyncTask extends AsyncTask<Void, Void, Void> {
		User host;
		Friend invitee;
		GetInviteeCallback inviteeCallback;
		EventResponse isAttending;

		public StoreInviteeDataAsyncTask(User host, Friend invitee, GetInviteeCallback inviteeCallback, EventResponse isAttending) {
			this.host = host;
			this.invitee = invitee;
			this.inviteeCallback = inviteeCallback;
			this.isAttending = isAttending;
		}

		@Override
		protected Void doInBackground(Void... params) {
			ArrayList<NameValuePair> dataToSend = new ArrayList<>();
			dataToSend.add(new BasicNameValuePair("hostEmail", host.email));
			dataToSend.add(new BasicNameValuePair("hostEventCount", Integer.toString(host.eventCount)));
			dataToSend.add(new BasicNameValuePair("friendName", invitee.getName()));
			dataToSend.add(new BasicNameValuePair("friendEmail", invitee.getEmail()));
      dataToSend.add(new BasicNameValuePair("friendFBId", invitee.getId()));
      dataToSend.add(new BasicNameValuePair("isAttending", isAttending.toString()));

      HttpParams httpRequestParam = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
      HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

      HttpClient client = new DefaultHttpClient(httpRequestParam);
      HttpPost post = new HttpPost(getSubmitInviteeFile());

      try {
        post.setEntity(new UrlEncodedFormEntity(dataToSend));
        client.execute(post);
      } catch(Exception e) {
        e.printStackTrace();
      }
      return null;
    }

		protected void onPostExecute(Void aVoid) {
			progressDialog.dismiss();
			inviteeCallback.done(null);
			super.onPostExecute(aVoid);
		}
	}

	public class StoreEventDataAsyncTask extends AsyncTask<Void, Void, Void> {
		Event event;
		GetEventCallback eventCallback;

		public StoreEventDataAsyncTask(Event event, GetEventCallback callBack) {
			this.event = event;
			this.eventCallback = callBack;
		}

		@Override
		protected Void doInBackground(Void... params) {
			ArrayList<NameValuePair> dataToSend = new ArrayList<>();
			dataToSend.add(new BasicNameValuePair("hostEmail", event.hostEmail));
			dataToSend.add(new BasicNameValuePair("hostEventCount", Integer.toString(event.hostEventCount)));
			dataToSend.add(new BasicNameValuePair("name", event.eventName));
			dataToSend.add(new BasicNameValuePair("location", event.eventLocation));
			dataToSend.add(new BasicNameValuePair("time", event.eventTime));
			dataToSend.add(new BasicNameValuePair("description", event.eventDescription));

			HttpParams httpRequestParam = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

			HttpClient client = new DefaultHttpClient(httpRequestParam);
			HttpPost post = new HttpPost(getSubmitEventFile());

			try {
				post.setEntity(new UrlEncodedFormEntity(dataToSend));
				client.execute(post);
			} catch(Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			progressDialog.dismiss();
			eventCallback.done(null);
			super.onPostExecute(aVoid);

		}
	}
	
	public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
		User user;
		GetUserCallback userCallback;
		
		public FetchUserDataAsyncTask(User user, GetUserCallback callBack) {
			this.user = user;
			this.userCallback = callBack;
		}

		@Override
		protected User doInBackground(Void... params) {
			ArrayList<NameValuePair> dataToSend = new ArrayList<>();
			dataToSend.add(new BasicNameValuePair("email", user.getEmail()));
			AccessToken isFBUser = AccessToken.getCurrentAccessToken();
			if(isFBUser == null) {
				dataToSend.add(new BasicNameValuePair("password", user.getPassword()));
			}
			
			HttpParams httpRequestParam = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);
			
			HttpClient client = new DefaultHttpClient(httpRequestParam);
			HttpPost post = new HttpPost(getFetchUserFile());
			
			User returnedUser = null;
			try {
				post.setEntity(new UrlEncodedFormEntity(dataToSend));
				HttpResponse httpResponse = client.execute(post);
				
				HttpEntity entity = httpResponse.getEntity();
				String result = EntityUtils.toString(entity);
				JSONObject jObject = new JSONObject(result);
				
				if(jObject.length() == 0) {
					returnedUser = null;
				} else {
					String name = jObject.getString("name");
					int eventCount = Integer.parseInt(jObject.getString("eventCount"));

					if(isFBUser != null) {
						returnedUser = new User(user.fullName, user.email, "", eventCount);
					} else {
						returnedUser = new User(name == null ? user.fullName : name, user.email, user.password,
              jObject.getString("eventCount") != null ? eventCount : user.eventCount);
					}
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			return returnedUser;
		}
		
		protected void onPostExecute(User returnedUser) {
			progressDialog.dismiss();
			userCallback.done(returnedUser);
			super.onPostExecute(returnedUser);
		}
	}

	public class FetchEventDataAsyncTask extends AsyncTask<Void, Void, Event> {
		Event event;
		GetEventCallback eventCallback;

		public FetchEventDataAsyncTask(Event event, GetEventCallback callBack) {
			this.event = event;
			this.eventCallback = callBack;
		}

		@Override
		protected Event doInBackground(Void... params) {
			ArrayList<NameValuePair> dataToSend = new ArrayList<>();
			dataToSend.add(new BasicNameValuePair("hostEmail", event.hostEmail));
			dataToSend.add(new BasicNameValuePair("hostEventCount", Integer.toString(event.hostEventCount)));
			dataToSend.add(new BasicNameValuePair("eventName", event.eventName));
			dataToSend.add(new BasicNameValuePair("eventLocation", event.eventLocation));
			dataToSend.add(new BasicNameValuePair("eventTime", event.eventTime));
			dataToSend.add(new BasicNameValuePair("eventGuestlist", event.eventDescription));

			HttpParams httpRequestParam = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

			HttpClient client = new DefaultHttpClient(httpRequestParam);
			HttpPost post = new HttpPost(getFetchEventFile());

			Event returnedEvent = null;
			try {
				post.setEntity(new UrlEncodedFormEntity(dataToSend));
				HttpResponse httpResponse = client.execute(post);

				HttpEntity entity = httpResponse.getEntity();
				String result = EntityUtils.toString(entity);
				JSONObject jObject = new JSONObject(result);

				if(jObject.length() == 0) {
					returnedEvent = null;
				} else {
					String name = jObject.getString("name");
					returnedEvent = new Event(event.hostEmail, event.hostEventCount, name, event.eventLocation, event.eventTime, event.eventDescription);
				}

			} catch(Exception e) {
				e.printStackTrace();
			}

			return returnedEvent;
		}

		protected void onPostExecute(Event returnedEvent) {
			progressDialog.dismiss();
			eventCallback.done(returnedEvent);
			super.onPostExecute(returnedEvent);
		}
	}

}
