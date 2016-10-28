package cs414.a4.n.test;

import org.junit.Before;
import org.junit.Test;

import cs414.a4.n.Player;
import cs414.a4.n.TokenType;

public class PlayerTest {

	Player Player;
	
	@Before
	public void setUp() throws Exception {
		Player = new Player("Frodo",TokenType.DOG);
	}

	@Test
	public void testPlayer() {
		assert(Player.getMoney() == 1500);
		assert(Player.getName().equals("Frodo"));
		assert(Player.getToken().getType() == TokenType.DOG);
	}

}
