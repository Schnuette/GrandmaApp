package com.grandmaapp.model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.model.Grandma.State;
import com.grandmaapp.model.Storeroom.Dish;

/*
 * handles the Request to Eat
 * the eat request needs a dish
 * if request was handled successfully a dialog will show up and a the eaten food will be removed from the storeroom
 * if the food is empty, request won't be handled  
 */

public class Eat extends Request {

	Dish foodWish;
	
	public Eat(){
		name = "Essen";
	}
	
	public Eat(Dish d){
		this.foodWish = d;
	}
	
	public boolean handleRequest(Requests r) {
		if (r == Requests.EAT) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(
					grandma.getMainActivity());
			builder.setTitle("");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							alert.dismiss();
						}
			});
			
			if (foodWish != null) {
				int numOfDish = grandma.getStoreroom().getFood().get(foodWish);
				if(numOfDish > 0){
					// update food in the storeroom 
					numOfDish -= 1;
					grandma.getStoreroom().getFood().put(foodWish, numOfDish);

					// update the food in the preferences 
					SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(grandma.getMainActivity());
					Editor editor = preferences.edit();
					switch (foodWish) {
					case BREAKFAST:
						editor.putInt("StoreBreakfast", numOfDish);
						builder.setMessage("Brunhilde hat gefr�hst�ckt. \n\n Anzahl: " + numOfDish);
						break;
					case SUPPER:
						editor.putInt("StoreSupper", numOfDish);
						builder.setMessage("Brunhilde hat zu Abend gegessen. \n\n Anzahl: " + numOfDish);
						break;
					case SCHNITZEL:
						editor.putInt("StoreSchnitzel", numOfDish);
						builder.setMessage("Brunhilde hat ein Schnitzel gegessen. \n\n Anzahl: " + numOfDish);
						break;
					case NOODLES:
						editor.putInt("StoreNoodles", numOfDish);
						builder.setMessage("Brunhilde hat Nudeln gegessen. \n\n Anzahl: " + numOfDish);
						break;
					case DOENER:
						editor.putInt("StoreDoener", numOfDish);
						builder.setMessage("Brunhilde hat einen D�ner gegessen. \n\n Anzahl: " + numOfDish);
						break;
					case PIZZA:
						editor.putInt("StorePizza", numOfDish);
						builder.setMessage("Brunhilde hat eine Pizza gegessen. \n\n Anzahl: " + numOfDish);
						break;
					}
					editor.commit();

					// different dialog message if no food request exists or brunhilde sleeps
					if (!realRequest) {
						builder.setMessage("Brunhilde hat das Essen verschm�ht! \n Sie hat keinen Hunger.");
						if(grandma.getState() == State.ASLEEP){
							builder.setMessage("Brunhilde kann jetzt nicht essen \n Sie schl�ft.");
						}
					}

					alert = builder.create();
					alert.show();
					return true;
				}
				else{
					builder.setMessage("Das Essen ist leer. Du musst erst einkaufen gehen!");
					alert = builder.create();
					alert.show();
				}
			}
		}
		return false;
	}

	public Requests kind(){
		return Requests.EAT;
	}
	
	public Dish getFoodWish() {
		return foodWish;
	}

	public void setFoodWish(Dish foodWish) {
		this.foodWish = foodWish;
		switch (foodWish) {
		case BREAKFAST:
			name = "Fr�hst�cken";
			break;
		case SUPPER:
			name = "Abendbrot";
			break;
		case SCHNITZEL:
			name = "Essen (Schnitzel)";
			break;
		case NOODLES:
			name = "Essen (Nudeln)";
			break;
		case DOENER:
			name = "Essen (D�ner)";
			break;
		case PIZZA:
			name = "Essen (Pizza)";
			break;
		}
	}
	
}
