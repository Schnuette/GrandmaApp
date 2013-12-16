package com.example.grandmaapp;

import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class GrandmaActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grandma);
		
		adjustGUI();
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.grandma, menu);
		return true;
	}
	
	public void zeigeEinstellungen(View view){
		 
	}
	
	private void adjustGUI(){
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels / 2;
		int width = displaymetrics.widthPixels / 3;
		ImageView grandmaImgV = (ImageView) findViewById(R.id.grandmaImgView);
		grandmaImgV.getLayoutParams().height = height;
		
		grandmaImgV.setImageResource(R.drawable.grandma_zufrieden);
		
		Button settingsBtn = (Button) findViewById(R.id.einstellungenBtn);
		Button supplyBtn = (Button) findViewById(R.id.vorraeteBtn);
		Button testBtn = (Button) findViewById(R.id.testBtn);
		
		settingsBtn.setBackgroundResource(R.drawable.settings_selector);
		supplyBtn.setBackgroundResource(R.drawable.supply_selector);
		testBtn.setBackgroundResource(R.drawable.test_selector);
		
		settingsBtn.getLayoutParams().width = (width - (width/10));
		supplyBtn.getLayoutParams().width = (width - (width/10));
		testBtn.getLayoutParams().width = (width - (width/10));
		
		LinearLayout linLay = (LinearLayout) findViewById(R.id.tasksLinLay);
		Button txt1 = new Button(this);
		txt1.setText("hallo hallo");
		txt1.setBackgroundResource(R.drawable.button_selector);
		txt1.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)));
        linLay.addView(txt1);
        
        Button txt2 = new Button(this);
        txt2.setText("hallo hallo");
        txt2.setBackgroundResource(R.drawable.button_selector);
        txt2.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)));
        linLay.addView(txt2);
        
        Button txt3 = new Button(this);
        txt3.setText("hallo hallo");
        txt3.setBackgroundResource(R.drawable.button_selector);
        txt3.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)));
        linLay.addView(txt3);
        
        Button txt4 = new Button(this);
        txt4.setText("hallo hallo");
        txt4.setBackgroundResource(R.drawable.button_selector);
        txt4.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)));
        linLay.addView(txt4);
		
	}

}
