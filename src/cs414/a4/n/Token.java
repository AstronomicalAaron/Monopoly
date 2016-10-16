package cs414.a4.n;

public class Token {
	
	private TokenType type;
	
	private Tile currentTile;
	
	public Token(TokenType type) {
		this.type = type;
	}
	
	public TokenType getType() {
		return type;
	}
	
	public Tile getCurrentTile() {
		return currentTile;
	}
	
	public void move(Tile tile) {
		currentTile = tile;
	}
}