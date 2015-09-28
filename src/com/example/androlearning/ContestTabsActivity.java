package com.example.androlearning;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class ContestTabsActivity extends FragmentActivity {

	private ViewPager pager;
	private TabsAdapter mTabsAdapter;
	private String username = null;
	private String url=null;
	private int game;
	private String mode;
	private BroadcastReceiver myReceiver;
	private IntentFilter iF=new IntentFilter("tabs_finish");
	public static int check=0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mode=getIntent().getStringExtra("mode");

		if(!mode.equals("2")){
			url=getIntent().getStringExtra("url");
			username = getIntent().getStringExtra("username");
			game=getIntent().getIntExtra("game", 0);
		}
				
		pager = new ViewPager(this);
		pager.setId(R.id.pager);
		pager.setOffscreenPageLimit(waitForContestActivity.ntabs-1);
		setContentView(pager);
		
		final ActionBar bar = getActionBar();
		Log.d("CONTEST Successful!", "");
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Log.d("CONTEST Successful!", "");
		mTabsAdapter = new TabsAdapter(this, pager);
		
		for(int i=0;i<waitForContestActivity.ntabs;i++){
			Bundle List_view_args = new Bundle();
			List_view_args.clear();
			if(!mode.equals("2")){
				List_view_args.putString("url", url);
				List_view_args.putString("username", username);
				List_view_args.putInt("game", game);
			}
			List_view_args.putString("mode", mode);
			List_view_args.putString("arithmos", ""+i);
			List_view_args.putString("question", waitForContestActivity.quest.get(i).getQuestion());
			List_view_args.putInt("nanswers", waitForContestActivity.quest.get(i).getAnswers().size());
			for(int j=0;j<waitForContestActivity.quest.get(i).getAnswers().size();j++)
				List_view_args.putString("answer"+(j+1), waitForContestActivity.quest.get(i).getAnswers().get(j));

			mTabsAdapter.addTab(bar.newTab().setText("Ερώτηση "+ (i+1)), List_view.class, List_view_args);
		}	
		
		myReceiver = new BroadcastReceiver() {		
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
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
		ContestTabsActivity.check=0;
	  	Intent iR = new Intent(this, ChooseLessonActivity.class);
	  	iR.putExtra("mode", mode);
	  	iR.putExtra("username", username);
	  	iR.putExtra("url", url);
		iR.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(iR);
		getActionBar().removeAllTabs();
		finish();
	 }
}
