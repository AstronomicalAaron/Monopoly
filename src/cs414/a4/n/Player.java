package cs414.a4.n;

public class Player extends Owner {
	
	private Token token;
	
	private double bid = -1;
	
	private boolean isBankrupt = false;
	
	public boolean isBankrupt() {
		return isBankrupt;
	}

	public void setBankrupt(boolean isBankrupt) {
		this.isBankrupt = isBankrupt;
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