package com.grandmaapp.sensors;

import com.example.grandmaapp.R;
import com.example.grandmaapp.R.id;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

/**
 * Class that performs actions when the device is shaking and when it is not<br>
 * Must be enabled and disabled on Activity's onResume and onPause methods so the app doesn't drain the battery<br>
 * Use as singleton
 * @author Marcel Viehmeier
 *
 */
public class WashingShaker implements SensorEventListener
{
	private static WashingShaker instance;
	
	private static final long TIME_UNTIL_WASHED = 3000000000L;
	
	private static final double SHAKE_START = 2.5;
	private static final double SHAKE_STOP = 1.0;
	
	private Activity activity;
	
	private SensorManager sensorManager;
	private Sensor accelerometer;
	
	private float x;
	private float y;
	private float z;
	private float gX;
	private float gY;
	private float gZ;
	private double s;
	
	private long time;
	private long currentTime;
	private long oldTime;
	private boolean shaking;

	private TextView text;

	private AlertDialog dialog;
	
	public static WashingShaker getInstance( )
	{
		if( instance == null )
		{
			instance = new WashingShaker( );
		}
		
		return instance;
	}
	
	
	private WashingShaker( )
	{
	}
	
	/**
	 * Initialize the class members
	 * @param act The Activity of which the sensor can be received from and registered to 
	 */
	public void init( Activity act )
	{
		this.activity = act;
		
		sensorManager = (SensorManager) activity.getSystemService( Context.SENSOR_SERVICE );
		accelerometer = sensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );
		
		time = TIME_UNTIL_WASHED;
		currentTime = System.nanoTime( );
		oldTime = System.nanoTime( );
	}
	
	/**
	 * Enable the WashingShaker by registering the accelerometer
	 */
	public void enable( )
	{
		sensorManager.registerListener( this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL );
	}
	
	/**
	 * Disable the WashingShaker by unregistering the accelerometer
	 */
	public void disable( )
	{
		sensorManager.unregisterListener( this, accelerometer );
	}

	/**
	 * Show the dialog that informs the user about what he needs to do
	 */
	public void show( )
	{
		enable( );
		
		AlertDialog.Builder builder = new AlertDialog.Builder( activity );
		builder.setTitle( "Brunhilde will die Wäsche gewaschen haben!" );
		builder.setCancelable( false );
		builder.setPositiveButton( "OK", new OnClickListener( )
		{
			@Override
            public void onClick( DialogInterface dialog, int which )
            {
				disable( );
            }
		} );
		
		text = new TextView( builder.getContext( ) );
		text.setText( "Schüttle dein Smartphone um die Wäsche zu waschen!" );
		builder.setView( text );
		
		dialog = builder.create( );
		
		dialog.setOnShowListener( new OnShowListener( )
		{
			@Override
			public void onShow( DialogInterface dialogInterface )
			{
				dialog.getButton( Dialog.BUTTON_POSITIVE ).setEnabled( false );
			}
		} );
		
		dialog.show( );
	}
	
	@Override
    public void onAccuracyChanged( Sensor sensor, int accuracy )
    {
	    // No need for this
    }

	@Override
    public void onSensorChanged( SensorEvent event )
    {
		// Save current event values to members
		x = event.values[ 0 ];
		y = event.values[ 1 ];
		z = event.values[ 2 ];
		
		// Account the gravity of earth
		gX = x / SensorManager.GRAVITY_EARTH;
		gY = y / SensorManager.GRAVITY_EARTH;
		gZ = z / SensorManager.GRAVITY_EARTH;
		
		// Standard derivation of handled values
		s = Math.sqrt( gX * gX + gY * gY + gZ * gZ );
		
		oldTime = currentTime;
		currentTime = System.nanoTime( );
		
		// If the device is currently being shaked...
		if( shaking )
		{
			// ...and the standard derivation value shows the device lays still...
			if( s <= SHAKE_STOP )
			{
				// ...do something appropriate
				onShakingStopped( );
			}
			else
			{
				time -= currentTime - oldTime;
				
				if( time < 0 )
				{
					time = 0;
				}
			}
		}
		// ...or if it is not...
		else
		{
			// ...and the standard derivation shows that the device is being shaked...
			if( s >= SHAKE_START )
			{
				// ...do something appropriate
				onShaking( );
			}			
		}
    }
	
	/**
	 * Called when a begin of shaking is noticed
	 */
	public void onShaking( )
	{
		shaking = true;
		
		if( time == 0 )
		{
			dialog.getButton( AlertDialog.BUTTON_POSITIVE ).setEnabled( true );
			text.setText( "Die Wäsche ist gewaschen!" );
		}
		else if ( time > TIME_UNTIL_WASHED / 3.f * 2.f )
		{
			text.setText( "Schüttle weiter, die Wäsche ist noch schmutzig!" );
		}
		else if( time <= TIME_UNTIL_WASHED / 3.f * 2.f )
		{
			text.setText( "Schüttle weiter, die Wäsche ist fast sauber!" );
		}
	}
	
	/**
	 * Called when a stop of shaking is noticed
	 */
	public void onShakingStopped( )
	{
		shaking = false;
	}
	
	public boolean isShaking( )
	{
		return shaking;
	}
}
