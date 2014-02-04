package com.grandmaapp.model;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.grandmaapp.model.Grandma.Requests;

/*
 * handles the Request to Wash the Dishes
 * if request was handled successfully a dialog will show up
 * 
 */

public class WashDishes extends Request {
	
	public WashDishes(){
		//timeMS = HOUR_IN_MS;
		name = "Geschirr spülen";
	}
	
	public boolean handleRequest(Requests r) {
		if (r == Requests.WASHDISHES) {
			AlertDialog.Builder builder = new AlertDialog.Builder(grandma.getMainActivity());
			builder.setTitle("");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							alert.dismiss();
						}
			});
			builder.setMessage("Das Geschirr ist wieder sauber!");
			alert = builder.create();
			alert.show();
			return true;
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.WASHDISHES;
	}
}
