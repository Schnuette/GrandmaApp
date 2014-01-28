package com.grandmaapp.model;

import com.grandmaapp.model.Grandma.Requests;

public class WashDishes extends Request {
	
	public WashDishes(){
		timeMS = HOUR_IN_MS;
		name = "Geschirr sp�len";
	}
	
	public boolean handleRequest(Requests r) {
		if (r == Requests.WASHDISHES) {
			// sobald button gedr�ckt wird gesp�lt und request wird geloescht
			return true;
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.WASHDISHES;
	}
}
