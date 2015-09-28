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

public class WSregister extends IntentService {

	private String flname=null;
	private String school=null;
	private String username = null;
	private String password = null;
	private String url=null;
	
	// JSON parser class
    JSONParser jsonParser = new JSONParser();
	private String mode;
    

    //private static final String LOGIN_URL = "http://127.0.0.1:8080/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    
	public WSregister() {
		super("WSregister");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("WSREGISTER", "Service started.");
		flname=intent.getStringExtra("flname");
		school=intent.getStringExtra("school");
		username = intent.getStringExtra("username");
		password = intent.getStringExtra("password");
		url=intent.getStringExtra("url")+"register.php";
		mode=intent.getStringExtra("mode");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sname", flname));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("school_id", school));
		
		
		JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
		
		// check your log for json response
        Log.d("Register attempt", json.toString());

        // json success tag
        int success = 0;
		try {
			success = json.getInt(TAG_SUCCESS);
        
	        if (success == 1) {
	        	Log.d("Register Successful!", json.toString());
	        	
	        	Intent iT = new Intent(getApplicationContext(), LoginActivity.class);
				iT.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				iT.putExtra("mode", mode);
				iT.putExtra("url", intent.getStringExtra("url"));
				startActivity(iT);
				
				Intent bi = new Intent();
        		bi.setAction("register_finish");
        		sendBroadcast(bi);
	        	//json.getString(TAG_MESSAGE);
	        }else{
	        	Log.d("Register Failure!", json.getString(TAG_MESSAGE));
	        	//json.getString(TAG_MESSAGE);
	
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}