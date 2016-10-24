package cs414.a4.n;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Tile {

	private String name;
	private TileType type = TileType.NONE;
	private int ownerIndex = -1;
	
	public int numHouses = 0;
	private boolean mortgaged = false;
	
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
	public double propertyCost;

	public static double with2Railroads = 50;
	public static double with3Railroads = 100;
	public static double with4Railroads = 200;
	
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
	public boolean hasOwner(){
		return ownerIndex != -1;
	}
}
