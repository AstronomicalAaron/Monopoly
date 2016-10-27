package cs414.a4.n.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cs414.a4.n.Token;
import cs414.a4.n.TokenType;

public class TokenTest {
	Token t;
	@Before
	public void setUp() throws Exception {
		t = new Token(TokenType.DOG);
	}

	@Test
	public void testToken() {
		assertNotNull(t);
	}

	@Test
	public void testGetType() {
		assertEquals(t.getType(), TokenType.DOG);
	}

	@Test
	public void testGetTileIndex() {
		//When created, a token is not set to any tile
		assertNull(t.getTileIndex());
	}

	@Test
	public void testMoveTo() {
		assertNull(t.getTileIndex());
		t.moveTo(2);
		assertEquals(t.getTileIndex(), 2);
	}

	@Test
	public void testMoveBy() {
		//Tile is on 2, based off of past test
		assertEquals(t.getTileIndex(), 2);
		t.moveBy(1);
		assertEquals(t.getTileIndex(), 3);
	}

}
