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
			createSuitUpRequest( time, editor );
		}
		else if( time == 830 )
		{
			createMorningMedicineRequest( time, editor );
		}
		else if( time == 900 )
		{
			createBreakfastRequest( time, editor );
		}
		else if ( time == 1000 || time == 1300 || time == 1800 )
		{
			createDrinkRequest( preferences, time, editor );
		}
		else if( time == 1200 )
		{
			noFood = createLunchRequest( preferences, time, editor );
		}
		else if( time == 1100 || time == 1400 || time == 1900 )
		{
			createWashDishesRequest( time, editor );
		}
		else if( time == 1650 )
		{
			createEveningMedicineRequest( time, editor );
		}
		else if(  time == 1700 )
		{
			createSupperRequest( time, editor );
		}
		else if( time == 2000 )
		{
			createSleepRequest( time, editor );
		}
		
		// Storeroom supplies based request
		
		if( preferences.getInt( "StoreClothes", -1  ) <= 0 )
		{
			createWashClothesRequest( time, editor );
		}
		else if( noFood || preferences.getInt( "StoreWater", -1 ) <= 0 )
		{
			createShoppingRequest( time, editor );
		}
		
		editor.commit();
		
		return Service.START_NOT_STICKY;
	}

	public void createShoppingRequest( int time, Editor editor )
    {
	    editor.putInt( "SHOPPING", time + 100 );
    }

	public void createWashClothesRequest( int time, Editor editor )
    {
	    editor.putInt( "WASHCLOTHES", time + 1200 );
    }

	public void createSleepRequest( int time, Editor editor )
    {
	    editor.putInt(  "SLEEP", time + 100 );
	    notifyUser( "Brunhilde möchte schlafen!" );
    }

	public void createSupperRequest( int time, Editor editor )
    {
	    editor.putInt( "EAT", time + 100 );
	    editor.putString( "FoodWish", "SUPPER" );
	    notifyUser( "Brunhilde möchte zu Abend essen!" );
    }

	public void createEveningMedicineRequest( int time, Editor editor )
    {
	    editor.putInt( "MEDICINE", time + 100 );
	    editor.putString( "MedWish", "EVENING" );
    }

	public void createWashDishesRequest( int time, Editor editor )
    {
	    editor.putInt( "WASHDISHES", time + 100 );
	    notifyUser( "Brunhilde hat schmutziges Geschirr!" );
    }

	public boolean createLunchRequest( SharedPreferences preferences, int time, Editor editor )
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
	    	return true;
	    }
	    
	    return false;
    }

	public void createDrinkRequest( SharedPreferences preferences, int time, Editor editor )
    {
	    if( preferences.getInt( "StoreWater", -1 ) > 0 )
	    {
	    	editor.putInt( "DRINK", time + 30 );
	    	notifyUser( "Brunhilde möchte trinken!" );
	    }
	    else
	    {
	    	notifyUser( "Kein Wasser mehr vorrätig!" );
	    }
    }

	public void createBreakfastRequest( int time, Editor editor )
    {
	    editor.putInt( "EAT", time + 100 );
	    
	    editor.putString( "FoodWish", "BREAKFAST" );
	    notifyUser( "Brunhilde möchte Frühstück essen!" );
    }

	public void createMorningMedicineRequest( int time, Editor editor )
    {
	    editor.putInt( "MEDICINE", time + 100 );
	    editor.putString( "MedWish", "MORNING" );
	    
	    notifyUser( "Brunhilde muss ihre Morgen-Medizin nehmen!" );
    }

	public void createSuitUpRequest( int time, Editor editor )
    {
	    editor.putInt( "SUITUP", time + 30 );
	    notifyUser( "Brunhilde möchte angezogen werden!" );
    }

	public void notifyUser( String message )
	{
		Notifications.newNotification( message, this );
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO for communication return IBinder implementation
		return null;
	}

}
