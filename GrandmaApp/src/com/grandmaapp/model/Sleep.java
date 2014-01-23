package com.grandmaapp.model;

import com.grandmaapp.model.Grandma.Requests;

public class Sleep extends Request {

	public boolean handleRequest() {
		// TODO kram der so gemacht werden muss
		return false;
	}
	
	public boolean handleRequest(Requests r) {
		if(r == Requests.SLEEP){
			// sobald button gedrückt wird request wird geloescht
			// TODO die app "schlafen legen"
			return true;
		}
		return false;
	}
	
}
