package com.example.androlearning;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.json.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WSgetlessons extends IntentService {

	private String username = null;
	private String url=null;
	
	// JSON parser class
    JSONParser jsonParser = new JSONParser();
    

    //private static final String LOGIN_URL = "http://127.0.0.1:8080/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_LESSONS = "lessons";
    
	public WSgetlessons() {
		super("WSgetlessons");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Log.d("WSGETLESSONS", "Service started.");
		url=intent.getStringExtra("url") + "get_all_lessons.php";
		username = intent.getStringExtra("username");

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		JSONArray lessons = null;
		params.add(new BasicNameValuePair("username", username));
		
		JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
		Log.d("WSGETLESSONS", url);

        // json success tag
        int success = 0;
		try {
			success = json.getInt(TAG_SUCCESS);
        
	        if (success == 1) {
	        	lessons = json.getJSONArray(TAG_LESSONS);
	        	ChooseLessonActivity.list.clear();
	        	for (int i = 0; i < lessons.length(); i++) {
                    JSONObject c = lessons.getJSONObject(i);

                    // Storing each json item in variable
                   
                    String name = c.getString("name");
            		ChooseLessonActivity.list.add(name);

                }
	        	Intent bi = new Intent();
        		bi.setAction("lessons_added");
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
