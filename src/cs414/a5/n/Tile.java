package cs414.a5.n;

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
 * 									Aaron Barczewski								 *
 * 																					 *
 *************************************************************************************/

/*************************************************************************************
 * 											TILE									 *
 *************************************************************************************/

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Tile {

	private String name;
	private TileType type = TileType.NONE;
	private int ownerIndex = -1;
	
	public int numHouses = 0;
	private boolean mortgaged = false;
	private boolean hasHotel = false;
	
	int index;
	
	public String color;
	public double rent;
	public double with1House;
	public double with2Houses;
	public double with3Houses;
	public double with4Houses;
	public double withHotel;
	public double mortgageValue;
	public double houseCost;
	public double hotelCost;
	public double propertyCost = 0;

	public final double with2Railroads = 50;
	public final double with3Railroads = 100;
	public final double with4Railroads = 200;
	
	public Tile(){
		
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isMortgaged(){
		return mortgaged;
	}
	
	public void setMortgaged(boolean isMortgaged){
		mortgaged = isMortgaged;
	}
	
	public TileType getType(){
		return type;
	}
	
	public int getOwnerIndex() {
		return ownerIndex;
	}
	
	public void setOwnerIndex(int index){
		ownerIndex = index;
	}
	
	public int getNumHouses() {
		return numHouses;
	}
	
	@JsonIgnore
	public boolean isProperty(){
		return type.equals(TileType.PROPERTY);
	}
	
	@JsonIgnore
	public boolean isRailRoad(){
		return type.equals(TileType.RAILROAD);
	}
	
	@JsonIgnore
	public boolean isUtility(){
		return type.equals(TileType.UTILITY);
	}

	@JsonIgnore
	public boolean isCommunityChestOrChanceCard(){
		return type.equals(TileType.CHANCE) || type.equals(TileType.COMMUNITYCHEST);
	}
	
	@JsonIgnore
	public boolean hasOwner(){
		return ownerIndex >= 0;
	}
	
	public boolean getHasHotel(){
		return hasHotel;		
	}
	
	@JsonIgnore
	public void setHotel(boolean h){
		hasHotel = h;		
	}
}
