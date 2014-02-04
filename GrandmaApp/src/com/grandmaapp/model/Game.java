package com.grandmaapp.model;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.model.Grandma.State;
import com.grandmaapp.sensors.MiniGame;

/*
 * handles the Request to play minigame
 * the minigame is started if grandma is awake
 * otherwise a dialog shows up
 * 
 */

public class Game extends Request {

	public Game(){
		name = "Spielen";
	}
	
	public boolean handleRequest(Requests r) {
		if (r == Requests.GAME) {
			if(grandma.getState() != State.ASLEEP){
				// starts the minigame
				MiniGame.getInstance( ).show( );
				return true;
			}
			else{
				AlertDialog.Builder builder = new AlertDialog.Builder(
						grandma.getMainActivity());
				builder.setTitle("");
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								alert.dismiss();
							}
				});
				builder.setMessage("Brunhilde schläft und kann jetzt nicht spielen.");
				alert = builder.create();
				alert.show();
			}	
			
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.GAME;
	}
	
}
