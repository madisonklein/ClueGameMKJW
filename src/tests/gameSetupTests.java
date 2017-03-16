package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.Card;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;

public class gameSetupTests {

	private static Board board;
	private static final int NUM_PEOPLE = 6;
	private static final int DECK_SIZE = 21;
	
	@Before
	public void setUpClass() throws IOException, BadConfigFormatException  {
		board = Board.getInstance();
		board.setConfigFiles("Layout.csv", "Legend.txt");
		// board.initalize() loads config files, creates the deck, and deals the cards
		board.initialize();
	}
	
	@Test
	public void testPeople() {
		ArrayList<ComputerPlayer> people = board.getPeople();
		HumanPlayer human = board.getHumanPlayer();
		//assertTrue(human.equals(new Human("Dr. Rader", Color.)))
		
	}
	
	@Test
	public void testCreateDeck() {
		ArrayList<Card> deck = board.getDeck();
		assertEquals(deck.size(), DECK_SIZE);
	}
	
	@Test
	public void testDealCard() {
		
	}

}
