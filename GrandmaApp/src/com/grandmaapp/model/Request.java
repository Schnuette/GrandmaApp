package com.grandmaapp.model;

import android.app.AlertDialog;

import com.grandmaapp.model.Grandma.Requests;

/*
 * father class of all requests
 * important values:
 *  runtime = represents the left time of request, when object is created
 *  name = is the german name for the buttons
 *  AlertDialog = for the popup dialogs
 *  realRequest = needed if brunhilde has a real request or you fake a request
 * 
 */

public class Request {
	
	protected static long HOUR_IN_MS = 3600000;
	
	Grandma grandma;
	int runtime;
	String name = null;
	AlertDialog alert;
	boolean realRequest = true;
	
	public Requests kind(){
		return null;
	}
	
	public boolean handleRequest(Requests r) {	
		return false;
	}

	public Grandma getGrandma() {
		return grandma;
	}

	public void setGrandma(Grandma grandma) {
		this.grandma = grandma;
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRealRequest() {
		return realRequest;
	}

	public void setRealRequest(boolean realRequest) {
		this.realRequest = realRequest;
	}	
	
}
