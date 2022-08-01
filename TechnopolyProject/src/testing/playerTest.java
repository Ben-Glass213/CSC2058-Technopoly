package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import technopoly.*;

import java.util.ArrayList;

public class playerTest {
	
	@Test
	public void testPlayer() throws Exception {
		Player player = new Player("Adam");
		OwnableSquare square = new Company("SecureIT", Type.COMPANY, CompanyTypes.CYBERSECURITY, 15, 25, 125, 2);
		player.setCurrentSquare(square);
		Square expectedSquare = square;
		ArrayList<OwnableSquare> expectedOwnedSquares = new ArrayList<OwnableSquare>();
		ArrayList<Company> expectedProducts = new ArrayList<Company>();
		ArrayList<Company> expectedTools = new ArrayList<Company>();
		
		assertEquals("Name", "Adam", player.getName());
		assertEquals("Developers", 375, player.getDevelopers());
		assertEquals("Current Square", expectedSquare, player.getCurrentSquare());
		assertEquals("SquareID", 0, player.getSquareID());
		assertEquals("Owned Squares", expectedOwnedSquares, player.getOwnedSquares());
		assertEquals("Server Crash", false, player.getServerCrash());
		assertEquals("Reboot Card", false, player.getRebootCard());
		assertEquals("Turn", false, player.getTurn());
		assertEquals("Rolls", 0, player.getRolls());
		assertEquals("Products", expectedProducts, player.getProducts());
		assertEquals("Tools", expectedTools, player.getTool());
		
	}

	@Test
	public void testAddOwnedSquare() throws Exception {

		Player test = new Player("Test for valid");

		OwnableSquare addOS = new OwnableSquare("New added Square", Type.ISP, 25);

		test.addOwnedSquare(addOS);

		OwnableSquare expectedResult = new OwnableSquare("New added Square", Type.ISP, 25);

		OwnableSquare result = test.getOwnedSquares().get(0);

		assertEquals("Ownable Squares Name", expectedResult.getSquareName(), result.getSquareName());
		assertEquals("Ownable Squares Type", expectedResult.getSquareType(), result.getSquareType());
		assertEquals("Ownable Squares Cost", expectedResult.getCost(), result.getCost());

	}

	@Test
	public void testAddTool() throws Exception {

		Player test = new Player("Tester");

		Company addTool = new Company("New added Tool", Type.COMPANY, CompanyTypes.ARCHITECTURE, 25, 20, 15, 10);

		test.addTools(addTool);

		Company expectedResult = new Company("New added Tool", Type.COMPANY, CompanyTypes.ARCHITECTURE, 25, 20, 15, 10);

		Company result = test.getTool().get(0);

		assertEquals("Ownable Squares Name", expectedResult.getCompanyName(), result.getCompanyName());
		assertEquals("Ownable Squares Type", expectedResult.getCompanyType(), result.getCompanyType());
		assertEquals("Ownable Squares Cost", expectedResult.getCost(), result.getCost());

	}

	@Test
	public void testAddProduct() throws Exception {

		Player test = new Player("Tester");

		Company addProd = new Company("New added Product", Type.COMPANY, CompanyTypes.ARCHITECTURE, 25, 20, 15, 10);

		test.addProducts(addProd);

		Company expectedResult = new Company("New added Product", Type.COMPANY, CompanyTypes.ARCHITECTURE, 25, 20, 15,
				10);

		Company result = test.getProducts().get(0);

		assertEquals("Ownable Squares Name", expectedResult.getCompanyName(), result.getCompanyName());
		assertEquals("Ownable Squares Type", expectedResult.getCompanyType(), result.getCompanyType());
		assertEquals("Ownable Squares Cost", expectedResult.getCost(), result.getCost());

	}

	@Test
	public void testDevelopers() throws Exception {

		Player test = new Player("Tester");
		int testDevelopers = 100;
		test.setDevelopers(testDevelopers);
		int result = test.getDevelopers();
		int expectedResult = 100;

		assertEquals("Developers", expectedResult, result);

	}

	@Test
	public void testName() throws Exception {

		Player test = new Player("Tester");
		String result = test.getName();
		String expectedResult = "Tester";

		assertEquals("Name", expectedResult, result);

	}

	@Test
	public void testCurrentSquare() throws Exception {

		Player test = new Player("Tester");
		Square testSqu = new Square("Test Square", Type.CHANCE, 50);
		test.setCurrentSquare(testSqu);
		Square result = test.getCurrentSquare();
		Square expectedResult = new Square("Test Square", Type.CHANCE, 50);

		assertEquals("Current Square Name", expectedResult.getSquareName(), result.getSquareName());
		assertEquals("Current Square Type", expectedResult.getSquareType(), result.getSquareType());
		assertEquals("Current Square Cost", expectedResult.getSquareCost(), result.getSquareCost());

	}

	@Test
	public void testServerCrash() throws Exception {

		Player test = new Player("Tester");
		test.setServerCrash(true);
		boolean expectedResult = true;
		boolean result = test.getServerCrash();
		assertEquals("Server Crash", expectedResult, result);

	}

	@Test
	public void testRebootCard() throws Exception {

		Player test = new Player("Tester");
		test.setRebootCard(true);
		boolean expectedResult = true;
		boolean result = test.getRebootCard();
		assertEquals("Reboot Card", expectedResult, result);

	}
	
	@Test
	public void testTurn() throws Exception {

		Player test = new Player("Tester");
		test.setTurn(true);
		boolean expectedResult = true;
		boolean result = test.getTurn();
		assertEquals("Turn", expectedResult, result);

	}
	
	@Test
	public void testRolls() throws Exception {

		Player test = new Player("Tester");
		test.setRolls(2);
		int expectedResult = 2;
		int result = test.getRolls();
		assertEquals("Rolls", expectedResult, result);

	}
	
	@Test
	public void testTradeOwnableSquare() throws Exception {
		Player player1 = new Player("Adam");
		Player player2 = new Player("Oskar");
		OwnableSquare square = new Company("SecureIT", Type.COMPANY, CompanyTypes.CYBERSECURITY, 15, 25, 125, 2);
		player1.addOwnedSquare(square);
		player1.tradeOwnableSquares(player2, 50, square);
		
		ArrayList<OwnableSquare> p1ExpectedSquares = new ArrayList<OwnableSquare>();
		ArrayList<OwnableSquare> p2ExpectedSquares = new ArrayList<OwnableSquare>();
		p2ExpectedSquares.add(square);
		
		assertEquals("Player1 Developers", 425, player1.getDevelopers());
		assertEquals("Player2 Developers", 325, player2.getDevelopers());
		assertEquals("Player1 Owned Squares", p1ExpectedSquares, player1.getOwnedSquares());
		assertEquals("Player2 Owned Squares", p2ExpectedSquares, player2.getOwnedSquares());
	}

}
