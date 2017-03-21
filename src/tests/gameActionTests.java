package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.ComputerPlayer;
import clueGame.Solution;
import clueGame.BoardCell;

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
	
		assertTrue(board.checkAccusation(new Solution("Boolena", "Boolane", "Bool")));
		assertFalse(board.checkAccusation(new Solution("Boolean", "Boolane", "Bool")));
		assertFalse(board.checkAccusation(new Solution("Boolena", "Boolean", "Bool")));
		assertFalse(board.checkAccusation(new Solution("Boolena", "Boolane", "Boolean")));
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
		assertEquals("test", suggestion.person);
		assertEquals("test", suggestion.weapon);
		assertEquals("testRoom", suggestion.room);
		temp.add("another");
		temp.add("third");
		player.setUnseenPeople(temp);
		player.setUnseenWeapons(temp);
		//randomChecks store booleans that will be set if certain rooms or weapons are picked
		Boolean[] randomChecks = {false, false, false, false, false, false};
		for (int i = 0; i < 50; i++) {
			player.createSuggestion("testRoom");
			suggestion = player.getSuggestion();
			if (suggestion.person.equals("test")) randomChecks[0] = true;
			else if (suggestion.person.equals("another")) randomChecks[1] = true;
			else if (suggestion.person.equals("third")) randomChecks[2] = true;
			
			if (suggestion.weapon.equals("test")) randomChecks[3] = true;
			else if (suggestion.weapon.equals("another")) randomChecks[4] = true;
			else if (suggestion.weapon.equals("third")) randomChecks[5] = true;
		}
		
		for (int i = 0; i < 6; i++) {
			assertTrue(randomChecks[i]);
		}
		
	}

}
