package com.grandmaapp.model;

import android.os.CountDownTimer;
import android.widget.Button;


/*
 * implementation of the CountDownTimer
 * sets every second the values for the request buttons to show how much time is left for the request
 * 
 */

public class CountDown extends CountDownTimer{

	Button reqButton;
	String reqName;
	
	public CountDown(long millisInFuture, long countDownInterval, Button reqButton, String reqName) {
		super(millisInFuture, countDownInterval);
		this.reqButton = reqButton;
		this.reqName = reqName;
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTick(long millisUntilFinished) {
		if(millisUntilFinished < 3600000){
			// if left time < 1 hour, then time in format mm:ss
			reqButton.setText(reqName + " " + String.format("%02d", millisUntilFinished/60000) + ":" + String.format("%02d", (millisUntilFinished%60000)/1000));
		}else{
			// else time in format hh:mm
			reqButton.setText(reqName + " " + String.format("%02d", millisUntilFinished/3600000) + ":" + String.format("%02d", (millisUntilFinished%3600000)/60000));
		}
	}

}
