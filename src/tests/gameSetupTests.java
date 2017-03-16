package tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;

public class gameSetupTests {

	private static Board board;
	private static final int NUM_PEOPLE = 6;
	
	@Before
	public void setUpClass() throws IOException, BadConfigFormatException  {
		board = Board.getInstance();
		board.setConfigFiles("Layout.csv", "Legend.txt");
		// board.initalize() loads config files, creates the deck, and deals the cards
		board.initialize();
	}
	
	@Test
	public void testPeople() {
		
	}
	
	@Test
	public void testCreateDeck() {
		
	}
	
	@Test
	public void testDealCard() {
		
	}

}
