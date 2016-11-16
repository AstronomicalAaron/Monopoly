package cs414.a4.n;

import java.time.LocalTime;

/*************************************************************************************
 *                                      MONOPOLY									 *
 *************************************************************************************
 *                               CREATED ON: (C)10/28/2016							 *
 *                                   UPDATED ON: 10/28/2016						     *
 *                                   VERSION: 0.0.1									 *
 *                                     WRITTEN BY:									 *
 * 	    							   Joey Bzdek	                                 *
 * 								    Dylan Crescibene 								 *
 * 									 Chris Geohring 								 *
 * 									Aaron Barczewski 								 *
 * 																					 *
 *************************************************************************************/

/*************************************************************************************
 * 										FACADE										 *
 *************************************************************************************/

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Monopoly {

	private GamePhase phase = GamePhase.WAITING;

	private Board board;

	private Bank bank;

	private ArrayList<Player> players;

	private int currentPlayerIndex;

	private int numberBankrupt = 0;

	private boolean inAuction;

	private double highestBid = 0;

	private int highestBidderIndex = -1;

	private int auctionTimeLeft = 10;

	private String nameOfAuctionTile = "";

	private String cardString = "";
	private Card currentCard;

	private int rolledValue = 0;

	private boolean rolledDoubles = false;

	private int numberOfHouses = 0;

	private LocalTime endTime;
	
	private Timer gameTimer;
	
	private int timeLeft;
	
	private String winner;

	public Monopoly() {
		board = new Board();
		bank = new Bank();
		players = new ArrayList<Player>();
		gameTimer = new Timer();
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

	public String getNameOfAuctionTile() {
		return nameOfAuctionTile;
	}

	public int getHighestBidderIndex() {
		return highestBidderIndex;
	}
	
	public int getTimeLeft() {
		return timeLeft;
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

	public String getCardString() {
		return cardString;
	}
	
	public String getWinner() {
		return winner;
	}

	public String landOnChance(Card chanceCard){

		Player currentPlayer = players.get(currentPlayerIndex);

		int currentTileIndex = currentPlayer.getToken().getTileIndex();

		//Player draws chance card that gives them credit
		if(chanceCard.getPayment() > 0){ 

			bank.transfer(currentPlayer, (double)chanceCard.getPayment());

		}

		//Player draws chance card in which they either have to pay bank or other players a penalty
		if(chanceCard.getCardIndex() != 5 && chanceCard.getDefPent() > 0){

			//When player draws card at index 3 they have to pay each player $50
			if(chanceCard.getCardIndex() == 3){

				for(Player p : players){

					if(!p.equals(currentPlayer)){

						currentPlayer.transfer(p, (double)chanceCard.getDefPent());

					}

				}

			}else{

				currentPlayer.transfer(bank, (double)chanceCard.getDefPent());

			}			

		}

		if(chanceCard.getCardIndex() == 5){

			//When player draws the chance card at index 5
			int totalPenalty = 0;

			int sumHouses = 0;

			int sumHotel = 0;

			for(int i : currentPlayer.getDeeds()){

				sumHouses += board.getTiles().get(i).getNumHouses();

				if(board.getTiles().get(i).getHasHotel()){

					sumHotel++;

				}

			}

			totalPenalty = (sumHouses*chanceCard.getDefPent()) + (sumHotel*chanceCard.getSecdPent());

			//If player doesn't have hotels or houses, they receive no penalty
			currentPlayer.transfer(bank, (double) totalPenalty);

			return chanceCard.getCardDesc();

		}

		//Move player's token on board depending on which card they draw.
		if(chanceCard.moveToIndex() != -1){

			double currentMoney = currentPlayer.getMoney();

			if(chanceCard.getCardIndex() == 0){

				//Cannot collect $200 when player goes to jail

				currentPlayer.getToken().moveTo(chanceCard.moveToIndex());

				if(currentMoney == currentMoney + 200){

					currentPlayer.transfer(bank, 200.0);

				}

				//Player is now in jail and jail rules apply				
				currentPlayer.jail();

			}

			if(chanceCard.getCardIndex() == 12)
				currentPlayer.getToken().moveTo(-1 * chanceCard.moveToIndex());
			else
				currentPlayer.getToken().moveTo(chanceCard.moveToIndex());		

		}

		//Player draws get-out-of-jail-free card
		if(chanceCard.getCardIndex() == 2){

			currentPlayer.setHasFreeJailCard(true);

			//Removes card out of deck
			chanceCard.removeFreedomCard();

		}

		int amountOnDice = board.getDice()[0].getValue() + board.getDice()[1].getValue();

		//Utility indices are: 12, 28
		if(chanceCard.getCardIndex() == 4){

			if(currentTileIndex < 12 || currentTileIndex >= 28){

				currentPlayer.getToken().moveTo(12);
				payRent(amountOnDice*10);

			}else{

				currentPlayer.getToken().moveTo(28);
				payRent(amountOnDice*10);

			}

		}

		//Railroad Indices are: 5, 15, 25, 35
		if(chanceCard.getCardIndex() == 6){

			if(currentTileIndex < 5 || currentTileIndex >= 35)
				currentPlayer.getToken().moveTo(5);
			else if(currentTileIndex < 15 || currentTileIndex >= 5)
				currentPlayer.getToken().moveTo(15);
			else if(currentTileIndex < 25 || currentTileIndex >= 15)
				currentPlayer.getToken().moveTo(25);
			else
				currentPlayer.getToken().moveTo(35);

			payRent(amountOnDice);
			payRent(amountOnDice);

		}


		return chanceCard.getCardDesc();


	}

	public String landOnCommunity(Card comChestCard){

		Player currentPlayer = players.get(currentPlayerIndex);

		//Player draws community chest card that gives them credit
		if(comChestCard.getPayment() > 0){ 

			bank.transfer(currentPlayer, (double)comChestCard.getPayment());

		}

		//Player draws community chest card in which they either have to pay bank or other players a penalty
		if(comChestCard.getCardIndex() != 5 && comChestCard.getDefPent() > 0){

			//When player draws card at index 3, each other player pays him $50
			if(comChestCard.getCardIndex() == 3){

				for(Player p : players){

					if(!p.equals(currentPlayer)){

						p.transfer(currentPlayer, (double)comChestCard.getDefPent());

					}

				}

			}else{

				currentPlayer.transfer(bank, (double)comChestCard.getDefPent());

			}			

		}

		if(comChestCard.getCardIndex() == 5){

			//When player draws the chance card at index 5
			int totalPenalty = 0;

			int sumHouses = 0;

			int sumHotel = 0;

			for(int i : currentPlayer.getDeeds()){

				sumHouses += board.getTiles().get(i).getNumHouses();

				if(board.getTiles().get(i).getHasHotel()){

					sumHotel++;

				}

			}

			totalPenalty = (sumHouses*comChestCard.getDefPent()) + (sumHotel*comChestCard.getSecdPent());

			//If player doesn't have hotels or houses, they receive no penalty
			currentPlayer.transfer(bank, (double) totalPenalty);

			return comChestCard.getCardDesc();

		}

		//Move player's token on board depending on which card they draw.
		if(comChestCard.moveToIndex() != -1){

			double currentMoney = currentPlayer.getMoney();

			if(comChestCard.getCardIndex() == 0){

				//Cannot collect $200 when player goes to jail

				currentPlayer.getToken().moveTo(comChestCard.moveToIndex());

				if(currentMoney == currentMoney + 200){

					currentPlayer.transfer(bank, 200.0);

				}

				//Player is now in jail and jail rules apply				
				currentPlayer.jail();

			}		

		}

		//Player draws get-out-of-jail-free card
		if(comChestCard.getCardIndex() == 2){

			currentPlayer.setHasFreeJailCard(true);

			//Removes card out of deck
			comChestCard.removeFreedomCard();

		}

		return comChestCard.getCardDesc();

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

		if (playerIndex == -1 || players.get(playerIndex).getMoney() < bid){
			return;
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

	public void start(int timeLimit) {
		if (phase != GamePhase.WAITING) {
			throw new IllegalStateException("Cannot start the game unless in the waiting phase.");
		}
		
		endTime = LocalTime.now().plusMinutes(timeLimit);
		timeLeft = timeLimit;
		gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
            	if(timeLeft > 0)
            		timeLeft--;
            }
        }, 60000, 60000);
		
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
		startTurn();
	}

	private void startTurn() {
		
		if(LocalTime.now().isAfter(endTime)){
			endGame();
			return;
		}
		
		Player currentPlayer = players.get(currentPlayerIndex);

		if (currentPlayer.isJailed()) {
			phase = GamePhase.JAILED;
			return;
		}
		else {
			rollDice();
		}
	}

	private void rollDice(){
		phase = GamePhase.ROLLING;
		Player currentPlayer = players.get(currentPlayerIndex);

		int dieOneValue = 5; //board.getDice()[0].roll();
		int dieTwoValue = 2; //board.getDice()[1].roll();
		rolledDoubles = dieOneValue == dieTwoValue;
		rolledValue = dieOneValue + dieTwoValue;

		if (currentPlayer.isJailed())
		{
			new java.util.Timer().schedule( 
					new java.util.TimerTask() {
						@Override
						public void run() {
							endJailRoll(currentPlayer, rolledDoubles);
						}
					}, 
					3000 
					);	
		}
		else {
			new java.util.Timer().schedule( 
					new java.util.TimerTask() {
						@Override
						public void run() {
							doTile(currentPlayer, rolledValue);
						}
					}, 
					3000 
					);	
		}
	}

	private void endJailRoll(Player currentPlayer, boolean rolledDoubles) {
		if (rolledDoubles) {
			currentPlayer.remainingTurnsJailed = 0;
			rolledDoubles = false;
		}
		else {
			currentPlayer.remainingTurnsJailed--;
			startManagement();
			return;
		}
	}

	private void doTile(Player currentPlayer, int rolledValue) {

		int previousTileIndex = currentPlayer.getToken().getTileIndex();
		currentPlayer.getToken().moveBy(rolledValue);
		int currentTileIndex = currentPlayer.getToken().getTileIndex();
		Tile currentTile = board.getTiles().get(currentTileIndex);

		// Pass go
		if(previousTileIndex > currentTileIndex) {
			bank.transfer(currentPlayer, 200.0);
		}

		switch (currentTile.getType()){
		case PROPERTY:
		case UTILITY:
		case RAILROAD:
			if (currentTile.hasOwner()) {
				if (currentTile.getOwnerIndex() != currentPlayerIndex) {
					payRent(rolledValue);
				}
			}
			else {
				phase = GamePhase.BUY_PROPERTY;
				return;
			}
			break;
		case TAXES:
			bank.payTax(currentPlayer, currentTile);
			break;
		case GOTOJAIL:
			currentPlayer.jail();
			endTurn();
			return;
		case FREEPARKING:
			bank.awardFreeParking(currentPlayer);		
			break;
		case COMMUNITYCHEST:
			currentCard = new Card(cardType.COMMUNITY);
			cardString = currentCard.getCardDesc();
			phase = GamePhase.SHOWCARD;
			return;
		case CHANCE:
			currentCard = new Card(cardType.CHANCE);
			cardString = currentCard.getCardDesc();
			phase = GamePhase.SHOWCARD;
			return;
		default:
			break;
		}

		startManagement();
	}

	private void startManagement()
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

		Tile currentTile = board.getTiles().get(currentPlayer.getToken().getTileIndex());

		if(phase != GamePhase.BUY_PROPERTY){
			throw new IllegalStateException("Not currently in BUY_PROPERTY phase.");
		}

		currentPlayer.transfer(bank, currentTile.propertyCost);
		currentPlayer.getDeeds().add(currentPlayer.getToken().getTileIndex());
		currentTile.setOwnerIndex(currentPlayerIndex);

		if(currentTile.isRailRoad())
			currentPlayer.setNumRailRoadsOwned(currentPlayer.getNumRailRoadsOwned() + 1);

		if(currentTile.isUtility())
			currentPlayer.setUtilitiesOwned(currentPlayer.getNumUtilitiesOwned() + 1);

		startManagement();
	}

	public void auctionProperty(int tileIndex, double startingBid){
		inAuction = true;
		phase = GamePhase.AUCTION;

		highestBid = startingBid;
		highestBidderIndex = -1;
		auctionTimeLeft = 10;
		nameOfAuctionTile = board.getTiles().get(tileIndex).getName();

		Owner recipient;
		if (startingBid == 0) {
			recipient = bank;
		}
		else {
			recipient = players.get(currentPlayerIndex);
		}

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
							if (highestBid > startingBid)
							{
								Player winner = players.get(highestBidderIndex);
								winner.transfer(recipient, highestBid);
								if (!(recipient instanceof Bank)) {
									int i = recipient.getDeeds().indexOf(tileIndex);
									recipient.getDeeds().remove(i);
								}
								winner.getDeeds().add(tileIndex);
								board.getTiles().get(tileIndex).setOwnerIndex(highestBidderIndex);
								inAuction = false;
								if(board.getTiles().get(tileIndex).getType() == TileType.RAILROAD) {
									int numOwned = winner.getNumRailRoadsOwned();
									winner.setNumRailRoadsOwned(numOwned + 1);

									if(!recipient.equals(bank))
									{
										numOwned = recipient.getNumRailRoadsOwned();
										recipient.setNumRailRoadsOwned(numOwned - 1);
									}
								} else if (board.getTiles().get(tileIndex).getType() == TileType.UTILITY) {
									int numOwned = winner.getNumUtilitiesOwned();
									winner.setUtilitiesOwned(numOwned + 1);

									if(!recipient.equals(bank))
									{
										numOwned = recipient.getNumUtilitiesOwned();
										recipient.setUtilitiesOwned(numOwned - 1);
									}
								}
							}
							startManagement();
							this.cancel();

							for (Player p : players){
								p.setBid(0);
							}
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
		if(property.getHasHotel() || property.numHouses > 0){
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

		if(board.getTiles().get(propIndex).getType() == TileType.RAILROAD) {
			int numOwned = currentPlayer.getNumRailRoadsOwned();
			currentPlayer.setNumRailRoadsOwned(numOwned - 1);

			if(!recipient.equals(bank)){
				int numOwnedRecipient = recipient.getNumRailRoadsOwned();
				recipient.setNumRailRoadsOwned(numOwnedRecipient + 1);
			}
		} else if (board.getTiles().get(propIndex).getType() == TileType.UTILITY) {
			int numOwned = currentPlayer.getNumUtilitiesOwned();
			currentPlayer.setUtilitiesOwned(numOwned - 1);

			if(!recipient.equals(bank)){
				int numOwnedRecipient = recipient.getNumUtilitiesOwned();
				recipient.setUtilitiesOwned(numOwnedRecipient + 1);
			}
		}

		currentPlayer.getDeeds().remove(propIndex);
	}

	public void upgradeProperty(int index){
		//This method will add houses and hotels depending on some cases
		if(phase != GamePhase.TURN){

			throw new IllegalStateException("Can only upgrade property in TURN phase.");

		}

		//Bank is out of houses
		if(numberOfHouses >= bank.getNumberOfHouses())
			return;

		Player currentPlayer = players.get(currentPlayerIndex);

		Tile tile = board.getTiles().get(index);

		String curTileColor = tile.color;

		if(!tile.isProperty()){
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

		properties.remove(tile);

		//Check to see if player owns all the properties in same color group
		for(Tile temp : properties){

			if(!currentPlayer.getDeeds().contains(board.getTiles().indexOf(temp))){
				return; 
			}

		}

		//Can't add additional houses until houses are on other properties
		for(Tile temp : properties){

			if(tile.numHouses > temp.numHouses){
				return;				
			}

		}

		if(tile.numHouses == 4){

			currentPlayer.transfer(bank, tile.hotelCost);
			tile.setHotel(true);

		}else{	
			currentPlayer.transfer(bank, tile.houseCost);
			tile.numHouses++;
		}

		numberOfHouses++;

	}

	public void degradeProperty(int index){

		//This method will remove houses and hotels depending on some cases
		if(phase != GamePhase.TURN){

			throw new IllegalStateException("Can only degrade property in TURN phase.");

		}

		Player currentPlayer = players.get(currentPlayerIndex);

		Tile currentTile = board.getTiles().get(index);

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

		if(currentTile.getHasHotel()){

			bank.transfer(currentPlayer, currentTile.hotelCost/2);
			currentTile.setHotel(false);

		}else{	
			bank.transfer(currentPlayer, currentTile.houseCost/2);
			currentTile.numHouses--;
		}

		if(numberOfHouses > 0)
			numberOfHouses--;

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
		if(currentTile.getHasHotel() || currentTile.numHouses > 0){
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

				if(currentPlayer.getMoney() >= mortgageAmount) {
					currentPlayer.transfer(bank, mortgageAmount);
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

	public void sellToPlayers(int propertyIndex, double startingBid) {
		Tile tile = board.getTiles().get(propertyIndex);

		if (startingBid < tile.propertyCost / 2) {
			return;
		}

		auctionProperty(propertyIndex, startingBid);
	}

	public void sellToBank(int propertyIndex) {

		if (phase != GamePhase.TURN) {
			throw new IllegalStateException("Not currently in TURN phase.");
		}

		Tile property = board.getTiles().get(propertyIndex);
		Player currentPlayer = players.get(currentPlayerIndex);
		int propIndex = currentPlayer.getDeeds().indexOf(property);

		if (property.getHasHotel() || property.numHouses > 0) {
			return;
		}

		if (property.isMortgaged()) {
			return;
		}

		if (!currentPlayer.getDeeds().contains(property)) {
			return;
		}

		bank.transfer(currentPlayer, property.propertyCost / 2);

		currentPlayer.getDeeds().remove(propIndex);
		property.setOwnerIndex(-1);
	}

	public void payRent(int amountOnDice){

		Player currentPlayer = players.get(currentPlayerIndex);
		int tileIndex = currentPlayer.getToken().getTileIndex();
		Tile currentTile = board.getTiles().get(tileIndex);
		Player propOwner = players.get(currentTile.getOwnerIndex());

		if(currentTile.isMortgaged()){
			endTurn();
			return;
		}

		if(currentTile.getType() == TileType.PROPERTY){
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
			else if(currentTile.getHasHotel())
				currentPlayer.transfer(propOwner, currentTile.withHotel);
			else{
				try {
					throw new Exception("Cannot pay rent, no such rent option available.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if(currentTile.getType() == TileType.RAILROAD){

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
			if(currentTile.getType() == TileType.UTILITY){

				if(propOwner.getNumUtilitiesOwned() == 1) {			
					currentPlayer.transfer(propOwner, (double)4*amountOnDice);
				} else if (propOwner.getNumUtilitiesOwned() == 2) {			
					currentPlayer.transfer(propOwner, (double)10*amountOnDice);
				} else {
					try {
						throw new Exception("Cannot pay rent, no such rent option available.");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

	public void jailChoice(boolean choice) {
		Player player = players.get(currentPlayerIndex);
		if (choice == true && player.getMoney() >= 50) {
			player.remainingTurnsJailed = 0;
			player.transfer(bank, 50.0);
			player.getToken().moveBy(1);
			endTurn();
		}
		else {
			rollDice();
		}
	}

	public void endTurn() {
		Player currentPlayer = players.get(currentPlayerIndex);
		if (currentPlayer.getMoney() == 0) {
			//Player needs to be able to sell whatever they can to be able to pay taxes
			//A new button on ui that says pay tax? and if you cant pay tax and have nothing to left to sell than you lose.
			//phase = GamePhase.TURN;
			//False in this case means player is bankrupt
			//removePlayer(currentPlayer);
			bankrupt(currentPlayer);
		}

		if(phase == GamePhase.ENDGAME)
		{
			return;
		}
		
		// Automatically start the next turn
		if(rolledDoubles && currentPlayer.doublesRolled < 3) {
			++currentPlayer.doublesRolled;
		} else if (rolledDoubles && currentPlayer.doublesRolled == 3) {
			//go to jail you speed
			currentPlayer.jail();
		}

		if (!rolledDoubles)
		{
			currentPlayer.doublesRolled = 0;
			currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

			//If player is bankrupt, increment to the next player
			while(players.get(currentPlayerIndex).isBankrupt())
			{
				currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
			}
		}		
		startTurn();
	}

	public boolean isRolledDoubles() {
		return rolledDoubles;
	}

	public void setRolledDoubles(boolean rolledDoubles) {
		this.rolledDoubles = rolledDoubles;
	}

	public void bankrupt(Player patheticLoser) {
		patheticLoser.setBankrupt(true);
		numberBankrupt++;

		if(numberBankrupt == players.size() - 1){
			endGame();
			return;
		}
		
		for (int tileIndex : patheticLoser.getDeeds())
		{
			while(inAuction)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			auctionProperty(tileIndex, 0);
		}
	}

	public String endGame() {
		phase = GamePhase.ENDGAME;
		
		int [] netWorths = new int[players.size()];
		
		for(int i = 0; i < players.size(); i++){
			
			netWorths[i] = calculateNetWorth(players.get(i));
			
		}
		
		int maxIndex = 0;
		
		for(int i = 0; i < netWorths.length - 1; i++){
			
			if(netWorths[i] > netWorths[i+1]){
				
				maxIndex = i;
				
			}else{
				
				maxIndex = i+1;
				
			}
			
		}
		winner = players.get(maxIndex).getName();
		
		return winner;
			
	}
	
	public void resetGame(){
		
		board = new Board();
		bank = new Bank();
		players = new ArrayList<Player>();

		this.phase = GamePhase.WAITING;
		this.inAuction = false;
		this.numberBankrupt = 0;
		this.numberOfHouses = 0;
		
	}

	//Helper method to help determine the WINNER WINNER CHICKEN DINNER!!!
	public int calculateNetWorth(Player p){

		int totalNetWorth = 0;

		totalNetWorth += p.getMoney();

		for(int i : p.getDeeds()){

			totalNetWorth += board.getTiles().get(i).propertyCost;
			
			for(int j = 0; j < board.getTiles().get(i).numHouses; j++)
				totalNetWorth += board.getTiles().get(i).houseCost;
			
			if(board.getTiles().get(i).getHasHotel())
				totalNetWorth += board.getTiles().get(i).hotelCost;

		}

		return totalNetWorth;

	}

	public void acknowledgeCard() {
		
		if (currentCard == null) {
			return; // TODO: Log
		}
		
		if (currentCard.type == cardType.CHANCE) {
			landOnChance(currentCard);
		}
		else if (currentCard.type == cardType.COMMUNITY) {
			landOnCommunity(currentCard);
		}
		
		// Reset card
		currentCard = null;
		cardString = null;
		
		startManagement();
	}
}