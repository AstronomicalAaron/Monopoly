package cs414.a4.n.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cs414.a4.n.Player;
import cs414.a4.n.TokenType;

public class PlayerTest {

	Player player;
	
	@Before
	public void setUp() throws Exception {
		player = new Player("Frodo",TokenType.DOG);
	}

	@Test
	public void testPlayer() {
		assert(player.getMoney() == 1500);
		assert(player.getName().equals("Frodo"));
		assert(player.getToken().getType() == TokenType.DOG);
	}

	@Test
	public void testGetName() {
		assertEquals(player.getName(), "Frodo");
	}

	@Test
	public void testGetMoney() {
		assertTrue(player.getMoney() == 1500);
	}

	@Test
	public void testGetDeeds() {
		assert(player.getDeeds().size() == 0);
	}

	@Test
	public void testHasDeed() {
		//Player does not own tile 1 // Medit ave...
		assert(player.hasDeed(1) == false);
	}

	@Test
	public void testTransfer() {
		Player player2 = new Player("Sam", TokenType.HAT);
		assert(player.getMoney() == 1500);
		assert(player.transfer(player2, 100.0) == 1400);
		assert(player.getMoney() == 1400);
	}

	@Test
	public void testGetNumRailRoadsOwned() {
		assert(player.getNumRailRoadsOwned() == 0);
	}

	@Test
	public void testGetNumUtilitiesOwned() {
		assert(player.getNumUtilitiesOwned() == 0);
	}

	@Test
	public void testSetUtilitiesOwned() {
		player.setUtilitiesOwned(1);
		assert(player.getNumUtilitiesOwned() == 1);
	}

	@Test
	public void testSetNumRailRoadsOwned() {
		player.setNumRailRoadsOwned(1);
		assert(player.getNumRailRoadsOwned() == 1);
	}
}
