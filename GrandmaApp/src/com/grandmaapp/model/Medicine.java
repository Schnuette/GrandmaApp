package com.grandmaapp.model;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.model.Grandma.State;

public class Medicine extends Request {

	public enum Daytime {
		MORNING, NOON, EVENING
	}

	Daytime daytime;

	public Medicine(Daytime dt) {
		this.daytime = dt;
		//this.timeMS = HOUR_IN_MS;
		setMedName();	
	}

	public Medicine() {
		this.daytime = Daytime.MORNING;
		//this.timeMS = HOUR_IN_MS;
		name = "Medizin";
	}

	public boolean handleRequest(Requests r) {
		/*
		 * pruefen welches Medikament verabreicht wird und welches Medikament
		 * die Grandma braucht
		 */
		AlertDialog.Builder builder = new AlertDialog.Builder(grandma.getMainActivity());
		builder.setTitle("");
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						alert.dismiss();
					}
		});
		
		if ((r == Requests.MEDICINE_MORNING && daytime == Daytime.MORNING) || 
				(r == Requests.MEDICINE_EVENING && daytime == Daytime.EVENING) || 
				(r == Requests.MEDICINE_NOON && daytime == Daytime.NOON)) {

			builder.setMessage("Brunhilde hat ihre richtigen Medikamente bekommen!");
			alert = builder.create();
			alert.show();
			return true;
		} else {
			// TODO stirbt! popup warnung falsches med? oma bild tot setzen?
			grandma.setState(State.DEAD);
			
			builder.setMessage("Brunhilde hat die falschen Medikamente bekommen und stirbt!");
			alert = builder.create();
			alert.show();
			return false;
		}
	}

	public Requests kind() {
		return Requests.MEDICINE;
	}

	public Daytime getDaytime() {
		return daytime;
	}

	public void setDaytime(Daytime daytime) {
		this.daytime = daytime;
		setMedName();
	}
	
	private void setMedName(){
		if(daytime == Daytime.EVENING){
			name = "Abend-Medizin";
		}else if(daytime == Daytime.MORNING){
			name = "Morgen-Medizin";
		}else if(daytime == Daytime.NOON){
			name = "Mittags-Medizin";
		}
	}

}
