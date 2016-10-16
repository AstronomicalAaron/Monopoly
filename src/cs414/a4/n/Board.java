package cs414.a4.n;

public class Board {

	private TileList tiles;

	private Die[] dice;

	public Board() {
		tiles = Json.<TileList>deserializeFromFile("resources/tiles.json", TileList.class);
		dice = new Die[] { new Die(), new Die() };
	}

	public TileList getTiles() {
		return tiles;
	}
	
	public Die[] getDice() {
		return dice;
	}
	
	public Tile getGo() {
		return tiles.get(0);
	}
}
