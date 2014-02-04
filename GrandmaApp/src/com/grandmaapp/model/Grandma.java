package com.grandmaapp.model;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.grandmaapp.R;
import com.grandmaapp.activities.GrandmaActivity;
import com.grandmaapp.model.Medicine.Daytime;
import com.grandmaapp.model.Storeroom.Dish;

public class Grandma {

	public enum Requests{
		EAT,
		DRINK,
		MEDICINE,
		MEDICINE_MORNING,
		MEDICINE_NOON,
		MEDICINE_EVENING,
		SLEEP,
		SHOPPING,
		SUITUP,
		CLEANFLAT,
		WASHDISHES,
		WASHCLOTHES,
		GAME
	}
	
	public enum State{
		DEAD,
		HAPPY,
		MAD,
		ASLEEP
	}
	
	
	List<Request> requestsToHandle = new ArrayList<Request>();
	Storeroom storeroom;
	GrandmaActivity mainActivity;
	State state;

	public Grandma(GrandmaActivity activity){
		storeroom = new Storeroom();
		mainActivity = activity;
		state = State.HAPPY;
	}
	
	public void addRequest(Request r){
		r.setGrandma(this);
		requestsToHandle.add(r);
		//TODO nur einmal einkaufen
		
		state = State.MAD;
		ImageView grandmaImgV = (ImageView) mainActivity.findViewById(R.id.grandmaImgView);
		grandmaImgV.setImageResource(R.drawable.grandma_mad);
	}
	
	public boolean handleRequest(Requests r){
		for(Request request: requestsToHandle){
			if(request.handleRequest(r)){
				// button entfernen aus requestList loeschen
				Button button = mainActivity.getRequestList().get(request.kind().toString());
				ViewGroup layout = (ViewGroup) button.getParent();
				if(null!=layout) //for safety only  as you are doing onClick
				layout.removeView(button);
				mainActivity.getRequestList().remove(request.kind().toString());
				
				// aus den Prefs loeschen
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
				Editor editor = preferences.edit();
				editor.remove(request.kind().toString());
				editor.commit();
				
				// aus Datenmodell loeschen
				requestsToHandle.remove(request);
				
				if(requestsToHandle.isEmpty()){
					state = State.HAPPY;
					ImageView grandmaImgV = (ImageView) mainActivity.findViewById(R.id.grandmaImgView);
					grandmaImgV.setImageResource(R.drawable.grandma_happy);
				}
				return true;
			}
		}
		return false;
	}
	
	public GrandmaActivity getMainActivity() {
		return mainActivity;
	}

	public void setMainActivity(GrandmaActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	public int calcExpireTime(long l){
		Time now = new Time();
		now.setToNow();
		
		//time as integer from 0 to 2359 (hour and minutes)
		int hour = Integer.parseInt(now.format2445().substring(9, 13));
		int minute = Integer.parseInt(now.format2445().substring(11, 13));
		long expireTimeInMS = (hour * 60 * 60 * 1000) + (minute * 60 * 1000) + l;
		
		int expireTime = (int)(((expireTimeInMS / 3600000L) * 100L) + (expireTimeInMS % 3600000L));		
		
		return expireTime;
	}
	
	public int calcRuntime(int expireTime){
		//aktuelle Zeit in ms
		Time now = new Time();
		now.setToNow();
		
		//time as integer from 0 to 2359 (hour and minutes)
		int hour = Integer.parseInt(now.format2445().substring(9, 11));
		int minute = Integer.parseInt(now.format2445().substring(11, 13));
		long currentTimeInMS = (hour * 60 * 60 * 1000) + (minute * 60 * 1000);
		
		// ablaufzeit in ms
		long expireTimeInMS = HHMMtoMS(expireTime);
				
		// ablaufzeit - aktuelle uhrzeit in ms
		long runtimeinMS = (expireTimeInMS - currentTimeInMS);	
		// laufzeit in hhmm format
		int runtimeH = (int)((runtimeinMS / 3600000L));
		int runtimeM = (int)((runtimeinMS % 3600000L) / 60000L);
		int runtime = runtimeH * 100 + runtimeM;
		
		return runtime;
	}
	
	public void init() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(mainActivity);
		Editor editor = prefs.edit();

		// Vorratskammer wird geladen
		int maxDinner = this.getStoreroom().getMAXDINNER();
		int maxBreakfastSupper = this.getStoreroom().getMAXBREAKFASTSUPPER();
		int numSchnitzel = prefs.getInt("StoreSchnitzel", -1);
		if (numSchnitzel > -1) {
			this.getStoreroom().getFood().put(Dish.SCHNITZEL, numSchnitzel);
		} else {
			this.getStoreroom().getFood().put(Dish.SCHNITZEL, maxDinner);
			editor.putInt("StoreSchnitzel", Storeroom.MAXDINNER);
		}
		int numNoodles = prefs.getInt("StoreNoodles", -1);
		if (numNoodles > -1) {
			this.getStoreroom().getFood().put(Dish.NOODLES, numNoodles);
		} else {
			this.getStoreroom().getFood().put(Dish.NOODLES, maxDinner);
			editor.putInt("StoreNoodles", Storeroom.MAXDINNER);
		}
		int numDoener = prefs.getInt("StoreDoener", -1);
		if (numDoener > -1) {
			this.getStoreroom().getFood().put(Dish.DOENER, numDoener);
		} else {
			this.getStoreroom().getFood().put(Dish.DOENER, maxDinner);
			editor.putInt("StoreDoener", Storeroom.MAXDINNER);
		}
		int numPizza = prefs.getInt("StorePizza", -1);
		if (numPizza > -1) {
			this.getStoreroom().getFood().put(Dish.PIZZA, numPizza);
		} else {
			this.getStoreroom().getFood().put(Dish.PIZZA, maxDinner);
			editor.putInt("StorePizza", Storeroom.MAXDINNER);
		}
		int numBreakfast = prefs.getInt("StoreBreakfast", -1);
		if (numBreakfast > -1) {
			this.getStoreroom().getFood().put(Dish.BREAKFAST, numBreakfast);
		} else {
			this.getStoreroom().getFood()
					.put(Dish.BREAKFAST, maxBreakfastSupper);
			editor.putInt("StoreBreakfast", Storeroom.MAXBREAKFASTSUPPER);
		}
		int numSupper = prefs.getInt("StoreSupper", -1);
		if (numSupper > -1) {
			this.getStoreroom().getFood().put(Dish.SUPPER, numSupper);
		} else {
			this.getStoreroom().getFood()
					.put(Dish.SUPPER, maxBreakfastSupper);
			editor.putInt("StoreSupper", Storeroom.MAXBREAKFASTSUPPER);
		}
		int numWater = prefs.getInt("StoreWater", -1);
		if (numWater > -1) {
			this.getStoreroom().setWaterBottles(numWater);
		} else {
			this.getStoreroom().setWaterBottles(
					this.getStoreroom().getMAXWATER());
			editor.putInt("StoreWater", Storeroom.MAXWATER);
		}

		int numClothes = prefs.getInt("StoreClothes", -1);
		if (numClothes > -1) {
			this.getStoreroom().setCleanClothes(numClothes);
		} else {
			this.getStoreroom().setCleanClothes(
					this.getStoreroom().getMAXCLEANCLOTHES());
			editor.putInt("StoreClothes", Storeroom.MAXCLEANCLOTHES);
		}

		this.getStoreroom().calcDinnerSum();
		
		editor.commit();

		// Requests werden geladen
		int time = prefs.getInt(Requests.EAT.toString(), -1);
		if (time > -1) {
			createEatRequest(prefs, time);
		}
		time = prefs.getInt(Requests.CLEANFLAT.toString(), -1);
		if (time > -1) {
			createCleanFlatRequest(time);
		}
		time = prefs.getInt(Requests.DRINK.toString(), -1);
		if (time > -1) {
			createDrinkRequest(time);
		}
		time = prefs.getInt(Requests.MEDICINE.toString(), -1);
		if (time > -1) {
			createMedicineRequest(prefs, time);
		}
		time = prefs.getInt(Requests.SHOPPING.toString(), -1);
		if (time > -1) {
			createShoppingRequest(time);
		}
		time = prefs.getInt(Requests.SLEEP.toString(), -1);
		if (time > -1) {
			createSleepRequest(time);
		}
		time = prefs.getInt(Requests.SUITUP.toString(), -1);
		if (time > -1) {
			createSuitUpRequest(time);
		}
		time = prefs.getInt(Requests.WASHCLOTHES.toString(), -1);
		if (time > -1) {
			createWashClothesRequest(time);
		}
		time = prefs.getInt(Requests.WASHDISHES.toString(), -1);
		if (time > -1) {
			createWashDishesRequest(time);
		}

	}

	public WashDishes createWashDishesRequest(int time) {
		WashDishes request = new WashDishes();
		request.setRuntime(calcRuntime(time));
		this.addRequest(request);
		
		return request;
	}

	public WashClothes createWashClothesRequest(int time) {
		WashClothes request = new WashClothes();
		request.setRuntime(calcRuntime(time));
		this.addRequest(request);
		
		return request;
	}

	public SuitUp createSuitUpRequest(int time) {
		SuitUp request = new SuitUp();
		request.setRuntime(calcRuntime(time));
		this.addRequest(request);
		
		return request;
	}

	public Sleep createSleepRequest(int time) {
		Sleep request = new Sleep();
		request.setRuntime(calcRuntime(time));
		this.addRequest(request);
		
		return request;
	}

	public Shopping createShoppingRequest(int time) {
		Shopping request = new Shopping();
		request.setRuntime(calcRuntime(time));
		this.addRequest(request);
		
		return request;
	}

	public Medicine createMedicineRequest(SharedPreferences prefs, int time) {
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
		request.setRuntime(calcRuntime(time));
		this.addRequest(request);
		
		return request;
	}

	public Drink createDrinkRequest(int time) {
		Drink request = new Drink();
		request.setRuntime(calcRuntime(time));
		this.addRequest(request);
		
		return request;
	}

	public CleanFlat createCleanFlatRequest(int time) {
		CleanFlat request = new CleanFlat();
		request.setRuntime(calcRuntime(time));
		Log.d("test", "cleanflat runtime " + time);
		Log.d("test", "cleanflat runtime " + request.getRuntime());
		this.addRequest(request);
		
		return request;
	}

	public Eat createEatRequest(SharedPreferences prefs, int time) {
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
		request.setRuntime(calcRuntime(time));
		this.addRequest(request);
		
		return request;
	}
	
	public Storeroom getStoreroom() {
		return storeroom;
	}

	public void setStoreroom(Storeroom storeroom) {
		this.storeroom = storeroom;
	}

	public List<Request> getRequestsToHandle() {
		return requestsToHandle;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public long HHMMtoMS(int time) {
		// hhmm format in ms umrechnen
		int hour = (time/100);
		int minute = (time%100);
		long timeInMS = (hour * 60 * 60 * 1000) + (minute * 60 * 1000);
		return timeInMS;
	}
	
}
