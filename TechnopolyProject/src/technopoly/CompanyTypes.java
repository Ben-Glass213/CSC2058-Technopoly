package technopoly;

public enum CompanyTypes { 
	CYBERSECURITY(0), VIDEOGAME(1), ROBOTICS(2), GRAPHICS(3), CLOUDCOMPUTING(4), DIGITALMARKETING(5), COMPUTERSOFTWARE(6), ARCHITECTURE(7);
	
	private String info[] = {"Cyber Security", "Video Game Design", "Robotics", "Graphics", "Cloud Computing","Digital Marketing", "Computer Software + Hardware", "Architecture and organisation"};
	
	private int index;
	
	/**
	 * Constructor for Type
	 * @param num - number associated with the value of the companies enumeration
	 */
	private CompanyTypes (int num) {
		index = num;
	}
	
	/**
	 * Converts the value of the type enumeration to string
	 */
	public String toString() {
		return info[index];
	}

}
