package com.grandmaapp.model;

import android.os.CountDownTimer;

import com.grandmaapp.model.Grandma.Requests;

public class Request {
	
	protected static long HOUR_IN_MS = 3600000;
	
	Grandma grandma;
	long timeMS;
	String name = null;
	
	public Requests kind(){
		return null;
	}
	
	public boolean handleRequest(Requests r) {	
		return false;
	}

	public Grandma getGrandma() {
		return grandma;
	}

	public void setGrandma(Grandma grandma) {
		this.grandma = grandma;
	}

	public long getTimeMS() {
		return timeMS;
	}

	public void setTimeMS(long timeMS) {
		this.timeMS = timeMS;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
