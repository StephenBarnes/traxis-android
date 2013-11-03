package com.stbarnes.tracksys;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

public class ServerPostTask extends AsyncTask<Object, Void, Void> {
	private Activity activity;

	@Override
	protected Void doInBackground(Object... params) {
		assert params.length == 3;
		activity = (Activity) params[0];
		String url = (String) params[1];
		JSONObject param = (JSONObject) params[2];

		if ( !param.has("timestamp") ) {
			try {
				param.put("timestamp", System.currentTimeMillis() / 1000L);
			} catch ( JSONException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		try {
			post.setEntity(new StringEntity(param.toString(), "UTF8"));
		} catch ( UnsupportedEncodingException e ) {
			// pass
		}

		try {
			client.execute(post);
		} catch ( ClientProtocolException e ) {
			// pass
		} catch ( IOException e ) {
			// pass
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void ret) {
		final AlertDialog alert = new AlertDialog.Builder(activity).setMessage(
				"Submitted.").show();

		Runnable runner = new Runnable() {
			public void run() {
				alert.dismiss();
				activity.finish();
			}
		};
		new Handler().postDelayed(runner, 750);
	}
}
