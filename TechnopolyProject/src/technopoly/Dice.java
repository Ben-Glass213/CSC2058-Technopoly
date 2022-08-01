package technopoly;
import java.lang.Math;
public class Dice {
	private int rollResult = 0;
	private int result1 = 0;
	private int result2 = 0;
	private int doubles = 0;
	
	public Dice() {
		
	}
	
	public void rollDice() {
		
		int result1 = (int)(Math.random()*(6-1+1)+1);
		this.result1 = result1;
		
		int result2 = (int)(Math.random()*(6-1+1)+1);
		this.result2 = result2;
		
		if (result2 == result1) {
		doubles++;
		}
		
		int totalR = result1 + result2;
		this.rollResult = totalR;
	}
	
	public int getResult1() {
		return this.result1;
	}
	public int getResult2() {
		return this.result2;
	}
	
	public int getRollScore() {
		return this.rollResult;
	}
	
	public boolean getDoubles() {
		if (doubles != 0) {
			return true;
		}
		return false;
	}
	
	
	
}


