package technopoly;

public class VPNISP extends OwnableSquare {
	private int rent1;
	private int rent2;

	
	public VPNISP(String sName, Type sType, int cost, int r1, int r2) throws Exception {
		super(sName, sType, cost);
		rent1 = r1;
		rent2 = r2;
		
	}
	public int getRent1() {
		return this.rent1;
	}
	public int getRent2() {
		return this.rent2;
	}
}
