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

public class WSfinishedcontest extends IntentService {

	private String username = null;
	private String url=null;
	
	// JSON parser class
    JSONParser jsonParser = new JSONParser();
	private String mode;
	private int game;
    
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    
	public WSfinishedcontest() {
		super("WSfinishedcontest");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		//MainActivity.isServiceon = 1;
		Log.d("WSfinishedcontest", "Service started.");
		game = intent.getIntExtra("game",0);
		username=intent.getStringExtra("username");
		mode=intent.getStringExtra("mode");
		url=intent.getStringExtra("url")+"finishedcontest.php";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    Log.d("GameID", ""+game);
		
		params.add(new BasicNameValuePair("game", ""+game));
		boolean i = true;
	
		
		while(true){
		
			JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
			
			// check your log for json response
	        Log.d("Check contest attempt", json.toString());
	
	        // json success tag
	        int success = 0;
	       
			try {
				success = json.getInt(TAG_SUCCESS);
		        if (success == 1) {
		        	Log.d("Check contest Successful!", json.toString());
		        	JSONArray jArray=json.getJSONArray("points");
		        	ArrayList<String> names=new ArrayList<String>();
		        	ArrayList<String> points=new ArrayList<String>();
		        	for (int j = 0; j < jArray.length(); j++) {       
                        JSONArray jsonArray2 = jArray.getJSONArray(j); 
                        
                        for(int k=0;k<jsonArray2.length();k++){
                        	if(k==0)
                        		names.add(jsonArray2.getString(k));
                        	else
                        		points.add(jsonArray2.getString(k));
                        		
                        }
                        
		        	}
		        	Intent chooseLesson = new Intent(getApplicationContext(), ResultsActivity.class);
		        	chooseLesson.putStringArrayListExtra("names", names);
		        	chooseLesson.putExtra("mode", mode);
		        	chooseLesson.putStringArrayListExtra("points", points);
		        	chooseLesson.putExtra("username", username);
		        	chooseLesson.putExtra("url", intent.getStringExtra("url"));

		        	chooseLesson.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(chooseLesson);
					Intent bi = new Intent();
	        		bi.setAction("tabs_finish");
	        		sendBroadcast(bi);
		        	
					break;
		        	
		        	//json.getString(TAG_MESSAGE);
		        }else{
		        	Log.d("Check contest Failure!", json.toString());
		        	//json.getString(TAG_MESSAGE);
		
		        }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	
	}
}