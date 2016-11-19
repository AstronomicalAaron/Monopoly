package cs414.a4.n;

/*************************************************************************************
 *                                      MONOPOLY									 *
 *************************************************************************************
 *                               CREATED ON: (C)10/28/2016							 *
 *                                   UPDATED ON: 10/28/2016						     *
 *                                   VERSION: 0.0.1									 *
 *                                     WRITTEN BY:									 *
 * 	    								 Joey Bzdek	                                 *
 * 								    Dylan Crescibene 								 *
 * 									 Chris Geohring 								 *
 * 									Aaron Barczewski 								 *
 * 																					 *
 *************************************************************************************/

/*************************************************************************************
 * 											OWNER									 *
 *************************************************************************************/

import java.util.*;

public abstract class Owner {

	int index;
	
	private String name;
	
	private double money;
	
	private ArrayList<Integer> deedIndices;
	
	private int numRailRoadsOwned;
	
	private int numUtilitiesOwned;
	
	public Owner(String id, double initialMoney) {
		this.name = id;
		money = initialMoney;
		deedIndices = new ArrayList<Integer>();
	}
	
	public String getName() {
		return name;
	}

	public double getMoney() {
		return money;
	}
	
	public ArrayList<Integer> getDeeds() {
		return deedIndices;
	}
	
	public boolean hasDeed(int tileIndex){
		
		return deedIndices.contains(tileIndex);
		
	}
	
	public double transfer(Owner recipient, Tile tile, double price) {
		if (!(this instanceof Bank)) {
			int i = this.getDeeds().indexOf(tile.index);
			this.getDeeds().remove(i);
		}
		recipient.getDeeds().add(tile.index);
		tile.setOwnerIndex(recipient.index);
		
		if(tile.getType() == TileType.RAILROAD) {
			int numOwned = recipient.getNumRailRoadsOwned();
			recipient.setNumRailRoadsOwned(numOwned + 1);

			if(this.name != "Bank")
			{
				numOwned = this.getNumRailRoadsOwned();
				this.setNumRailRoadsOwned(numOwned - 1);
			}
		} else if (tile.getType() == TileType.UTILITY) {
			int numOwned = recipient.getNumUtilitiesOwned();
			recipient.setUtilitiesOwned(numOwned + 1);

			if(this.name != "Bank")
			{
				numOwned = this.getNumUtilitiesOwned();
				this.setUtilitiesOwned(numOwned - 1);
			}
		}
		
		return recipient.transfer(this, price);
	}

	public double transfer(Owner recipient, Double amount) {
		double newBalance = money - amount;
		if (newBalance < 0) newBalance = 0;
		double transfer = money - newBalance;
		recipient.money += transfer;
		money -= transfer;
		return transfer;
	}

	public int getNumRailRoadsOwned() {
		return numRailRoadsOwned;
	}
	
	public int getNumUtilitiesOwned() {
		return numUtilitiesOwned;
	}
	
	public void setUtilitiesOwned(int numUtilitiesOwned) {
		this.numUtilitiesOwned = numUtilitiesOwned;
	}

	public void setNumRailRoadsOwned(int numRailRoadsOwned) {
		this.numRailRoadsOwned = numRailRoadsOwned;
	}
}