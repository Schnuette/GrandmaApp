package com.grandmaapp.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.grandmaapp.activities.GrandmaActivity;
import com.grandmaapp.notification.Notifications;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.Time;

public class WishesService extends Service {

	public static final String BROADCAST_ID = "GRANDMA_APP";
	public static final String NEW_REQUEST = "NewRequest";
	public static final String NEW_REQUEST_TIME = "NewRequestTime";

	private static WishesService instance;
	private boolean noFood;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		instance = this;

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		Time now = new Time();
		now.setToNow();

		// time as integer from 0 to 2359 (hour and minutes)
		int time = Integer.parseInt(now.format2445().substring(9, 13));

		sendMessageToActivity("TEEEEST", time);

		Editor editor = preferences.edit();

		noFood = false;

		if (!preferences.getString("State", "HAPPY").equals("DEAD"))
		{
			// Time based requests
			if (time == 800) {
				createSuitUpRequest(time, editor);
			} else if (time == 830) {
				createMorningMedicineRequest(time, editor);
			} else if (time == 900) {
				createBreakfastRequest(time, editor);
			} else if (time == 1000 || time == 1300 || time == 1800) {
				createDrinkRequest(preferences, time, editor);
			} else if (time == 1200) {
				createLunchRequest(preferences, time, editor);
			} else if (time == 1100 || time == 1400 || time == 1900) {
				createWashDishesRequest(time, editor);
			} else if (time == 1630) {
				createEveningMedicineRequest(time, editor);
			} else if (time == 1700) {
				createSupperRequest(time, editor);
			} else if (time == 2000) {
				createSleepRequest(time, editor);
			}

			if (time == 1000 && checkForCleanFlatDay()) {
				createCleanFlatRequest(time, editor);
			}

			if (time == 1500) {
				createGameRequest(time, editor);
			}
			// Storeroom supplies based request

			if (preferences.getInt("StoreClothes", -1) <= 0) {
				createWashClothesRequest(time, editor);
			}

			if (noFood || preferences.getInt("StoreWater", -1) <= 0) {
				createShoppingRequest(time, editor);
			}

			if (time > preferences.getInt("EAT", 2500)
					|| time > preferences.getInt("DRINK", 2500)
					|| time > preferences.getInt("CLEANFLAT", 2500)
					|| time > preferences.getInt("MEDICINE", 2500)
					|| time > preferences.getInt("SHOPPING", 2500)
					|| time > preferences.getInt("SLEEP", 2500)
					|| time > preferences.getInt("SUITUP", 2500)
					|| time > preferences.getInt("WASHCLOTHES", 2500)
					|| time > preferences.getInt("WASHDISHES", 2500)
					|| time > preferences.getInt("GAME", 2500)) {
				killBruni();
			}
		}
		editor.commit();

		return Service.START_NOT_STICKY;
	}

	public void killBruni() {SharedPreferences prefs = PreferenceManager
			.getDefaultSharedPreferences(this);
	Editor edit = prefs.edit();
	edit.putInt("EAT", -1);
	edit.putInt("DRINK", -1);
	edit.putInt("CLEANFLAT", -1);
	edit.putInt("MEDICINE", -1);
	edit.putInt("SHOPPING", -1);
	edit.putInt("SLEEP", -1);
	edit.putInt("SUITUP", -1);
	edit.putInt("WASHCLOTHES", -1);
	edit.putInt("WASHDISHES", -1);
	edit.putInt("GAME", -1);
	edit.putString("State", "DEAD");
	edit.commit();
		sendMessageToActivity("BrunhildeDied", 0);
	}

	private boolean checkForCleanFlatDay() {
		String weekDay;
		SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.GERMAN);

		Calendar calendar = Calendar.getInstance();
		weekDay = dayFormat.format(calendar.getTime());

		if (weekDay.equals("Mi.")) {
			return true;
		}

		return false;
	}

	public void sendMessageToActivity(String message, int time) {
		Intent intent = new Intent(BROADCAST_ID);
		intent.putExtra(NEW_REQUEST, message);
		intent.putExtra(NEW_REQUEST_TIME, time);
		sendBroadcast(intent);
	}

	public void createGameRequest(int time, Editor editor) {
		editor.putInt("GAME", time + 200);
		editor.commit();

		notifyUser("Brunhilde m�chte spielen!");

		sendMessageToActivity("GAME", time + 200);
	}

	public void createCleanFlatRequest(int time, Editor editor) {
		editor.putInt("CLEANFLAT", time + 1000);
		editor.commit();

		notifyUser("Brunhilde m�chte, dass du die Wohnung s�uberst!");

		sendMessageToActivity("CLEANFLAT", time + 1000);
	}

	public void createShoppingRequest(int time, Editor editor) {
		editor.putInt("SHOPPING", time + 100);
		editor.commit();

		notifyUser("Brunhilde m�chte, dass du einkaufst!");

		sendMessageToActivity("SHOPPING", time + 100);
	}

	public void createWashClothesRequest(int time, Editor editor) {
		editor.putInt("WASHCLOTHES", time + 1200);
		editor.commit();

		notifyUser("Brunhilde braucht saubere Kleidung!");

		sendMessageToActivity("WASHCLOTHES", time + 1200);
	}

	public void createSleepRequest(int time, Editor editor) {
		editor.putInt("SLEEP", time + 100);
		editor.commit();

		notifyUser("Brunhilde m�chte schlafen!");

		sendMessageToActivity("SLEEP", time + 100);
	}

	public void createSupperRequest(int time, Editor editor) {
		editor.putInt("EAT", time + 100);
		editor.putString("FoodWish", "SUPPER");
		editor.commit();

		notifyUser("Brunhilde m�chte zu Abend essen!");

		sendMessageToActivity("EAT", time + 100);
	}

	public void createEveningMedicineRequest(int time, Editor editor) {
		editor.putInt("MEDICINE", time + 100);
		editor.commit();

		editor.putString("MedWish", "EVENING");

		notifyUser("Brunhilde braucht ihre Abendmedizin!");

		sendMessageToActivity("MEDICINE", time + 100);
	}

	public void createWashDishesRequest(int time, Editor editor) {
		editor.putInt("WASHDISHES", time + 100);
		editor.commit();

		notifyUser("Brunhilde hat schmutziges Geschirr!");

		sendMessageToActivity("WASHDISHES", time + 100);
	}

	public void createLunchRequest(SharedPreferences preferences, int time,
			Editor editor) {
		String foodwish = "";

		ArrayList<String> availableFood = new ArrayList<String>();

		if (preferences.getInt("StoreSchnitzel", -1) > 0) {
			availableFood.add("SCHNITZEL");
		}

		if (preferences.getInt("StoreNoodles", -1) > 0) {
			availableFood.add("NOODLES");
		}

		if (preferences.getInt("StoreDoener", -1) > 0) {
			availableFood.add("DOENER");
		}

		if (preferences.getInt("StorePizza", -1) > 0) {
			availableFood.add("PIZZA");
		}

		// If nothing is available a wish for shopping food is generated
		if (availableFood.size() != 0) {
			int choice = (int) (Math.random() * availableFood.size());

			foodwish = availableFood.get(choice);

			if (foodwish.equals("SCHNITZEL") || foodwish.equals("PIZZA")) {
				editor.putInt("MEDICINE", time + 100);
				editor.putString("MedWish", "NOON");
				editor.commit();

				notifyUser("Brunhilde braucht ihre Medizin!");

				sendMessageToActivity("MEDICINE", time + 100);
			}

			editor.putInt("EAT", time + 100);
			editor.putString("FoodWish", foodwish);
			editor.commit();

			notifyUser("Brunhilde m�chte essen!");

			sendMessageToActivity("EAT", time + 100);
		} else {
			notifyUser("Kein Mittagessen mehr vorr�tig!");
			createShoppingRequest(time, editor);
		}
	}

	public void createDrinkRequest(SharedPreferences preferences, int time,
			Editor editor) {
		if (preferences.getInt("StoreWater", -1) > 0) {
			editor.putInt("DRINK", time + 30);
			editor.commit();

			notifyUser("Brunhilde m�chte trinken!");

			sendMessageToActivity("DRINK", time + 30);
		} else {
			notifyUser("Kein Wasser mehr vorr�tig!");
			createShoppingRequest(time, editor);
		}
	}

	public void createBreakfastRequest(int time, Editor editor) {
		editor.putInt("EAT", time + 100);

		editor.putString("FoodWish", "BREAKFAST");
		editor.commit();

		notifyUser("Brunhilde m�chte Fr�hst�ck essen!");

		sendMessageToActivity("EAT", time + 100);
	}

	public void createMorningMedicineRequest(int time, Editor editor) {
		editor.putInt("MEDICINE", time + 100);
		editor.putString("MedWish", "MORNING");
		editor.commit();

		notifyUser("Brunhilde muss ihre Morgen-Medizin nehmen!");

		sendMessageToActivity("MEDICINE", time + 100);
	}

	public void createSuitUpRequest(int time, Editor editor) {
		editor.putInt("SUITUP", time + 30);
		editor.commit();

		notifyUser("Brunhilde m�chte angezogen werden!");

		sendMessageToActivity("SUITUP", time + 30);
	}

	public void notifyUser(String message) {
		if (!GrandmaActivity.isAppRunning()) {
			Notifications.getInstance().newNotification(message, this);
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO for communication return IBinder implementation
		return null;
	}

	public static WishesService getInstance() {
		return instance;
	}
}
