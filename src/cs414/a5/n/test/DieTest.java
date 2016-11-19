package cs414.a5.n.test;

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
 * 									Aaron Barczewski         						 *
 * 																					 *
 *************************************************************************************/

/*************************************************************************************
 * 										DIE TEST									 *
 *************************************************************************************/

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cs414.a5.n.Die;

public class DieTest {

	Die die;
	int rollValue;
	@Before
	public void setUp() throws Exception {
		die = new Die();
	}

	@Test
	public void testDie() {
		assertNotNull(die);
	}

	@Test
	public void testRoll() {
		rollValue = die.roll();
		assert(rollValue >= 0 && rollValue <= 6);
	}

	@Test
	public void testGetValue() {
		//Equal to the roll in testRoll()
		assertEquals(rollValue, die.getValue());
	}


}
