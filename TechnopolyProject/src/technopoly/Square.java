package technopoly;

public class Square {
	private int id = 0;
	private static int nextId = 1;
	private String squName; 
	private Type squType; 
	private int cost;

	
	public Square(String sName, Type sType, int cost) throws Exception {
		this.id = useNextId();

		if (sName.isBlank() == false && sName.isEmpty() == false && sName != null) {
			this.squName = sName;

		} else {

			throw new Exception("Error in square name");
		}
		
		if (sType != null) {
			this.squType = sType;
		} else {
			throw new Exception("Error in type");
		}
		
		if (cost >= 0) {
			this.cost = cost;
		} else {
			throw new Exception("Error, invalid cost");
		}

	}

	private int useNextId() {
		id = nextId;
		nextId = nextId + 1;

		return id;
	}
	
	public int getSquareId() {
		return this.id;
	}
	
	public String getSquareName() {
		return this.squName;
	}
	
	public Type getSquareType() {
		return this.squType;
	}
	
	public int getSquareCost() {
		return this.cost;
	}
	
	
	public String toString() {
		String str = "";
		str += this.id + ", ";
		str += this.squName + ", ";
		str += this.squType + ", ";
		return str;

	}
}
