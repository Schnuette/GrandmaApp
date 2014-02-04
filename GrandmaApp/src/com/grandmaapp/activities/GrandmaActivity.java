package com.grandmaapp.activities;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.example.grandmaapp.R;
import com.grandmaapp.model.CleanFlat;
import com.grandmaapp.model.CountDown;
import com.grandmaapp.model.Drink;
import com.grandmaapp.model.Eat;
import com.grandmaapp.model.Game;
import com.grandmaapp.model.Grandma;
import com.grandmaapp.model.Grandma.State;
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
import com.grandmaapp.services.WishesReceiver;
import com.grandmaapp.services.WishesService;

public class GrandmaActivity extends Activity {

	Grandma grandma;
	HashMap<String, Button> requestList;
	AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grandma);
		
		test();
		
		grandma = new Grandma(this);
		requestList = new HashMap<String, Button>();
		grandma.init();
		for (Request request : grandma.getRequestsToHandle()) {
			addRequestButton(request);
		}

		if(grandma.getRequestsToHandle().isEmpty()){
			test();
		}
		
		WishesReceiver.setActivity( this );
		WishesReceiver.setGrandma( grandma );
		
		// TODO read from sharedpreferences

		// startWishesService();
		adjustGUI();
		initPopupView();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.grandma, menu);
		return true;
	}

	@Override
	protected void onNewIntent( Intent intent ) {
		super.onNewIntent( intent );
		
		// Checks if the intent has the extra named Notify with the message reset that indicates a counter reset request
		String message = intent.getStringExtra( "Notify" );
		if(message != null && message.equals( "reset" ))
		{
			Notifications.getInstance( ).resetMessageCounter();			
		}	
	}
	
	@Override
	protected void onResume(  ) {
		super.onResume(  );
		// Resets the message counter on app resume
		Notifications.getInstance( ).resetMessageCounter();			
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

		grandmaImgV.setImageResource(R.drawable.grandma_happy);

		Button workBtn = (Button) findViewById(R.id.workBtn);
		Button supplyBtn = (Button) findViewById(R.id.storeroomBtn);
		Button testBtn = (Button) findViewById(R.id.testBtn);

		workBtn.setBackgroundResource(R.drawable.work_selector);
		supplyBtn.setBackgroundResource(R.drawable.supply_selector);
		testBtn.setBackgroundResource(R.drawable.test_selector);
		
		workBtn.getLayoutParams().width = (width - (width / 10));
		supplyBtn.getLayoutParams().width = (width - (width / 10));
		testBtn.getLayoutParams().width = (width - (width / 10));

	}

	public void addRequestButton(Request request) {
		LinearLayout linLay = (LinearLayout) findViewById(R.id.tasksLinLay);
		Button requestButton = new Button(this);
		requestButton.setTag(request.kind());
		long runtimeInMS = grandma.HHMMtoMS(request.getRuntime());
		CountDown countdown = new CountDown(runtimeInMS, 1000, requestButton, request.getName());
		countdown.start();
		requestButton.setBackgroundResource(R.drawable.button_selector);
		requestButton.setLayoutParams((new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)));
		Typeface font = Typeface.createFromAsset(getAssets(),
				"fonts/Blippo.ttf");
		requestButton.setTypeface(font);
		requestButton.setTextSize(30);
		requestButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				grandma.handleRequest((Requests) v.getTag());
			}
		});
		linLay.addView(requestButton);
		requestList.put(request.kind().toString(), requestButton);
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
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = prefs.edit();

		// add time, format DD.MM.YYYY HH:MM:SS
		editor.putString("time",
				DateFormat.getDateTimeInstance().format(new Date()));

		editor.commit();

		super.onDestroy();
	}

	private void initPopupView() {
		Typeface font = Typeface.createFromAsset(getAssets(),
				"fonts/Blippo.ttf");
		View requestsPopupView = getLayoutInflater().inflate(
				R.layout.all_requests_popup, null);

		LinearLayout requestsList = (LinearLayout) requestsPopupView.findViewById(R.id.requestList);
		// add requestButtons
		Button cleanFlat = new Button(this);
		cleanFlat.setBackgroundResource(R.drawable.button_selector);
		cleanFlat.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT)));
		cleanFlat.setTypeface(font);
		cleanFlat.setTextSize(30);
		cleanFlat.setText("Wohnung putzen");
		cleanFlat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				if(grandma.handleRequest(Requests.CLEANFLAT) == false){
					CleanFlat clean = new CleanFlat();
					clean.setGrandma(grandma);
					clean.handleRequest(Requests.CLEANFLAT);
				}
			}
		});
		requestsList.addView(cleanFlat);

		Button drink = new Button(this);
		drink.setBackgroundResource(R.drawable.button_selector);
		drink.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT)));
		drink.setTypeface(font);
		drink.setTextSize(30);
		drink.setText("Trinken");
		drink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				if(grandma.handleRequest(Requests.DRINK) == false){
					Drink drink = new Drink();
					drink.setGrandma(grandma);
					drink.handleRequest(Requests.DRINK);
				}
			}
		});
		requestsList.addView(drink);

		Button eatSchnitzel = new Button(this);
		eatSchnitzel.setBackgroundResource(R.drawable.button_selector);
		eatSchnitzel.setLayoutParams((new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)));
		eatSchnitzel.setTypeface(font);
		eatSchnitzel.setTextSize(30);
		eatSchnitzel.setText("Essen (Schnitzel)");
		eatSchnitzel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				if(grandma.handleRequest(Requests.EAT) == false){
					Eat eat = new Eat(Dish.SCHNITZEL);
					eat.setGrandma(grandma);
					eat.handleRequest(Requests.EAT);
				}
			}
		});
		requestsList.addView(eatSchnitzel);

		Button eatNoodles = new Button(this);
		eatNoodles.setBackgroundResource(R.drawable.button_selector);
		eatNoodles.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT)));
		eatNoodles.setTypeface(font);
		eatNoodles.setTextSize(30);
		eatNoodles.setText("Essen (Nudeln)");
		eatNoodles.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				if(grandma.handleRequest(Requests.EAT) == false){
					Eat eat = new Eat(Dish.NOODLES);
					eat.setGrandma(grandma);
					eat.handleRequest(Requests.EAT);
				}
			}
		});
		requestsList.addView(eatNoodles);

		Button eatDoener = new Button(this);
		eatDoener.setBackgroundResource(R.drawable.button_selector);
		eatDoener.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT)));
		eatDoener.setTypeface(font);
		eatDoener.setTextSize(30);
		eatDoener.setText("Essen (Doener)");
		eatDoener.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				if(grandma.handleRequest(Requests.EAT) == false){
					Eat eat = new Eat(Dish.DOENER);
					eat.setGrandma(grandma);
					eat.handleRequest(Requests.EAT);
				}
			}
		});
		requestsList.addView(eatDoener);

		Button eatPizza = new Button(this);
		eatPizza.setBackgroundResource(R.drawable.button_selector);
		eatPizza.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT)));
		eatPizza.setTypeface(font);
		eatPizza.setTextSize(30);
		eatPizza.setText("Essen (Pizza)");
		eatPizza.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				if(grandma.handleRequest(Requests.EAT) == false){
					Eat eat = new Eat(Dish.PIZZA);
					eat.setGrandma(grandma);
					eat.handleRequest(Requests.EAT);
				}
			}
		});
		requestsList.addView(eatPizza);

		Button eatBreakfast = new Button(this);
		eatBreakfast.setBackgroundResource(R.drawable.button_selector);
		eatBreakfast.setLayoutParams((new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)));
		eatBreakfast.setTypeface(font);
		eatBreakfast.setTextSize(30);
		eatBreakfast.setText("Essen (Frühstück)");
		eatBreakfast.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				if(grandma.handleRequest(Requests.EAT) == false){
					Eat eat = new Eat(Dish.BREAKFAST);
					eat.setGrandma(grandma);
					eat.handleRequest(Requests.EAT);
				}
			}
		});
		requestsList.addView(eatBreakfast);

		Button eatSupper = new Button(this);
		eatSupper.setBackgroundResource(R.drawable.button_selector);
		eatSupper.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT)));
		eatSupper.setTypeface(font);
		eatSupper.setTextSize(30);
		eatSupper.setText("Essen (Abendbrot)");
		eatSupper.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				if(grandma.handleRequest(Requests.EAT) == false){
					Eat eat = new Eat(Dish.SUPPER);
					eat.setGrandma(grandma);
					eat.handleRequest(Requests.EAT);
				}
			}
		});
		requestsList.addView(eatSupper);

		Button game = new Button(this);
		game.setBackgroundResource(R.drawable.button_selector);
		game.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT)));
		game.setTypeface(font);
		game.setTextSize(30);
		game.setText("Spielen");
		game.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				if(grandma.getState() != State.ASLEEP){
					if(grandma.handleRequest(Requests.GAME) == false){
						Game game = new Game();
						game.setGrandma(grandma);
						game.handleRequest(Requests.GAME);
					}
				}
			}
		});
		requestsList.addView(game);

		Button medicineMorning = new Button(this);
		medicineMorning.setBackgroundResource(R.drawable.button_selector);
		medicineMorning.setLayoutParams((new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)));
		medicineMorning.setTypeface(font);
		medicineMorning.setTextSize(30);
		medicineMorning.setText("Medizin (Morgen)");
		medicineMorning.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				if(grandma.handleRequest(Requests.MEDICINE_MORNING) == false){
					Medicine meds = new Medicine(Daytime.EVENING);
					meds.setGrandma(grandma);
					meds.handleRequest(Requests.MEDICINE_MORNING);
					//grandma dies!
				}
			}
		});
		requestsList.addView(medicineMorning);

		Button medicineNoon = new Button(this);
		medicineNoon.setBackgroundResource(R.drawable.button_selector);
		medicineNoon.setLayoutParams((new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)));
		medicineNoon.setTypeface(font);
		medicineNoon.setTextSize(30);
		medicineNoon.setText("Medizin (Mittag)");
		medicineNoon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				if(grandma.handleRequest(Requests.MEDICINE_NOON) == false){
					Medicine meds = new Medicine(Daytime.EVENING);
					meds.setGrandma(grandma);
					meds.handleRequest(Requests.MEDICINE_NOON);
					//grandma dies!
				}
			}
		});
		requestsList.addView(medicineNoon);

		Button medicineEvening = new Button(this);
		medicineEvening.setBackgroundResource(R.drawable.button_selector);
		medicineEvening.setLayoutParams((new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)));
		medicineEvening.setTypeface(font);
		medicineEvening.setTextSize(30);
		medicineEvening.setText("Medizin (Abend)");
		medicineEvening.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				if(grandma.handleRequest(Requests.MEDICINE_EVENING) == false){
					Medicine meds = new Medicine(Daytime.MORNING);
					meds.setGrandma(grandma);
					meds.handleRequest(Requests.MEDICINE_EVENING);
					//grandma dies!
				}
			}
		});
		requestsList.addView(medicineEvening);

		Button shopping = new Button(this);
		shopping.setBackgroundResource(R.drawable.button_selector);
		shopping.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT)));
		shopping.setTypeface(font);
		shopping.setTextSize(30);
		shopping.setText("Einkaufen");
		shopping.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				if(grandma.handleRequest(Requests.SHOPPING) == false){
					Shopping shop = new Shopping();
					shop.setGrandma(grandma);
					shop.handleRequest(Requests.SHOPPING);
				}
			}
		});
		requestsList.addView(shopping);

		Button sleep = new Button(this);
		sleep.setBackgroundResource(R.drawable.button_selector);
		sleep.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT)));
		sleep.setTypeface(font);
		sleep.setTextSize(30);
		sleep.setText("Schlafen");
		sleep.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//do nothing (?)
				alert.dismiss();
				grandma.handleRequest(Requests.SLEEP);
			}
		});
		requestsList.addView(sleep);

		Button suitUp = new Button(this);
		suitUp.setBackgroundResource(R.drawable.button_selector);
		suitUp.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT)));
		suitUp.setTypeface(font);
		suitUp.setTextSize(30);
		suitUp.setText("Ankleiden");
		suitUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				if(grandma.handleRequest(Requests.SUITUP) == false){
					SuitUp suitUp = new SuitUp();
					suitUp.setGrandma(grandma);
					suitUp.handleRequest(Requests.SUITUP);
				}
			}
		});
		requestsList.addView(suitUp);

		Button washClothes = new Button(this);
		washClothes.setBackgroundResource(R.drawable.button_selector);
		washClothes.setLayoutParams((new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)));
		washClothes.setTypeface(font);
		washClothes.setTextSize(30);
		washClothes.setText("Kleidung waschen");
		washClothes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				if(grandma.handleRequest(Requests.WASHCLOTHES) == false){
					WashClothes washC = new WashClothes();
					washC.setGrandma(grandma);
					washC.handleRequest(Requests.WASHCLOTHES);
				}
			}
		});
		requestsList.addView(washClothes);

		Button washDishes = new Button(this);
		washDishes.setBackgroundResource(R.drawable.button_selector);
		washDishes.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT)));
		washDishes.setTypeface(font);
		washDishes.setTextSize(30);
		washDishes.setText("Geschirr spülen");
		washDishes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				if(grandma.handleRequest(Requests.WASHDISHES) == false){
					WashDishes washD = new WashDishes();
					washD.setGrandma(grandma);
					washD.handleRequest(Requests.WASHDISHES);
				}
			}
		});
		requestsList.addView(washDishes);

		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("");
		builder.setView(requestsPopupView);
		builder.setPositiveButton("zurück", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				alert.dismiss();
				
			}
		});
		alert = builder.create();
	}

	public void onDoRequestButton(View doRequestButton) {
		alert.show();
	}

	private void test() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = preferences.edit();
		
		Time now = new Time();
		now.setToNow();
		
		//time as integer from 0 to 2359 (hour and minutes)
		int hour = Integer.parseInt(now.format2445().substring(9, 11));
		int minute = Integer.parseInt(now.format2445().substring(11, 13));
		//long currentTimeInMS = (hour * 60 * 60 * 1000) + (minute * 60 * 1000);
		
		editor.putInt("DRINK", ((hour+1)*100 + minute));
		editor.putInt("CLEANFLAT", ((hour+2)*100 + minute));
		editor.commit();
	}

	public HashMap<String, Button> getRequestList() {
		return requestList;
	}

}
