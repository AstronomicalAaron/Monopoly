package cs414.a4.n.test;

/*************************************************************************************
 *                                      MONOPOLY									 *
 *************************************************************************************
 *                               CREATED ON: (C)10/28/2016							 *
 *                                   UPDATED ON: 10/28/2016						     *
 *                                   VERSION: 0.0.1									 *
 *                                     WRITTEN BY:									 *
 * 	    								Joey Bzdek	                                 *
 * 								    Dylan Crescibene 								 *
 * 									 Chris Geohring 								 *
 * 									Aaron Barczewski								 *
 * 																					 *
 *************************************************************************************/

/*************************************************************************************
 * 										BANK TEST									 *
 *************************************************************************************/

import org.junit.Before;
import org.junit.Test;

import cs414.a4.n.Bank;

public class BankTest {
	Bank bank;
	
	@Before
	public void setUp() throws Exception {
		bank = new Bank();
	}

	@Test
	public void testBank() {
		assert(bank.getMoney() == Double.MAX_VALUE);
		assert(bank.getName().equals("Bank"));
	}

}
