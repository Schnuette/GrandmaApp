package com.grandmaapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;


public class WishesService extends Service {
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Wuensche generieren, Benutzer informieren usw
		
		Time now = new Time();
		now.setToNow();
		
		//time as integer from 0 to 2359 (hour and minutes)
		int time = Integer.parseInt(now.format2445().substring(9, 13));
		Log.d("test", String.valueOf(time));
		
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO for communication return IBinder implementation
		return null;
	}

}
