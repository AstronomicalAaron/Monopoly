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
 * 									Aaron Barczewski								 *
 * 																					 *
 *************************************************************************************/

/*************************************************************************************
 * 										DIE											 *
 *************************************************************************************/

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