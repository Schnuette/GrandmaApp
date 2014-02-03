package com.grandmaapp.model;

import com.grandmaapp.model.Grandma.Requests;

public class Game extends Request {

	public Game(){
		//timeMS = HOUR_IN_MS;
		name = "Spielen";
	}
	
	public boolean handleRequest(Requests r) {
		if (r == Requests.GAME) {
			// TODO sobald button gedrückt wird hier spiel starten
			
			return true;
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.GAME;
	}
	
}
