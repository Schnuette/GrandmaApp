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
 * Class that detects if the proximity sensor has been covered by a specified amount of time<br>
 * Use as singleton
 * @author Marcel Viehmeier
 *
 */
public class SleepDetector implements SensorEventListener
{
	private static SleepDetector instance;
	
	private static final long TIME_UNTIL_SLEEP = 5000000000L;
	
	private Activity activity;
	private SensorManager sensorManager;
	private Sensor proximity;
	private double distance;
	private boolean sleeping;
	
	private long currentTime;
	private long oldTime;
	
	public static SleepDetector getInstance( )
	{
		if( instance == null )
		{
			instance = new SleepDetector( );
		}
		
		return instance;
	}
	
	private SleepDetector( )
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
		proximity = sensorManager.getDefaultSensor( Sensor.TYPE_PROXIMITY );
	}
	
	/**
	 * Enable the SleepDetector by registering the proximity sensor
	 */
	public void enable( )
	{
		sensorManager.registerListener( this, proximity, SensorManager.SENSOR_DELAY_NORMAL );
	}
	
	/**
	 * Disable the SleepDetector by unregistering the proximity sensor
	 */
	public void disable( )
	{
		sensorManager.unregisterListener( this, proximity );
	}	
	
	@Override
    public void onAccuracyChanged( Sensor sensor, int accuracy )
    {
	    
    }

	@Override
    public void onSensorChanged( SensorEvent event )
    {
	    distance = event.values[ 0 ];

	    // If sleep has not yet been detected...
	    if( !sleeping )
	    {
	    	// ...and the proxmity sensor isn't covered...
	    	if( distance > 0 )
	    	{
	    		// ...get the current time...
		    	currentTime = System.nanoTime( );
		    	
	    		// ...and if the time counter has reached the time limit for sleep...
	    		if( oldTime != 0 && ( currentTime - oldTime ) >= TIME_UNTIL_SLEEP)
	    		{
	    			// ...do something appropriate
	    			onSleep( );
	    		}
	    	}
	    	// ...otherwise the proximity sensor is covered and we need the start time of the coverage
	    	else
	    	{
	    		oldTime = System.nanoTime( );
	    	}
	    }
    }
	
	/**
	 * Called when sleep time has been reached
	 */
	public void onSleep( )
	{
		sleeping = true;
	}
	
	/**
	 * Resets sleep state and distance to a non-covered value
	 */
	public void reset( )
	{
		sleeping = false;
		distance = proximity.getMaximumRange( );
		currentTime = 0;
		oldTime = 0;
	}
}
