package com.grandmaapp.model;

import android.text.format.Time;

import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.model.Storeroom.Dish;

public class Shopping extends Request {
	
	public Shopping(){
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
		
		name = "Einkaufen";
	}
	
	public boolean handleRequest(Requests r) {
		if (r == Requests.SHOPPING) {
			//grandma.getStoreroom().fillFood();
			
			/* den kram koennte man evtl noch mal brauchen
			 * falls einkaufsliste kommt
			 */
			
			 
			// wasser bei 3 flaschen auf 12 auffuellen
			if (grandma.getStoreroom().getWaterBottles() <= 2) {
				grandma.getStoreroom().setWaterBottles(Storeroom.MAXWATER);
			}
			// bei 2 mittagessen auffüllen
			if (grandma.getStoreroom().getNumDinner() <= 2) {
				grandma.getStoreroom().getFood()
						.put(Dish.SCHNITZEL, Storeroom.MAXDINNER);
				grandma.getStoreroom().getFood()
						.put(Dish.NOODLES, Storeroom.MAXDINNER);
				grandma.getStoreroom().getFood()
						.put(Dish.DOENER, Storeroom.MAXDINNER);
				grandma.getStoreroom().getFood()
						.put(Dish.PIZZA, Storeroom.MAXDINNER);
				grandma.getStoreroom().calcDinnerSum();
			}
			// frühstück bei 1 auf 5
			if (grandma.getStoreroom().getFood().get(Dish.BREAKFAST) <= 1) {
				grandma.getStoreroom().getFood()
						.put(Dish.BREAKFAST, Storeroom.MAXBREAKFASTSUPPER);
			}
			// abendmahl bei 1 auf 5
			if (grandma.getStoreroom().getFood().get(Dish.SUPPER) <= 1) {
				grandma.getStoreroom().getFood()
						.put(Dish.SUPPER, Storeroom.MAXBREAKFASTSUPPER);
			}
			return true;
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.SHOPPING;
	}
	
}
