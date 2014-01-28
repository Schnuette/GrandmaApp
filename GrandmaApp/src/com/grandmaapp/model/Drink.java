package com.grandmaapp.model;

import com.grandmaapp.model.Grandma.Requests;

public class Drink extends Request {
	
	public Drink(){
		timeMS = HOUR_IN_MS;
		name = "Trinken";
	}
	
	public boolean handleRequest(Requests r) {
		if(r == Requests.DRINK){
			int numBottles = grandma.getStoreroom().getWaterBottles() - 1;
			grandma.getStoreroom().setWaterBottles(numBottles);
			if (numBottles < 3) {
				grandma.addRequest(new Shopping());
			}
			return true;
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.DRINK;
	}
}
