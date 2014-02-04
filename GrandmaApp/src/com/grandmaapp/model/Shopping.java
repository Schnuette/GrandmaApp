package com.grandmaapp.model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.model.Storeroom.Dish;

/*
 * handles the Request to do shopping
 * refills food and water in the storeroom if values are nearly empty
 * a dialog with storeroom values is shown after shopping
 * 
 */

public class Shopping extends Request {
	
	public Shopping(){
		name = "Einkaufen";
	}
	
	public boolean handleRequest(Requests r) {
		if (r == Requests.SHOPPING) {
			
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(grandma.getMainActivity());
			Editor editor = preferences.edit();
			 
			// refill water in the storeroom and preferences if less than 2 left
			if (grandma.getStoreroom().getWaterBottles() <= 2) {
				grandma.getStoreroom().setWaterBottles(Storeroom.MAXWATER);
				editor.putInt("StoreWater", Storeroom.MAXWATER);	
			}
			// refill Schnitzel in the storeroom and preferences if empty
			if (grandma.getStoreroom().getFood().get(Dish.SCHNITZEL) < 1) {
				grandma.getStoreroom().getFood().put(Dish.SCHNITZEL, Storeroom.MAXDINNER);
				editor.putInt("StoreSchnitzel", Storeroom.MAXDINNER);
			}
			// refill Noodles in the storeroom and preferences if empty
			if (grandma.getStoreroom().getFood().get(Dish.NOODLES) < 1) {
				grandma.getStoreroom().getFood().put(Dish.NOODLES, Storeroom.MAXDINNER);
				editor.putInt("StoreNoodles", Storeroom.MAXDINNER);
			}
			// refill Doener in the storeroom and preferences if empty
			if (grandma.getStoreroom().getFood().get(Dish.DOENER) < 1) {
				grandma.getStoreroom().getFood().put(Dish.DOENER, Storeroom.MAXDINNER);
				editor.putInt("StoreDoener", Storeroom.MAXDINNER);
			}
			// refill Pizza in the storeroom and preferences if empty
			if (grandma.getStoreroom().getFood().get(Dish.PIZZA) < 1) {
				grandma.getStoreroom().getFood().put(Dish.PIZZA, Storeroom.MAXDINNER);
				editor.putInt("StorePizza", Storeroom.MAXDINNER);
			}
			grandma.getStoreroom().calcDinnerSum();

			// refill breakfast in the storeroom and preferences if less than 1 left
			if (grandma.getStoreroom().getFood().get(Dish.BREAKFAST) <= 1) {
				grandma.getStoreroom().getFood().put(Dish.BREAKFAST, Storeroom.MAXBREAKFASTSUPPER);
				editor.putInt("StoreBreakfast", Storeroom.MAXBREAKFASTSUPPER);
			}
			// refill supper in the storeroom and preferences if less than 1 left
			if (grandma.getStoreroom().getFood().get(Dish.SUPPER) <= 1) {
				grandma.getStoreroom().getFood()
						.put(Dish.SUPPER, Storeroom.MAXBREAKFASTSUPPER);
				editor.putInt("StoreSupper", Storeroom.MAXBREAKFASTSUPPER);
			}
			editor.commit();
			
			AlertDialog.Builder builder = new AlertDialog.Builder(grandma.getMainActivity());
			builder.setTitle("");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							alert.dismiss();
						}
			});
			builder.setMessage("Du warst einkaufen. \n" + grandma.getMainActivity().getStoreroomStock());
			alert = builder.create();
			alert.show();
			return true;
		}
		return false;
	}
	
	public Requests kind(){
		return Requests.SHOPPING;
	}
	
}
