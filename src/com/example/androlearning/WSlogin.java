package com.example.androlearning;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.json.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WSlogin extends IntentService {

	private String username = null;
	private String password = null;
	private String url=null;
	
	// JSON parser class
    JSONParser jsonParser = new JSONParser();
	private String mode;
    
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    
	public WSlogin() {
		super("WSlogin");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Log.d("WSLOGIN", "Service started.");
		username = intent.getStringExtra("username");
		password = intent.getStringExtra("password");
		url=intent.getStringExtra("url")+"login.php";
		mode=intent.getStringExtra("mode");
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		
		Log.d("Login attempt",url);

		JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
		
		// check your log for json response
        Log.d("Login attempt", json.toString());

        // json success tag
        int success = 0;
		try {
			success = json.getInt(TAG_SUCCESS);
        
	        if (success == 1) {
	        	Log.d("Login Successful!", json.toString());
	        	
	        	Intent chooseLesson = new Intent(getApplicationContext(), ChooseLessonActivity.class);
	        	chooseLesson.putExtra("url", intent.getStringExtra("url"));
	        	chooseLesson.putExtra("username", username);
	        	chooseLesson.putExtra("password", password);
	        	chooseLesson.putExtra("mode", mode);
	        	chooseLesson.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(chooseLesson);
				Intent bi = new Intent();
        		bi.setAction("login_finish");
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