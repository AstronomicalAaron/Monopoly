package cs414.a4.n;

public class Deed {

	private Owner owner;
	
	
	// Property
	public boolean boughtMortgage;
	
	
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