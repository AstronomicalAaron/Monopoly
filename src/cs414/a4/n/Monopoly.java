package cs414.a4.n;

import java.util.ArrayList;
import java.util.Random;

public class Monopoly {

	private GamePhase phase = GamePhase.WAITING;
	
	private Board board;

	private Bank bank;

	private ArrayList<Player> players;
	
	private Player currentPlayer;

	public Monopoly() {
		board = new Board();
		bank = new Bank();
		players = new ArrayList<Player>();
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
	
	public Player currentPlayer() {
		return currentPlayer;
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
		player.getToken().move(board.getGo());
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
		
		int first = (new Random()).nextInt(players.size());
		currentPlayer = players.get(first);

		// Reset player positions on board
		for (Player player : players) {
			player.getToken().move(board.getGo());
		}
		
		phase = GamePhase.TURN;
	}

	public void rollDice(){
		if (phase != GamePhase.TURN) {
			throw new IllegalStateException("Cannot roll dice unless in the turn phase.");
		}
		
		int currentPos = 0;
		int dieOneValue = 0;
		int dieTwoValue = 0;
		int destination = 0;

		currentPos = board.getTiles().indexOf(currentPlayer.getToken().getCurrentTile());

		dieOneValue = board.getDice()[0].roll();
		dieTwoValue = board.getDice()[1].roll();

		destination = (currentPos + (dieOneValue + dieTwoValue)) % 40;

		currentPlayer.getToken().move(board.getTiles().get(destination));
	}

	public void buyMortgage(Player player){

		Tile currentTile = player.getToken().getCurrentTile();


		if(!currentTile.isProperty() && !currentTile.isRailRoad()){

			//create error message that the current tile is not a property tile

		}

		if(currentTile.getDeed().hasOwner()){

			if(player.hasDeed(currentTile.getDeed())){

				int deedIndex = player.getDeeds().indexOf(currentTile);

				if(player.getDeeds().get(deedIndex).hasMortgage()){

					//create an error message telling player they already have a
					//mortgage on this property

				}else{
					
					//Update mortgage status
					currentTile.getDeed().boughtMortgage = true;
					
					//Update player deed status
					player.getDeeds().get(deedIndex).boughtMortgage = true;
					
					double mortgageAmount = currentTile.mortgageValue;
					
					//Player pays bank the mortgage value
					currentTile.getDeed().getOwner().transfer(bank, mortgageAmount);

				}


			}else{

				//create an error message that the property is owned by a
				//different player

			}

		}

	}

	public void buyProperty(Player player){
		
		Tile currentTile = player.getToken().getCurrentTile();

		if(currentTile.isProperty()){

			//Have some sort of box/window that asks the player
			//if they want to buy the property

			if(currentTile.getDeed().hasOwner()){

				//If the property already has an owner, the player can
				//ask the owner to buy the property or cancel

			}else{

				//I'm not sure if this is right.
				//What it's saying is the current tile that the player is own
				//transfers ownership from the bank to the player.
				currentTile.getDeed().getOwner().transfer(bank,
						currentTile.propertyCost);
				
				//Add the deed to the player's deed list
				player.getDeeds().add(currentTile.getDeed());

			}

		}

	}

}
