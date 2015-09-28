package com.example.androlearning;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.models.Question;

import java.util.ArrayList;
import java.util.List;

public class waitForContestActivity extends Activity {
	
	private String url=null;
	private String username=null;
	private IntentFilter iF = new IntentFilter("contest_started");
	private BroadcastReceiver myReceiver;
	private String lesson;
	private String mode;
	public static List<Question> quest = new ArrayList<Question>();
	public static int ntabs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wait_for_contest);
		
		
		lesson=getIntent().getStringExtra("lesson");
		mode=getIntent().getStringExtra("mode");
		url=getIntent().getStringExtra("url");
		username=getIntent().getStringExtra("username");
		
		
		Intent msgIntent = new Intent(this, WScheckcontest.class);
		if(!mode.equals("2")){
			msgIntent.putExtra("url", url);
			msgIntent.putExtra("username", username);
		}
		msgIntent.putExtra("lesson", lesson);
		msgIntent.putExtra("mode", mode);
		
		startService(msgIntent);
		
		myReceiver = new BroadcastReceiver() {		
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				Intent chooseLesson = new Intent(getApplicationContext(), ContestTabsActivity.class);
	        	
	        	chooseLesson.putExtra("mode", mode);
	        	if(mode.equals("2")) 
	        		MainActivity.db.createGame();
	        	if(!mode.equals("2")){
	        		chooseLesson.putExtra("url", url);
		        	chooseLesson.putExtra("username", username);
		        	chooseLesson.putExtra("game", intent.getIntExtra("game", 0));
	        	}
	        	chooseLesson.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(chooseLesson);
				finish();
				
			}
		};
	    this.getApplicationContext().registerReceiver(myReceiver,iF);
		
	}
	
	@Override
	 public void onBackPressed() {
	    //Display alert message when back button has been pressed
	     backButtonHandler();
	     return;
	 }
	 
	 public void backButtonHandler() {
	  	Intent iR = new Intent(this, ChooseLessonActivity.class);
	  	iR.putExtra("mode", mode);
	  	iR.putExtra("username", username);
	  	iR.putExtra("url", url);
		iR.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(iR);
		finish();
	 }

}
