package cs414.a4.n;

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
 * 									  Aaron Barczewski								 *
 * 																					 *
 *************************************************************************************/

/*************************************************************************************
 * 											BOARD									 *
 *************************************************************************************/

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Board {

	private TileList tiles;
	private Die[] dice;

	public Board() {
		tiles = Json.<TileList>deserializeFromFile("resources/tiles.json", TileList.class);
		
		// cache tile indices
		for (int i = 0; i < tiles.size(); ++i) {
			tiles.get(i).index = i;
		}
		
		dice = new Die[] { new Die(), new Die() };
	}

	public TileList getTiles() {
		return tiles;
	}
	
	public Die[] getDice() {
		return dice;
	}
	
	@JsonIgnore
	public Tile getGo() {
		return tiles.get(0);
	}
}
