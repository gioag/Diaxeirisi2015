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
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{
	
	private EditText username=null;
	private EditText password=null;
	private String url=null;
	private String mode=null;
	private BroadcastReceiver myReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		username = (EditText)findViewById(R.id.editUser);
		password = (EditText)findViewById(R.id.editPass);
		url=getIntent().getStringExtra("url");
		mode=getIntent().getStringExtra("mode");
		
		
		
		myReceiver = new BroadcastReceiver() {		
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				finish();
			}
		};
	    this.getApplicationContext().registerReceiver(myReceiver,new IntentFilter("login_finish"));
	}
	
	
	public void registeract(View view) {
		//Start RegisterActivity
		if(isNetworkAvailable()){	
			Intent iR = new Intent(this, RegisterActivity.class);
			iR.putExtra("url", url);
			iR.putExtra("mode", mode);
			iR.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(iR);
			finish();
		}
		else{
			Toast.makeText(getApplicationContext(), "Δεν υπάρχει σύνδεση σε δίκτυο!", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	public void login(View view){
		//Start WSlogin with proper arguments
		if(isNetworkAvailable()){	
			Intent msgIntent = new Intent(this, WSlogin.class);
			msgIntent.putExtra("username", username.getText().toString());
			msgIntent.putExtra("password", password.getText().toString());
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

