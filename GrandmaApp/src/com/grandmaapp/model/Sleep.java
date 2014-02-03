package com.grandmaapp.model;

import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.model.Grandma.State;

public class Sleep extends Request {

	public Sleep() {
		//timeMS = HOUR_IN_MS / 2;
		name = "Schlafen";
	}
	
	public boolean handleRequest(Requests r) {
		if(r == Requests.SLEEP){
			// sobald button gedrückt wird request wird geloescht
			// TODO die app "schlafen legen"
			grandma.setState(State.ASLEEP);
			return true;
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.SLEEP;
	}
	
}
