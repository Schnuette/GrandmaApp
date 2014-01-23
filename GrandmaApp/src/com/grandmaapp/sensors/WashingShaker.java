package com.grandmaapp.sensors;

import com.example.grandmaapp.R;
import com.example.grandmaapp.R.id;

import android.app.Activity;
import android.content.Context;
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
	
	private boolean shaking;

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
		
		// If the device is currently being shaked...
		if( shaking )
		{
			// ...and the standard derivation value shows the device lays still...
			if( s <= SHAKE_STOP )
			{
				// ...do something appropriate
				onShakingStopped( );
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
		((TextView) activity.findViewById( R.id.Shake )).setText( "You are shaking!" );
		((TextView) activity.findViewById( R.id.Shake )).setBackgroundColor( Color.GREEN );		
	}
	
	/**
	 * Called when a stop of shaking is noticed
	 */
	public void onShakingStopped( )
	{
		shaking = false;
		((TextView) activity.findViewById( R.id.Shake )).setText( "You are not shaking!" );
		((TextView) activity.findViewById( R.id.Shake )).setBackgroundColor( Color.RED );		
	}
	
	public boolean isShaking( )
	{
		return shaking;
	}
}
