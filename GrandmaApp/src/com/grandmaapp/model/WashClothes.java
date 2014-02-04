package com.grandmaapp.model;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.sensors.WashingShaker;

/*
 * handles the Request to wash clothes
 * starts the WashingShaker and refill clean clothes in the storeroom
 * 
 */

public class WashClothes extends Request {
	
	public WashClothes(){
		name = "Kleidung waschen";
	}
	
	public boolean handleRequest(Requests r) {
		if (r == Requests.WASHCLOTHES) {
			// starts the Washingshaker
			WashingShaker.getInstance( ).show( );
			
			// TODO in washingshaker ok button auslagern?
			// refill storeroom with clean clothes
			grandma.getStoreroom().setCleanClothes(Storeroom.MAXCLEANCLOTHES);
			
			// update clothes in the preferences
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
