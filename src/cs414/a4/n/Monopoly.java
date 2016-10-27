package cs414.a4.n;

import java.util.ArrayList;
import java.util.Random;

public class Monopoly {

	private GamePhase phase = GamePhase.WAITING;

	private Board board;

	private Bank bank;

	private ArrayList<Player> players;

	private int currentPlayerIndex;

	private double highestBid = 0;
	
	private int highestBidderIndex = -1;
	
	private int auctionTimeLeft = 10;

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
	
	public double getHighestBid() {
		return highestBid;
	}
	
	public double getAuctionTimeLeft() {
		return auctionTimeLeft;
	}
	
	public int getHighestBidderIndex() {
		return highestBidderIndex;
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
	
	public void setBid(String username, double bid)
	{
		int playerIndex = -1;
		
		for (Player player : players){
			if (player.getName().equals(username)){
				playerIndex = players.indexOf(player);
				break;
			}
		}
		
		players.get(playerIndex).setBid(bid);
		if(bid > highestBid)
		{
			auctionTimeLeft = 10;
			highestBid = bid;
			highestBidderIndex = playerIndex;
		}
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
		int index = 0;
		while (players.size() < 2) {
			Player p = new Player("Test", TokenType.SHOE);
			players.add(p);
			Random rand = new Random();
			for (int i = 0; i < 3; ++i) {
				board.getTiles().get(rand.nextInt(40)).setOwnerIndex(index);
			}
			++index;
		}
		//

		if(players.size() < 2){

			//Must have more than 2 players to start a game
			return;
		}else if(players.size() > 4){

			//Must have 4 or less players
			return;
		}
		
		//For testing purposes
		//currentPlayerIndex = 0;

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

		//for testing purposes, lands on a property to test buy
		//dieOneValue = 5;
		//dieTwoValue = 5;
		
		dieOneValue = board.getDice()[0].roll();
		dieTwoValue = board.getDice()[1].roll();
		rolledDoubles = dieOneValue == dieTwoValue;

		Player currentPlayer = players.get(currentPlayerIndex);
		int previousTileIndex = currentPlayer.getToken().getTileIndex();
		currentPlayer.getToken().moveBy(dieOneValue + dieTwoValue);
		int currentTileIndex = currentPlayer.getToken().getTileIndex();
	    Tile currentTile = board.getTiles().get(currentTileIndex);
		String tileName = currentTile.getName();
		
		while (tileName.equals("JAIL") || 
				tileName.equals("GO") || 
				tileName.equals("CHANCE") || 
				tileName.equals("COMMUNITY CHEST") ||
				tileName.equals("FREE PARKING") || 
				tileName.equals("GO TO JAIL")) {
			
			currentPlayer.getToken().moveBy(1);
			currentTileIndex = currentPlayer.getToken().getTileIndex();
		    currentTile = board.getTiles().get(currentTileIndex);
			tileName = currentTile.getName();
		};
		
		// To make java happy
		Tile landedTile = currentTile;
		new java.util.Timer().schedule( 
			new java.util.TimerTask() {
				@Override
				public void run() {
					endRoll(currentPlayer, landedTile);
				}
			}, 
			3000 
		);

		if(previousTileIndex > currentTileIndex) {
			bank.transfer(currentPlayer, 200.0);
		}
	}

	private void endRoll(Player currentPlayer, Tile currentTile) {

		if(currentTile.getType() == TileType.TAXES) {
			//Player needs to pay taxes.
			//Player can possibly go bankrupt here.
			if(!currentPlayer.transfer(bank, currentTile.propertyCost)) {

				//Player needs to be able to sell whatever they can to be able to pay taxes
				//A new button on ui that says pay tax? and if you cant pay tax and have nothing to left to sell than you lose.
				//phase = GamePhase.TURN;
				//False in this case means player is bankrupt
				//removePlayer(currentPlayer);
				endTurn();
			} else {
				endTurn();
			}
		}
		else if(currentTile.hasOwner()) {
			startTurn();
			payRent();
		} else if(currentTile.propertyCost !=0) {
			phase = GamePhase.BUY_PROPERTY;
		} else {
			startTurn();
		}
	}
	
	private void startTurn()
	{
		if (players.get(currentPlayerIndex).getDeeds().isEmpty())
		{
			endTurn();
		}
		else
		{
			phase = GamePhase.TURN;
		}
	}

	public void buyProperty(){

		Player currentPlayer = players.get(currentPlayerIndex);

		Player recipient;

		double agreedAmount = 0.0;

		Tile currentTile = board.getTiles().get(currentPlayer.getToken().getTileIndex());

		if(phase != GamePhase.BUY_PROPERTY){

			throw new IllegalStateException("Not currently in BUY_PROPERTY phase.");

		}

		//Cannot buy a mortgage property
		if(currentTile.isMortgaged()){
			endTurn();
			return;
		}

		if(currentTile.hasOwner()){

			recipient = players.get(currentTile.getOwnerIndex());
			currentPlayer.transfer(recipient, agreedAmount);
			currentPlayer.getDeeds().add(currentPlayer.getToken().getTileIndex());

		}else{
			currentPlayer.transfer(bank, currentTile.propertyCost);
			currentPlayer.getDeeds().add(currentPlayer.getToken().getTileIndex());
			currentTile.setOwnerIndex(currentPlayerIndex);
		}

		if(currentTile.isRailRoad())
			currentPlayer.setNumRailRoadsOwned(currentPlayer.getNumRailRoadsOwned() + 1);
		
		startTurn();
	}
	
	public void passProperty(int tileIndex){
		phase = GamePhase.AUCTION;
		
		new java.util.Timer().scheduleAtFixedRate(
				new java.util.TimerTask() 
				{
					@Override
					public void run() {
						if(auctionTimeLeft > 0)
						{
							auctionTimeLeft--;
						}
						else
						{
							if (highestBid != 0)
							{
								Player winner = players.get(highestBidderIndex);
								winner.transfer(bank, highestBid);
								winner.getDeeds().add(tileIndex);
								board.getTiles().get(tileIndex).setOwnerIndex(highestBidderIndex);
								highestBid = 0;
								highestBidderIndex = -1;
								auctionTimeLeft = 10;
							}
							startTurn();
							this.cancel();
						}
					}
				}, 
				0, 1000 );
	}

	public void sellProperty(int propertyIndex, int recIndex, double amount){

		if(phase != GamePhase.TURN){

			throw new IllegalStateException("Not currently in TURN phase.");

		}

		Tile property = board.getTiles().get(propertyIndex);
		Player recipient = players.get(recIndex);
		Player currentPlayer = players.get(currentPlayerIndex);
		int propIndex = currentPlayer.getDeeds().indexOf(property);

		//Cannot sell a property with houses/hotels on it.
		if(property.hasHotel() || property.numHouses > 0){
			return;
		}

		//Cannot sell a property with a mortgage on it
		if(property.isMortgaged()){
			return;
		}

		if(recipient.equals(bank)){
			
			recipient.transfer(currentPlayer, property.propertyCost/2);
			property.setOwnerIndex(-1);
			
		}else{
			
			recipient.transfer(currentPlayer, amount);
			recipient.getDeeds().add(propIndex);
			
			
		}
		
		currentPlayer.getDeeds().remove(propIndex);
	}

	public void upgradeProperty(){
		//This method will add houses and hotels depending on some cases
		if(phase != GamePhase.TURN){

			throw new IllegalStateException("Can only upgrade property in TURN phase.");

		}

		Player currentPlayer = players.get(currentPlayerIndex);

		Tile currentTile = board.getTiles().get(currentPlayer.getToken().getTileIndex());

		String curTileColor = currentTile.color;

		if(!currentTile.isProperty()){
			return;
		}

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

		if(!currentTile.isProperty()) return;

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

		//Can't lift a mortgage on a upgraded property
		if(currentTile.hasHotel() || currentTile.numHouses > 0){
			return;
		}

		//Want to check if the player actually owns the tile and has a mortgage on it
		if(currentTile.isMortgaged() &&
				currentPlayer.getDeeds().contains(currentTile)){

			currentPlayer.transfer(getBank(), currentTile.mortgageValue + (currentTile.mortgageValue * 0.1));

		}

	}

	public void buyMortgage(){

		if(phase != GamePhase.BUY_PROPERTY){

			throw new IllegalStateException("Cannot buy mortgage, not currently in BUY_PROPERTY phase.");

		}

		Player currentPlayer = players.get(currentPlayerIndex);

		int currentTileIndex = currentPlayer.getToken().getTileIndex();

		Tile currentTile = board.getTiles().get(currentTileIndex);

		if(!currentTile.isProperty() && !currentTile.isRailRoad()){
			//create error message that the current tile is not a property tile
			endTurn();
			return;
		}

		if(currentPlayer.hasDeed(currentTileIndex)){

			if(currentTile.isMortgaged()){

				endTurn();
				return;

			}else{
				double mortgageAmount = currentTile.mortgageValue;

				if(currentPlayer.transfer(bank, mortgageAmount)) {
					//Update mortgage status
					currentTile.setMortgaged(true);
				}

			}


		}else{

			//create an error message that the property is owned by a
			//different player

		}

		endTurn();

	}

	public void payRent(){
		if(phase != GamePhase.TURN){

			throw new IllegalStateException("Cannot pay rent, not currently in TURN phase.");

		}

		Player currentPlayer = players.get(currentPlayerIndex);
		int tileIndex = currentPlayer.getToken().getTileIndex();
		Tile currentTile = board.getTiles().get(tileIndex);
		Player propOwner = players.get(currentTile.getOwnerIndex());

		if(currentTile.isMortgaged()){
			endTurn();
			return;
		}

		if(currentTile.equals(TileType.PROPERTY)){
			if(currentTile.numHouses == 0)
				currentPlayer.transfer(propOwner, currentTile.rent);
			else if(currentTile.numHouses == 1)
				currentPlayer.transfer(propOwner,  currentTile.with1House);
			else if(currentTile.numHouses == 2)
				currentPlayer.transfer(propOwner, currentTile.with2Houses);
			else if(currentTile.numHouses == 3)
				currentPlayer.transfer(propOwner, currentTile.with3Houses);
			else if(currentTile.numHouses == 4)
				currentPlayer.transfer(propOwner, currentTile.with4Houses);
			else if(currentTile.hasHotel())
				currentPlayer.transfer(propOwner, currentTile.withHotel);
			else{
				try {
					throw new Exception("Cannot pay rent, no such rent option available.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if(currentTile.equals(TileType.RAILROAD)){

			if(propOwner.getNumRailRoadsOwned() == 1)				
				currentPlayer.transfer(propOwner, currentTile.rent);
			else if(propOwner.getNumRailRoadsOwned() == 2)
				currentPlayer.transfer(propOwner, currentTile.with2Railroads);
			else if(propOwner.getNumRailRoadsOwned() == 3)
				currentPlayer.transfer(propOwner, currentTile.with3Railroads);
			else if(propOwner.getNumRailRoadsOwned() == 4)
				currentPlayer.transfer(propOwner, currentTile.with4Railroads);
			else{
				try {
					throw new Exception("Cannot pay rent, no such rent option available.");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
		
		endTurn();

	}
	
	public void endTurn() {
		// Automatically start the next turn
		if (!rolledDoubles)
		{
			currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
		}
		rollDice();
	}

	public boolean isRolledDoubles() {
		return rolledDoubles;
	}

	public void setRolledDoubles(boolean rolledDoubles) {
		this.rolledDoubles = rolledDoubles;
	}

	public void removePlayer(Player currentPlayer) {
		players.remove(currentPlayer);

		if(players.size() == 1) {
			endGame();
		}
	}

	public void endGame() {
		//Display Player Won
		//reset game
	}
}