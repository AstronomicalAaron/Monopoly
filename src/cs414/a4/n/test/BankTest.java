package cs414.a4.n.test;

import static org.junit.Assert.*;

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
