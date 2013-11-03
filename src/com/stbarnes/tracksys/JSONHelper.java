package com.stbarnes.tracksys;

import org.json.JSONArray;

public class JSONHelper {
	public static String[] convertJSONStringArray(JSONArray obj) {
		String[] out = new String[obj.length()];
		
		for ( int i = 0; i < obj.length(); i++ ) {
			out[i] = obj.optString(i);
		}
		
		return out;
	}
}
