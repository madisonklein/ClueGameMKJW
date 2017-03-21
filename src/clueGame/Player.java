package clueGame;

import java.awt.Color;
import java.util.ArrayList;

public class Player {
	
	private String playerName;
	private int row;
	private int column;
	private Color color;
	private ArrayList<String> hand;
	
	public Player(String playerName, int row, int column, Color color) {
		this.playerName = playerName;
		this.row = row;
		this.column = column;
		this.color = color;
	}
	
	public Card disproveSuggestion(Solution suggestion) {
		Card person = new Card(suggestion.person, CardType.PERSON);
		Card weapon = new Card(suggestion.weapon, CardType.WEAPON);
		Card room = new Card(suggestion.room, CardType.ROOM);
		ArrayList<Card> matches = new ArrayList<Card>();
		if (hand.contains(person.getCardName())) matches.add(person);
		if (hand.contains(weapon.getCardName())) matches.add(weapon);
		if (hand.contains(room.getCardName())) matches.add(room);
		
		if (matches.size() == 0) return null;
		else if (matches.size() == 1) return matches.get(0);
		else {
			int index = (int) (Math.random()*matches.size());
			return matches.get(index);
		}
		
	}
	 public String getName() {
		 return playerName;
	 }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + column;
		result = prime * result + ((playerName == null) ? 0 : playerName.hashCode());
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (column != other.column)
			return false;
		if (playerName == null) {
			if (other.playerName != null)
				return false;
		} else if (!playerName.equals(other.playerName))
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public void setColumn(int col) {
		this.column = col;
	}
	
	public ArrayList<String> getHand() {
		return hand;
	}
	
	public void setHand(ArrayList<Card> hand) {
		this.hand = new ArrayList<String>();
		for (Card c : hand) this.hand.add(c.getCardName());
	}

	 
}
