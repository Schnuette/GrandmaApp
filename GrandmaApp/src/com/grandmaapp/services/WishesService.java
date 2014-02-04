package com.grandmaapp.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.grandmaapp.activities.GrandmaActivity;
import com.grandmaapp.model.Eat;
import com.grandmaapp.model.Medicine;
import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.notification.Notifications;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;


public class WishesService extends Service {
	
	public static final String BROADCAST_ID = "GRANDMA_APP";
	public static final String NEW_REQUEST = "NewRequest";
	
	private Context context;
	
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
		
		sendMessageToActivity( NEW_REQUEST, "TEEEEST" );
		
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
		
		context = this;
		
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
		else if( time == 1600 )
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
		
		if( checkForCleanFlatDay( ) )
		{
			createCleanFlatRequest( time, editor );
		}
		
		if( time >= 1500 && time <= 1800 )
		{
			int hours = (int) Math.random( ) * 3;
			int min = (int) Math.random( ) * 60;
			createGameRequest( time + hours * 100 + min, editor );
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

	private boolean checkForCleanFlatDay( )
    {
	    String weekDay;
		SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.GERMAN );

		Calendar calendar = Calendar.getInstance();
		weekDay = dayFormat.format(calendar.getTime());
		
		if( weekDay.equals( "MIT" ) )
		{
			return true;
		}
		
		return false;
    }

	public void sendMessageToActivity( String type, String message )
    {
		Intent intent = new Intent( BROADCAST_ID );
		intent.putExtra( type, message );
		sendBroadcast( intent );
    }

	public void createGameRequest( int time, Editor editor )
	{
	    editor.putInt( "GAME", time + 100 );
	    sendMessageToActivity( NEW_REQUEST, "GAME" );	
	}
	
	public void createCleanFlatRequest( int time, Editor editor )
	{
	    editor.putInt( "CLEANFLAT", time + 200 );
	    sendMessageToActivity( NEW_REQUEST, "CLEANFLAT" );		
	}
	
	public void createShoppingRequest( int time, Editor editor )
    {
	    editor.putInt( "SHOPPING", time + 100 );
	    sendMessageToActivity( NEW_REQUEST, "SHOPPING" );
    }

	public void createWashClothesRequest( int time, Editor editor )
    {
	    editor.putInt( "WASHCLOTHES", time + 1200 );
	    sendMessageToActivity( NEW_REQUEST, "WASHCLOTHES" );
    }

	public void createSleepRequest( int time, Editor editor )
    {
	    editor.putInt(  "SLEEP", time + 100 );
	    notifyUser( "Brunhilde möchte schlafen!", context );
	    
	    sendMessageToActivity( NEW_REQUEST, "SLEEP" );
    }

	public void createSupperRequest( int time, Editor editor )
    {
	    editor.putInt( "EAT", time + 100 );
	    editor.putString( "FoodWish", "SUPPER" );
	    notifyUser( "Brunhilde möchte zu Abend essen!", context );
	    
	    sendMessageToActivity( NEW_REQUEST, "EAT" );
    }

	public void createEveningMedicineRequest( int time, Editor editor )
    {
	    editor.putInt( "MEDICINE", time + 100 );
	    editor.putString( "MedWish", "EVENING" );
	    
	    sendMessageToActivity( NEW_REQUEST, "MEDICINE" );
    }

	public void createWashDishesRequest( int time, Editor editor )
    {
	    editor.putInt( "WASHDISHES", time + 100 );
	    notifyUser( "Brunhilde hat schmutziges Geschirr!", context );
	    
	    sendMessageToActivity( NEW_REQUEST, "WASHDISHES" );
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
	    		
	    		notifyUser( "Brunhilde braucht ihre Medizin!", context );
	    		
	    	    sendMessageToActivity( NEW_REQUEST, "MEDICINE" );
	    	}
	    	
	    	editor.putInt( "EAT", time + 100 );
	    	editor.putString( "FoodWish", foodwish );
	    	notifyUser( "Brunhilde möchte essen!", context );
	    	
		    sendMessageToActivity( NEW_REQUEST, "EAT" );
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
	    	notifyUser( "Brunhilde möchte trinken!", context );
	    	
		    sendMessageToActivity( NEW_REQUEST, "DRINK" );
	    }
	    else
	    {
	    	notifyUser( "Kein Wasser mehr vorrätig!", context );
	    }
    }

	public void createBreakfastRequest( int time, Editor editor )
    {
	    editor.putInt( "EAT", time + 100 );
	    
	    editor.putString( "FoodWish", "BREAKFAST" );
	    notifyUser( "Brunhilde möchte Frühstück essen!", context );
	    
	    sendMessageToActivity( NEW_REQUEST, "EAT" );
    }

	public void createMorningMedicineRequest( int time, Editor editor )
    {
	    editor.putInt( "MEDICINE", time + 100 );
	    editor.putString( "MedWish", "MORNING" );
	    
	    notifyUser( "Brunhilde muss ihre Morgen-Medizin nehmen!", context );
	    
	    sendMessageToActivity( NEW_REQUEST, "MEDICINE" );
    }

	public void createSuitUpRequest( int time, Editor editor )
    {
	    editor.putInt( "SUITUP", time + 30 );
	    notifyUser( "Brunhilde möchte angezogen werden!", context );
	    
	    sendMessageToActivity( NEW_REQUEST, "SUITUP" );
    }

	public void notifyUser( String message, Context context )
	{
		Notifications.getInstance( ).newNotification( message, this );
	}
	
	public Context getContext( )
    {
	    return context;
    }

	public void setContext( Context context )
    {
	    this.context = context;
    }

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO for communication return IBinder implementation
		return null;
	}

}
