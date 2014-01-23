package com.grandmaapp.model;

import java.util.HashMap;

import com.grandmaapp.model.Grandma.Requests;

public class Storeroom {

	public static int MAXCLEANCLOTHES = 7;
	public static int MAXWATER = 12;
	public static int MAXDINNERSUM = 8;
	public static int MAXDINNER = 2;
	public static int MAXBREAKFASTSUPPER = 5;
	
	public enum Dish{
		SCHNITZEL,
		NOODLES,
		DOENER,
		PIZZA,
		BREAKFAST,
		SUPPER
	}
	
	int cleanClothes;
	int waterBottles;
	int numDinner;
	HashMap<Dish, Integer> food = new HashMap<Dish, Integer>();
	
	public void fillStoreroom(){
		cleanClothes = MAXCLEANCLOTHES;
		this.fillFood();
	}
	
	public void fillFood(){
		waterBottles = MAXWATER;
		numDinner = MAXDINNERSUM;
		food.put(Dish.SCHNITZEL, MAXDINNER);
		food.put(Dish.NOODLES, MAXDINNER);
		food.put(Dish.DOENER, MAXDINNER);
		food.put(Dish.PIZZA, MAXDINNER);
		food.put(Dish.BREAKFAST, MAXBREAKFASTSUPPER);
		food.put(Dish.SUPPER, MAXBREAKFASTSUPPER);
	}
	
	public int calcDinnerSum(){
		int sum = 0;
		sum += food.get(Dish.SCHNITZEL);
		sum += food.get(Dish.NOODLES);
		sum += food.get(Dish.DOENER);
		sum += food.get(Dish.PIZZA);
		return sum;
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

	public int getWaterBottles() {
		return waterBottles;
	}

	public void setWaterBottles(int waterBottles) {
		this.waterBottles = waterBottles;
	}

	public int getNumDinner() {
		return numDinner;
	}

	public void setNumDinner(int numDinner) {
		this.numDinner = numDinner;
	}
	
}
