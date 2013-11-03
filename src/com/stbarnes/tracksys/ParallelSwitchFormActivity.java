package com.stbarnes.tracksys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ParallelSwitchFormActivity extends Activity {

	private static final String POST_URL = "http://tracksys-mothership.appspot.com/parallel-switch";

	private String[] types;
	private Spinner typeSpinner;

	private HashMap<String, String[]> targetMapping;
	private Spinner targetSpinner;
	private ArrayAdapter<String> targetAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parallel_switch_form);

		try {
			MainActivity.typeFetchTask.get();
		} catch ( Exception e ) {
			e.printStackTrace();
			return;
		}

		JSONObject typesJSON = MainActivity.datumTypeDefinitions
				.optJSONObject("parallel-switch");

		// Fill key array and mapping for targets
		int i = 0;
		Iterator<String> iter = typesJSON.keys();
		types = new String[typesJSON.length()];
		targetMapping = new HashMap<String, String[]>();
		while ( iter.hasNext() ) {
			String type = iter.next();
			types[i] = type;
			i++;

			String[] targets = JSONHelper.convertJSONStringArray(typesJSON
					.optJSONArray(type));
			targetMapping.put(type, targets);
		}

		typeSpinner = (Spinner) findViewById(R.id.type_spinner);
		targetSpinner = (Spinner) findViewById(R.id.target_spinner);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.custom_spinner, types);
		typeSpinner.setAdapter(adapter);

		targetAdapter = new ArrayAdapter<String>(this,
				R.layout.custom_spinner, new ArrayList<String>());
		targetSpinner.setAdapter(targetAdapter);

		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				targetAdapter.clear();

				for ( String target : targetMapping.get(types[position]) ) {
					targetAdapter.add(target);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parallel_switch_form, menu);
		return true;
	}

	public void submit(View v) {
		String type = types[typeSpinner.getSelectedItemPosition()];
		JSONObject body = new JSONObject();

		try {
			body.put("type", type);
			body.put("target", targetMapping.get(type)[targetSpinner
					.getSelectedItemPosition()]);
		} catch ( JSONException e ) {
			e.printStackTrace();
			return;
		}

		new ServerPostTask().execute(this, POST_URL, body);
	}
}
