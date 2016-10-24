package cs414.a4.n;

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