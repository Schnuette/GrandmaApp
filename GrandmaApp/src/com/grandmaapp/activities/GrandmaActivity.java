/*
 * Main-Activity
 * contains all GUI elements
 * (tried to implement background music, didn't work properly, commented it out)
 */
package com.grandmaapp.activities;

import java.text.DateFormat;
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
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.example.grandmaapp.R;
import com.grandmaapp.model.CleanFlat;
import com.grandmaapp.model.CountDown;
import com.grandmaapp.model.Drink;
import com.grandmaapp.model.Eat;
import com.grandmaapp.model.Game;
import com.grandmaapp.model.Grandma;
import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.model.Medicine;
import com.grandmaapp.model.Medicine.Daytime;
import com.grandmaapp.model.Request;
import com.grandmaapp.model.Shopping;
import com.grandmaapp.model.Sleep;
import com.grandmaapp.model.Storeroom.Dish;
import com.grandmaapp.model.SuitUp;
import com.grandmaapp.model.WashClothes;
import com.grandmaapp.model.WashDishes;
import com.grandmaapp.notification.Notifications;
import com.grandmaapp.sensors.MiniGame;
import com.grandmaapp.sensors.SleepDetector;
import com.grandmaapp.sensors.WashingShaker;
import com.grandmaapp.services.WishesReceiver;
import com.grandmaapp.services.WishesService;

public class GrandmaActivity extends Activity {

	Grandma grandma;
	HashMap<String, Button> requestList;
	AlertDialog workDialog;
	AlertDialog storeroomDialog;
	AlertDialog testDialog;
	Time now;
	SharedPreferences preferences;
	Editor editor;
	View testDialogView;
	// static MediaPlayer mediaPlayer;
	static boolean appRunning;
	Button restartButton;
	GrandmaActivity context;

	// called when application is started initializes the global variables,
	// adjusts and inits the GUI, starts the service and calls all init-methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grandma);

		context = this;
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();
		now = new Time();

		if (!preferences.getString("State", "HAPPY").equals("DEAD")) {
			grandma = new Grandma(this);
			requestList = new HashMap<String, Button>();
			grandma.init();
			for (Request request : grandma.getRequestsToHandle()) {
				addRequestButton(request);
			}
			WishesReceiver.setGrandma(grandma);
		}

		MiniGame.getInstance().setActivity(this);
		WashingShaker.getInstance().init(this);
		SleepDetector.getInstance().init(this);
		WishesReceiver.setActivity(this);

		if (!preferences.getBoolean("ServiceRunning", false)) {
			startWishesService();
			editor.putBoolean("ServiceRunning", true);
			editor.commit();
		}

		adjustGUI();
		initWorkDialog();
		initTestDialog();

		if (preferences.getString("State", "HAPPY").equals("DEAD")) {
			LinearLayout linLay = (LinearLayout) findViewById(R.id.tasksLinLay);
			linLay.addView(restartButton);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.grandma, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		/*
		 * if (mediaPlayer != null) { try { mediaPlayer.stop();
		 * mediaPlayer.reset(); mediaPlayer.release(); } catch (Exception e) {
		 * Log.d("Nitif Activity", e.toString()); } } mediaPlayer = null;
		 */
		appRunning = false;
	}

	// method notifying the user
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		// Checks if the intent has the extra named Notify with the message
		// reset that indicates a counter reset request
		String message = intent.getStringExtra("Notify");
		if (message != null && message.equals("reset")) {
			Notifications.getInstance().resetMessageCounter();
		}
	}

	// resetting the message counter on app resume
	@Override
	protected void onResume() {
		super.onResume();
		/*
		 * if (Notifications.getInstance().getNumMessages() == 0) {
		 * startMusic(); }
		 */
		// Resets the message counter on app resume
		Notifications.getInstance().resetMessageCounter();

		appRunning = true;
	}

	// method adjusting the GUI changing height and width of background image
	// view and buttons
	private void adjustGUI() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels / 2;
		int width = displaymetrics.widthPixels / 3;
		ImageView grandmaImgV = (ImageView) findViewById(R.id.grandmaImgView);
		grandmaImgV.getLayoutParams().height = height;
		if (preferences.getString("State", "HAPPY").equals("ASLEEP")) {
			grandmaImgV.setImageResource(R.drawable.grandma_asleep);
		} else if (preferences.getString("State", "HAPPY").equals("DEAD")) {
			grandmaImgV.setImageResource(R.drawable.grandma_dead);
		}

		Button workBtn = (Button) findViewById(R.id.workBtn);
		Button supplyBtn = (Button) findViewById(R.id.storeroomBtn);
		Button testBtn = (Button) findViewById(R.id.testBtn);

		workBtn.setBackgroundResource(R.drawable.work_selector);
		supplyBtn.setBackgroundResource(R.drawable.supply_selector);
		testBtn.setBackgroundResource(R.drawable.test_selector);

		workBtn.getLayoutParams().width = (width - (width / 10));
		supplyBtn.getLayoutParams().width = (width - (width / 10));
		testBtn.getLayoutParams().width = (width - (width / 10));

		if (preferences.getString("State", "HAPPY").equals("DEAD")) {
			workBtn.setClickable(false);
			supplyBtn.setClickable(false);
			testBtn.setClickable(false);
		}

		Typeface font = Typeface.createFromAsset(getAssets(),
				"fonts/Blippo.ttf");
		restartButton = new Button(this);
		restartButton.setBackgroundResource(R.drawable.button_selector);
		restartButton.setLayoutParams((new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)));
		restartButton.setTypeface(font);
		restartButton.setTextSize(30);
		restartButton.setText("Neues Spiel starten");
		restartButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editor.clear();
				editor.commit();
				editor.putBoolean("ServiceRunning", true);
				editor.commit();
				grandma = new Grandma(context);
				requestList = new HashMap<String, Button>();
				grandma.init();
				WishesReceiver.setGrandma(grandma);
				Button workBtn = (Button) context.findViewById(R.id.workBtn);
				workBtn.setClickable(true);
				Button supplyBtn = (Button) context
						.findViewById(R.id.storeroomBtn);
				supplyBtn.setClickable(true);
				Button testBtn = (Button) context.findViewById(R.id.testBtn);
				testBtn.setClickable(true);
				ViewGroup lay = (ViewGroup) v.getParent();
				lay.removeView(v);
				ImageView grandmaImgV = (ImageView) context
						.findViewById(R.id.grandmaImgView);
				grandmaImgV.setImageResource(R.drawable.grandma_happy);
			}
		});

	}

	// adds a request button to the global hash map and the interface list
	public void addRequestButton(Request request) {
		LinearLayout linLay = (LinearLayout) findViewById(R.id.tasksLinLay);
		Button requestButton = new Button(this);
		requestButton.setTag(request.kind());
		long runtimeInMS = grandma.HHMMtoMS(request.getRuntime());
		// countdown showing name of request and time to fullfil request
		CountDown countdown = new CountDown(runtimeInMS, 1000, requestButton,
				request.getName());
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

	// starts the service, service refreshes every 60 seconds checking if a
	// request hast to be created
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

	// if the application terminates this method is called
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// add time, format DD.MM.YYYY HH:MM:SS
		editor.putString("time",
				DateFormat.getDateTimeInstance().format(new Date()));

		editor.commit();
		/*
		 * if (mediaPlayer != null) { try { mediaPlayer.stop();
		 * mediaPlayer.reset(); mediaPlayer.release(); } catch (Exception e) {
		 * Log.d("Nitif Activity", e.toString()); } } mediaPlayer = null;
		 */
	}

	// initilise the work dialog, creating a linear layout and adding all
	// request buttons
	private void initWorkDialog() {
		Typeface font = Typeface.createFromAsset(getAssets(),
				"fonts/Blippo.ttf");
		View requestsPopupView = getLayoutInflater().inflate(
				R.layout.all_requests_popup, null);

		LinearLayout requestsList = (LinearLayout) requestsPopupView
				.findViewById(R.id.requestList);
		// create and add requestButtons
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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.CLEANFLAT) == false) {
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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.DRINK) == false) {
					Drink drink = new Drink();
					drink.setGrandma(grandma);
					drink.setRealRequest(false);
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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.EAT) == false) {
					Eat eat = new Eat(Dish.SCHNITZEL);
					eat.setGrandma(grandma);
					eat.setRealRequest(false);
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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.EAT) == false) {
					Eat eat = new Eat(Dish.NOODLES);
					eat.setGrandma(grandma);
					eat.setRealRequest(false);
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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.EAT) == false) {
					Eat eat = new Eat(Dish.DOENER);
					eat.setGrandma(grandma);
					eat.setRealRequest(false);
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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.EAT) == false) {
					Eat eat = new Eat(Dish.PIZZA);
					eat.setGrandma(grandma);
					eat.setRealRequest(false);
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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.EAT) == false) {
					Eat eat = new Eat(Dish.BREAKFAST);
					eat.setGrandma(grandma);
					eat.setRealRequest(false);
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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.EAT) == false) {
					Eat eat = new Eat(Dish.SUPPER);
					eat.setGrandma(grandma);
					eat.setRealRequest(false);
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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.GAME) == false) {
					Game game = new Game();
					game.setGrandma(grandma);
					game.setRealRequest(false);
					game.handleRequest(Requests.GAME);
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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.MEDICINE_MORNING) == false) {
					Medicine meds = new Medicine(Daytime.EVENING);
					meds.setGrandma(grandma);
					meds.handleRequest(Requests.MEDICINE_MORNING);
					// grandma dies!
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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.MEDICINE_NOON) == false) {
					Medicine meds = new Medicine(Daytime.EVENING);
					meds.setGrandma(grandma);
					meds.handleRequest(Requests.MEDICINE_NOON);
					// grandma dies!
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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.MEDICINE_EVENING) == false) {
					Medicine meds = new Medicine(Daytime.MORNING);
					meds.setGrandma(grandma);
					meds.handleRequest(Requests.MEDICINE_EVENING);
					// grandma dies!
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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.SHOPPING) == false) {
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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.SLEEP) == false) {
					Sleep sleep = new Sleep();
					sleep.setGrandma(grandma);
					sleep.setRealRequest(false);
					sleep.handleRequest(Requests.SLEEP);
				}

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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.SUITUP) == false) {
					SuitUp suitUp = new SuitUp();
					suitUp.setGrandma(grandma);
					suitUp.setRealRequest(false);
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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.WASHCLOTHES) == false) {
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
				workDialog.dismiss();
				if (grandma.handleRequest(Requests.WASHDISHES) == false) {
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
		builder.setPositiveButton("zurück",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						workDialog.dismiss();

					}
				});
		workDialog = builder.create();
	}

	// initialise the dialog for creating requests, adding onclicklistener to
	// all buttons in the linear layout
	private void initTestDialog() {
		testDialogView = getLayoutInflater().inflate(
				R.layout.test_dialog_layout, null);

		Button eatBreakfast = (Button) testDialogView
				.findViewById(R.id.eatBreakfast);
		eatBreakfast.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!requestList.containsKey(Requests.EAT.toString())) {
					now.setToNow();
					int time = Integer.parseInt(now.format2445().substring(9,
							13));
					WishesService.getInstance().createBreakfastRequest(time,
							editor);
				}
				testDialog.dismiss();
			}
		});
		Button eatLunch = (Button) testDialogView.findViewById(R.id.eatLunch);
		eatLunch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!requestList.containsKey(Requests.EAT.toString())) {
					now.setToNow();
					int time = Integer.parseInt(now.format2445().substring(9,
							13));
					WishesService.getInstance().createLunchRequest(preferences,
							time, editor);
				}
				testDialog.dismiss();
			}
		});
		Button eatSupper = (Button) testDialogView.findViewById(R.id.eatSupper);
		eatSupper.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!requestList.containsKey(Requests.EAT.toString())) {
					now.setToNow();
					int time = Integer.parseInt(now.format2445().substring(9,
							13));
					WishesService.getInstance().createSupperRequest(time,
							editor);
				}
				testDialog.dismiss();
			}
		});
		Button drink = (Button) testDialogView.findViewById(R.id.drink);
		drink.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!requestList.containsKey(Requests.DRINK.toString())) {
					now.setToNow();
					int time = Integer.parseInt(now.format2445().substring(9,
							13));
					WishesService.getInstance().createDrinkRequest(preferences,
							time, editor);
				}
				testDialog.dismiss();
			}
		});
		Button medsMorning = (Button) testDialogView
				.findViewById(R.id.medsMorning);
		medsMorning.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!requestList.containsKey(Requests.MEDICINE_MORNING
						.toString())
						|| !requestList.containsKey(Requests.MEDICINE_EVENING
								.toString())) {
					now.setToNow();
					int time = Integer.parseInt(now.format2445().substring(9,
							13));
					WishesService.getInstance().createMorningMedicineRequest(
							time, editor);
				}
				testDialog.dismiss();
			}
		});
		Button medsEvening = (Button) testDialogView
				.findViewById(R.id.medsEvening);
		medsEvening.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!requestList.containsKey(Requests.MEDICINE_EVENING
						.toString())
						|| !requestList.containsKey(Requests.MEDICINE_MORNING
								.toString())) {
					now.setToNow();
					int time = Integer.parseInt(now.format2445().substring(9,
							13));
					WishesService.getInstance().createEveningMedicineRequest(
							time, editor);
				}
				testDialog.dismiss();
			}
		});
		Button shopping = (Button) testDialogView.findViewById(R.id.shopping);
		shopping.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!requestList.containsKey(Requests.SHOPPING.toString())) {
					now.setToNow();
					int time = Integer.parseInt(now.format2445().substring(9,
							13));
					WishesService.getInstance().createShoppingRequest(time,
							editor);
				}
				testDialog.dismiss();
			}
		});
		Button cleanFlat = (Button) testDialogView.findViewById(R.id.cleanFlat);
		cleanFlat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!requestList.containsKey(Requests.CLEANFLAT.toString())) {
					now.setToNow();
					int time = Integer.parseInt(now.format2445().substring(9,
							13));
					WishesService.getInstance().createCleanFlatRequest(time,
							editor);
				}
				testDialog.dismiss();
			}
		});
		Button suitUp = (Button) testDialogView.findViewById(R.id.suitUp);
		suitUp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!requestList.containsKey(Requests.SUITUP.toString())) {
					now.setToNow();
					int time = Integer.parseInt(now.format2445().substring(9,
							13));
					WishesService.getInstance().createSuitUpRequest(time,
							editor);
				}
				testDialog.dismiss();
			}
		});
		Button washDishes = (Button) testDialogView
				.findViewById(R.id.washDishes);
		washDishes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!requestList.containsKey(Requests.WASHDISHES.toString())) {
					now.setToNow();
					int time = Integer.parseInt(now.format2445().substring(9,
							13));
					WishesService.getInstance().createWashDishesRequest(time,
							editor);
				}
				testDialog.dismiss();
			}
		});
		Button washClothes = (Button) testDialogView
				.findViewById(R.id.washClothes);
		washClothes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!requestList.containsKey(Requests.WASHCLOTHES.toString())) {
					now.setToNow();
					int time = Integer.parseInt(now.format2445().substring(9,
							13));
					WishesService.getInstance().createWashClothesRequest(time,
							editor);
				}
				testDialog.dismiss();
			}
		});
		Button game = (Button) testDialogView.findViewById(R.id.game);
		game.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!requestList.containsKey(Requests.GAME.toString())) {
					now.setToNow();
					int time = Integer.parseInt(now.format2445().substring(9,
							13));
					WishesService.getInstance().createGameRequest(time, editor);
				}
				testDialog.dismiss();
			}
		});
		Button sleep = (Button) testDialogView.findViewById(R.id.sleep);
		sleep.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!requestList.containsKey(Requests.SLEEP.toString())) {
					now.setToNow();
					int time = Integer.parseInt(now.format2445().substring(9,
							13));
					WishesService.getInstance()
							.createSleepRequest(time, editor);
				}
				testDialog.dismiss();
			}
		});
		Button simulate = (Button) testDialogView.findViewById(R.id.simulate);
		simulate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				testDialog.dismiss();
				((Button) testDialogView.findViewById(R.id.eatBreakfast))
						.performClick();
				((Button) testDialogView.findViewById(R.id.drink))
						.performClick();
				((Button) testDialogView.findViewById(R.id.medsMorning))
						.performClick();
				((Button) testDialogView.findViewById(R.id.shopping))
						.performClick();
				((Button) testDialogView.findViewById(R.id.cleanFlat))
						.performClick();
				((Button) testDialogView.findViewById(R.id.suitUp))
						.performClick();
				((Button) testDialogView.findViewById(R.id.washDishes))
						.performClick();
				((Button) testDialogView.findViewById(R.id.washClothes))
						.performClick();
				((Button) testDialogView.findViewById(R.id.game))
						.performClick();
				((Button) testDialogView.findViewById(R.id.sleep))
						.performClick();
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("");
		builder.setView(testDialogView);
		builder.setPositiveButton("zurück",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						testDialog.dismiss();

					}
				});
		testDialog = builder.create();
	}

	// work button clicked, shows the dialog for handling requests
	public void onDoRequestButton(View doRequestButton) {
		workDialog.show();
	}

	// test Button clicked, shows the dialog for adding requests
	public void testButtonClicked(View testBtn) {
		testDialog.show();
	}

	// storeroom Button clicked, showing the actual stock of the storeroom in a
	// dialog
	public void storeroomButtonClicked(View storeroomBtn) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("");
		builder.setMessage(getStoreroomStock());
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				storeroomDialog.dismiss();

			}
		});
		storeroomDialog = builder.create();
		storeroomDialog.show();
	}

	// removes all wishes and data from model and shared preferences, disable
	// buttons, change image of brunhilde and add the restart button
	public void brunhildeDied() {
		editor.clear();
		editor.commit();
		editor.putString("State", "DEAD");
		editor.putBoolean("ServiceRunning", true);
		editor.commit();
		LinearLayout linLay = (LinearLayout) findViewById(R.id.tasksLinLay);
		linLay.removeAllViews();
		requestList.clear();
		grandma.getRequestsToHandle().clear();
		Button workBtn = (Button) findViewById(R.id.workBtn);
		workBtn.setClickable(false);
		Button supplyBtn = (Button) findViewById(R.id.storeroomBtn);
		supplyBtn.setClickable(false);
		Button testBtn = (Button) findViewById(R.id.testBtn);
		testBtn.setClickable(false);
		linLay.addView(restartButton);
		ImageView grandmaImgV = (ImageView) findViewById(R.id.grandmaImgView);
		grandmaImgV.setImageResource(R.drawable.grandma_dead);
	}

	// returns a string with the actual storeroom stock
	public String getStoreroomStock() {
		String stock = "Lagerraum\n\nMahlzeiten\nSchnitzel: "
				+ grandma.getStoreroom().getFood().get(Dish.SCHNITZEL)
				+ "\nNudeln: "
				+ grandma.getStoreroom().getFood().get(Dish.NOODLES)
				+ "\nDoener: "
				+ grandma.getStoreroom().getFood().get(Dish.DOENER)
				+ "\nPizza: "
				+ grandma.getStoreroom().getFood().get(Dish.PIZZA)
				+ "\nFrühstück: "
				+ grandma.getStoreroom().getFood().get(Dish.BREAKFAST)
				+ "\nAbendbrot: "
				+ grandma.getStoreroom().getFood().get(Dish.SUPPER)
				+ "\n\nWasserflaschen: "
				+ grandma.getStoreroom().getWaterBottles()
				+ "\nsaubere Kleidung: "
				+ grandma.getStoreroom().getCleanClothes();
		return stock;
	}

	// returns the list of request buttons
	public HashMap<String, Button> getRequestList() {
		return requestList;
	}

	/*
	 * public void startMusic() { if (mediaPlayer != null) { mediaPlayer.stop();
	 * mediaPlayer.reset(); mediaPlayer.release(); } switch (grandma.getState())
	 * { case HAPPY: mediaPlayer = MediaPlayer.create(this,
	 * R.raw.song_of_storms); mediaPlayer.setLooping(true); mediaPlayer.start();
	 * break; case MAD: mediaPlayer = MediaPlayer.create(this,
	 * R.raw.imperial_march); mediaPlayer.setLooping(true); mediaPlayer.start();
	 * break; case DEAD: mediaPlayer = MediaPlayer.create(this,
	 * R.raw.shadow_temple); mediaPlayer.setLooping(true); mediaPlayer.start();
	 * break; case ASLEEP: mediaPlayer = null; break; } }
	 */

	public static boolean isAppRunning() {
		return appRunning;
	}

}
