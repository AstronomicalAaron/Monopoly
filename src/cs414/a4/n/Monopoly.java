package cs414.a4.n;

import java.util.ArrayList;

public class Monopoly {
	
	private Board board;
	
	private Bank bank;
	
	private ArrayList<Player> players;
	
	public Monopoly() {
		board = new Board();
		bank = new Bank();
		players = new ArrayList<Player>();
	}
	
	public void Start() {
		// Reset player positions on board
		for (Player player : players) {
			player.getToken().move(board.getGo());
		}
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void addPlayer(String name, TokenType token) {
		
	}
}
