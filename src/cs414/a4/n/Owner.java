package cs414.a4.n;

import java.util.*;

public abstract class Owner {

	private String name;
	
	private double money;
	
	private ArrayList<Integer> deeds;
	
	public Owner(String id, double initialMoney) {
		this.name = id;
		money = initialMoney;
		deeds = new ArrayList<Integer>();
	}
	
	public String getName() {
		return name;
	}

	public double getMoney() {
		return money;
	}
	
	public ArrayList<Integer> getDeeds() {
		return deeds;
	}
	
	public boolean hasDeed(int tileIndex){
		
		return deeds.contains(tileIndex);
		
	}

	public void transfer(Owner recipient, Double amount) {
		if (money >= amount) {
			this.money -= amount;
			recipient.money += amount;
		}
	}

}