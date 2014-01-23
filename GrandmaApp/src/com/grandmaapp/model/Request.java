package com.grandmaapp.model;

import com.grandmaapp.model.Grandma.Requests;

public class Request {
	
	Grandma grandma;
	
	public boolean handleRequest(Requests r) {	
		return false;
	}

	public Grandma getGrandma() {
		return grandma;
	}

	public void setGrandma(Grandma grandma) {
		this.grandma = grandma;
	}
	
}
