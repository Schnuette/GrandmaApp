package com.grandmaapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.grandmaapp.model.Medicine.Daytime;
import com.grandmaapp.model.Storeroom.Dish;

public class Grandma {

	public enum Requests{
		EAT,
		DRINK,
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

	public Grandma(){
		//TODO falls altes Spiel geladen wird, fuellstand aktualisieren
		//TODO falls altes Spiel geladen wird, requests einpflegen
		storeroom = new Storeroom();
	}
	
	public void addRequest(Request r){
		r.setGrandma(this);
		requestsToHandle.add(r);
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
