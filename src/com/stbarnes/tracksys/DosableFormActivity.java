package com.stbarnes.tracksys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class DosableFormActivity extends Activity {

	private static final String POST_URL = "http://tracksys-mothership.appspot.com/dosable";

	private String[] types;
	private Spinner typeSpinner;
	private EditText amountInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dosable_form);

		try {
			MainActivity.typeFetchTask.get();
		} catch ( Exception e ) {
			e.printStackTrace();
			return;
		}
		
		JSONArray typesJSON = MainActivity.datumTypeDefinitions
				.optJSONArray("dosable");
		types = JSONHelper.convertJSONStringArray(typesJSON);

		typeSpinner = (Spinner) findViewById(R.id.type_spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.custom_spinner, types);
		typeSpinner.setAdapter(adapter);
		
		amountInput = (EditText) findViewById(R.id.amount_input);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dosable_form, menu);
		return true;
	}

	public void submit(View v) {
		JSONObject body = new JSONObject();
		
		try {
			body.put("type", types[typeSpinner.getSelectedItemPosition()]);
			
			String amount = amountInput.getText().toString();
			if ( !amount.equals("") ) {
				body.put("amount", Double.parseDouble(amount));
			} else {
				body.put("amount", null);
			}
		} catch ( JSONException e ) {
			e.printStackTrace();
			return;
		}
		
		new ServerPostTask().execute(this, POST_URL, body);
	}
}
