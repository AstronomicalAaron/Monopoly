package cs414.a4.n;

/*************************************************************************************
 *                                      MONOPOLY									 *
 *************************************************************************************
 *                               CREATED ON: (C)10/28/2016							 *
 *                                   UPDATED ON: 10/28/2016						     *
 *                                   VERSION: 0.0.1									 *
 *                                     WRITTEN BY:									 *
 * 	    							   Joey Bzdek	                                 *
 * 								    Dylan Crescibene 								 *
 * 									 Chris Geohring 								 *
 * 									Aaron Barczewski								 *
 * 																					 *
 *************************************************************************************/

/*************************************************************************************
 * 											PLAYER									 *
 *************************************************************************************/

public class Player extends Owner {
	
	private Token token;
	
	private double bid = -1;
	
	private boolean bankrupt = false;
	
	int remainingTurnsJailed = 0;
	
	public boolean isBankrupt() {
		return bankrupt;
	}

	public void setBankrupt(boolean isBankrupt) {
		this.bankrupt = isBankrupt;
	}
	
	public boolean isJailed() {
		return remainingTurnsJailed > 0;
	}
	
	public void jail(){
		token.moveTo(10);
		this.remainingTurnsJailed = 3;
	}
	
	public Player(String name, TokenType tokenType) {
		super(name, 1500);
		
		token = new Token(tokenType);
	}
	
	public Token getToken() {
		return token;
	}
	
	public void setToken(Token token){
		this.token = token;		
	}
	
	public double getBid() {
		return bid;
	}
	
	public void setBid(double bid){
		this.bid = bid;		
	}
}