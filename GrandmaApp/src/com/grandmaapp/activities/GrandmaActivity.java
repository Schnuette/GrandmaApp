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
import android.preference.PreferenceManager;
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
import com.grandmaapp.model.Eat;
import com.grandmaapp.model.Grandma;
import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.model.Request;
import com.grandmaapp.model.Storeroom.Dish;
import com.grandmaapp.notification.Notifications;
import com.grandmaapp.services.WishesService;

public class GrandmaActivity extends Activity {

	Grandma grandma;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grandma);
		
		grandma = new Grandma(this);
		init();
		
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
	
	public void init(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		int numSchnitzel = prefs.getInt("StoreSchnitzel", -1);
		if(numSchnitzel > -1){
			grandma.getStoreroom().getFood().put(Dish.SCHNITZEL, numSchnitzel);
		}
		int numNoodles = prefs.getInt("StoreNoodles", -1);
		if(numNoodles > -1){
			grandma.getStoreroom().getFood().put(Dish.NOODLES, numNoodles);
		}
		int numDoener = prefs.getInt("StoreDoener", -1);
		if(numDoener > -1){
			grandma.getStoreroom().getFood().put(Dish.DOENER, numDoener);
		}
		int numPizza = prefs.getInt("StorePizza", -1);
		if(numPizza > -1){
			grandma.getStoreroom().getFood().put(Dish.PIZZA, numPizza);
		}
		int numBreakfast = prefs.getInt("StoreBreakfast", -1);
		if(numBreakfast > -1){
			grandma.getStoreroom().getFood().put(Dish.BREAKFAST, numBreakfast);
		}
		int numSupper = prefs.getInt("StoreSupper", -1);
		if(numSupper > -1){
			grandma.getStoreroom().getFood().put(Dish.SUPPER, numSupper);
		}
		int numWater = prefs.getInt("StoreWater", -1);
		if(numWater > -1){
			grandma.getStoreroom().setWaterBottles(numWater);
		}
		int numClothes = prefs.getInt("StoreClothes", -1);
		if(numWater > -1){
			grandma.getStoreroom().setCleanClothes(numClothes);
		}
		
		grandma.getStoreroom().calcDinnerSum();
		
		int time = prefs.getInt(Requests.EAT.toString(), -1);
		if(time > -1){
			Eat request = new Eat();
			String foodWish = prefs.getString("FoodWish", null);
			if(foodWish.equals(Dish.BREAKFAST.toString())){
				request.setFoodWish(Dish.BREAKFAST);
			}else if(foodWish.equals(Dish.DOENER.toString())){
				request.setFoodWish(Dish.DOENER);
			}else if(foodWish.equals(Dish.NOODLES.toString())){
				request.setFoodWish(Dish.NOODLES);
			}else if(foodWish.equals(Dish.PIZZA.toString())){
				request.setFoodWish(Dish.PIZZA);
			}else if(foodWish.equals(Dish.SCHNITZEL.toString())){
				request.setFoodWish(Dish.SCHNITZEL);
			}else if(foodWish.equals(Dish.SUPPER.toString())){
				request.setFoodWish(Dish.SUPPER);
			}
			request.setTimeMS(time);
			grandma.addRequest(request);
		}
		
				
	}

	public void zeigeEinstellungen(View view) {

	}
	
	@Override
	protected void onNewIntent(Intent intent)
	{
		String message = intent.getStringExtra( "Notify" );
		if(message != null && message.equals( "reset" ))
		{
			Notifications.resetMessageCounter( );
		}
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
