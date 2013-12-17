package com.grandmaapp.model;

import java.util.HashMap;

public class Grandma {

	public enum Requests{
		FOOD,
		DRINK,
		MEDICINE,
		SLEEP,
		SHOPPING,
		SUITUP,
		CLEANFLAT,
		WASHDISHES,
		WASHCLOTHES
	}
	
	HashMap<Requests, Request> requests = new HashMap<Requests, Request>();
	Storeroom storeroom = new Storeroom();

	public Grandma(){
		requests.put(Requests.FOOD, new Food());
		requests.put(Requests.DRINK, new Drink());
		requests.put(Requests.MEDICINE, new Medicine());
		requests.put(Requests.SLEEP, new Sleep());
		requests.put(Requests.SHOPPING, new Shopping());
		requests.put(Requests.SUITUP, new SuitUp());
		requests.put(Requests.CLEANFLAT, new CleanFlat());
		requests.put(Requests.WASHDISHES, new WashDishes());
		requests.put(Requests.WASHCLOTHES, new WashClothes());
	}
	
	public boolean handleRequest() {
		boolean success = false;
		for(int key = 1; key <= requests.size(); key++){
			success = requests.get(key).handleRequest();
			if(success){
				return true;
			}
		}
		return false;
	}
	
	public HashMap<Requests, Request> getRequests() {
		return requests;
	}
}
