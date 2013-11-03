package com.stbarnes.tracksys;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	public static String TYPE_DEFINITION_URL = "http://tracksys-mothership.appspot.com/types";
	public static String DATUM_DELETE_URL = "http://tracksys-mothership.appspot.com/delete-most-recent";

	public static FetchTypeDefinitionsTask typeFetchTask;
	public static JSONObject datumTypeDefinitions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Fetch type definitions
		// TODO: Cache
		typeFetchTask = new FetchTypeDefinitionsTask();
		typeFetchTask.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void launchMeasurementForm(View v) {
		startActivity(new Intent(this, MeasurementFormActivity.class));
	}

	public void launchDosableForm(View v) {
		startActivity(new Intent(this, DosableFormActivity.class));
	}

	public void launchParallelSwitchForm(View v) {
		startActivity(new Intent(this, ParallelSwitchFormActivity.class));
	}

	public void deleteMostRecentDatum(View v) {
		new DeleteMostRecentDatumTask().execute();
	}

	public class FetchTypeDefinitionsTask extends
			AsyncTask<Void, Void, JSONObject> {
		protected JSONObject doInBackground(Void... params) {
			String jsonString = null;

			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet request = new HttpGet(TYPE_DEFINITION_URL);

				HttpResponse httpResponse = httpClient.execute(request);
				HttpEntity entity = httpResponse.getEntity();
				jsonString = EntityUtils.toString(entity);
			} catch ( ClientProtocolException e ) {
				// pass
			} catch ( IOException e ) {
				// pass
			}

			JSONObject obj = null;
			try {
				obj = new JSONObject(jsonString);
			} catch ( JSONException e ) {
				// pass
			}

			return obj;
		}

		protected void onPostExecute(JSONObject obj) {
			Log.e("a", "stuff!!");
			MainActivity.datumTypeDefinitions = obj;
		}
	}

	public class DeleteMostRecentDatumTask extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... params) {
			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet request = new HttpGet(DATUM_DELETE_URL);
				httpClient.execute(request);
			} catch ( IOException e ) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void ret) {
			new AlertDialog.Builder(MainActivity.this).setMessage(
					"Datum deleted.").show();
		}
	}

}
