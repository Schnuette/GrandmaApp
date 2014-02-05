package com.grandmaapp.sensors;

import com.example.grandmaapp.R;
import com.grandmaapp.model.Request;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

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
	
	private static MiniGame instance;
	
	private Activity activity;
	private Type choice;
	private Result result;
	private Type grandmaChoice;
	private Request request;
	
	private MiniGame( )
	{
		
	}
	
	public static MiniGame getInstance( )
	{
		if( instance == null )
		{
			instance = new MiniGame( );
		}
			
		return instance;
	}
	
	public void show( )
	{
		AlertDialog.Builder builder = new AlertDialog.Builder( activity );
		builder.setTitle( "Schere Stein Papier" );
		builder.setCancelable( false );
		builder.setPositiveButton( "Schere", new OnClickListener( )
		{
			@Override
            public void onClick( DialogInterface dialog, int which )
            {
				choice = Type.SCISSORS;
				
				determineWinner( );
            }
		} )
		.setNeutralButton( "Stein", new OnClickListener( )
		{
			@Override
            public void onClick( DialogInterface dialog, int which )
            {
				choice = Type.ROCK;
				
				determineWinner( );
            }
		} )
		.setNegativeButton( "Papier", new OnClickListener( )
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
				Drawable scissors = activity.getResources( ).getDrawable( R.drawable.scissors );
	            scissors.setBounds(0, 0, scissors.getIntrinsicWidth( ), scissors.getIntrinsicHeight( ));
				dialog.getButton( AlertDialog.BUTTON_POSITIVE )
				.setCompoundDrawables( scissors, null, null, null );

				Drawable rock = activity.getResources( ).getDrawable( R.drawable.rock );
				rock.setBounds( 0, 0, rock.getIntrinsicWidth( ), rock.getIntrinsicHeight( ) );
				dialog.getButton( AlertDialog.BUTTON_NEUTRAL )
				.setCompoundDrawables( rock, null, null, null );

				Drawable paper = activity.getResources( ).getDrawable( R.drawable.paper );
				paper.setBounds( 0, 0, paper.getIntrinsicWidth( ), paper.getIntrinsicHeight( ) );
				dialog.getButton( AlertDialog.BUTTON_NEGATIVE )
				.setCompoundDrawables( paper, null, null, null );
			}
		} );
		
		dialog.show( );
	}
	
	public void showResult( )
	{
		String text = "Brunhilde wählte " + grandmaChoice + "!";
		
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
			text += " Es gibt ein Unentschieden!";
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
		} ).create( ).show( );	
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
	
	public void setActivity( Activity activity )
	{
		this.activity = activity;
	}

	public void setRequest( Request request )
	{
		this.request = request;
	}	
}