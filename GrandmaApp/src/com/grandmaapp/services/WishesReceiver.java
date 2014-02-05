package com.grandmaapp.services;

import com.grandmaapp.activities.GrandmaActivity;
import com.grandmaapp.model.Grandma;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class WishesReceiver extends BroadcastReceiver
{
	private static GrandmaActivity activity;
	private static Grandma grandma;
	
	@Override
	public void onReceive( Context context, Intent intent )
	{
        if(intent.getAction().equals( WishesService.BROADCAST_ID) )
        {
            String extra = intent.getStringExtra(WishesService.NEW_REQUEST);
            int time = intent.getIntExtra( WishesService.NEW_REQUEST_TIME, -1 );
            
            if( extra != null && activity != null )
            {
        		SharedPreferences prefs = PreferenceManager
        				.getDefaultSharedPreferences(activity);
        		
            	if( extra.equals( "EAT" ) )
            	{
            		activity.addRequestButton( grandma.createEatRequest( prefs, time ) );
            	}
            	else if( extra.equals( "DRINK" ) )
            	{
            		activity.addRequestButton( grandma.createDrinkRequest( time ) );
            	}
            	else if( extra.equals( "WASHCLOTHES" ) )
            	{
            		activity.addRequestButton( grandma.createWashClothesRequest( time ) );
            	}
            	else if( extra.equals( "SHOPPING" ) )
            	{
            		activity.addRequestButton( grandma.createShoppingRequest( time ) );
            	}
            	else if( extra.equals( "MEDICINE" ) )
            	{
            		activity.addRequestButton( grandma.createMedicineRequest( prefs, time ) );
            	}
            	else if( extra.equals( "SLEEP" ) )
            	{
            		activity.addRequestButton( grandma.createSleepRequest( time ) );
            	}
            	else if( extra.equals( "WASHDISHES" ) )
            	{
            		activity.addRequestButton( grandma.createWashDishesRequest( time ) );
            	}
            	else if( extra.equals( "SUITUP" ) )
            	{
            		activity.addRequestButton( grandma.createSuitUpRequest( time ) );
            	}
            	else if( extra.equals( "GAME" ) )
            	{
            		activity.addRequestButton( grandma.createGameRequest( time ) );
            	}
            	else if( extra.equals( "CLEANFLAT" ) )
            	{
            		activity.addRequestButton( grandma.createCleanFlatRequest( time ) );
            	}
            	
                //Do something with the string
                Log.i( "Grandma", extra );
            }
        }
	}
	
	public static GrandmaActivity getActivity( )
	{
		return activity;
	}

	public static void setActivity( GrandmaActivity activity )
	{
		WishesReceiver.activity = activity;
	}

	public static Grandma getGrandma( )
	{
		return grandma;
	}

	public static void setGrandma( Grandma grandma )
	{
		WishesReceiver.grandma = grandma;
	}
}
