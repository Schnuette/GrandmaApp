package com.grandmaapp.activities;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.example.grandmaapp.R;
import com.grandmaapp.services.WishesService;

public class GrandmaActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grandma);
		
		//TODO read from sharedpreferences 

		//startWishesService();
		adjustGUI();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.grandma, menu);
		return true;
	}

	public void zeigeEinstellungen(View view) {

	}

	private void adjustGUI() {
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

		settingsBtn.getLayoutParams().width = (width - (width / 10));
		supplyBtn.getLayoutParams().width = (width - (width / 10));
		testBtn.getLayoutParams().width = (width - (width / 10));

		LinearLayout linLay = (LinearLayout) findViewById(R.id.tasksLinLay);
		Button txt1 = new Button(this);
		txt1.setText("hallo hallo");
		txt1.setBackgroundResource(R.drawable.button_selector);
		txt1.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT)));
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Blippo.ttf");
		txt1.setTypeface(font);
		txt1.setTextSize(30);
		linLay.addView(txt1);

		Button txt2 = new Button(this);
		txt2.setText("hallo hallo");
		txt2.setBackgroundResource(R.drawable.button_selector);
		txt2.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT)));
		linLay.addView(txt2);

		Button txt3 = new Button(this);
		txt3.setText("hallo hallo");
		txt3.setBackgroundResource(R.drawable.button_selector);
		txt3.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT)));
		linLay.addView(txt3);

		Button txt4 = new Button(this);
		txt4.setText("hallo hallo");
		txt4.setBackgroundResource(R.drawable.button_selector);
		txt4.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT)));
		linLay.addView(txt4);

	}

	private void startWishesService() {
		// Sekundentakt in dem der Service gestartet wird.
		int refreshInS = 60;

		Calendar cal = Calendar.getInstance();

		Intent intent = new Intent(this, WishesService.class);
		PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);

		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				refreshInS * 1000, pintent);
	}

	@Override
	protected void onDestroy() {
		// save current state to shared preferences
		SharedPreferences prefs = getSharedPreferences("grandmaapp", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		
		//add time, format DD.MM.YYYY HH:MM:SS
		editor.putString("time", DateFormat.getDateTimeInstance().format(
				new Date()));
		
		//TODO
		//add storage
		
		//TODO
		//add wishes
		
        editor.commit();

		super.onDestroy();
	}

}
