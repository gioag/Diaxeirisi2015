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

public class WSdbrefresh extends IntentService {

	private String url=null;
	
	// JSON parser class
    JSONParser jsonParser = new JSONParser();
    

    //private static final String LOGIN_URL = "http://127.0.0.1:8080/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_RESULTS = "results";
    
	public WSdbrefresh() {
		super("WSdbrefresh");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Log.d("WSdbrefresh", "Service started.");
		url="http://cgi.di.uoa.gr/~std10142/php/offlinedownload.php";
		Log.d("Url ", url);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		JSONArray results = null;
		//Log.d("USERNAME", username);
		JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
		
		// check your log for json response
		Log.d("url", url);
        Log.d("Lessons222342332", json.toString());

        // json success tag
        int success = 0;
		try {
			success = json.getInt(TAG_SUCCESS);
        
	        if (success == 1) {
	        	results = json.getJSONArray(TAG_RESULTS);
	        	MainActivity.db.deleteAnswer();
	        	MainActivity.db.deleteLessons();
	        	MainActivity.db.deleteQuestion();
	        	for (int i = 0; i < results.length(); i++) {
	        		JSONArray jsonArray2 = results.getJSONArray(i); 
	        		for(int j = 0; j < jsonArray2.length(); j++) {
	        			JSONObject jsonArray3 = jsonArray2.getJSONObject(j); 
	        			if(i==0){
	        				MainActivity.db.createQuestion(jsonArray3.getInt("id") , jsonArray3.getInt("lesson_id"), jsonArray3.getString("thequestion"));
	        			}
	        			else if(i==1){
	        				MainActivity.db.createAnswer(jsonArray3.getInt("answer_id"), jsonArray3.getString("theanswer"), jsonArray3.getInt("right"), jsonArray3.getInt("question_id"));
	        			}
	        			else if(i==2){
	        				MainActivity.db.createLesson(jsonArray3.getInt("id"),jsonArray3.getString("name"), "A");
	        			}
	        		}

                    // Storing each json item in variable

                }
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

