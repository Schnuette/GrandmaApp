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
		WASHCLOTHES
	}
	
	
	List<Request> requestsToHandle = new ArrayList<Request>();
	Storeroom storeroom;
	Activity mainActivity;
	Editor editor;

	public Grandma(Activity activity){
		//TODO falls altes Spiel geladen wird, fuellstand aktualisieren
		//TODO falls altes Spiel geladen wird, requests einpflegen
		storeroom = new Storeroom();
		mainActivity = activity;
	}
	
	public void addRequest(Request r){
		r.setGrandma(this);
		requestsToHandle.add(r);
		//TODO nur einmal einkaufen
		
		// ablaufzeiten der wuensche in shared prefs speichern
		//SharedPreferences prefs = getSharedPreferences("grandmaapp", Context.MODE_PRIVATE);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity);
		editor = prefs.edit();
		editor.putInt(r.kind().toString(), calcExpireTime(r.getTimeMS()));	
		
		if(r.kind() == Requests.EAT){
			Eat eat = (Eat) r;
			editor.putString("FoodWish", eat.getFoodWish().toString());
		}
		if(r.kind() == Requests.MEDICINE){
			Medicine meds = (Medicine) r;
			editor.putString("MedWish", meds.getDaytime().toString());
		}
		editor.commit();
		
		r.startTimer();
	}
	
	public boolean handleRequest(Requests r){
		for(Request request: requestsToHandle){
			if(request.handleRequest(r)){
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
	
	public Storeroom getStoreroom() {
		return storeroom;
	}

	public void setStoreroom(Storeroom storeroom) {
		this.storeroom = storeroom;
	}

	public List<Request> getRequestsToHandle() {
		return requestsToHandle;
	}

}
