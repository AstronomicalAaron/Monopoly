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

		// Automatically roll the dice for the first player
		rollDice();
	}

	private void rollDice(){
		phase = GamePhase.ROLLING;

		int dieOneValue = 0;
		int dieTwoValue = 0;

		dieOneValue = board.getDice()[0].roll();
		dieTwoValue = board.getDice()[1].roll();
		rolledDoubles = dieOneValue == dieTwoValue;	

		Player currentPlayer = players.get(currentPlayerIndex);
		int previousTileIndex = currentPlayer.getToken().getTileIndex();
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

		if(previousTileIndex > currentTileIndex) {
			bank.transfer(currentPlayer, 200.0);
		}
	}

	public void buyMortgage(){

		Player currentPlayer = players.get(currentPlayerIndex);

		int currentTileIndex = currentPlayer.getToken().getTileIndex();

		Tile currentTile = board.getTiles().get(currentTileIndex);

		if(!currentTile.isProperty() && !currentTile.isRailRoad()){
			//create error message that the current tile is not a property tile
		}

		if(currentTile.hasOwner()){

			if(currentPlayer.hasDeed(currentTileIndex)){

				if(currentTile.isMortgaged()){

					//create an error message telling player they already have a
					//mortgage on this property

				}else{

					//Update mortgage status
					currentTile.setMortgaged(true);

					double mortgageAmount = currentTile.mortgageValue;

					//Player pays bank the mortgage value
					currentPlayer.transfer(bank, mortgageAmount);

				}


			}else{

				//create an error message that the property is owned by a
				//different player

			}

		}

	}

	public void buyProperty(){

		Player currentPlayer = players.get(currentPlayerIndex);

		Tile currentTile = board.getTiles().get(currentPlayer.getToken().getTileIndex());

		if(phase != GamePhase.BUY_PROPERTY){

			throw new IllegalStateException("Not currently in BUY_PROPERTY phase.");

		}

		if(currentTile.hasOwner()){

			//If the property already has an owner, the player can
			//ask the owner to buy the property or cancel

		}else{



		}


	}

	public void sellProperty(){

		if(phase != GamePhase.TURN){

			throw new IllegalStateException("Not currently in TURN phase.");

		}

		Player currentPlayer = players.get(currentPlayerIndex);

		Tile currentTile = board.getTiles().get(currentPlayer.getToken().getTileIndex());

		if(currentPlayer.getDeeds().contains(currentTile)){

			phase = GamePhase.SELL_PROPERTY;

		}

	}

	public void upgradeProperty(){
		//This method will add houses and hotels depending on some cases
		if(phase != GamePhase.TURN){

			throw new IllegalStateException("Can only upgrade property in TURN phase.");

		}

		Player currentPlayer = players.get(currentPlayerIndex);

		Tile currentTile = board.getTiles().get(currentPlayer.getToken().getTileIndex());

		String curTileColor = currentTile.color;

		//Extract properties with same color group
		ArrayList<Tile> properties = new ArrayList<Tile>();

		//Loop through the whole board and add tiles with same color to arraylist
		for(int i = 0; i < board.getTiles().size(); i++){

			if(curTileColor.equals(board.getTiles().get(i).color)){

				properties.add(board.getTiles().get(i));

			}

		}

		//Check to see if player owns all the properties in same color group
		for(Tile temp : properties){

			if(!currentPlayer.getDeeds().contains(temp)){
				return; 
			}

		}

		properties.remove(currentTile);

		//Can't add additional houses until houses are on other properties
		for(Tile temp : properties){

			if(currentTile.numHouses > temp.numHouses){
				return;				
			}

		}

		if(currentTile.numHouses == 4){

			currentPlayer.transfer(bank, currentTile.hotelCost);
			currentTile.setHotel(true);

		}else{	
			currentPlayer.transfer(bank, currentTile.houseCost);
			currentTile.numHouses++;
		}


	}

	public void degradeProperty(){

		//This method will remove houses and hotels depending on some cases
		if(phase != GamePhase.TURN){

			throw new IllegalStateException("Can only degrade property in TURN phase.");

		}

		Player currentPlayer = players.get(currentPlayerIndex);

		Tile currentTile = board.getTiles().get(currentPlayer.getToken().getTileIndex());

		String curTileColor = currentTile.color;

		//Extract properties with same color group
		ArrayList<Tile> properties = new ArrayList<Tile>();

		//Loop through the whole board and add tiles with same color to arraylist
		for(int i = 0; i < board.getTiles().size(); i++){

			if(curTileColor.equals(board.getTiles().get(i).color)){

				properties.add(board.getTiles().get(i));

			}

		}

		//If there's no houses, you can't degrade property, duh!
		if(currentTile.numHouses == 0){
			return; 
		}


		properties.remove(currentTile);

		//Can't remove houses unless the number of houses on all properties are same
		for(Tile temp : properties){

			if(currentTile.numHouses < temp.numHouses){
				return;				
			}

		}

		if(currentTile.hasHotel()){

			bank.transfer(currentPlayer, currentTile.hotelCost/2);
			currentTile.setHotel(false);

		}else{	
			bank.transfer(currentPlayer, currentTile.houseCost/2);
			currentTile.numHouses--;
		}

	}

	public void liftMortgage(){
		//Player lifts a mortgage buy paying the full mortgage value + another 10%
		//interest

		if(phase != GamePhase.TURN){

			throw new IllegalStateException("Can only lift mortgage in TURN phase.");

		}

		Player currentPlayer = players.get(currentPlayerIndex);

		Tile currentTile = board.getTiles().get(currentPlayer.getToken().getTileIndex());


		//Want to check if the player actually owns the tile and has a mortgage on it
		if(currentTile.isMortgaged() &&
				currentPlayer.getDeeds().contains(currentTile)){

			currentPlayer.transfer(getBank(), currentTile.mortgageValue + (currentTile.mortgageValue * 0.1));

		}

	}

	public void endTurn() {
		// Automatically start the next player's roll
		currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
		rollDice();
	}
}