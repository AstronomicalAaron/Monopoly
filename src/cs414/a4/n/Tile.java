package cs414.a4.n;

public class Tile {

	private String name;
	private TileType type;
	private Deed deed;
	
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
	//

	public double with2Railroads = 50;
	public double with3Railroads = 100;
	public double with4Railroads = 200;
	
	public boolean isProperty(){
		
		return type.equals(TileType.Property);
		
	}
	
	public boolean isRailRoad(){
		
		return type.equals(TileType.Railroad);
		
	}
	
	public boolean isUtility(){
		
		return type.equals(TileType.Utility);
		
	}
	
	public TileType getType(){
		
		return type;
		
	}
	
	public void setType(TileType type){
		
		this.type = type;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Deed getDeed() {
		return deed;
	}

	public void setDeed(Deed deed) {
		this.deed = deed;
	}
}
