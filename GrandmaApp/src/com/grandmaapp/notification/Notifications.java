package com.grandmaapp.notification;

import com.example.grandmaapp.R;
import com.grandmaapp.activities.GrandmaActivity;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Notifications
{
	static NotificationManager notificationManager;
	static private int notifyId = 1337; //Notification id for updating notification
	static private int numMessages = 0; // Counts messages
				
	static public void newNotification(String subject, Activity myActivity)
	{
		//Creating intent, notification opens activity if touched
		Intent resultIntent = new Intent (myActivity, GrandmaActivity.class);
		resultIntent.putExtra( "Notify", "reset" );
		resultIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		
		PendingIntent pResultIntent = PendingIntent.getActivity( myActivity, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT );

		// Creating the Notification object
		Notification.Builder notiB = new Notification.Builder(myActivity)
		.setContentTitle( "Grandmaapp" )
		.setContentText( subject )
		.setSmallIcon( R.drawable.ic_launcher )
		.setContentIntent(pResultIntent)
		.setNumber( ++numMessages );

		Notification noti = notiB.getNotification( );
		
		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		
		// notify the notification manager
		notificationManager = (NotificationManager) myActivity.getSystemService( Context.NOTIFICATION_SERVICE );
		
		notificationManager.notify(1337, noti);
	}
	
	public int getNotifyId ()
	{
		return notifyId;
	}
	
	static public void resetMessageCounter()
	{
		numMessages = 0;
	}
}
