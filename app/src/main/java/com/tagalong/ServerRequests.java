package com.tagalong;

import java.util.ArrayList;

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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

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
	
	public void fetchUserDataAsyncTask(User user, GetUserCallback callback) {
		progressDialog.show();
		new FetchUserDataAsyncTask(user, callback).execute();
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
			dataToSend.add(new BasicNameValuePair("email", user.email));
			dataToSend.add(new BasicNameValuePair("password", user.password));
			
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
					user = null;
				} else {
					String name = jObject.getString("name");
					
					returnedUser = new User(name, user.password, user.email);
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
}
