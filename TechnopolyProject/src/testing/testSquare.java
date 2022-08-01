package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import technopoly.OwnableSquare;
import technopoly.Player;
import technopoly.Square;
import technopoly.Type;

public class testSquare {

	@Test
	public void testSqu() throws Exception {
		Square test = new Square("Test OS", Type.COMPANY, 10);
		String nameResult = test.getSquareName();
		Type typeResult = test.getSquareType();
		int costResult = test.getSquareCost();
		
		assertEquals("Square name", "Test OS", nameResult);
		assertEquals("Square type", Type.COMPANY, typeResult);
		assertEquals("Square cost", 10, costResult);
		
	}

}
