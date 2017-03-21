package clueGame;

import java.awt.Color;

public class HumanPlayer extends Player {
	
	public HumanPlayer(String playerName, int row, int col, Color color) {
		super(playerName, row, col, color);
	}
	public HumanPlayer() {
		super("test", 0, 0, Color.red);
	}

}
