package cs414.a4.n.test;

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
 * 									MONOPOLY TEST									 *
 *************************************************************************************/

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cs414.a4.n.GamePhase;
import cs414.a4.n.Monopoly;
import cs414.a4.n.Player;
import cs414.a4.n.TokenType;

public class MonopolyTest {

	Monopoly monopoly;
	Player player;
	Player player2;
	
	@Before
	public void setUp() throws Exception {
		monopoly = new Monopoly();
		
		monopoly.join("Johnson", TokenType.IRON);
		player = monopoly.getPlayers().get(0);
		
		monopoly.join("Bob", TokenType.CAR);
		player2 = monopoly.getPlayers().get(1);
	}

	@Test
	public void testMonopoly() {
		assert(monopoly.getBoard() != null);
		assert(monopoly.getBank() != null);
		assert(monopoly.getPlayers() != null);
	}

	@Test
	public void testGetPhase() {
		assert(monopoly.getPhase() == GamePhase.WAITING);
	}

	@Test
	public void testGetHighestBid() {
		assert(monopoly.getHighestBid() == 0);
	}

	@Test
	public void testGetAuctionTimeLeft() {
		assert(monopoly.getAuctionTimeLeft() == 10);
	}

	@Test
	public void testGetHighestBidderIndex() {
		assert(monopoly.getHighestBidderIndex() == -1);
	}

	@Test
	public void testGetCurrentPlayerIndex() {
		assert(monopoly.getCurrentPlayerIndex() == 0);
	}

	@Test
	public void testSetBid() {
		monopoly.setBid("Johnson", 420);
		Player player = monopoly.getPlayers().get(0);
		assertEquals(player.getBid(), 420, 0);
	}

	@Test
	public void testJoin() {
		assert(monopoly.getPlayers().size() == 2);
	}

	@Test
	public void testStart() {
		monopoly.start(10);
		assert(monopoly.getPhase() != GamePhase.WAITING);
	}

	@Test
	public void testBuyProperty() {
boolean thrown = false;
		
		try	{
			monopoly.buyProperty();
		}
		catch (IllegalStateException e){
			thrown = true;
		}
		
		assert(thrown);
	}

	@Test
	public void testAuctionProperty() {
		monopoly.auctionProperty(monopoly.getBank(), 1, 0);
		assert(monopoly.getBoard().getTiles().get(1).getOwnerIndex() == -1);
	}

	@Test
	public void testSellProperty() {
		boolean thrown = false;
		
		try	{
			monopoly.sellProperty(0,1,200);
		}
		catch (IllegalStateException e){
			thrown = true;
		}
		
		assert(thrown);
	}

	@Test
	public void testUpgradeProperty() {
		boolean thrown = false;
			
		try	{
			monopoly.upgradeProperty(1);
		}
		catch (IllegalStateException e){
			thrown = true;
		}
		
		assert(thrown);
	}

	@Test
	public void testDegradeProperty() {
		boolean thrown = false;
		
		try	{
			monopoly.degradeProperty(1);
		}
		catch (IllegalStateException e){
			thrown = true;
		}
		
		assert(thrown);
	}

	@Test
	public void testLiftMortgage() {
		boolean thrown = false;
		
		try	{
			monopoly.liftMortgage(2);
		}
		catch (IllegalStateException e){
			thrown = true;
		}
		
		assert(thrown);
	}

	@Test
	public void testBuyMortgage() {
		boolean thrown = false;
		
		try	{
			monopoly.buyMortgage();
		}
		catch (IllegalStateException e){
			thrown = true;
		}
		
		assert(thrown);
		
	}

	@Test
	public void testSellToPlayers() {
		monopoly.sellToPlayers(1, 5);
		assert(monopoly.getPhase() == GamePhase.WAITING);
	}

	@Test
	public void testSellToBank() {
		boolean thrown = false;
		
		try	{
			monopoly.sellToBank(1);
		}
		catch (IllegalStateException e){
			thrown = true;
		}
		
		assert(thrown);
	}

	@Test
	public void testPayRent() {
		player2.getDeeds().add(1);
		player.getToken().moveTo(1);
		monopoly.getBoard().getTiles().get(1).setOwnerIndex(1);
		
		monopoly.payRent();
		
		assertEquals(player.getMoney(), 1498, 0);
	}

	@Test
	public void testJailChoice() {
		monopoly.jailChoice(true);
		assertEquals(player.getMoney(), 1450, 0);
	}

	@Test
	public void testEndTurn() {
		monopoly.endTurn();
		assert(monopoly.getCurrentPlayerIndex() == 1);
	}

	@Test
	public void testIsRolledDoubles() {
		assert(!monopoly.isRolledDoubles());
	}

	@Test
	public void testSetRolledDoubles() {
		monopoly.setRolledDoubles(true);
		assert(monopoly.isRolledDoubles());
	}

	@Test
	public void testBankrupt() {
		monopoly.bankrupt(player);
		assert(player.isBankrupt());
	}

	@Test
	public void testEndGame() {
		monopoly.endGame();
		assert(monopoly.getPlayers().size() == 0);
	}


}
