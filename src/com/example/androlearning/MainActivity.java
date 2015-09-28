package com.example.androlearning;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dbhelper.DatabaseHelper;

public class MainActivity extends Activity {

	public static DatabaseHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_mode);

		//Creating the database helper
		db = new DatabaseHelper(getApplicationContext());
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void classmode(View view) {
		if(isNetworkAvailable()){
			Intent iR = new Intent(this, LoginActivity.class);
			iR.putExtra("url","http://10.0.0.1/php/");
			//iR.putExtra("url","http://192.168.1.2/php/");
			iR.putExtra("mode","0");
			iR.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(iR);
			finish();
		}
		else{
			Toast.makeText(getApplicationContext(), "Δεν υπάρχει σύνδεση σε δίκτυο!", Toast.LENGTH_SHORT).show();

		}
	}
	
	public void onlinemode(View view) {
		if(isNetworkAvailable()){
			Intent iR = new Intent(this, LoginActivity.class);
			iR.putExtra("url","http://cgi.di.uoa.gr/~std10142/php/");
			iR.putExtra("mode","1");
			iR.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(iR);
			Intent msgIntent = new Intent(this, WSdbrefresh.class);
			startService(msgIntent);
			finish();
		}
		else{
			Toast.makeText(getApplicationContext(), "Δεν υπάρχει σύνδεση σε δίκτυο!", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void offlinemode(View view) {
		if(isNetworkAvailable()){
			Intent msgIntent = new Intent(this, WSdbrefresh.class);
			startService(msgIntent);
		}
		Intent iR = new Intent(this, ChooseLessonActivity.class);
		iR.putExtra("url","http://cgi.di.uoa.gr/~std10142/php/");
		iR.putExtra("mode","2");
		iR.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(iR);
		finish();
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
		 Intent startMain = new Intent(Intent.ACTION_MAIN);
		    startMain.addCategory(Intent.CATEGORY_HOME);
		    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    startActivity(startMain);
	 }

}
