package com.grandmaapp.model;

import com.grandmaapp.model.Grandma.Requests;

public class CleanFlat extends Request {
	
	public CleanFlat(){
		timeMS = HOUR_IN_MS * 14;
	}
	
	public boolean handleRequest(Requests r) {
		if(r == Requests.CLEANFLAT){
			// sobald button gedr�ckt wird wohnung sauber und request wird geloescht
			return true;
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.CLEANFLAT;
	}
}
