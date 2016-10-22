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
		if(players.size() < 2){

			//Must have more than 2 players to start a game

		}else if(players.size() > 4){

			//Must have 4 or less players

		}else{



		}

		// Reset player positions on board
		for (Player player : players) {
			player.getToken().move(board.getGo());
		}
	}

	public Board getBoard() {
		return board;
	}

	public void addPlayer(String name, TokenType token) {

		Player player = new Player(name, token);

		players.add(player);

	}

	public void rollDice(Player player){

		int currentPos = 0;
		int dieOneValue = 0;
		int dieTwoValue = 0;
		int destination = 0;
		int rollDoubleCount = 0;

		do {

			currentPos = board.getTiles().indexOf(player.getToken().getCurrentTile());

			dieOneValue = board.getDice()[0].roll();
			dieTwoValue = board.getDice()[1].roll();

			destination = currentPos + (dieOneValue + dieTwoValue);

			player.getToken().move(board.getTiles().get(destination));

			if(rollDoubleCount == 3){

				break;

			}

			
			rollDoubleCount++;
			

		}while(dieOneValue == dieTwoValue);

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
					
					double mortgageAmount = currentTile.getDeed().mortgageValue;
					
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
						currentTile.getDeed().propertyCost);
				
				//Add the deed to the player's deed list
				player.getDeeds().add(currentTile.getDeed());

			}

		}

	}

}
