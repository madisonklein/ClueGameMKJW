package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.BadConfigFormatException;
import clueGame.Board;

public class FileInitializationTests {
	
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 20;
	public static final int NUM_COLS = 20;
	public static final int NUM_DOORS = 12;
	
	private static Board board;

	@BeforeClass
	public static void setUp() throws IOException, BadConfigFormatException {	
	board = Board.getInstance();
	board.setConfigFiles("Layout.csv", "Legend.txt");
	board.initialize();
	}
	
	@Test
	public void testRooms() {
		Map<Character, String> legend = board.getLegend();
		assertEquals("Master", legend.get('M'));
		assertEquals("Rotunda", legend.get('X'));
		}
	
	@Test
	public void testSize(){
		assertEquals(NUM_ROWS, board.getNumColumns());
		assertEquals(NUM_COLS, board.getNumColumns());
	}
	
	@Test
	public void testDoorways(){
		BoardCell cell = board.getCellAt(0, 3);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		cell = board.getCellAt(4, 7);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		cell = board.getCellAt(9, 0);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		cell = board.getCellAt(14, 14);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		cell = board.getCellAt(15, 14);
		assertFalse(cell.isDoorway());
		cell = board.getCellAt(13, 14);
		assertFalse(cell.isDoorway());		
	}
	
	@Test
	public void testDoorNumber(){
		int counter = 0;
		BoardCell cell;
		for(int i = 0; i < NUM_ROWS; i++){
			for(int j = 0; j < NUM_COLS; j++){
				cell = board.getCellAt(i, j);
				if(cell.isDoorway()){
					counter++;
				}
			}
		}
		assertEquals(counter, NUM_DOORS);
	}
	
	@Test
	public void testCellInitials(){
		BoardCell cell;
		cell = board.getCellAt(0, 0);
		assertEquals(cell.getInitial(), 'M');
		cell = board.getCellAt(0, 5);
		assertEquals(cell.getInitial(), 'W');
		cell = board.getCellAt(9, 10);
		assertEquals(cell.getInitial(), 'X');
		cell = board.getCellAt(13, 17);
		assertEquals(cell.getInitial(), 'S');
		cell = board.getCellAt(13, 12);
		assertEquals(cell.getInitial(), 'W');
		cell = board.getCellAt(8, 19);
		assertEquals(cell.getInitial(), 'B');
		cell = board.getCellAt(11, 3);
		assertEquals(cell.getInitial(), 'N');
		cell = board.getCellAt(4, 10);
		assertEquals(cell.getInitial(), 'C');
	}

}
