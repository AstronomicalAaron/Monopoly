package cs414.a4.n;

public class Bank extends Owner {
	
	private double freeParkingPool = 100;
	
	public Bank() {
		super("Bank", Double.MAX_VALUE);
	}
	
	public double getFreeParkingPool() {
		return freeParkingPool;
	}
	
	public void payTax(Player player, Tile tile)
	{
		if (tile.getName().equals("LUXURY TAX")) {
			freeParkingPool += player.transfer(this, 75.0);
		}
		else if (player.getMoney() > 2000){
			freeParkingPool += player.transfer(this, 200.0);
		}
		else{
			freeParkingPool += player.transfer(this, 0.1 * player.getMoney());
		}
	}
	
	public void awardFreeParking(Player player){
		this.transfer(player, freeParkingPool);
		freeParkingPool = 100;
	}
}