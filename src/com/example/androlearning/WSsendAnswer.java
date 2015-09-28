package com.example.androlearning;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.json.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

public class WSsendAnswer extends IntentService {

	private String username = null;
	private String url=null;
	
	// JSON parser class
    JSONParser jsonParser = new JSONParser();
	private String question;
	private String answer;
	private int game;
	private String arithmos;
	private String mode;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    
	public WSsendAnswer() {
		super("WSsendAnswer");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		Log.d("WSsendAnswer", "Service started.");
		
		mode = intent.getStringExtra("mode");
		username = intent.getStringExtra("username");
		url=intent.getStringExtra("url")+"sendAnswer.php";
		question=intent.getStringExtra("question");
		answer=intent.getStringExtra("answer");
		game=intent.getIntExtra("game",0);
		arithmos=intent.getStringExtra("id");
		
		if(!mode.equals("2")){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("answer", answer));
			
			params.add(new BasicNameValuePair("question",question));
			
			Log.d("POST REQUEST", "is about to send");
			Log.d("PARAMS", params.toString());
			Log.d("URL", url);
			
			do{
				if(isNetworkAvailable()){
					JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
						
					
					// check your log for json response
			        Log.d("Login attempt", json.toString());
			
			        // json success tag
			        int success = 0;
					try {
						success = json.getInt(TAG_SUCCESS);
			        
				        if (success == 1) {
				        	Log.d("Login Successful!", json.toString());
				        	Intent points = new Intent();
				    		points.putExtra("point", json.getInt("points"));
				    		points.setAction(arithmos+game);
				    		Log.d("broadcast:",points.getAction());
			        		sendBroadcast(points);
				        	
			        		if(ContestTabsActivity.check==waitForContestActivity.ntabs){
			        			ContestTabsActivity.check=0;
						        Intent msgIntent = new Intent(this, WSfinishedcontest.class);
						    	msgIntent.putExtra("url", intent.getStringExtra("url"));
						    	msgIntent.putExtra("username", username);
						    	msgIntent.putExtra("game", game);
						    	msgIntent.putExtra("mode", mode);
						    	startService(msgIntent);
			        		}
			
				    		
			        		
			        		
				        	
				        	//json.getString(TAG_MESSAGE);
				        }else{
				        	Log.d("Login Failure!", "failure");
				        	//json.getString(TAG_MESSAGE);
				
				        }
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}while(!isNetworkAvailable());
		}
		else{
			Intent points = new Intent();
    		points.putExtra("point", MainActivity.db.getPointsByAnswer(answer, MainActivity.db.getQuestionIdByName(question)));
    		points.setAction(arithmos+MainActivity.db.getGame());
    		Log.d("broadcast:",points.getAction());
    		sendBroadcast(points);
		}
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}	
}