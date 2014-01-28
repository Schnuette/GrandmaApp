package com.grandmaapp.services;

import com.grandmaapp.model.Eat;
import com.grandmaapp.model.Medicine;
import com.grandmaapp.model.Grandma.Requests;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;


public class WishesService extends Service {
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Wuensche generieren, Benutzer informieren usw
		
		//zeiten der wünsche abfragen/speichern
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		Time now = new Time();
		now.setToNow();
		
		//time as integer from 0 to 2359 (hour and minutes)
		int time = Integer.parseInt(now.format2445().substring(9, 13));
		Log.d("test", String.valueOf(time));
		
		/*
		Editor editor = preferences.edit();
		editor.putInt(r.kind().toString(), calcExpireTime(r.getTimeMS()));	
		
		if(r.kind() == Requests.EAT){
			Eat eat = (Eat) r;
			editor.putString("FoodWish", eat.getFoodWish().toString());
		}
		if(r.kind() == Requests.MEDICINE){
			Medicine meds = (Medicine) r;
			editor.putString("MedWish", meds.getDaytime().toString());
		}
		editor.commit();
		*/
		
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO for communication return IBinder implementation
		return null;
	}

}
