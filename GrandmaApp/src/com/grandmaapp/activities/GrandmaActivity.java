package com.grandmaapp.activities;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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
import com.grandmaapp.model.CleanFlat;
import com.grandmaapp.model.Drink;
import com.grandmaapp.model.Eat;
import com.grandmaapp.model.Grandma;
import com.grandmaapp.model.Medicine;
import com.grandmaapp.model.Shopping;
import com.grandmaapp.model.SuitUp;
import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.model.Medicine.Daytime;
import com.grandmaapp.model.Request;
import com.grandmaapp.model.Sleep;
import com.grandmaapp.model.Storeroom.Dish;
import com.grandmaapp.model.WashClothes;
import com.grandmaapp.model.WashDishes;
import com.grandmaapp.notification.Notifications;
import com.grandmaapp.services.WishesService;

public class GrandmaActivity extends Activity {

	Grandma grandma;
	HashMap<String, Button> requestList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grandma);

		grandma = new Grandma(this);
		requestList = new HashMap<String, Button>();
		init();

		// TODO read from sharedpreferences

		// startWishesService();
		adjustGUI();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.grandma, menu);
		return true;
	}

	public void init() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		int maxDinner = grandma.getStoreroom().getMAXDINNER();
		int maxBreakfastSupper = grandma.getStoreroom().getMAXBREAKFASTSUPPER();
		int numSchnitzel = prefs.getInt("StoreSchnitzel", -1);
		if (numSchnitzel > -1) {
			grandma.getStoreroom().getFood().put(Dish.SCHNITZEL, numSchnitzel);
		} else {
			grandma.getStoreroom().getFood().put(Dish.SCHNITZEL, maxDinner);
		}
		int numNoodles = prefs.getInt("StoreNoodles", -1);
		if (numNoodles > -1) {
			grandma.getStoreroom().getFood().put(Dish.NOODLES, numNoodles);
		} else {
			grandma.getStoreroom().getFood().put(Dish.NOODLES, maxDinner);
		}
		int numDoener = prefs.getInt("StoreDoener", -1);
		if (numDoener > -1) {
			grandma.getStoreroom().getFood().put(Dish.DOENER, numDoener);
		} else {
			grandma.getStoreroom().getFood().put(Dish.DOENER, maxDinner);
		}
		int numPizza = prefs.getInt("StorePizza", -1);
		if (numPizza > -1) {
			grandma.getStoreroom().getFood().put(Dish.PIZZA, numPizza);
		} else {
			grandma.getStoreroom().getFood().put(Dish.PIZZA, maxDinner);
		}
		int numBreakfast = prefs.getInt("StoreBreakfast", -1);
		if (numBreakfast > -1) {
			grandma.getStoreroom().getFood().put(Dish.BREAKFAST, numBreakfast);
		} else {
			grandma.getStoreroom().getFood()
					.put(Dish.BREAKFAST, maxBreakfastSupper);
		}
		int numSupper = prefs.getInt("StoreSupper", -1);
		if (numSupper > -1) {
			grandma.getStoreroom().getFood().put(Dish.SUPPER, numSupper);
		} else {
			grandma.getStoreroom().getFood()
					.put(Dish.SUPPER, maxBreakfastSupper);
		}
		int numWater = prefs.getInt("StoreWater", -1);
		if (numWater > -1) {
			grandma.getStoreroom().setWaterBottles(numWater);
		} else {
			grandma.getStoreroom().setWaterBottles(
					grandma.getStoreroom().getMAXWATER());
		}

		int numClothes = prefs.getInt("StoreClothes", -1);
		if (numClothes > -1) {
			grandma.getStoreroom().setCleanClothes(numClothes);
		} else {
			grandma.getStoreroom().setCleanClothes(
					grandma.getStoreroom().getMAXCLEANCLOTHES());
		}

		grandma.getStoreroom().calcDinnerSum();

		long time = prefs.getInt(Requests.EAT.toString(), -1);
		if (time > -1) {
			Eat request = new Eat();
			String foodWish = prefs.getString("FoodWish", null);
			if (foodWish.equals(Dish.BREAKFAST.toString())) {
				request.setFoodWish(Dish.BREAKFAST);
			} else if (foodWish.equals(Dish.DOENER.toString())) {
				request.setFoodWish(Dish.DOENER);
			} else if (foodWish.equals(Dish.NOODLES.toString())) {
				request.setFoodWish(Dish.NOODLES);
			} else if (foodWish.equals(Dish.PIZZA.toString())) {
				request.setFoodWish(Dish.PIZZA);
			} else if (foodWish.equals(Dish.SCHNITZEL.toString())) {
				request.setFoodWish(Dish.SCHNITZEL);
			} else if (foodWish.equals(Dish.SUPPER.toString())) {
				request.setFoodWish(Dish.SUPPER);
			}
			request.setTimeMS(time);
			grandma.addRequest(request);
		}
		time = prefs.getInt(Requests.CLEANFLAT.toString(), -1);
		if (time > -1) {
			CleanFlat request = new CleanFlat();
			request.setTimeMS(time);
			grandma.addRequest(request);
		}
		time = prefs.getInt(Requests.DRINK.toString(), -1);
		if (time > -1) {
			Drink request = new Drink();
			request.setTimeMS(time);
			grandma.addRequest(request);
		}
		time = prefs.getInt(Requests.MEDICINE.toString(), -1);
		if (time > -1) {
			Medicine request = new Medicine();
			String medWish = prefs.getString("MedWish", null);
			if (medWish.equals(Daytime.MORNING.toString())) {
				request.setDaytime(Daytime.MORNING);
			}
			if (medWish.equals(Daytime.NOON.toString())) {
				request.setDaytime(Daytime.NOON);
			}
			if (medWish.equals(Daytime.EVENING.toString())) {
				request.setDaytime(Daytime.EVENING);
			}
			request.setTimeMS(time);
			grandma.addRequest(request);
		}
		time = prefs.getInt(Requests.SHOPPING.toString(), -1);
		if (time > -1) {
			Shopping request = new Shopping();
			request.setTimeMS(time);
			grandma.addRequest(request);
		}
		time = prefs.getInt(Requests.SLEEP.toString(), -1);
		if (time > -1) {
			Sleep request = new Sleep();
			request.setTimeMS(time);
			grandma.addRequest(request);
		}
		time = prefs.getInt(Requests.SUITUP.toString(), -1);
		if (time > -1) {
			SuitUp request = new SuitUp();
			request.setTimeMS(time);
			grandma.addRequest(request);
		}
		time = prefs.getInt(Requests.WASHCLOTHES.toString(), -1);
		if (time > -1) {
			WashClothes request = new WashClothes();
			request.setTimeMS(time);
			grandma.addRequest(request);
		}
		time = prefs.getInt(Requests.WASHDISHES.toString(), -1);
		if (time > -1) {
			WashDishes request = new WashDishes();
			request.setTimeMS(time);
			grandma.addRequest(request);
		}

	}

	public void zeigeEinstellungen(View view) {

	}

	@Override
	protected void onNewIntent(Intent intent) {
		String message = intent.getStringExtra("Notify");
		if (message != null && message.equals("reset")) {
			Notifications.resetMessageCounter();
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
		Typeface font = Typeface.createFromAsset(getAssets(),
				"fonts/Blippo.ttf");
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
	
	void addRequestButton(Request request){
		//steffen fragen wegen name/typ für button
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
		SharedPreferences prefs = getSharedPreferences("grandmaapp",
				Context.MODE_PRIVATE);
		Editor editor = prefs.edit();

		// add time, format DD.MM.YYYY HH:MM:SS
		editor.putString("time",
				DateFormat.getDateTimeInstance().format(new Date()));

		// TODO
		// add storage

		// TODO
		// add wishes

		editor.commit();

		super.onDestroy();
	}

}
