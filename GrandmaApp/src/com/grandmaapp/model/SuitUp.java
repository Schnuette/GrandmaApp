package com.grandmaapp.model;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.grandmaapp.model.Grandma.Requests;

public class SuitUp extends Request {
	
	public SuitUp(){
		//timeMS = HOUR_IN_MS;
		name = "Ankleiden";
	}

	public boolean handleRequest(Requests r) {
		if (r == Requests.SUITUP) {
			// saubere Klamotten aus Vorratsschrank
			int cleanClothes = grandma.getStoreroom().getCleanClothes();
					
			if (cleanClothes > 0) {
				grandma.getStoreroom().setCleanClothes(cleanClothes - 1);
				
				// prefs aktualisieren
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(grandma.getMainActivity());
				Editor editor = preferences.edit();
				editor.putInt("StoreClothes", cleanClothes);
				editor.commit();
				
				/*if (cleanClothes == 1) {
					grandma.addRequest(new WashClothes());
				}*/
				return true;
			}
			/*else {
				grandma.addRequest(new WashClothes());
				//TODO nur noch begrenzt zeit bis oma ausrastet und stirbt wegen alter kleidung
			}*/
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.SUITUP;
	}
}
