package com.grandmaapp.model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.format.Time;

import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.model.Storeroom.Dish;

public class Shopping extends Request {
	
	public Shopping(){
		/*
		//zeit dynamisch
		Time now = new Time();
		now.setToNow();
		
		//time as integer from 0 to 2359 (hour and minutes)
		int hour = Integer.parseInt(now.format2445().substring(9, 11));
		int minute = Integer.parseInt(now.format2445().substring(11, 13));
		long currentTimeInMS = (hour * 60 * 60 * 1000) + (minute * 60 * 1000);
		long tenInMS = 22 * 60 * 60 * 1000;
		
		// 2200 - aktuelle uhrzeit in ms
		timeMS = tenInMS - currentTimeInMS;
		*/
		name = "Einkaufen";
	}
	
	public boolean handleRequest(Requests r) {
		if (r == Requests.SHOPPING) {
			
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(grandma.getMainActivity());
			Editor editor = preferences.edit();
			 
			// wasser bei 3 flaschen auf 12 auffuellen
			if (grandma.getStoreroom().getWaterBottles() <= 2) {
				grandma.getStoreroom().setWaterBottles(Storeroom.MAXWATER);
				editor.putInt("StoreWater", Storeroom.MAXWATER);	
			}
			// wenn ein mittagessen leer -> auffüllen
			if (grandma.getStoreroom().getFood().get(Dish.SCHNITZEL) < 1) {
				grandma.getStoreroom().getFood().put(Dish.SCHNITZEL, Storeroom.MAXDINNER);
				editor.putInt("StoreSchnitzel", Storeroom.MAXDINNER);
			}
			if (grandma.getStoreroom().getFood().get(Dish.NOODLES) < 1) {
				grandma.getStoreroom().getFood().put(Dish.NOODLES, Storeroom.MAXDINNER);
				editor.putInt("StoreNoodles", Storeroom.MAXDINNER);
			}
			if (grandma.getStoreroom().getFood().get(Dish.DOENER) < 1) {
				grandma.getStoreroom().getFood().put(Dish.DOENER, Storeroom.MAXDINNER);
				editor.putInt("StoreDoener", Storeroom.MAXDINNER);
			}
			if (grandma.getStoreroom().getFood().get(Dish.PIZZA) < 1) {
				grandma.getStoreroom().getFood().put(Dish.PIZZA, Storeroom.MAXDINNER);
				editor.putInt("StorePizza", Storeroom.MAXDINNER);
			}
			grandma.getStoreroom().calcDinnerSum();

			// frühstück bei 1 auf 5
			if (grandma.getStoreroom().getFood().get(Dish.BREAKFAST) <= 1) {
				grandma.getStoreroom().getFood()
						.put(Dish.BREAKFAST, Storeroom.MAXBREAKFASTSUPPER);
				editor.putInt("StoreBreakfast", Storeroom.MAXBREAKFASTSUPPER);
			}
			// abendmahl bei 1 auf 5
			if (grandma.getStoreroom().getFood().get(Dish.SUPPER) <= 1) {
				grandma.getStoreroom().getFood()
						.put(Dish.SUPPER, Storeroom.MAXBREAKFASTSUPPER);
				editor.putInt("StoreSupper", Storeroom.MAXBREAKFASTSUPPER);
			}
			editor.commit();
			
			AlertDialog.Builder builder = new AlertDialog.Builder(grandma.getMainActivity());
			builder.setTitle("");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							alert.dismiss();
						}
			});
			builder.setMessage("Du warst einkaufen. \n" + grandma.getMainActivity().getStoreroomStock());
			alert = builder.create();
			alert.show();
			return true;
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.SHOPPING;
	}
	
}
