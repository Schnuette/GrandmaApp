package com.grandmaapp.sensors;

import com.example.grandmaapp.R;
import com.grandmaapp.model.Request;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;

public class MiniGame
{
	enum Type
	{
		SCISSORS("Schere"),
		ROCK("Stein"),
		PAPER("Papier");
		
		private String name;
		
		Type( String name )
		{
			this.name = name;
		}
		
		public String toString( )
		{
			return name;
		}
	}
	
	enum Result
	{
		LOST,
		DRAW,
		WON;
	}
	
	private Activity activity;
	private Type choice;
	private Result result;
	private Type grandmaChoice;
	private Request request;
	
	public void showChoices( )
	{
		AlertDialog.Builder builder = new AlertDialog.Builder( activity );
		builder.setCancelable( false );
		builder.setPositiveButton( "", new OnClickListener( )
		{
			@Override
            public void onClick( DialogInterface dialog, int which )
            {
				choice = Type.SCISSORS;
				
				determineWinner( );
            }
		} )
		.setNeutralButton( "", new OnClickListener( )
		{
			@Override
            public void onClick( DialogInterface dialog, int which )
            {
				choice = Type.ROCK;
				
				determineWinner( );
            }
		} )
		.setNegativeButton( "", new OnClickListener( )
		{
			@Override
            public void onClick( DialogInterface dialog, int which )
            {
				choice = Type.PAPER;
				
				determineWinner( );
            }
		} );
		
		final AlertDialog dialog = builder.create( );
		
		dialog.setOnShowListener( new OnShowListener( )
		{
			
			@Override
			public void onShow( DialogInterface dialogInterface )
			{
				dialog.getButton( AlertDialog.BUTTON_POSITIVE )
				.setCompoundDrawables( activity.getResources( ).getDrawable( R.drawable.scissors ), null, null, null );
				
				dialog.getButton( AlertDialog.BUTTON_NEUTRAL )
				.setCompoundDrawables( activity.getResources( ).getDrawable( R.drawable.rock ), null, null, null );

				dialog.getButton( AlertDialog.BUTTON_NEGATIVE )
				.setCompoundDrawables( activity.getResources( ).getDrawable( R.drawable.paper ), null, null, null );
			}
		} );
		
		dialog.show( );
	}
	
	public void showResult( )
	{
		String text = "Oma waehlte " + grandmaChoice + " und du hast " + choice + " gewaehlt!";
		
		if( result == Result.LOST )
		{
			text += " Du hast verloren!";
		}
		else if( result == Result.WON )
		{
			text += " Du hast gewonnen!";
		}
		else
		{
			text += " Es gab ein Unentschieden!";
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder( activity );
		builder.setTitle( text );
		builder.setCancelable( false );
		builder.setPositiveButton( "OK", new OnClickListener( )
		{
			@Override
            public void onClick( DialogInterface dialog, int which )
            {
				
            }
		} );	
	}
	
	public void determineWinner( )
	{
		int grandmaChoiceRnd = (int) (Math.random( ) * 3);
		grandmaChoice = Type.SCISSORS;
		
		result = Result.DRAW;
		
		switch( grandmaChoiceRnd )
		{
			case 0:
			{
				grandmaChoice = Type.SCISSORS;
				break;
			}
			case 1:
			{
				grandmaChoice = Type.ROCK;
				break;
			}
			case 2:
			{
				grandmaChoice = Type.PAPER;
				break;
			}
		}
		
		if( choice == Type.SCISSORS )
		{
			if( grandmaChoice == Type.ROCK )
			{
				result = Result.LOST;
			}
			else if( grandmaChoice == Type.PAPER )
			{
				result = Result.WON;
			}
		}
		else if( choice == Type.ROCK )
		{
			if( grandmaChoice == Type.PAPER )
			{
				result = Result.LOST;
			}
			else if( grandmaChoice == Type.SCISSORS )
			{
				result = Result.WON;
			}
		}
		else if( choice == Type.PAPER )
		{
			if( grandmaChoice == Type.SCISSORS )
			{
				result = Result.LOST;
			}
			else if( grandmaChoice == Type.ROCK )
			{
				result = Result.WON;
			}
		}
		
		showResult( );
	}
}
