package com.grandmaapp.model;

import com.grandmaapp.model.Grandma.Requests;

public class Medicine extends Request {

	public enum Daytime {
		MORNING, NOON, EVENING
	}

	Daytime daytime;

	public Medicine(Daytime dt) {
		this.daytime = dt;
		this.timeMS = HOUR_IN_MS;
		setMedName();	
	}

	public Medicine() {
		this.daytime = Daytime.MORNING;
		this.timeMS = HOUR_IN_MS;
		name = "Medizin";
	}

	public boolean handleRequest(Requests r) {
		/*
		 * pruefen welches Medikament verabreicht wird und welches Medikament
		 * die Grandma braucht
		 */
		if (r == Requests.MEDICINE_MORNING && daytime == Daytime.MORNING) {
			return true;
		} else if (r == Requests.MEDICINE_EVENING && daytime == Daytime.EVENING) {
			return true;
		} else if (r == Requests.MEDICINE_NOON && daytime == Daytime.NOON) {
			return true;
		} else {
			// TODO stirbt! popup warnung falsches med?
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
