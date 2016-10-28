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
 * 										TILE TEST									 *
 *************************************************************************************/

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cs414.a4.n.Monopoly;
import cs414.a4.n.Tile;
import cs414.a4.n.TileType;

public class TileTest {
	
	Monopoly m;
	Tile meditTile;

	@Before
	public void setUp() throws Exception {
		m = new Monopoly();
		meditTile = m.getBoard().getTiles().get(1);
	}

	@Test
	public void testTile() {
		assert(meditTile != null);
	}

	@Test
	public void testGetName() {
		assertEquals(meditTile.getName(), "MEDITERRANEAN AVENUE");
	}

	@Test
	public void testIsMortgaged() {
		assertEquals(meditTile.isMortgaged(), false);	
	}

	@Test
	public void testSetMortgaged() {
		meditTile.setMortgaged(true);
		assertEquals(meditTile.isMortgaged(), true);	
	}

	@Test
	public void testGetType() {
		assertEquals(meditTile.getType(), TileType.PROPERTY);	
	}

	@Test
	public void testGetOwnerIndex() {
		assertEquals(meditTile.getOwnerIndex(), -1);
	}

	@Test
	public void testIsProperty() {
		assert(meditTile.isProperty());
		
		Tile goTile = m.getBoard().getTiles().get(0);
		assertFalse(goTile.isProperty());
	}

	@Test
	public void testIsRailRoad() {
		assertFalse(meditTile.isRailRoad());
		
		Tile readRailTile = m.getBoard().getTiles().get(5);
		assert(readRailTile.isRailRoad());
	}

	@Test
	public void testIsUtility() {
		assertFalse(meditTile.isUtility());
		
		Tile electricTile = m.getBoard().getTiles().get(12);
		assert(electricTile.isUtility());
	}

	@Test
	public void testHasOwner() {
		assertFalse(meditTile.hasOwner());
	}

	@Test
	public void testHasHotel() {
		assertFalse(meditTile.getHasHotel());
	}

	@Test
	public void testSetHotel() {
		meditTile.setHotel(true);
		assert(meditTile.getHasHotel());
	}

}
