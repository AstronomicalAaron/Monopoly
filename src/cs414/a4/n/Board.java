package cs414.a4.n;

public class Board {

	public Board() {
		tiles = Json.<TileList>deserializeFromFile("resources/tiles.json", TileList.class);
	}
	
	public TileList tiles;
	
}
