package technopoly;

import java.util.ArrayList;

public class Company extends OwnableSquare {

	private String companyName;
	private int toolCost;
	private int productCost;
	private int rentCost;
	private CompanyTypes cType;

	public Company(String sName, Type sType, CompanyTypes cType, int cost, int tC, int pC, int rC) throws Exception {
		super(sName, sType, cost);
		if (cost >= 0 && tC >= 0 && pC >= 0 && rC >= 0 && cType != null) {
			this.toolCost = tC;
			this.productCost = pC;
			this.rentCost = rC;
			this.cType = cType;
			this.companyName = sName;
		}

	}

	public CompanyTypes getCompanyType() {
		return cType;
	}

	public int getToolCost() {
		return toolCost;
	}

	public String getCompanyName() {
		return companyName;
	}

	public int getProductCost() {
		return productCost;
	}

	public int getRentCost() {
		return rentCost;
	}

	public void setToolCost(int tCost) {
		this.toolCost = tCost;
	}

	public void setProductCost(int pCost) {
		this.productCost = pCost;
	}

	public void setRentCost(int rCost) {
		this.rentCost = rCost;
	}
}
