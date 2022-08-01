package testing;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;

import technopoly.Card;
import technopoly.Square;
import technopoly.Type;

class testCard {

	@Test
	public void testCard() throws Exception {
		Square move = new Square("Test Move Square", Type.CHANCE, 0);
		Card testCard = new Card("Test Card", Type.CHANCE, 10, 0, move );
		int resultID = testCard.getCardId();
		String resultDescript = testCard.getDescript();
		int resultGain = testCard.getGain();
		int resultLoss = testCard.getLoss();
		Square resultMove = testCard.getMove();
		
		
		assertEquals("Card ID", 1, resultID);
		assertEquals("Card Descript", "Test Card", resultDescript);
		assertEquals("Card Gain", 10, resultGain);
		assertEquals("Card Loss", 0, resultLoss);
		assertEquals("Card Move", move, resultMove);

	}

}
