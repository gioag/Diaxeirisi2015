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

public class WSsendlesson extends IntentService {

	private String username = null;
	private String lesson = null;
	private String url=null;
	
	// JSON parser class
    JSONParser jsonParser = new JSONParser();
	private String mode;
    
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    
	public WSsendlesson() {
		super("WSsendlesson");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("WSSENDLESSON", "Service started.");
		username = intent.getStringExtra("username");
		lesson = intent.getStringExtra("lesson");
		mode = intent.getStringExtra("mode");
		url=intent.getStringExtra("url")+"sendlesson.php";
		Log.d("WSSENDLESSON", url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("lesson", lesson));

		JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
		
		// check your log for json response
        Log.d("Sendlesson attempt", json.toString());

        // json success tag
        int success = 0;
		try {
			success = json.getInt(TAG_SUCCESS);
	        if (success == 1) {
	        	Log.d("Sendlesson Successful!", json.toString());
	        	
	        	Intent chooseLesson = new Intent(getApplicationContext(), waitForContestActivity.class);
	        	chooseLesson.putExtra("url", intent.getStringExtra("url"));
	        	chooseLesson.putExtra("username", username);
	        	chooseLesson.putExtra("lesson", lesson);
	        	chooseLesson.putExtra("mode", mode);
	        	chooseLesson.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(chooseLesson);
				Intent bi = new Intent();
        		bi.setAction("choose_finish");
        		sendBroadcast(bi);
	        	//json.getString(TAG_MESSAGE);
	        }else{
	        	//Log.d("Sendlesson Failure!", json.getString(TAG_MESSAGE));
	        	Intent bi = new Intent();
        		bi.setAction("no_game");
        		sendBroadcast(bi);
	        	//json.getString(TAG_MESSAGE);
	
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}