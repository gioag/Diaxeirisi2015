package com.example.androlearning;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.json.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WSgetschools extends IntentService {

	private String url=null;
	
	// JSON parser class
    JSONParser jsonParser = new JSONParser();
    

    //private static final String LOGIN_URL = "http://127.0.0.1:8080/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SCHOOLS = "schools";
    
	public WSgetschools() {
		super("WSgetschools");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Log.d("WSGETSCHOOLS", "Service started.");
		
		url=intent.getStringExtra("url") + "get_all_schools.php";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		JSONArray schools = null;
		
		JSONObject json = jsonParser.makeHttpRequest(url, "GET", params);
		
		// check your log for json response
        Log.d("Getschools attempt", json.toString());

        // json success tag
        int success = 0;
		try {
			success = json.getInt(TAG_SUCCESS);
        
	        if (success == 1) {
	        	schools = json.getJSONArray(TAG_SCHOOLS);
	        	RegisterActivity.list.clear();
	        	for (int i = 0; i < schools.length(); i++) {
                    JSONObject c = schools.getJSONObject(i);

                    // Storing each json item in variable
                   
                    String name = c.getString("sname");
            		RegisterActivity.list.add(name);

                }
	        	Intent bi = new Intent();
        		bi.setAction("schools_added");
        		sendBroadcast(bi);
	        	//json.getString(TAG_MESSAGE);
	        }else{
	        	Log.d("Login Failure!", json.getString(TAG_MESSAGE));
	        	//json.getString(TAG_MESSAGE);
	
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}