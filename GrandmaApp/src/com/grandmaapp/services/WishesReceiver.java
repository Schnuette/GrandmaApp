package com.grandmaapp.services;

import com.grandmaapp.activities.GrandmaActivity;
import com.grandmaapp.model.Grandma;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class WishesReceiver extends BroadcastReceiver
{
	private static GrandmaActivity activity;
	private static Grandma grandma;
	
	@Override
	public void onReceive( Context context, Intent intent )
	{
		// Check if the incoming message is from our broadcaster
        if(intent.getAction().equals( WishesService.BROADCAST_ID) )
        {
        	// Get name and time of the message
            String name = intent.getStringExtra(WishesService.NEW_REQUEST);
            int time = intent.getIntExtra( WishesService.NEW_REQUEST_TIME, -1 );
            
            // if name is given and activity is present then handle message
            if( name != null && activity != null )
            {
        		SharedPreferences prefs = PreferenceManager
        				.getDefaultSharedPreferences(activity);
        		
            	if( name.equals( "EAT" ) )
            	{
            		activity.addRequestButton( grandma.createEatRequest( prefs, time ) );
            	}
            	else if( name.equals( "DRINK" ) )
            	{
            		activity.addRequestButton( grandma.createDrinkRequest( time ) );
            	}
            	else if( name.equals( "WASHCLOTHES" ) )
            	{
            		activity.addRequestButton( grandma.createWashClothesRequest( time ) );
            	}
            	else if( name.equals( "SHOPPING" ) )
            	{
            		activity.addRequestButton( grandma.createShoppingRequest( time ) );
            	}
            	else if( name.equals( "MEDICINE" ) )
            	{
            		activity.addRequestButton( grandma.createMedicineRequest( prefs, time ) );
            	}
            	else if( name.equals( "SLEEP" ) )
            	{
            		activity.addRequestButton( grandma.createSleepRequest( time ) );
            	}
            	else if( name.equals( "WASHDISHES" ) )
            	{
            		activity.addRequestButton( grandma.createWashDishesRequest( time ) );
            	}
            	else if( name.equals( "SUITUP" ) )
            	{
            		activity.addRequestButton( grandma.createSuitUpRequest( time ) );
            	}
            	else if( name.equals( "GAME" ) )
            	{
            		activity.addRequestButton( grandma.createGameRequest( time ) );
            	}
            	else if( name.equals( "CLEANFLAT" ) )
            	{
            		activity.addRequestButton( grandma.createCleanFlatRequest( time ) );
            	}else if(name.equals("BrunhildeDied")){
            		activity.brunhildeDied();
            	}
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
