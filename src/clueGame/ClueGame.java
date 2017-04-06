package clueGame;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class ClueGame extends JFrame {
	
	Board board;
	
	public ClueGame() {
		setSize(700,700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board = new Board();
		board.setConfigFiles("Layout.csv", "Legend.txt", "People.txt");
		board.initialize();
		add(board, BorderLayout.CENTER);
	}
	
	public static void main(String[] args) {
		ClueGame game = new ClueGame();
		game.setVisible(true);
	}

}
