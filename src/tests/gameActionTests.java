package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;

public class gameActionTests {

	private static Board board;
	
	@Before
	public void setUpClass() throws BadConfigFormatException, IOException{
		board = Board.getInstance();
		board.setConfigFiles("Layout.csv", "Legend.txt", "People.txt");
		// board.initalize() loads config files, creates the deck, and deals the cards
		board.initialize();
	}
	
	
	@Test
	public void testTargetRandomSelection() {
		ComputerPlayer player = new ComputerPlayer();
		board.calcTargets(6, 10, 1);
		boolean loc_5_10 = false;
		boolean loc_7_10 = false;
		boolean loc_6_9 = false;
		boolean loc_6_11 = false;
		for (int i = 0; i<100;i++) {
			BoardCell chosen = player.pickLocation(board.getTargets());
			if (chosen == board.getCellAt(5, 10)) loc_5_10 = true;
			else if (chosen == board.getCellAt(7, 10)) loc_7_10 = true;
			else if (chosen == board.getCellAt(6, 9)) loc_6_9 = true;
			else if (chosen == board.getCellAt(6, 11)) loc_6_11 = true;
			else fail("Invalid target selected");
		}
		assertTrue(loc_5_10);
		assertTrue(loc_7_10);
		assertTrue(loc_6_9);
		assertTrue(loc_6_11);
	}
	
	@Test
	public void testTargetRoomSelection() {
		ComputerPlayer player = new ComputerPlayer();
		board.calcTargets(4, 6, 1);
		for (int i = 0; i < 5; i++) {
			BoardCell chosen = player.pickLocation(board.getTargets());
			if (chosen != board.getCellAt(4, 7)) fail("Player didn't select unvisited room");
			player.clearVisitedRooms();
		}
	}
	
	@Test
	public void testTargetVisitedRoomSelection() {
		ComputerPlayer player = new ComputerPlayer();
		board.calcTargets(4, 6, 1);
		BoardCell chosen = player.pickLocation(board.getTargets());
		boolean loc_3_6 = false;
		boolean loc_5_6 = false;
		boolean loc_4_7 = false;
		for (int i = 0; i < 100; i++) {
			chosen = player.pickLocation(board.getTargets());
			if (chosen == board.getCellAt(4, 7)) loc_4_7 = true;
			else if (chosen == board.getCellAt(3, 6)) loc_3_6 = true;
			else if (chosen == board.getCellAt(5, 6)) loc_5_6 = true;
			else fail("Invalid target selected");
		}
		assertTrue(loc_4_7);
		assertTrue(loc_3_6);
		assertTrue(loc_5_6);
	}
	
	@Test
	public void testAccusation() {
		board.setSolution(new Solution("Boolena", "Boolane", "Bool"));
		//test solution is correct
		assertTrue(board.checkAccusation(new Solution("Boolena", "Boolane", "Bool")));
		//test solution with wrong person
		assertFalse(board.checkAccusation(new Solution("Boolean", "Boolane", "Bool")));
		//test solution with wrong weapon
		assertFalse(board.checkAccusation(new Solution("Boolena", "Boolean", "Bool")));
		//test solution with wrong room
		assertFalse(board.checkAccusation(new Solution("Boolena", "Boolane", "Boolean")));
		// test completely incorrect solution
		assertFalse(board.checkAccusation(new Solution("Boolean", "Boolean", "Boolean")));
	}
	
	@Test
	public void testCreateSuggestion() {
		ComputerPlayer player = new ComputerPlayer();
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("test");
		player.setUnseenPeople(temp);
		player.setUnseenWeapons(temp);
		player.createSuggestion("testRoom");
		Solution suggestion = player.getSuggestion();
		//test solution matches room and only weapon/person unseen
		assertEquals("test", suggestion.person);
		assertEquals("test", suggestion.weapon);
		assertEquals("testRoom", suggestion.room);
		temp.add("another");
		temp.add("third");
		player.setUnseenPeople(temp);
		player.setUnseenWeapons(temp);
		//randomChecks store booleans that will be set if certain rooms or weapons are picked
		Boolean[] randomChecks = {false, false, false, false, false, false};
		for (int i = 0; i < 100; i++) {
			player.createSuggestion("testRoom");
			suggestion = player.getSuggestion();
			if (suggestion.person.equals("test")) randomChecks[0] = true;
			else if (suggestion.person.equals("another")) randomChecks[1] = true;
			else if (suggestion.person.equals("third")) randomChecks[2] = true;
			
			if (suggestion.weapon.equals("test")) randomChecks[3] = true;
			else if (suggestion.weapon.equals("another")) randomChecks[4] = true;
			else if (suggestion.weapon.equals("third")) randomChecks[5] = true;
		}
		
		//test random selection when multiple weapons/people unseen
		for (int i = 0; i < 6; i++) {
			assertTrue(randomChecks[i]);
		}
		
	}
	
	@Test
	public void testDisproveSuggestion() {
		ComputerPlayer player = new ComputerPlayer();
		Card rader = new Card("Dr. Rader", CardType.PERSON);
		Card knife = new Card("knife", CardType.WEAPON);
		Card ballroom = new Card("ballroom", CardType.ROOM);
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(rader);
		hand.add(knife);
		hand.add(ballroom);
		player.setHand(hand);
		Card shown = player.disproveSuggestion(new Solution("Unknown", "attic", "candlestick"));
		//tests null is returned when no matching cards
		assertEquals(shown, null);
		shown = player.disproveSuggestion(new Solution("Dr. Rader", "attic", "candlestick"));
		//tests card is returned when only have one matching card
		assertEquals(shown, rader);
		boolean showRader = false;
		boolean showKnife = false;
		for (int i = 0; i < 50; i ++) {
			shown = player.disproveSuggestion(new Solution("Dr. Rader", "ballroom", "knife"));
			if (shown.equals(rader)) showRader = true;
			else if (shown.equals(knife)) showKnife = true;
		}
		//tests card is randomly selected when have multiple matching cards
		assertTrue(showRader);
		assertTrue(showKnife);
	}
	
	@Test
	public void testHandleSuggestion() {
		HumanPlayer human = new HumanPlayer();
		ArrayList<ComputerPlayer> computerPlayers = new ArrayList<ComputerPlayer>();
		Card kevin = new Card("Kevin", CardType.PERSON);
		Card dungeon = new Card("Dungeon", CardType.ROOM);
		Card rifle = new Card("Rifle", CardType.WEAPON);
		Card knife = new Card("Knife", CardType.WEAPON);
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(kevin);
		for (int i = 0; i < 2; i++) {
			computerPlayers.add(new ComputerPlayer());
			computerPlayers.get(i).setHand(hand);
		}
		human.setHand(hand);
		
		//tests null when no one can disprove
		Card cardShown = board.handleSuggestion(new Solution("Niki", "Dungeon", "Brass Knuckles"), human, computerPlayers, 0);
		assertEquals(cardShown, null);
		
		//tests null when only accusing player can disprove
		hand.add(dungeon);
		computerPlayers.get(0).setHand(hand);
		cardShown = board.handleSuggestion(new Solution("Niki", "Dungeon", "Brass Knuckles"), human, computerPlayers, 0);
		assertEquals(cardShown, null);
		
		//tests card when only human can disprove
		hand.add(rifle);
		human.setHand(hand);
		cardShown = board.handleSuggestion(new Solution("Niki", "Bar", "Rifle"),  human, computerPlayers, 0);
		assertEquals(cardShown, rifle);
		
		//test null when only human can disprove but human is accuser
		cardShown = board.handleSuggestion(new Solution("Niki", "Bar", "Rifle"),  human, computerPlayers, -1);
		assertEquals(cardShown, null);
		
		//test first players card when two players can disprove
		hand.add(knife);
		computerPlayers.get(1).setHand(hand);
		cardShown = board.handleSuggestion(new Solution("Brandon", "Dungeon", "Knife"),  human, computerPlayers, -1);
		
		assertEquals(cardShown, dungeon);
		
		//test humans card when human and other player can disprove but human comes first
		cardShown = board.handleSuggestion(new Solution("Kevin", "Kitchen", "Candlestick"),  human, computerPlayers, 1);
		assertEquals(cardShown, kevin);
		
		
		
		
	}

}
