package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Set;

public class ComputerPlayer extends Player{
	
	private ArrayList<BoardCell> visitedRooms;
	private Solution solution;
	
	public ComputerPlayer() {
		super("test", 0, 0, Color.red);
		visitedRooms = new ArrayList<BoardCell>();
	}
	
	public ComputerPlayer(String playerName, int row, int column, Color color) {
		super(playerName, row, column, color);
		visitedRooms = new ArrayList<BoardCell>();

	}
	
	public BoardCell pickLocation(Set<BoardCell> targets) {
		ArrayList<BoardCell> targ = new ArrayList<BoardCell>();
		for (BoardCell b : targets) {
			targ.add(b);
			if (b.isDoorway() && !visitedRooms.contains(b)) {
				visitedRooms.add(b);
				return b;
			}
		}
		int index = (int) (Math.random()*targets.size());
		visitedRooms.add(targ.get(index));
		return targ.get(index);
	}
	
	public void makeAccusation() {
		
	}
	
	public void createSuggestion() {
		
	}
	
	public void clearVisitedRooms() {
		visitedRooms.clear();
	}

}
