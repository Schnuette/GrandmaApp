package com.grandmaapp.model;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.grandmaapp.R;
import com.grandmaapp.activities.GrandmaActivity;
import com.grandmaapp.model.Medicine.Daytime;
import com.grandmaapp.model.Storeroom.Dish;

/*
 * represents the grandma
 * enum Requests represents all kind of Requests
 * enum State represents the mood of brunhilde
 * 
 * this class does all the datamodel behaviour
 * it manages all the requests like adding and handle them
 * at start grandma loads the data from the preferences list in the model
 * 
 */

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
	SharedPreferences preferences;
	Editor editor;

	// constructor
	public Grandma(GrandmaActivity activity){
		storeroom = new Storeroom();
		mainActivity = activity;
		preferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
		editor = preferences.edit();
		if(preferences.getString("State", "HAPPY").equals("HAPPY")){
			setState(State.HAPPY);
		}else if(preferences.getString("State", "HAPPY").equals("MAD")){
			setState(State.MAD);
		}else if(preferences.getString("State", "HAPPY").equals("ASLEEP")){
			setState(State.ASLEEP);
		}else if(preferences.getString("State", "HAPPY").equals("DEAD")){
			setState(State.DEAD);
		}
	}
	
	// add a new Request to the Requestlist
	public void addRequest(Request r){
		r.setGrandma(this);
		// breaks if a request of this kind already exist
		for(Request request: requestsToHandle){
			if(request.getClass().toString().equals(r.getClass().toString())){
				Log.d("test", request.getClass().toString() + " " + r.getClass().toString());
				return;
			}
		}
		requestsToHandle.add(r);		
		
		// grandma is mad and image is changed
		if(r.kind() == Requests.SUITUP || state != State.ASLEEP){
			setState(State.MAD);
			ImageView grandmaImgV = (ImageView) mainActivity.findViewById(R.id.grandmaImgView);
			grandmaImgV.setImageResource(R.drawable.grandma_mad);
		}
		//mainActivity.startMusic();
	}
	
	// handle the request if it exists and deletes it
	public boolean handleRequest(Requests r){
		for(Request request: requestsToHandle){
			if(request.handleRequest(r)){
				// delete request button
				Button button = mainActivity.getRequestList().get(request.kind().toString());
				ViewGroup layout = (ViewGroup) button.getParent();
				if(null!=layout) //for safety only  as you are doing onClick
					layout.removeView(button);
				mainActivity.getRequestList().remove(request.kind().toString());
				
				// remove Request from preferences
				if(request.kind() == Requests.MEDICINE_MORNING || request.kind() == Requests.MEDICINE_EVENING || request.kind() == Requests.MEDICINE_NOON){
					// request is saved with key MEDICINE in the preferences and with daytime in the request
					editor.remove(Requests.MEDICINE.toString());
				}
				else{
					editor.remove(request.kind().toString());
				}
				editor.commit();
				
				// remove from data model
				requestsToHandle.remove(request);
				
				// sets the grandma to Happy if no requests left to handle
				if(requestsToHandle.isEmpty() && state != State.ASLEEP){
					setState(State.HAPPY);
					ImageView grandmaImgV = (ImageView) mainActivity.findViewById(R.id.grandmaImgView);
					grandmaImgV.setImageResource(R.drawable.grandma_happy);
					//mainActivity.startMusic();
				}
				return true;
			}
		}
		return false;
	}
	
	// calculates the runtime from the expireTime of the Request and the current time
	public int calcRuntime(int expireTime){
		// current time in ms
		Time now = new Time();
		now.setToNow();		
		// time as integer from 0 to 2359 (hour and minutes)
		int hour = Integer.parseInt(now.format2445().substring(9, 11));
		int minute = Integer.parseInt(now.format2445().substring(11, 13));
		
		long currentTimeInMS = (hour * 60 * 60 * 1000) + (minute * 60 * 1000);
		long expireTimeInMS = HHMMtoMS(expireTime);
				
		// expire - current time in ms
		long runtimeinMS = (expireTimeInMS - currentTimeInMS);	
		
		// runtime in hhmm format
		int runtimeH = (int)((runtimeinMS / 3600000L));
		int runtimeM = (int)((runtimeinMS % 3600000L) / 60000L);
		int runtime = runtimeH * 100 + runtimeM;
		
		return runtime;
	}
	
	// convert from hhmm format into ms 
	public long HHMMtoMS(int time) {
		int hour = (time/100);
		int minute = (time%100);
		long timeInMS = (hour * 60 * 60 * 1000) + (minute * 60 * 1000);
		
		return timeInMS;
	}
	
	// loads storeroom and old requests from the preferences
	public void init() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity);
		Editor editor = prefs.edit();

		/*
		 *  load storeroom from preferences
		 *  if no value is set in the prefs, value will be max value
		 */
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

		/*
		 *  load Requests from preferences
		 *  key: kind of request
		 *  value: expire time of the request
		 */
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
		time = prefs.getInt(Requests.GAME.toString(), -1);
		if (time > -1) {
			createGameRequest(time);
		}

	}
	
	/*
	 * new requests will be created and added to the data model
	 * runtime will be calculated from the expire time
	 */
	public Game createGameRequest(int time){
		Game request = new Game();
		request.setRuntime(calcRuntime(time));
		this.addRequest(request);
		
		return request;
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
		// med request saved as MEDECINE in prefs. daytime is saved under MedWish
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
		this.addRequest(request);
		
		return request;
	}

	public Eat createEatRequest(SharedPreferences prefs, int time) {
		Eat request = new Eat();
		// eat request saved as EAT in prefs. dish is saved under FoodWish
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
	
	public GrandmaActivity getMainActivity() {
		return mainActivity;
	}

	public void setMainActivity(GrandmaActivity mainActivity) {
		this.mainActivity = mainActivity;
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
		editor.putString("State", state.toString());
		editor.commit();
	}
	
}
