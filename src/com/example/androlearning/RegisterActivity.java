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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends Activity{
	private EditText username=null;
	private EditText password=null;
	private EditText flname=null;
	private Spinner school=null;
	private String url=null;
	// Receiver gia to Broadcast 
	private IntentFilter iF = new IntentFilter("schools_added");
	private BroadcastReceiver myReceiver;
	public static List<String> list = new ArrayList<String>();
	private ArrayAdapter<String> dataAdapter;
	private BroadcastReceiver Receiver;
	private String mode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		flname=(EditText)findViewById(R.id.editOnmo);
		username = (EditText)findViewById(R.id.editUsername);
		password = (EditText)findViewById(R.id.editPassword);
		school=(Spinner)findViewById(R.id.spinner1);
		url=getIntent().getStringExtra("url");
		mode=getIntent().getStringExtra("mode");
		
		dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dataAdapter.setNotifyOnChange(true);
		school.setAdapter(dataAdapter);
		
		Intent msgIntent = new Intent(this, WSgetschools.class);
		msgIntent.putExtra("url", url);
		//Toast.makeText(getApplicationContext(), "Starting service with credentials "+username.getText().toString()+" "+password.getText().toString(), Toast.LENGTH_SHORT).show();
		startService(msgIntent);
		
		myReceiver = new BroadcastReceiver() {		
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				dataAdapter.notifyDataSetChanged();
				school.setAdapter(dataAdapter);
			}
		};
	    this.getApplicationContext().registerReceiver(myReceiver,iF);
	    
	    Receiver = new BroadcastReceiver() {		
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				finish();
			}
		};
	    this.getApplicationContext().registerReceiver(Receiver,new IntentFilter("register_finish"));
		
		
	}
	
	public void register(View view){
		//TODO What happens when i login
		if(isNetworkAvailable()){
			Intent msgIntent = new Intent(this, WSregister.class);
			msgIntent.putExtra("flname", flname.getText().toString());
			msgIntent.putExtra("username", username.getText().toString());
			msgIntent.putExtra("password", password.getText().toString());
			msgIntent.putExtra("school", school.getSelectedItem().toString());
			msgIntent.putExtra("url", url);
			msgIntent.putExtra("mode", mode);
			startService(msgIntent);
		}
		else{
			Toast.makeText(getApplicationContext(), "Δεν υπάρχει σύνδεση σε δίκτυο!", Toast.LENGTH_SHORT).show();
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
