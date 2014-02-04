package com.grandmaapp.model;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
					
			AlertDialog.Builder builder = new AlertDialog.Builder(grandma.getMainActivity());
			builder.setTitle("");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							alert.dismiss();
						}
			});
			
			if (cleanClothes > 0) {
				cleanClothes -= 1;
				grandma.getStoreroom().setCleanClothes(cleanClothes);
				
				// prefs aktualisieren
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(grandma.getMainActivity());
				Editor editor = preferences.edit();
				editor.putInt("StoreClothes", cleanClothes);
				editor.commit();
				
				builder.setMessage("Du hast Brunhilde neu eingekleidet! Sie hat noch " + cleanClothes + " saubere Kleidung.");
				alert = builder.create();
				alert.show();
				return true;
			}
			else {
				builder.setMessage("Brunhilde hat keine saubere Kleidung mehr. Du musst erst waschen!");
				alert = builder.create();
				alert.show();
			}
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.SUITUP;
	}
}
