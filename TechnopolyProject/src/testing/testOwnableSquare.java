package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import technopoly.InternetDomain;
import technopoly.OwnableSquare;
import technopoly.Player;
import technopoly.Type;

public class testOwnableSquare {

	@Test
	public void testOwnSquare() throws Exception {
		OwnableSquare test = new OwnableSquare("Test OS", Type.COMPANY, 10);
		Player tester = new Player("Tester");
		test.setOwner(tester);
		int costResult = test.getCost();
		Player playerResult = test.getOwner();
		
		assertEquals("Ownable Square Cost", 10, costResult);
		assertEquals("Ownable Square Owner", tester, playerResult);

		
	}

}
