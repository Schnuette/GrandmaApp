package com.grandmaapp.model;

import com.grandmaapp.model.Grandma.Requests;

public class CleanFlat extends Request {
	
	public boolean handleRequest(Requests r) {
		if(r == Requests.CLEANFLAT){
			// sobald button gedrückt wird wohnung sauber und request wird geloescht
			return true;
		}
		return false;
	}
}
