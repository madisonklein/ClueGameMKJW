package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.IOException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;

public class gameSetupTests {

	private static Board board;
	private static final int DECK_SIZE = 23;
	private static final int NUM_PEOPLE = 6;
	private static final int NUM_WEAPONS = 6;
	private static final int NUM_ROOMS = 11;
	
	@Before
	public void setUpClass() throws IOException, BadConfigFormatException  {
		board = Board.getInstance();
		board.setConfigFiles("Layout.csv", "Legend.txt", "People.txt");
		// board.initalize() loads config files, creates the deck, and deals the cards
		board.initialize();
	}
	
	@Test
	public void testPeople() {
		ArrayList<ComputerPlayer> people = board.getPeople();
		assertEquals(people.size() + 1, NUM_PEOPLE);
		HumanPlayer human = board.getHumanPlayer();
		assertTrue(human.equals(new HumanPlayer("Dr. Rader", 10, 0, Color.blue)));
		assertTrue(people.get(0).equals(new ComputerPlayer("Kevin", 19, 6, Color.red)));
		assertTrue(people.get(people.size()-1).equals(new ComputerPlayer("Bill Murray", 6, 10, Color.magenta)));
		
	}
	
	@Test
	public void testCreateDeck() {
		board.createDeck();
		ArrayList<Card> deck = board.getDeck();
		assertEquals(deck.size(), DECK_SIZE);
		int people=0, rooms=0, weapons=0;
		for (Card c : deck) {
			if (c.getCardType() == CardType.PERSON) people++;
			if (c.getCardType() == CardType.ROOM) rooms++;
			if (c.getCardType() == CardType.WEAPON) weapons++;
		}
		assertEquals(people, NUM_PEOPLE);
		assertEquals(weapons, NUM_WEAPONS);
		assertEquals(rooms, NUM_ROOMS);
		assertTrue(deck.contains(new Card("Dr. Rader", CardType.PERSON)));
		assertTrue(deck.contains(new Card("knife", CardType.WEAPON)));
		assertTrue(deck.contains(new Card("Dungeon", CardType.ROOM)));
	}
	
	@Test
	public void testDealCard() {
		Map<Player, ArrayList<Card>> hands = board.getHands();
		assertEquals(hands.keySet().size(), NUM_PEOPLE);
		Set<Card> uniqueCards = new HashSet<Card>();
		for (Player p : hands.keySet()) {
			for (Player pl : hands.keySet()) 
				assertTrue(Math.abs(hands.get(p).size() - hands.get(pl).size()) <= 1);
			for (Card c : hands.get(p)) {
				uniqueCards.add(c);
			}
		}
		assertEquals(uniqueCards.size(), DECK_SIZE);
	}

}
