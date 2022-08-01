package technopoly;

public class OwnableSquare extends Square {

	private Player owner;
	private int cost;

	public OwnableSquare(String sName, Type sType, int cost) throws Exception {

		super(sName, sType, cost);
		this.owner = null;
		this.cost = cost;

	}

	public int getCost() {
		return this.cost;
	}

	public Player getOwner() {
		return this.owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}
}
