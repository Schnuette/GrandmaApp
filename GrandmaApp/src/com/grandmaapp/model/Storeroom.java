package com.grandmaapp.model;

import java.util.HashMap;

import com.grandmaapp.model.Grandma.Requests;

public class Storeroom {

	public enum Dish{
		SCHNITZEL,
		NUDELN
	}
	
	int cleanClothes = 7;
	HashMap<Dish, Integer> food = new HashMap<Dish, Integer>();
	//HashMap<Dish, Food> requests = new HashMap<Dish, Food>();
	
	public Storeroom(){
		food.put(Dish.SCHNITZEL, 1);
		food.put(Dish.NUDELN, 1);
	}
	
	public int getCleanClothes() {
		return cleanClothes;
	}

	public HashMap<Dish, Integer> getFood() {
		return food;
	}

	public void setCleanClothes(int cleanClothes) {
		this.cleanClothes = cleanClothes;
	}
	
}
