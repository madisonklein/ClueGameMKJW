package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.BoardCell;
import clueGame.BadConfigFormatException;
import clueGame.Board;

public class AdjacenciesTargetTest {

	private static Board board;
	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance and initialize it		
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("Layout.csv", "Legend.txt");		
		try {
			board.initialize();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (BadConfigFormatException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAdjacenciesInsideRooms()
	{
		// Test a corner
		Set<BoardCell> testList = board.getAdjList(1, 9);
		assertEquals(0, testList.size());
		// Test one that has walkway underneath
		testList = board.getAdjList(3, 16);
		assertEquals(0, testList.size());
		// Test one that has walkway above
		testList = board.getAdjList(9, 1);
		assertEquals(0, testList.size());
		// Test one that is in middle of room
		testList = board.getAdjList(12, 17);
		assertEquals(0, testList.size());
		// Test one beside a door
		testList = board.getAdjList(14, 1);
		assertEquals(0, testList.size());
		// Test one in a corner of room
		testList = board.getAdjList(17, 14);
		assertEquals(0, testList.size());
	}
	
	@Test
	public void testAdjacencyRoomExit()
	{
		// TEST DOORWAY RIGHT 
		Set<BoardCell> testList = board.getAdjList(1, 3);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(1, 4)));
		// TEST DOORWAY LEFT, WHERE THERE'S A WALKWAY UP 
		testList = board.getAdjList(8, 16);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(8, 15)));
		//TEST DOORWAY DOWN
		testList = board.getAdjList(9, 0);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(10, 0)));
		//TEST DOORWAY UP
		testList = board.getAdjList(12, 19);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(11, 19)));
		//TEST DOORWAY LEFT
		testList = board.getAdjList(15, 7);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(15, 6)));
		
	}
	
	@Test
	public void testAdjacencyDoorways()
	{
		// Test beside a door direction RIGHT
		Set<BoardCell> testList = board.getAdjList(0, 4);
		assertTrue(testList.contains(board.getCellAt(0, 3)));
		assertTrue(testList.contains(board.getCellAt(0, 5)));
		assertTrue(testList.contains(board.getCellAt(1, 4)));
		assertEquals(3, testList.size());
		// Test beside a door direction LEFT
		testList = board.getAdjList(4, 6);
		assertTrue(testList.contains(board.getCellAt(3, 6)));
		assertTrue(testList.contains(board.getCellAt(5, 6)));
		assertTrue(testList.contains(board.getCellAt(4, 7)));
		assertEquals(3, testList.size());
		// Test beside a door direction DOWN
		testList = board.getAdjList(7, 15);
		assertTrue(testList.contains(board.getCellAt(6, 15)));
		assertTrue(testList.contains(board.getCellAt(7, 14)));
		assertTrue(testList.contains(board.getCellAt(7, 16)));
		assertTrue(testList.contains(board.getCellAt(8, 15)));
		assertEquals(4, testList.size());
		// Test beside a door direction UP
		testList = board.getAdjList(11, 0);
		assertTrue(testList.contains(board.getCellAt(10, 0)));
		assertTrue(testList.contains(board.getCellAt(12, 0)));
		assertEquals(2, testList.size());
	}
	
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on top edge of board, just two walkway piece
		Set<BoardCell> testList = board.getAdjList(0, 6);
		assertTrue(testList.contains(board.getCellAt(0, 5)));
		assertTrue(testList.contains(board.getCellAt(1, 6)));
		assertEquals(2, testList.size());
		
		// Test on left edge of board, three walkway pieces
		testList = board.getAdjList(10, 0);
		assertTrue(testList.contains(board.getCellAt(9, 0)));
		assertTrue(testList.contains(board.getCellAt(10, 1)));
		assertTrue(testList.contains(board.getCellAt(11, 0)));
		assertEquals(3, testList.size());

		// Test between two rooms, walkways right and left
		testList = board.getAdjList(7, 18);
		assertTrue(testList.contains(board.getCellAt(7, 19)));
		assertTrue(testList.contains(board.getCellAt(7, 17)));
		assertEquals(2, testList.size());

		// Test surrounded by 4 walkways
		testList = board.getAdjList(11,15);
		assertTrue(testList.contains(board.getCellAt(11, 16)));
		assertTrue(testList.contains(board.getCellAt(11, 14)));
		assertTrue(testList.contains(board.getCellAt(10, 15)));
		assertTrue(testList.contains(board.getCellAt(12, 15)));
		assertEquals(4, testList.size());
		
		// Test on bottom edge of board, next to 1 room piece
		testList = board.getAdjList(19, 16);
		assertTrue(testList.contains(board.getCellAt(19, 17)));
		assertTrue(testList.contains(board.getCellAt(18, 16)));
		assertEquals(2, testList.size());
		
		// Test on right edge of board, next to 1 room piece
		testList = board.getAdjList(11, 19);
		assertTrue(testList.contains(board.getCellAt(12, 19)));
		assertTrue(testList.contains(board.getCellAt(11, 18)));
		assertEquals(2, testList.size());

		// Test on walkway next to  door that is not in the needed
		// direction to enter
		testList = board.getAdjList(6, 14);
		assertTrue(testList.contains(board.getCellAt(6, 13)));
		assertTrue(testList.contains(board.getCellAt(5, 14)));
		assertTrue(testList.contains(board.getCellAt(7, 14)));
		assertEquals(3, testList.size());
	}
	
	
	// Tests of just walkways, 1 step, includes on edge of board
	// and beside room
	// Have already tested adjacency lists on all four edges, will
	// only test two edges here
	// These are PINK on the planning spreadsheet
	@Test
	public void testTargetsOneStep() {
		board.calcTargets(6, 5, 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(6, 4)));
		assertTrue(targets.contains(board.getCellAt(7, 5)));
		assertTrue(targets.contains(board.getCellAt(6, 6)));	
		
		board.calcTargets(19, 11, 1);
		targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCellAt(18, 11)));	
	}
	
	// Tests of just walkways, 2 steps
		// These are PINK on the planning spreadsheet
		@Test
		public void testTargetsTwoSteps() {
			board.calcTargets(6, 5, 2);
			Set<BoardCell> targets= board.getTargets();
			assertEquals(5, targets.size());
			assertTrue(targets.contains(board.getCellAt(6, 3)));
			assertTrue(targets.contains(board.getCellAt(8, 5)));
			assertTrue(targets.contains(board.getCellAt(7, 6)));
			assertTrue(targets.contains(board.getCellAt(6, 7)));
			assertTrue(targets.contains(board.getCellAt(5, 6)));
			
			board.calcTargets(19, 11, 2);
			targets= board.getTargets();
			assertEquals(1, targets.size());
			assertTrue(targets.contains(board.getCellAt(17, 11)));				
		}
		
		// Tests of just walkways, 4 steps
		// These are PINK on the planning spreadsheet
		@Test
		public void testTargetsFourSteps() {
			board.calcTargets(6, 5, 4);
			Set<BoardCell> targets= board.getTargets();
			assertEquals(10, targets.size());
			assertTrue(targets.contains(board.getCellAt(6, 1)));
			assertTrue(targets.contains(board.getCellAt(10, 5)));
			assertTrue(targets.contains(board.getCellAt(7, 8)));
			assertTrue(targets.contains(board.getCellAt(6, 7)));
			assertTrue(targets.contains(board.getCellAt(6, 9)));
			assertTrue(targets.contains(board.getCellAt(3, 6)));
			assertTrue(targets.contains(board.getCellAt(4, 7)));
			assertTrue(targets.contains(board.getCellAt(8, 5)));
			assertTrue(targets.contains(board.getCellAt(5, 6)));
			assertTrue(targets.contains(board.getCellAt(7, 6)));
			
			// Includes a path that doesn't have enough length
			board.calcTargets(19, 11, 4);
			targets= board.getTargets();
			assertEquals(1, targets.size());
			assertTrue(targets.contains(board.getCellAt(15, 11)));	
		}
		
		// Tests of just walkways plus one door, 6 steps
		// These are PINK on the planning spreadsheet

		@Test
		public void testTargetsSixSteps() {
			board.calcTargets(6, 5, 6);
			Set<BoardCell> targets= board.getTargets();
			assertEquals(15, targets.size());
			assertTrue(targets.contains(board.getCellAt(10, 3)));
			assertTrue(targets.contains(board.getCellAt(12, 5)));	
			assertTrue(targets.contains(board.getCellAt(7, 10)));	
			assertTrue(targets.contains(board.getCellAt(6, 9)));	
			assertTrue(targets.contains(board.getCellAt(5, 10)));	
			assertTrue(targets.contains(board.getCellAt(3, 6)));
			assertTrue(targets.contains(board.getCellAt(6, 7)));
			assertTrue(targets.contains(board.getCellAt(6, 11)));	
			assertTrue(targets.contains(board.getCellAt(7, 8)));
			assertTrue(targets.contains(board.getCellAt(4, 7)));
			assertTrue(targets.contains(board.getCellAt(1, 6)));
			assertTrue(targets.contains(board.getCellAt(8, 5)));
			assertTrue(targets.contains(board.getCellAt(7, 6)));
			assertTrue(targets.contains(board.getCellAt(7, 8)));
			assertTrue(targets.contains(board.getCellAt(10, 5)));
		}	
		
		// Test getting into a room
		// These are PINK on the planning spreadsheet

		@Test 
		public void testTargetsIntoRoom()
		{
			
			board.calcTargets(13, 15, 2);
			Set<BoardCell> targets= board.getTargets();
			assertEquals(6, targets.size());
			// One room is exactly 2 away
			assertTrue(targets.contains(board.getCellAt(14, 14)));
			assertTrue(targets.contains(board.getCellAt(11, 15)));
			assertTrue(targets.contains(board.getCellAt(12, 14)));
			assertTrue(targets.contains(board.getCellAt(13, 13)));
			assertTrue(targets.contains(board.getCellAt(14, 16)));
			assertTrue(targets.contains(board.getCellAt(12, 16)));	
		}
		
		// Test getting out of a room
		// These are PINK on the planning spreadsheet
		@Test
		public void testRoomExit()
		{
			// Take one step, essentially just the adj list
			board.calcTargets(2, 13, 1);
			Set<BoardCell> targets= board.getTargets();
			// Ensure doesn't exit through the wall
			assertEquals(1, targets.size());
			assertTrue(targets.contains(board.getCellAt(2, 14)));
			// Take two steps
			board.calcTargets(2, 13, 2);
			targets= board.getTargets();
			assertEquals(2, targets.size());
			assertTrue(targets.contains(board.getCellAt(1, 14)));
			assertTrue(targets.contains(board.getCellAt(3, 14)));
		}

}
