package cs414.a4.n.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cs414.a4.n.Bank;
import cs414.a4.n.Player;
import cs414.a4.n.TokenType;

public class BankTest {
	Bank bank;
	Player playa;
	
	@Before
	public void setUp() throws Exception {
		bank = new Bank();
		playa = new Player("Dilan", TokenType.CAR);
	}

	@Test
	public void testBank() {
		assert(bank.getMoney() == 2000000);
		assert(bank.getName().equals("Bank"));
	}
	
	@Test
	public void testGetFreeParking() {
		assert(bank.getFreeParkingPool() == 100);
	}
	
	@Test
	public void testAwardFreeParking() {
		bank.awardFreeParking(playa);
		assertEquals(1600, playa.getMoney(), 0);
	}

}
