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
 * 									Aaron Barczewski 								 *
 * 																					 *
 *************************************************************************************/

/*************************************************************************************
 * 										BOARD TEST									 *
 *************************************************************************************/

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import cs414.a4.n.Board;
import cs414.a4.n.TileList;
import cs414.a4.n.TileType;

public class BoardTest {
	Board b;
	@Before
	public void setUp() throws Exception {
		b = new Board();
	}

	@Test
	public void testBoard() {
		assertNotNull(b);
	}

	@Test
	public void testGetTiles() {
		TileList t = b.getTiles();
		assertEquals(t.get(0).getType(), TileType.GO);
	}

	@Test
	public void testGetDice() {
		assertNotNull(b.getDice());
		int rollValue = b.getDice()[0].roll();
		assert(rollValue >= 0 && rollValue<= 6);
		
	}

	@Test
	public void testGetGo() {
		assertEquals(b.getGo(), b.getTiles().get(0));
	}

}
