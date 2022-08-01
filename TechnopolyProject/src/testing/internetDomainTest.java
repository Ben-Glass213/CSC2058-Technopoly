package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import technopoly.Company;
import technopoly.CompanyTypes;
import technopoly.InternetDomain;
import technopoly.Type;

class internetDomainTest {

	@Test
	public void testCompany() throws Exception {
		InternetDomain test = new InternetDomain("Test InternetDomain", Type.INTERNETDOMAINS, 5, 10, 15, 20, 25);
		
		int rentResult1 = test.getRent1();
		int rentResult2 = test.getRent2();
		int rentResult3 = test.getRent3();
		int rentResult4 = test.getRent4();

		assertEquals("Internet Domain R1", 10, rentResult1);
		assertEquals("Internet Domain R1", 15, rentResult2);
		assertEquals("Internet Domain R1", 20, rentResult3);
		assertEquals("Internet Domain R1", 25, rentResult4);
		
	}

}
