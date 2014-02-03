package com.grandmaapp.notification;

import com.example.grandmaapp.R;
import com.grandmaapp.activities.GrandmaActivity;
import android.app.Activity;
import android.app.Notification;
import android.app.Notification.InboxStyle;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Notifications
{
	static private Notifications instance;
	
	NotificationManager notificationManager;
	private int notifyId; //Notification id for updating notification
	private int numMessages; // Counts messages
	private Notification.InboxStyle inBoxStyle;
				
	static public Notifications getInstance()
	{
		if(instance == null)
		{
			instance = new Notifications( );
			instance.inBoxStyle  = new Notification.InboxStyle();
			instance.notifyId = 1337;
			instance.numMessages = 0;
		}
		return instance;
	}
	
	private Notifications (){};
	
	public void newNotification(String subject, Context myActivity)
	{
		//Creating intent, notification opens activity if touched
		Intent resultIntent = new Intent (myActivity, GrandmaActivity.class);
		
		// Creating an extra for the intent for resetting the counter on app resum
		resultIntent.putExtra( "Notify", "reset" );
		resultIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		PendingIntent pResultIntent = PendingIntent.getActivity( myActivity, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT );

		// Creating the Notification object
		Notification.Builder notiB = new Notification.Builder(myActivity)
		.setContentTitle( "Grandmaapp" )
		.setContentText( subject )
		.setSmallIcon( R.drawable.ic_launcher )
		.setContentIntent(pResultIntent)
		.setNumber( ++numMessages );
				
		// Sets a title for the Inbox style big view
		inBoxStyle.setBigContentTitle("Brunhildes Wünsche:");
		
		// Moves events into the big view
		inBoxStyle.addLine( subject );
		inBoxStyle.setBuilder( notiB );
		
		// Moves the big view style object into the notification object.
		Notification noti = inBoxStyle.build( );
				
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
	
	public void resetMessageCounter()
	{
		numMessages = 0;
		inBoxStyle = new Notification.InboxStyle( );
	}
}
