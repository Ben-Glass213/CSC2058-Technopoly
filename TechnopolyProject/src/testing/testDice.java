package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import technopoly.Company;
import technopoly.CompanyTypes;
import technopoly.Dice;
import technopoly.Type;

class testDice {

	@Test
	public void testDice() throws Exception {
		Dice test = new Dice();
		test.rollDice();
		boolean result = false;
		if (test.getResult1() <= 6 && test.getResult2() <= 6) {
			result = true;
		}
		
		boolean expectedResult = true;
		boolean testDoubles = false;
		
		if (test.getResult1() == test.getResult2()) {
			testDoubles = true;
		}
		boolean doubleResult = test.getDoubles();
		
		assertEquals("Roll Dice <= 6", expectedResult, result);
		assertEquals("Roll Dice  Doubles", testDoubles, doubleResult);
		
	}

}
