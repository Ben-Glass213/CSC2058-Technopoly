package technopoly;

import java.util.ArrayList;

public class Player {
	private int id = 0;
	private static int nextId = 1;
	private int developers;
	private String name;
	private Square currentSquare;
	private int squID; 
	private ArrayList<OwnableSquare> ownedSquares;
	private boolean serverCrash;
	private boolean rebootCard;
	private boolean turn; 
	private int rolls = 0;
	private ArrayList<Company> products;
	private ArrayList<Company> tools;

	public Player(String name) throws Exception {
		this.id = useNextId();
		this.name = name;
		this.developers = 375;
		Square firstSqu = new Square("Pass Go", Type.GO, 50);
		this.currentSquare = firstSqu;
		this.squID = 0;
		this.serverCrash = false;
		this.rebootCard = false;
		this.turn = false;
		this.ownedSquares = new ArrayList<OwnableSquare>();
		this.products = new ArrayList<Company>();
		this.tools = new ArrayList<Company>();
	}
	
	private int useNextId() {
		id = nextId;
		nextId = nextId + 1;

		return id;
	}
	
	public int getPlayerId() {
		return this.id;
	}
	
	public int getDevelopers() {
		return developers;
	}

	public void setDevelopers(int developers) {
		this.developers = developers;
	}

	public String getName() {
		return name;
	}
	
	public void setCurrentSquare(Square s) {
		this.currentSquare = s;
	}

	public Square getCurrentSquare() {
		return this.currentSquare;
	}

	public void setSquareID(int i) {
		this.squID = i;
	}

	public int getSquareID() {
		return this.squID;
	}

	public void addOwnedSquare(OwnableSquare c) {
		if (c.getSquareType() == Type.COMPANY || c.getSquareType() == Type.ISP || c.getSquareType() == Type.VPN
				|| c.getSquareType() == Type.INTERNETDOMAINS) {
			ownedSquares.add(c);
		}
	}
	
	public void removeOwnedSquare(OwnableSquare c) {
		if (c.getSquareType() == Type.COMPANY || c.getSquareType() == Type.ISP || c.getSquareType() == Type.VPN
				|| c.getSquareType() == Type.INTERNETDOMAINS) {
			ownedSquares.remove(c);
		}
	}

	public ArrayList<OwnableSquare> getOwnedSquares() {
		return this.ownedSquares;
	}

	public void tradeOwnableSquares(Player player, int developers, OwnableSquare square) {
		player.setDevelopers(player.getDevelopers() - developers);
		this.setDevelopers(this.developers + developers);
		this.ownedSquares.remove(square);
		player.addOwnedSquare(square);
	}

	public boolean getServerCrash() {
		return serverCrash;
	}

	public boolean getRebootCard() {
		return rebootCard;
	}

	public void setServerCrash(boolean sc) {
		serverCrash = sc;
	}

	public void setRebootCard(boolean rc) {
		rebootCard = rc;
	}

	public void addProducts(Company p) {
		if (p.getSquareType() == Type.COMPANY) {

			products.add(p);
		}
	}

	public void addTools(Company t) {
		if (t.getSquareType() == Type.COMPANY) {
			tools.add(t);
		}
	}

	public ArrayList<Company> getProducts() {
		return products;
	}

	public ArrayList<Company> getTool() {
		return tools;
	}

	public boolean getTurn() {
		return turn;
	}

	public void setTurn(boolean t) {
		turn = t;
	}

	public int getRolls() {
		return rolls;
	}

	public void setRolls(int r) {
		this.rolls = r;
	}
}
