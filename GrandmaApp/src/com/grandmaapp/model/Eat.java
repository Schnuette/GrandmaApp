package com.grandmaapp.model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.model.Medicine.Daytime;
import com.grandmaapp.model.Storeroom.Dish;

public class Eat extends Request {

	Dish foodWish;
	
	public Eat(){
		//this.timeMS = HOUR_IN_MS;
		name = "Essen";
	}
	
	public Eat(Dish d){
		this.foodWish = d;
		//this.timeMS = HOUR_IN_MS;
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
				// ein Essen nehmen, spuelen und einkaufen, falls das essen danach leer ist
				int numOfDish = grandma.getStoreroom().getFood().get(foodWish);
				if(numOfDish > 0){
					numOfDish -= 1;
					grandma.getStoreroom().getFood().put(foodWish, numOfDish);

					// prefs aktualisieren
					SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(grandma.getMainActivity());
					Editor editor = preferences.edit();
					switch (foodWish) {
					case BREAKFAST:
						editor.putInt("StoreBreakfast", numOfDish);
						builder.setMessage("Brunhilde hat gefrühstückt.");
						break;
					case SUPPER:
						editor.putInt("StoreSupper", numOfDish);
						builder.setMessage("Brunhilde hat zu Abend gegessen.");
						break;
					case SCHNITZEL:
						editor.putInt("StoreSchnitzel", numOfDish);
						builder.setMessage("Brunhilde hat ein Schnitzel gegessen.");
						break;
					case NOODLES:
						editor.putInt("StoreNoodles", numOfDish);
						builder.setMessage("Brunhilde hat Nudeln gegessen.");
						break;
					case DOENER:
						editor.putInt("StoreDoener", numOfDish);
						builder.setMessage("Brunhilde hat einen Döner gegessen.");
						break;
					case PIZZA:
						editor.putInt("StorePizza", numOfDish);
						builder.setMessage("Brunhilde hat eine Pizza gegessen.");
						break;
					}
					editor.commit();

					if (!realRequest) {
						builder.setMessage("Brunhilde hat das Essen verschmäht! Sie hat keinen Hunger.");
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
	}
	
}
