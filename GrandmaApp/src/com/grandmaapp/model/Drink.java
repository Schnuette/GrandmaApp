package com.grandmaapp.model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.grandmaapp.model.Grandma.Requests;

/*
 * handles the Request to Drink
 * if request was handled successfully a dialog will show up and a water will be removed from the storeroom
 * if there's no water left, request won't be handled 
 * 
 */

public class Drink extends Request {
	
	public Drink(){
		name = "Trinken";
	}
	
	public boolean handleRequest(Requests r) {
		if(r == Requests.DRINK){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(
					grandma.getMainActivity());
			builder.setTitle("");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							alert.dismiss();
						}
			});
			
			int numBottles = grandma.getStoreroom().getWaterBottles();
			if(numBottles > 0){
			
				// update water in the storeroom
				numBottles -= 1;
				grandma.getStoreroom().setWaterBottles(numBottles);

				// update water in the preferences 
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(grandma.getMainActivity());
				Editor editor = preferences.edit();
				editor.putInt("StoreWater", numBottles);
				editor.commit();

				// different dialog message if drink button was pressed without an request
				if (realRequest) {	
					builder.setMessage("Ein Wasser wurde aus der Vorratskammer entfernt.\n\n Noch " + numBottles + " Flaschen übrig.");
				}
				else{
					builder.setMessage("Brunhilde möchte jetzt nichts trinken!\n Sie schüttet das Wasser weg.\n\n Noch " + numBottles + " Flaschen übrig.");
				}
				alert = builder.create();
				alert.show();
				return true;
			}
			else{
				builder.setMessage("Kein Wasser mehr in der Vorratskammer. Du musst erst einkaufen gehen!");
				alert = builder.create();
				alert.show();
			}
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.DRINK;
	}
}
