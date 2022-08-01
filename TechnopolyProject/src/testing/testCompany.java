package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import technopoly.Company;
import technopoly.CompanyTypes;
import technopoly.OwnableSquare;
import technopoly.Player;
import technopoly.Type;

class testCompany {

	@Test
	public void testCompany() throws Exception {
		Company test = new Company("Test Comp", Type.COMPANY, CompanyTypes.CLOUDCOMPUTING, 10, 15, 20, 25);
		String nameResult = test.getCompanyName();
		CompanyTypes compTypeResult = test.getCompanyType();
		int costResult = test.getCost();
		int toolCostResult = test.getToolCost();
		int productCostResult = test.getProductCost();
		int rentCostResult = test.getRentCost();

		assertEquals("Company Name", "Test Comp", nameResult);
		assertEquals("Company Type", CompanyTypes.CLOUDCOMPUTING, compTypeResult);
		assertEquals("Company Cost", 10, costResult);
		assertEquals("Company Tool Cost", 15, toolCostResult);
		assertEquals("Company Product Cost", 20, productCostResult);
		assertEquals("Company Rent Cost", 25, rentCostResult);
	}

}
