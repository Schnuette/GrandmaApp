package com.grandmaapp.model;

import com.grandmaapp.model.Grandma.Requests;

public class Sleep extends Request {

	public Sleep() {
		timeMS = HOUR_IN_MS / 2;
	}
	
	public boolean handleRequest(Requests r) {
		if(r == Requests.SLEEP){
			// sobald button gedrückt wird request wird geloescht
			// TODO die app "schlafen legen"
			return true;
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.SLEEP;
	}
	
}
