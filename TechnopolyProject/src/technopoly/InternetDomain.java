package technopoly;

public class InternetDomain extends OwnableSquare{

	private int rent1;
	private int rent2;
	private int rent3;
	private int rent4;
	
	public InternetDomain(String sName, Type sType, int cost, int r1, int r2, int r3, int r4) throws Exception {
		super(sName, sType, cost);
		this.rent1 = r1;
		this.rent2 = r2;
		this.rent3 = r3;
		this.rent4 = r4;
		
	}
	
	public int getRent1() {
		return this.rent1;
	}
	
	public int getRent2() {
		return this.rent2;
	}
	
	public int getRent3() {
		return this.rent3;
	}
	
	public int getRent4() {
		return this.rent4;
	}

}
