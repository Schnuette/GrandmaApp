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
import android.widget.Button;
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
		//TODO falls altes Spiel geladen wird, fuellstand aktualisieren
		//TODO falls altes Spiel geladen wird, requests einpflegen
		storeroom = new Storeroom();
		mainActivity = activity;
		state = State.HAPPY;
	}
	
	public void addRequest(Request r){
		r.setGrandma(this);
		requestsToHandle.add(r);
		//TODO nur einmal einkaufen
		
		// ablaufzeiten der wuensche in shared prefs speichern
		//SharedPreferences prefs = getSharedPreferences("grandmaapp", Context.MODE_PRIVATE);
		
	}
	
	public boolean handleRequest(Requests r){
		for(Request request: requestsToHandle){
			if(request.handleRequest(r)){
				// button entfernen aus requestList loeschen
				LinearLayout linLay = (LinearLayout) mainActivity.findViewById(R.id.tasksLinLay);
				Button button = mainActivity.getRequestList().get(request.kind().toString());
				linLay.removeView(button);
				mainActivity.getRequestList().remove(request.kind().toString());
				
				// aus den Prefs loeschen
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
				Editor editor = preferences.edit();
				editor.remove(request.kind().toString());
				editor.commit();
				
				// aus Datenmodell loeschen
				requestsToHandle.remove(request);
				return true;
			}
		}
		return false;
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
	
	public void init() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(mainActivity);

		int maxDinner = this.getStoreroom().getMAXDINNER();
		int maxBreakfastSupper = this.getStoreroom().getMAXBREAKFASTSUPPER();
		int numSchnitzel = prefs.getInt("StoreSchnitzel", -1);
		if (numSchnitzel > -1) {
			this.getStoreroom().getFood().put(Dish.SCHNITZEL, numSchnitzel);
		} else {
			this.getStoreroom().getFood().put(Dish.SCHNITZEL, maxDinner);
		}
		int numNoodles = prefs.getInt("StoreNoodles", -1);
		if (numNoodles > -1) {
			this.getStoreroom().getFood().put(Dish.NOODLES, numNoodles);
		} else {
			this.getStoreroom().getFood().put(Dish.NOODLES, maxDinner);
		}
		int numDoener = prefs.getInt("StoreDoener", -1);
		if (numDoener > -1) {
			this.getStoreroom().getFood().put(Dish.DOENER, numDoener);
		} else {
			this.getStoreroom().getFood().put(Dish.DOENER, maxDinner);
		}
		int numPizza = prefs.getInt("StorePizza", -1);
		if (numPizza > -1) {
			this.getStoreroom().getFood().put(Dish.PIZZA, numPizza);
		} else {
			this.getStoreroom().getFood().put(Dish.PIZZA, maxDinner);
		}
		int numBreakfast = prefs.getInt("StoreBreakfast", -1);
		if (numBreakfast > -1) {
			this.getStoreroom().getFood().put(Dish.BREAKFAST, numBreakfast);
		} else {
			this.getStoreroom().getFood()
					.put(Dish.BREAKFAST, maxBreakfastSupper);
		}
		int numSupper = prefs.getInt("StoreSupper", -1);
		if (numSupper > -1) {
			this.getStoreroom().getFood().put(Dish.SUPPER, numSupper);
		} else {
			this.getStoreroom().getFood()
					.put(Dish.SUPPER, maxBreakfastSupper);
		}
		int numWater = prefs.getInt("StoreWater", -1);
		if (numWater > -1) {
			this.getStoreroom().setWaterBottles(numWater);
		} else {
			this.getStoreroom().setWaterBottles(
					this.getStoreroom().getMAXWATER());
		}

		int numClothes = prefs.getInt("StoreClothes", -1);
		if (numClothes > -1) {
			this.getStoreroom().setCleanClothes(numClothes);
		} else {
			this.getStoreroom().setCleanClothes(
					this.getStoreroom().getMAXCLEANCLOTHES());
		}

		this.getStoreroom().calcDinnerSum();

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
			this.addRequest(request);
		}
		time = prefs.getInt(Requests.CLEANFLAT.toString(), -1);
		if (time > -1) {
			CleanFlat request = new CleanFlat();
			request.setTimeMS(time);
			this.addRequest(request);
		}
		time = prefs.getInt(Requests.DRINK.toString(), -1);
		if (time > -1) {
			Drink request = new Drink();
			request.setTimeMS(time);
			this.addRequest(request);
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
			this.addRequest(request);
		}
		time = prefs.getInt(Requests.SHOPPING.toString(), -1);
		if (time > -1) {
			Shopping request = new Shopping();
			request.setTimeMS(time);
			this.addRequest(request);
		}
		time = prefs.getInt(Requests.SLEEP.toString(), -1);
		if (time > -1) {
			Sleep request = new Sleep();
			request.setTimeMS(time);
			this.addRequest(request);
		}
		time = prefs.getInt(Requests.SUITUP.toString(), -1);
		if (time > -1) {
			SuitUp request = new SuitUp();
			request.setTimeMS(time);
			this.addRequest(request);
		}
		time = prefs.getInt(Requests.WASHCLOTHES.toString(), -1);
		if (time > -1) {
			WashClothes request = new WashClothes();
			request.setTimeMS(time);
			this.addRequest(request);
		}
		time = prefs.getInt(Requests.WASHDISHES.toString(), -1);
		if (time > -1) {
			WashDishes request = new WashDishes();
			request.setTimeMS(time);
			this.addRequest(request);
		}

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
	
}
