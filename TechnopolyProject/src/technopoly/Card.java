package technopoly;

public class Card {
	 private int id;
	 private static int nextId = 1;
	 private String description;
	 private int gain;
	 private int lose;
	 private Square move;
	 
	 public Card(String descript, Type t, int gain, int lose, Square move) {
		 this.id = useNextId();
		 this.description = descript;
		 this.gain = gain;
		 this.lose = lose;
		 this.move = move;
	 }
	 private int useNextId() {
			id = nextId;
			nextId = nextId + 1;

			return id;
	}
	
	 public int getCardId() {
		 return this.id;
	 }
	
	 public String getDescript() {
		 return this.description;
	 }
	 
	 public int getGain() {
		 return this.gain;
	 }
	 
	 public int getLoss() {
		 return this.lose;
	 }
	 
	 public Square getMove() {
		 return this.move;
	 }
	 
	 
}
