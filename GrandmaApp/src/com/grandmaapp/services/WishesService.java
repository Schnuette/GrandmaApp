package com.grandmaapp.services;

import java.util.ArrayList;

import com.grandmaapp.activities.GrandmaActivity;
import com.grandmaapp.model.Eat;
import com.grandmaapp.model.Medicine;
import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.notification.Notifications;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
		
		
		Editor editor = preferences.edit();
//		editor.putInt(r.kind().toString(), calcExpireTime(r.getTimeMS()));	
//		
//		if(r.kind() == Requests.EAT){
//			Eat eat = (Eat) r;
//			editor.putString("FoodWish", eat.getFoodWish().toString());
//		}
//		if(r.kind() == Requests.MEDICINE){
//			Medicine meds = (Medicine) r;
//			editor.putString("MedWish", meds.getDaytime().toString());
//		}
		
		boolean noFood = false;
		
		// Time based requests
		if( time == 800 )
		{
			editor.putInt( "SUITUP", time + 50 );
			notifyUser( "Brunhilde möchte angezogen werden!" );
		}
		else if( time == 850 )
		{
			editor.putInt( "MEDICINE", time + 100 );
			editor.putString( "MedWish", "MORNING" );
			
			notifyUser( "Brunhilde muss ihre Morgen-Medizin nehmen!" );
		}
		else if( time == 900 )
		{
			editor.putInt( "EAT", time + 100 );
			
			editor.putString( "FoodWish", "BREAKFAST" );
			notifyUser( "Brunhilde möchte Frühstück essen!" );
		}
		else if ( time == 1000 || time == 1300 || time == 1800 )
		{
			if( preferences.getInt( "StoreWater", -1 ) > 0 )
			{
				editor.putInt( "DRINK", time + 50 );
				notifyUser( "Brunhilde möchte trinken!" );
			}
		}
		else if( time == 1200 )
		{
			String foodwish = "";
			
			ArrayList< String > availableFood = new ArrayList< String >( );
			
			if( preferences.getInt( "StoreSchnitzel", -1 ) > 0 )
			{
				availableFood.add( "SCHNITZEL" );
			}
			
			if( preferences.getInt( "StoreNoodles", -1 ) > 0 )
			{
				availableFood.add( "NOODLES" );
			}
			
			if( preferences.getInt( "StoreDoener", -1 ) > 0 )
			{
				availableFood.add( "DOENER" );
			}
			
			if( preferences.getInt( "StorePizza", -1 ) > 0 )
			{
				availableFood.add( "PIZZA" );
			}
			
			// If nothing is available a wish for shopping food is generated
			if( availableFood.size( ) != 0 )
			{
				int choice = (int) Math.random( ) * availableFood.size( );
			
				foodwish = availableFood.get( choice );
				
				if( foodwish.equals( "SCHNITZEL" ) || foodwish.equals( "PIZZA" ) )
				{
					editor.putInt( "MEDICINE", time + 100 );
					editor.putString( "MedWish", "NOON" );
					
					notifyUser( "Brunhilde braucht ihre Medizin!" );
				}
				
				editor.putInt( "EAT", time + 100 );
				editor.putString( "FoodWish", foodwish );
				notifyUser( "Brunhilde möchte essen!" );
			}
			else
			{
				noFood = true;
			}
		}
		else if( time == 1100 || time == 1400 || time == 1900 )
		{
			editor.putInt( "WASHDISHES", time + 100 );
			notifyUser( "Brunhilde hat schmutziges Geschirr!" );
		}
		else if( time == 1650 )
		{
			editor.putInt( "MEDICINE", time + 100 );
			editor.putString( "MedWish", "EVENING" );
		}
		else if(  time == 1700 )
		{
			editor.putInt( "EAT", time + 100 );
			editor.putString( "FoodWish", "SUPPER" );
			notifyUser( "Brunhilde möchte zu Abend essen!" );
		}
		else if( time == 2000 )
		{
			editor.putInt(  "SLEEP", time + 100 );
			notifyUser( "Brunhilde möchte schlafen!" );
		}
		
		// Storeroom supplies based request
		if( preferences.getInt( "StoreClothes", -1  ) <= 0 )
		{
			editor.putInt( "WASHCLOTHES", time + 1200 );
		}
		else if( noFood || preferences.getInt( "StoreWater", -1 ) <= 0 )
		{
			editor.putInt( "SHOPPING", time + 100 );
		}
		
		editor.commit();
		
		return Service.START_NOT_STICKY;
	}

	private void notifyUser( String message )
	{
		Notifications.newNotification( message, this );
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO for communication return IBinder implementation
		return null;
	}

}
