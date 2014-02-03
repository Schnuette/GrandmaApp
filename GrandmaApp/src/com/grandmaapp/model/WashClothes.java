package com.grandmaapp.model;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.grandmaapp.model.Grandma.Requests;

public class WashClothes extends Request {
	
	public WashClothes(){
		//timeMS = HOUR_IN_MS * 12;
		name = "Kleidung waschen";
	}
	
	public boolean handleRequest(Requests r) {
		if (r == Requests.WASHCLOTHES) {
			// Schrank wird wieder mit sauberer Kleidung gefuellt
			grandma.getStoreroom().setCleanClothes(Storeroom.MAXCLEANCLOTHES);
			
			// TODO prefs aktualisieren
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(grandma.getMainActivity());
			Editor editor = preferences.edit();
			editor.putInt("StoreClothes", Storeroom.MAXCLEANCLOTHES);
			editor.commit();
			
			return true;
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.WASHCLOTHES;
	}
}
