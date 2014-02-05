package com.grandmaapp.model;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.model.Grandma.State;

/*
 * handles the Request to take medicine
 * the eat request needs a daytime (morning, noon or evening)
 * request handled successfully if daytime of request and given medicine fit and dialog shows up
 * wrong medicine cause death of brunhilde
 * e.g. if daytime is MORNING, handleRequest needs MEDICINE_MORNING
 * 
 */

public class Medicine extends Request {

	public enum Daytime {
		MORNING, NOON, EVENING
	}

	Daytime daytime;

	public Medicine(Daytime dt) {
		this.daytime = dt;
		setMedName();	
	}

	public Medicine() {
		this.daytime = Daytime.MORNING;
		name = "Medizin";
	}

	public boolean handleRequest(Requests r) {
		AlertDialog.Builder builder = new AlertDialog.Builder(grandma.getMainActivity());
		builder.setTitle("");
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						alert.dismiss();
					}
		});
		// given med and daytime need to fit
		if ((r == Requests.MEDICINE_MORNING && daytime == Daytime.MORNING) || 
				(r == Requests.MEDICINE_EVENING && daytime == Daytime.EVENING) || 
				(r == Requests.MEDICINE_NOON && daytime == Daytime.NOON)) {
			
			builder.setMessage("Brunhilde hat ihre richtigen Medikamente bekommen!");
			alert = builder.create();
			alert.show();
			return true;
		} else {
			grandma.setState(State.DEAD);
			grandma.getMainActivity().brunhildeDied();			
			builder.setMessage("Brunhilde hat die falschen Medikamente bekommen und stirbt!");
			alert = builder.create();
			alert.show();
			return false;
		}
	}

	public Requests kind() {
		if(daytime == Daytime.MORNING){
			return Requests.MEDICINE_MORNING;
		}
		if(daytime == Daytime.EVENING){
			return Requests.MEDICINE_EVENING;
		}
		if(daytime == Daytime.NOON){
			return Requests.MEDICINE_NOON;
		}
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
