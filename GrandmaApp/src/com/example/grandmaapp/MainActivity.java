package com.example.grandmaapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		WashingShaker.getInstance( ).init( this );
		WashingShaker.getInstance( ).enable( );		
		
		SleepDetector.getInstance( ).init( this );
		SleepDetector.getInstance( ).enable( );
	}
	
	protected void onPause() {
        super.onPause();
        WashingShaker.getInstance( ).disable( );
        SleepDetector.getInstance( ).disable( );
    }

	protected void onResume() {
        super.onResume();
        WashingShaker.getInstance( ).enable( );
        SleepDetector.getInstance( ).enable( );
        SleepDetector.getInstance( ).reset( );
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
