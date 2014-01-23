package com.grandmaapp.model;

import com.grandmaapp.model.Grandma.Requests;

public class WashDishes extends Request {
	
	public boolean handleRequest(Requests r) {
		if (r == Requests.WASHDISHES) {
			// sobald button gedrückt wird gespült und request wird geloescht
			return true;
		}
		return false;
	}
}
