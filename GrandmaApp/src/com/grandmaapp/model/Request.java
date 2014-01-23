package com.grandmaapp.model;

import android.os.CountDownTimer;

import com.grandmaapp.model.Grandma.Requests;

public class Request {
	
	protected static long HOUR_IN_MS = 3600000;
	
	Grandma grandma;
	CountDownTimer timer;
	long timeMS;
	
	public void startTimer(){
		timer = new CountDownTimer(timeMS, 1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
		}.start();
	}
	
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

	public CountDownTimer getTimer() {
		return timer;
	}

	public void setTimer(CountDownTimer timer) {
		this.timer = timer;
	}

	public long getTimeMS() {
		return timeMS;
	}

	public void setTimeMS(long timeMS) {
		this.timeMS = timeMS;
	}
	
}
