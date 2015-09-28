package com.example.androlearning;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.json.JSONParser;
import com.example.models.Question;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WScheckcontest extends IntentService {

	private String username = null;
	private String lesson = null;
	private String url=null;
	
	// JSON parser class
    JSONParser jsonParser = new JSONParser();
	private String mode;
    
    private static final String TAG_SUCCESS = "success";
  
	public WScheckcontest() {
		super("WScheckcontest");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		//MainActivity.isServiceon = 1;
		Log.d("WScheckcontest", "Service started.");
		lesson = intent.getStringExtra("lesson");
		mode = intent.getStringExtra("mode");
		if(!mode.equals("2")){
			do{
				if(isNetworkAvailable()){
					url=intent.getStringExtra("url")+"checkcontest.php";
					username = intent.getStringExtra("username");
					
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("username", username));
					params.add(new BasicNameValuePair("lesson", lesson));
					boolean i = true;
				
					Log.d("Check ",username);
					Log.d("contest",lesson);
					if(mode.equals("0")){
						while(i==true){
							JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
							
							// check your log for json response
					        Log.d("Check contest attempt", json.toString());
					
					        // json success tag
					        int success = 0;
					        int game;
							try {
								success = json.getInt(TAG_SUCCESS);
						        if (success == 1) {
						        	waitForContestActivity.quest.clear();
						        	game= json.getInt("game");
						        	Log.d("Check contest Successful!", json.toString());
						        	JSONArray jArray=json.getJSONArray("questions");
						        	String question = null;
						        	waitForContestActivity.ntabs=jArray.length();
						        	for (int j = 0; j < jArray.length(); j++) {       
				                        JSONArray jsonArray2 = jArray.getJSONArray(j); 
				                        List<String> answers=new ArrayList<String>();
				                        
				                        for(int k=0;k<jsonArray2.length();k++){
				                        	if(k==0)
				                        		question = jsonArray2.getString(k);
				                        	else
				                        		answers.add(jsonArray2.getString(k));
				                        		
				                        }
				                        
				                        waitForContestActivity.quest.add(new Question(question,answers));
				                }
						        	
						        	Intent bi = new Intent();
						        	bi.putExtra("game", game);
					        		bi.setAction("contest_started");
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
					else if(mode.equals("1")){
						url=intent.getStringExtra("url")+"onlinecontest.php";
						JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
						
						// check your log for json response
				        Log.d("Check contest attempt", json.toString());
				
				        // json success tag
				        int success = 0;
				       int game;
						try {
							
							success = json.getInt(TAG_SUCCESS);
					        if (success == 1) {
					        	waitForContestActivity.quest.clear();
					        	game= json.getInt("game");
					        	Log.d("Check contest Successful!", json.toString());
					        	JSONArray jArray=json.getJSONArray("questions");
					        	String question = null;
					        	waitForContestActivity.ntabs=jArray.length();
					        	for (int j = 0; j < jArray.length(); j++) {       
			                        JSONArray jsonArray2 = jArray.getJSONArray(j); 
			                        List<String> answers=new ArrayList<String>();
			                        
			                        for(int k=0;k<jsonArray2.length();k++){
			                        	if(k==0)
			                        		question = jsonArray2.getString(k);
			                        	else
			                        		answers.add(jsonArray2.getString(k));
			                        		
			                        }
			                        
			                        waitForContestActivity.quest.add(new Question(question,answers));
			                }
					        	
					        	
					        	Intent bi = new Intent();
					        	bi.putExtra("game", game);
				        		bi.setAction("contest_started");
				        		sendBroadcast(bi);
					        	
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
			}while(!isNetworkAvailable());
		}
		else{
			List<String> erwt=new ArrayList<String>();
			List<Integer> ar=new ArrayList<Integer>();
			ar.clear();
			erwt.clear();
			erwt.addAll(MainActivity.db.getQuestionsByLesson(MainActivity.db.getLessonIdByName(lesson)));
			waitForContestActivity.ntabs=5;
			int j = 0;
			while (j<waitForContestActivity.ntabs){
				Random rand = new Random();

			    // nextInt is normally exclusive of the top value,
			    // so add 1 to make it inclusive
			    int randomNum = rand.nextInt(erwt.size());
			    if(ar.contains(randomNum)){
			    	continue;
			    }
			    j++;
			    ar.add(randomNum);
			    
			}
			waitForContestActivity.quest.clear();
			for (j = 0; j < waitForContestActivity.ntabs; j++)   
                waitForContestActivity.quest.add(new Question(erwt.get(ar.get(j)),MainActivity.db.getAnswersByQuestionId(MainActivity.db.getQuestionIdByName(erwt.get(ar.get(j))))));
			Intent bi = new Intent();
    		bi.setAction("contest_started");
    		sendBroadcast(bi);
		}
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}	
}