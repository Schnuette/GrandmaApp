package com.grandmaapp.model;

import com.grandmaapp.model.Grandma.Requests;

public class WashClothes extends Request {
	
	public boolean handleRequest(Requests r) {
		if (r == Requests.WASHCLOTHES) {
			// Schrank wird wieder mit sauberer Kleidung gefuellt
			grandma.getStoreroom().setCleanClothes(Storeroom.MAXCLEANCLOTHES);
			return true;
		}
		return false;
	}
}
