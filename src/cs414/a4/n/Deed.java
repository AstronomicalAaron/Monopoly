package cs414.a4.n;

public class Deed {

	private Owner owner;

	// Property
	public boolean boughtMortgage;
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
	
	public boolean hasOwner(){
		
		//There might be a better way?
		return !owner.getId().isEmpty();
		
	}
	
	public boolean hasMortgage(){
		
		return boughtMortgage;
		
	}
	
	public void setOwner(Owner owner){
		
		this.owner = owner;
		
	}
	
	public Owner getOwner(){
		
		return owner;
		
	}
	
}