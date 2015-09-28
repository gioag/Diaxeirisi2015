package com.example.androlearning;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultsActivity extends Activity {

	private ArrayList<String> names;
	private ArrayList<String> points;
	private String url;
	private String username;
	private String mode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiti_tabs);
		names=getIntent().getStringArrayListExtra("names");
		points=getIntent().getStringArrayListExtra("points");
		username=getIntent().getStringExtra("username");
		url=getIntent().getStringExtra("url");
		mode=getIntent().getStringExtra("mode");
		
final LinearLayout lm = (LinearLayout) findViewById(R.id.linLay);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity=Gravity.CENTER_HORIZONTAL;
		
		TextView product = new TextView(this);
        product.setText("Αποτελέσματα");
        product.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        product.setTextSize(45);
        product.setLayoutParams(params);
        lm.addView(product);
		
		for(int i=0;i<names.size();i++){
			// Create TextView
	        TextView productio = new TextView(this);
	        productio.setText(names.get(i)+"  "+points.get(i));
	        productio.setTextSize(40);
	        productio.setLayoutParams(params);
	        lm.addView(productio);
		}
		
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
