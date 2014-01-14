package com.grandmaapp.model;

import java.util.HashMap;

public class Grandma {

	public enum Requests{
		EAT,
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
		requests.put(Requests.EAT, new Eat());
		requests.put(Requests.DRINK, new Drink());
		requests.put(Requests.MEDICINE, new Medicine());
		requests.put(Requests.SLEEP, new Sleep());
		requests.put(Requests.SHOPPING, new Shopping());
		requests.put(Requests.SUITUP, new SuitUp());
		requests.put(Requests.CLEANFLAT, new CleanFlat());
		requests.put(Requests.WASHDISHES, new WashDishes());
		requests.put(Requests.WASHCLOTHES, new WashClothes());
	}
	
	public void handleRequest(Requests r){
		requests.get(r).handleRequest();
	}
	
	public boolean handleRequest(Grandma g) {
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

	public Storeroom getStoreroom() {
		return storeroom;
	}

	public void setStoreroom(Storeroom storeroom) {
		this.storeroom = storeroom;
	}
	
}
