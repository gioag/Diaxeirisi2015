package com.example.androlearning;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class ChooseLessonActivity extends Activity {
	
	private Spinner Lesson;
	public static List<String> list = new ArrayList<String>();
	private ArrayAdapter<String> dataAdapter;
	private String url=null;
	private String username=null;
	private IntentFilter iF = new IntentFilter("lessons_added");
	private BroadcastReceiver myReceiver;
	private String mode;
	private BroadcastReceiver Receiver;
	private BroadcastReceiver Receive;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_lesson);
		
		//Get Intent arguments 
		url=getIntent().getStringExtra("url");
		mode=getIntent().getStringExtra("mode");
		username=getIntent().getStringExtra("username");
		
		
		Lesson=(Spinner) findViewById(R.id.lessonSpinner);	
		
		// Mode ==  2  => It is a single-player game
		if(mode.equals("2")){
			list.clear();
			list.addAll(MainActivity.db.getLessons()); //get lessons from sqlite
		}
		
		
		
		dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dataAdapter.setNotifyOnChange(true);
		Lesson.setAdapter(dataAdapter);
		
		if(! mode.equals("2")){
			//if it is not a single-player game
			//calls WSgetlessons to get the lessons from the database
			do{
				if(isNetworkAvailable()){	
					Intent msgIntent = new Intent(this, WSgetlessons.class);
					msgIntent.putExtra("url", url);
					msgIntent.putExtra("username", username);
					startService(msgIntent);
				}
			}while(!isNetworkAvailable());
			
			//Receive what WSgetlessons broadcasted and add it to the spinner
			myReceiver = new BroadcastReceiver() {		
				@Override
				public void onReceive(Context context, Intent intent) {
					dataAdapter.notifyDataSetChanged();
					Lesson.setAdapter(dataAdapter);
				}
			};
		    this.getApplicationContext().registerReceiver(myReceiver,iF);
		}
		Receiver = new BroadcastReceiver() {		
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				finish();
			}
		};
	    this.getApplicationContext().registerReceiver(Receiver,new IntentFilter("choose_finish"));
	    
	    Receive = new BroadcastReceiver() {		
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Δεν έχει δημιουργηθεί ακόμα παιχνίδι!", Toast.LENGTH_SHORT).show();

			}
		};
	    this.getApplicationContext().registerReceiver(Receive,new IntentFilter("no_game"));
	}
	
	public void start(View view){
		if(!mode.equals("2")){
			//Start WSsendlesson with proper arguments
			if(isNetworkAvailable()){
				Intent msgIntent = new Intent(this, WSsendlesson.class);
				msgIntent.putExtra("username", username);
				msgIntent.putExtra("lesson", Lesson.getSelectedItem().toString());
				msgIntent.putExtra("url", url);
				msgIntent.putExtra("mode", mode);
				startService(msgIntent);
			}
			else{
				Toast.makeText(getApplicationContext(), "Δεν υπάρχει σύνδεση σε δίκτυο!", Toast.LENGTH_SHORT).show();
			}
		}
		else{
			Intent msgIntent = new Intent(this, waitForContestActivity.class);
			msgIntent.putExtra("username", username);
			msgIntent.putExtra("url", url);
			msgIntent.putExtra("lesson", Lesson.getSelectedItem().toString());
			msgIntent.putExtra("mode", mode);
			startActivity(msgIntent);
			finish();
		}
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}	
	
	@Override
	 public void onBackPressed() {
	    //Display alert message when back button has been pressed
	     backButtonHandler();
	     return;
	 }
	 
	 public void backButtonHandler() {
	  	Intent iR = new Intent(this, MainActivity.class);
		iR.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(iR);
		finish();
	 }

}
