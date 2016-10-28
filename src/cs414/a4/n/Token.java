package cs414.a4.n;

/*************************************************************************************
 *                                      MONOPOLY									 *
 *************************************************************************************
 *                               CREATED ON: (C)10/28/2016							 *
 *                                   UPDATED ON: 10/28/2016						     *
 *                                   VERSION: 0.0.1									 *
 *                                     WRITTEN BY:									 *
 * 	    							    Joey Bzdek	                                 *
 * 								    Dylan Crescibene 								 *
 * 									 Chris Geohring 								 *
 * 									Aaron Barczewski								 *
 * 																					 *
 *************************************************************************************/

/*************************************************************************************
 * 											TOKEN									 *
 *************************************************************************************/

public class Token {
	
	private TokenType type;
	
	private int tileIndex;
	
	public Token(TokenType type) {
		this.type = type;
	}
	
	public TokenType getType() {
		return type;
	}
	
	public int getTileIndex() {
		return tileIndex;
	}
	
	public void moveTo(int tileIndex) {
		this.tileIndex = tileIndex;
	}
	
	public void moveBy(int amount) {
		tileIndex = (tileIndex + amount) % 40;
	}
}