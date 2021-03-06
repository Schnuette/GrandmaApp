package com.grandmaapp.model;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.grandmaapp.model.Grandma.Requests;

/*
 * handles the Request to clean the flat
 * if request was handled successfully a dialog will show up
 * 
 */

public class CleanFlat extends Request {
	
	public CleanFlat(){
		name = "Wohnung putzen";
	}
	
	public boolean handleRequest(Requests r) {
		if(r == Requests.CLEANFLAT){
		
			AlertDialog.Builder builder = new AlertDialog.Builder(
					grandma.getMainActivity());
			builder.setTitle("");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							alert.dismiss();
						}
			});
			builder.setMessage("Die Wohnung ist jetzt wieder sauber!");
			alert = builder.create();
			alert.show();
			
			return true;
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.CLEANFLAT;
	}
}
