package com.grandmaapp.model;

import com.grandmaapp.model.Grandma.Requests;

public class SuitUp extends Request {

	public boolean handleRequest(Requests r) {
		if (r == Requests.SUITUP) {
			// saubere Klamotten aus Vorratsschrank
			int cleanClothes = grandma.getStoreroom().getCleanClothes();
			if (cleanClothes > 0) {
				grandma.getStoreroom().setCleanClothes(cleanClothes - 1);
				if (cleanClothes == 1) {
					grandma.addRequest(new WashClothes());
				}
				return true;
			} else {
				grandma.addRequest(new WashClothes());
				//TODO nur noch begrenzt zeit bis oma ausrastet und stirbt wegen alter kleidung
			}
		}
		return false;
	}
}
