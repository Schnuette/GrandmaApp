package com.grandmaapp.model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ImageView;

import com.example.grandmaapp.R;
import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.model.Grandma.State;
import com.grandmaapp.sensors.SleepDetector;

/*
 * handles the Request to Sleep
 * starts the sleepdetector if brunhilde has a real Sleep Request
 * else a dialog is shown
 * 
 */

public class Sleep extends Request {

	public Sleep() {
		name = "Schlafen";
	}
	
	public boolean handleRequest(Requests r) {
		if(r == Requests.SLEEP){
			AlertDialog.Builder builder = new AlertDialog.Builder(grandma.getMainActivity());
			builder.setTitle("");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							alert.dismiss();
						}
					});
			
			if (realRequest) {
				// starts Sleepdetector
				SleepDetector.getInstance().show();
				grandma.setState(State.ASLEEP);

				// TODO auslagern in SleepDetector.onSleep()
				ImageView grandmaImgV = (ImageView) grandma.getMainActivity().findViewById(R.id.grandmaImgView);
				grandmaImgV.setImageResource(R.drawable.grandma_asleep);
				grandma.getMainActivity().startMusic();
				return true;
			}
			else{
				if (grandma.getState() != State.ASLEEP) {
					builder.setMessage("Brunhilde möchte jetzt nicht schlafen.");
					alert = builder.create();
					alert.show();
				}
				else {
					builder.setMessage("Brunhilde schläft bereits.");
					alert = builder.create();
					alert.show();
				}
			}
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.SLEEP;
	}
	
}
