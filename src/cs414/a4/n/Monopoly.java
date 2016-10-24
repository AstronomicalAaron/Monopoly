package cs414.a4.n;

import java.util.ArrayList;
import java.util.Random;

public class Monopoly {

	private GamePhase phase = GamePhase.WAITING;
	
	private Board board;

	private Bank bank;

	private ArrayList<Player> players;
	
	private int currentPlayerIndex;
	
	private boolean rolledDoubles = false;

	public Monopoly() {
		board = new Board();
		bank = new Bank();
		players = new ArrayList<Player>();
	}
	
	public GamePhase getPhase() {
		return phase;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public Bank getBank() {
		return bank;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}
	
	public void join(String name, TokenType token) {
		if (phase != GamePhase.WAITING) {
			throw new IllegalStateException("Cannot join unless in the waiting phase.");
		}
		
		for (Player player : players) {
			if (player.getName().equals(name)) {
				throw new IllegalArgumentException("Player name already exists.");
			}
			if(player.getToken().getType() == token) {
				throw new IllegalArgumentException("Player token already exists.");
			}
		}
		
		Player player = new Player(name, token);
		player.getToken().moveTo(0);
		players.add(player);
	}

	public void start() {
		if (phase != GamePhase.WAITING) {
			throw new IllegalStateException("Cannot start the game unless in the waiting phase.");
		}
		
		// TEMP
		while (players.size() < 2) {
			players.add(new Player("Test", TokenType.SHOE));
		}
		//
		
		if(players.size() < 2){

			//Must have more than 2 players to start a game
			return;
		}else if(players.size() > 4){

			//Must have 4 or less players
			return;
		}
		
		currentPlayerIndex = (new Random()).nextInt(players.size());

		// Reset player positions on board
		for (Player player : players) {
			player.getToken().moveTo(0);
		}
		
		phase = GamePhase.ROLL;
	}

	public void rollDice(){
		if (phase != GamePhase.ROLL) {
			throw new IllegalStateException("Cannot roll dice unless in the turn phase.");
		}
		
		int dieOneValue = 0;
		int dieTwoValue = 0;

		dieOneValue = board.getDice()[0].roll();
		dieTwoValue = board.getDice()[1].roll();
		rolledDoubles = dieOneValue == dieTwoValue;	

		Player currentPlayer = players.get(currentPlayerIndex);
		currentPlayer.getToken().moveBy(dieOneValue + dieTwoValue);
		
		int currentTileIndex = currentPlayer.getToken().getTileIndex();
		Tile currentTile = board.getTiles().get(currentTileIndex);
		
		new java.util.Timer().schedule( 
	        new java.util.TimerTask() {
	            @Override
	            public void run() {
	            	if(currentTile.propertyCost !=0 && !currentTile.hasOwner())
	        		{
	        			phase = GamePhase.BUY_PROPERTY;
	        		}
	        		else
	        		{
	        			phase = GamePhase.TURN;
	        		}
	            }
	        }, 
	        3000 
		);
	}

	public void buyMortgage(Player player){

		int currentTileIndex = player.getToken().getTileIndex();
		Tile currentTile = board.getTiles().get(currentTileIndex);

		if(!currentTile.isProperty() && !currentTile.isRailRoad()){
			//create error message that the current tile is not a property tile
		}

		if(currentTile.hasOwner()){

			if(player.hasDeed(currentTileIndex)){

				int deedIndex = player.getDeeds().indexOf(currentTile);

				if(currentTile.isMortgaged()){

					//create an error message telling player they already have a
					//mortgage on this property

				}else{
					
					//Update mortgage status
					currentTile.setMortgaged(true);
					
					//Update player deed status
					//player.getDeeds().get(deedIndex).boughtMortgage = true;
					
					double mortgageAmount = currentTile.mortgageValue;
					
					//Player pays bank the mortgage value
					//currentTile.getOwner().transfer(bank, mortgageAmount);

				}


			}else{

				//create an error message that the property is owned by a
				//different player

			}

		}

	}

	public void buyProperty(Player player){
		
		Tile currentTile = board.getTiles().get(player.getToken().getTileIndex());

		if(currentTile.isProperty()){

			//Have some sort of box/window that asks the player
			//if they want to buy the property

			if(currentTile.hasOwner()){

				//If the property already has an owner, the player can
				//ask the owner to buy the property or cancel

			}else{

				

			}

		}

	}

}
