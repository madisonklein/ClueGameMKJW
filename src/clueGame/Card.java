package clueGame;

public class Card {
	
	private String cardName;
	private CardType type;
	
	public Card(String cardName, CardType type) {
		this.cardName = cardName;
		this.type = type;
	}
	
	public boolean equals(Card card) {
		return true;
	}

}
