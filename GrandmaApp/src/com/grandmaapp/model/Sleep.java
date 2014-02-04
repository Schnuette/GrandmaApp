package com.grandmaapp.model;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.model.Grandma.State;
import com.grandmaapp.sensors.SleepDetector;

public class Sleep extends Request {

	public Sleep() {
		//timeMS = HOUR_IN_MS / 2;
		name = "Schlafen";
	}
	
	public boolean handleRequest(Requests r) {
		if(r == Requests.SLEEP){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(grandma.getMainActivity());
			builder.setTitle("");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							alert.dismiss();
						}
					});
			
			// sobald button gedrückt wird request wird geloescht
			if (realRequest) {
				if (grandma.getState() != State.ASLEEP) {
					SleepDetector.getInstance().show();
					grandma.setState(State.ASLEEP);
				} 
				else {
					builder.setMessage("Brunhilde schläft bereits.");
					alert = builder.create();
					alert.show();
				}
			return true;
			}
			else{
				builder.setMessage("Brunhilde möchte jetzt nicht schlafen.");
				alert = builder.create();
				alert.show();
			}
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.SLEEP;
	}
	
}
