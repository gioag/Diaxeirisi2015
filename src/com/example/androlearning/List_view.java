package com.example.androlearning;

import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class List_view extends Fragment{
	
	private BroadcastReceiver myReceiver;
	private String username=null;
	private String question;
	private int nanswers;
	private String[] answers;
	private String url;
	private int game;
	private String arithmos;
	private String mode;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		final View rootView = inflater.inflate(R.layout.activiti_tabs, container, false);
		
		
		//get arguments 
		mode=getArguments().getString("mode");
		if(!mode.equals("2")){
			username = getArguments().getString("username");
			url =getArguments().getString("url");
			game=getArguments().getInt("game");
		}
		arithmos=getArguments().getString("arithmos");
		question = getArguments().getString("question");
		nanswers = getArguments().getInt("nanswers");
		
		
		//create array of possible answers
		answers=new String[nanswers];
		
		//initialize array of possible answers
		for(int i=0;i<nanswers;i++)
			answers[i]=getArguments().getString("answer"+(i+1));
		
		
		// Set Layout
		final LinearLayout lm = (LinearLayout) rootView.findViewById(R.id.linLay);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//params.setMargins(70, 10, 50, 10);
		params.gravity=Gravity.CENTER_HORIZONTAL;
		
		// Create TextView for question
        TextView product = new TextView(getActivity());
        product.setText(question);
        product.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        product.setTextSize(40);
        product.setLayoutParams(params);
        lm.addView(product);
        
        

        // Create #answers buttons
        for(int j=0;j<nanswers;j++){
        	Button btn = new Button(getActivity());
        	
            // Give button an ID
            btn.setId(j+1);
            btn.setText(answers[j]);
            
            // set the layoutParams of the button
            btn.setLayoutParams(params);
            final Button button=btn;
            
            // Set on-click listener to button
            btn.setOnClickListener(new View.OnClickListener() {
        	    @Override
        	    public void onClick(View v) {
        	    	ContestTabsActivity.check++;
        	    	// Call WSsend Answer with the proper Extras
        	    	Intent msgIntent = new Intent(getActivity(), WSsendAnswer.class);
        			msgIntent.putExtra("username", username);
        			msgIntent.putExtra("question", question);
        			msgIntent.putExtra("url", url);
        			msgIntent.putExtra("mode", mode);
        			msgIntent.putExtra("game", game);
        			msgIntent.putExtra("id", arithmos);
        			msgIntent.putExtra("answer", button.getText().toString());
        			
        			getActivity().startService(msgIntent);
        			
        			//Check the log for the selected answer
        			Log.d("ANSWER!",button.getText().toString());
        			
        			//Remove question and answer
        			lm.removeAllViews();
        			
        			
        			//Create new layout to write the points given by the answer
        			
        	        
        	        // Create receiver to get the points given by the selected answer
        	     	myReceiver = new BroadcastReceiver() {		
        	     		@Override
        	    		public void onReceive(Context context, Intent intent) {
        	     		//	lm.removeAllViews();
        	     			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                			params.gravity=Gravity.CENTER_HORIZONTAL;
                			TextView product = new TextView(getActivity());
                	        product.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                	        product.setTextSize(50);
                	        product.setLayoutParams(params);
                	        lm.addView(product);
                	        if(intent.getIntExtra("point", 0)!=0){
	        	    			TextView production = new TextView(getActivity());
	        	    			if(!mode.equals("2"))
	        	    				production.setText("Μπράβο!!\nΒοήθησες την ομάδα σου κερδίζοντας "+ intent.getIntExtra("point", 5)+ " πόντους!!");
	        	    			else 
	        	    				production.setText("Μπράβο!!\nKέρδισες "+ intent.getIntExtra("point", 5)+ " πόντους!!");

	        	       	        production.setGravity(Gravity.CENTER | Gravity.BOTTOM);
	        	       	        production.setTextSize(50);
	        	       	        production.setLayoutParams(params);
	        	       	        lm.addView(production);
                	        }
                	        else
                	        {
                	        	product.setText("Θα"+'\n' + "μπορούσες"+'\n'+ "να απαντήσεις"+'\n'+ "καλύτερα");
                    	    }
        	    			Log.d("RECEIVE","rec");
        	    		}
        	    	};
        	    	if(mode.equals("2"))
        	    		getActivity().getApplicationContext().registerReceiver(myReceiver,new IntentFilter(arithmos+MainActivity.db.getGame()));
        	    	else
        	    		getActivity().getApplicationContext().registerReceiver(myReceiver,new IntentFilter(arithmos+game));
        	        
        	        
        	    }
        	});
            lm.addView(btn);
        }	
    
		return rootView;
	}

	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

}