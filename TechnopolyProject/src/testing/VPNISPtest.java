package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import technopoly.Square;
import technopoly.Type;
import technopoly.VPNISP;

class VPNISPtest {

	@Test
	public void testingVPNISP() throws Exception {
		VPNISP test = new VPNISP("Test OS", Type.ISP, 10, 15, 20);
		int rentResult1 = test.getRent1();
		int rentResult2 = test.getRent2();
		
		assertEquals("VPNISP rent1", 15, rentResult1);
		assertEquals("VPNISP rent2", 20, rentResult2);
	
		
	}
}
