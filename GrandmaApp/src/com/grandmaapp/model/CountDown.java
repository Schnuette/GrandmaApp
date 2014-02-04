package com.grandmaapp.model;

import android.os.CountDownTimer;
import android.widget.Button;

public class CountDown extends CountDownTimer{

	Button reqButton;
	String reqName;
	
	public CountDown(long millisInFuture, long countDownInterval, Button reqButton, String reqName) {
		super(millisInFuture, countDownInterval);
		this.reqButton = reqButton;
		this.reqName = reqName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTick(long millisUntilFinished) {
		// TODO Auto-generated method stub
		if(millisUntilFinished < 3600000){
			// Ausgabe im Format mm:ss
			reqButton.setText(reqName + " " + String.format("%02d", millisUntilFinished/60000) + ":" + String.format("%02d", (millisUntilFinished%60000)/1000));
		}else{
			// Ausgabe im Format hh:mm
			reqButton.setText(reqName + " " + String.format("%02d", millisUntilFinished/3600000) + ":" + String.format("%02d", (millisUntilFinished%3600000)/60000));
		}
		
	}

}
