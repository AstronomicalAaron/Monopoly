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
		assertEquals(t.getTileIndex(), 0);
	}

	@Test
	public void testMoveTo() {
		assertEquals(t.getTileIndex(), 0);
		t.moveTo(2);
		assertEquals(t.getTileIndex(), 2);
	}

	@Test
	public void testMoveBy() {
		assertEquals(t.getTileIndex(), 0);
		t.moveBy(1);
		assertEquals(t.getTileIndex(), 1);
	}

}
