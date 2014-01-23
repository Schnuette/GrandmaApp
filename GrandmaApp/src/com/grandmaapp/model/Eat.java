package com.grandmaapp.model;

import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.model.Medicine.Daytime;
import com.grandmaapp.model.Storeroom.Dish;

public class Eat extends Request {

	Dish foodWish;
	
	public Eat(){
		this.timeMS = HOUR_IN_MS;
	}
	
	public Eat(Dish d){
		this.foodWish = d;
		this.timeMS = HOUR_IN_MS;
	}
	
	public boolean handleRequest(Requests r) {
		if (r == Requests.EAT) {
			if (foodWish != null) {
				// ein Essen nehmen, spuelen und einkaufen, falls das essen danach leer ist
				int numOfDish = grandma.getStoreroom().getFood().get(foodWish);
				numOfDish -= 1;
				grandma.getStoreroom().getFood().put(foodWish, numOfDish);
				grandma.addRequest(new WashDishes());
				// Fruehstueck oder abendessen sonst Dinner
				if (foodWish == Dish.BREAKFAST || foodWish == Dish.SUPPER) {
					//einkaufen, falls Fruehstueck oder Abendessen leer ist
					if (numOfDish < 1) {
						grandma.addRequest(new Shopping());
					}
				} else {
					int numDinner = grandma.getStoreroom().getNumDinner() - 1;
					grandma.getStoreroom().setNumDinner(numDinner);
					//einkaufen, falls nur noch wenige Mittagessen übrig sind
					if (numDinner < 2) {
						grandma.addRequest(new Shopping());
					}
					// bei schwerem Essen: Medikamente
					if (foodWish == Dish.SCHNITZEL) {
						grandma.addRequest(new Medicine(Daytime.NOON));
					}
				}
				return true;
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
