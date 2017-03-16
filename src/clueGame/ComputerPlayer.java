package clueGame;

import java.util.Set;

public class ComputerPlayer extends Player{

	public ComputerPlayer(String playerName, int row, int column, String color) {
		super(playerName, row, column, color);

	}
	
	public BoardCell pickLocation(Set<BoardCell> targets) {
		return null;
	}
	
	public void makeAccusation() {
		
	}
	
	public void createSuggestion() {
		
	}

}
