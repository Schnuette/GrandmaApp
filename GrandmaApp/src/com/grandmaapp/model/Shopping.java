package com.grandmaapp.model;

import com.grandmaapp.model.Grandma.Requests;
import com.grandmaapp.model.Storeroom.Dish;

public class Shopping extends Request {
	
	public boolean handleRequest(Requests r) {
		if (r == Requests.SHOPPING) {
			grandma.getStoreroom().fillFood();
			
			/* den kram koennte man evtl noch mal brauchen
			 * falls einkaufsliste kommt
			 */
			
			/* 
			// wasser bei 3 flaschen auf 12 auffuellen
			if (grandma.getStoreroom().getWaterBottles() <= 2) {
				grandma.getStoreroom().setWaterBottles(Storeroom.MAXWATER);
			}
			// bei 2 mittagessen auffüllen
			if (grandma.getStoreroom().getNumDinner() <= 2) {
				grandma.getStoreroom().getFood()
						.put(Dish.SCHNITZEL, Storeroom.MAXDINNER);
				grandma.getStoreroom().getFood()
						.put(Dish.NOODLES, Storeroom.MAXDINNER);
				grandma.getStoreroom().getFood()
						.put(Dish.DOENER, Storeroom.MAXDINNER);
				grandma.getStoreroom().getFood()
						.put(Dish.PIZZA, Storeroom.MAXDINNER);
				grandma.getStoreroom().setNumDinner(Storeroom.MAXDINNERSUM);
			}
			// frühstück bei 1 auf 5
			if (grandma.getStoreroom().getFood().get(Dish.BREAKFAST) <= 1) {
				grandma.getStoreroom().getFood()
						.put(Dish.BREAKFAST, Storeroom.MAXBREAKFASTSUPPER);
			}
			// abendmahl bei 1 auf 5
			if (grandma.getStoreroom().getFood().get(Dish.SUPPER) <= 1) {
				grandma.getStoreroom().getFood()
						.put(Dish.SUPPER, Storeroom.MAXBREAKFASTSUPPER);
			}*/
			return true;
		}
		return false;
	}
	
}
