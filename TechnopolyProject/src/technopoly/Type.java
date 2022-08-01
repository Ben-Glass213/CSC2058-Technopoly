package technopoly;

public enum Type {
	CHANCE(0), COMCHEST(1), VPN(2), ISP(3), LOSEDEVELOPERS(4), INTERNETDOMAINS(5), SERVERCRASH(6), CHECKSERVER(7), COFFEEBREAK(8), GO(9), COMPANY(10);

private String info[] = {"Chance Card", "Community Card", "VPN: Utility", "ISP: Utility", "Lose Developers", "Internet Domains", "Server Crash", "Check Server", "Coffee Break", "Pass go" , "Company"};
	
	private int index;
	
	/**
	 * Constructor for Type
	 * @param num - number associated with the value of the type enumeration
	 */
	private Type (int num) {
		index = num;
	}
	
	/**
	 * Converts the value of the type enumeration to string
	 */
	public String toString() {
		return info[index];
	}
}



