package cs414.a4.n;

public class Player extends Owner {
	
	private Token token;
	
	public Player(String name, TokenType tokenType) {
		super(name, 1500);
		
		token = new Token(tokenType);
	}
	
	public Token getToken() {
		return token;
	}
}