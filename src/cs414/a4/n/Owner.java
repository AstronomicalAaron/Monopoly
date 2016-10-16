package cs414.a4.n;

import java.util.*;

public abstract class Owner {

	private String id;
	
	private double money;
	
	private ArrayList<Deed> deeds;
	
	public Owner(String id, double initialMoney) {
		this.id = id;
		money = initialMoney;
		deeds = new ArrayList<Deed>();
	}
	
	public String getId() {
		return id;
	}

	public double getMoney() {
		return money;
	}
	
	public ArrayList<Deed> getDeeds() {
		return deeds;
	}

	public void transfer(Owner recipient, Double amount) {
		if (money >= amount) {
			this.money -= amount;
			recipient.money += amount;
		}
	}

}