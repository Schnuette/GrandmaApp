package com.grandmaapp.model;

import java.util.HashMap;

/*
 * represent a store room and contains all food, water and clothes
 * food saved in a hashmap and food is separated by the enum Dish
 * 
 */

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
	
	
	// calculates the sum of all left dinners
	public void calcDinnerSum(){
		int sum = 0;
		sum += food.get(Dish.SCHNITZEL);
		sum += food.get(Dish.NOODLES);
		sum += food.get(Dish.DOENER);
		sum += food.get(Dish.PIZZA);
		numDinner = sum;
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

	public int getMAXCLEANCLOTHES() {
		return MAXCLEANCLOTHES;
	}

	public int getMAXWATER() {
		return MAXWATER;
	}

	public int getMAXDINNERSUM() {
		return MAXDINNERSUM;
	}

	public int getMAXDINNER() {
		return MAXDINNER;
	}

	public int getMAXBREAKFASTSUPPER() {
		return MAXBREAKFASTSUPPER;
	}
	
}
