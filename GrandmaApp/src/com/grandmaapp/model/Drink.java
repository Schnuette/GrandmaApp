package com.grandmaapp.model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.grandmaapp.model.Grandma.Requests;

public class Drink extends Request {
	
	public Drink(){
		//timeMS = HOUR_IN_MS;
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
			
				numBottles -= 1;
				grandma.getStoreroom().setWaterBottles(numBottles);

				// prefs aktualisieren
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(grandma.getMainActivity());
				Editor editor = preferences.edit();
				editor.putInt("StoreWater", numBottles);
				editor.commit();


				if (realRequest) {	
					builder.setMessage("Ein Wasser wurde aus der Vorratskammer entfernt.\n Noch " + numBottles + " Flaschen übrig.");
				}
				else{
					builder.setMessage("Brunhilde möchte jetzt nichts trinken!\n Noch " + numBottles + " Flaschen übrig.");
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
