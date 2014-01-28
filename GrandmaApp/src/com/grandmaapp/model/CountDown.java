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
			reqButton.setText(reqName + " " + millisUntilFinished/60000 + ":" + (millisUntilFinished%60000)/1000);
		}else{
			reqButton.setText(reqName + " " + millisUntilFinished/3600000 + ":" + (millisUntilFinished%3600000)/60000);
		}
		
	}

}
