package cs414.a4.n;

import java.util.Random;

public class Die {
	
	private Random random;
	private int value;
	
	public Die() {
		random = new Random();
	}

	public int roll() {
		value = random.nextInt(6) + 1;
		return value;
	}
	
	public int getValue() {
		return value;
	}
}