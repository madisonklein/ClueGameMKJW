package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.border.TitledBorder;


import javax.swing.*;

public class ClueGame extends JFrame implements ActionListener, ItemListener, MouseListener{
	
	Board board;
	JMenuItem notes;
	JMenuItem exit;
	JDialog notesDialog;
	JButton nextPlayer;
	JButton makeAccusation;
	ControlGUI control;
	
	public ClueGame() {
		setTitle("Clue Game");
		setSize(700,700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board = Board.getInstance();
		board.setConfigFiles("Layout.csv", "Legend.txt", "People.txt");
		board.initialize();
		
		JOptionPane.showMessageDialog(this, "You are Dr. Rader, press Next Player to begin play", "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
		
		add(board, BorderLayout.CENTER);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		
		notes = new JMenuItem("Show Notes");
		notes.addActionListener(this);
		menu.add(notes);
		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		menu.add(exit);
		
		setJMenuBar(menuBar);
		
		notesDialog = new JDialog();
		notesDialog.setSize(500,700);
		notesDialog.setLayout(new GridLayout(3,2));
		addCheckBoxes(notesDialog, "People", board.names,3,2);
		addDropDowns(notesDialog, "Person Guess", board.names);
		addCheckBoxes(notesDialog, "Rooms", board.rooms, 6, 2);
		addDropDowns(notesDialog, "Room Guess", board.rooms);
		addCheckBoxes(notesDialog, "Weapons", board.weapons,3,2);
		addDropDowns(notesDialog, "Weapon Guess", board.weapons);
		
		control = new ControlGUI();
		add(control, BorderLayout.SOUTH);
		
		ArrayList<Card> humanHand = board.getHands().get(board.getHumanPlayer());
		JPanel cards = new JPanel(new GridLayout(3,1));
		cards.setSize(300,700);
		JPanel people = new JPanel(new GridLayout(4,1));
		people.setBorder(new TitledBorder(null, "People"));
		JPanel rooms = new JPanel(new GridLayout(4,1));
		rooms.setBorder(new TitledBorder(null, "Rooms"));
		JPanel weapons = new JPanel(new GridLayout(4,1));
		weapons.setBorder(new TitledBorder(null, "Weapons"));
		for (Card c: humanHand) {
			JLabel card = new JLabel(c.getCardName() + "              ");
			if (c.getCardType() == CardType.PERSON) {
				people.add(card);
			}
			else if (c.getCardType() == CardType.ROOM) rooms.add(card);
			else weapons.add(card);
		}
		cards.add(people);
		cards.add(rooms);
		cards.add(weapons);
		
		add(cards,BorderLayout.EAST);
		
		nextPlayer = control.nextPlayer;
		makeAccusation = control.makeAccusation;
		nextPlayer.addMouseListener(this);
		makeAccusation.addMouseListener(this);
		board.addMouseListener(this);
		
	}
	
	public void addCheckBoxes(JDialog dialog, String title, String[] boxes, int rows, int cols) {
		JPanel panel = new JPanel(new GridLayout(rows,cols));
		for (String s : boxes) {
			JCheckBox box = new JCheckBox(s);
			panel.add(box);
		}
		panel.setBorder(new TitledBorder(null, title));
		dialog.add(panel);
	}
	
	public void addDropDowns(JDialog dialog, String title, String[] options) {
		JPanel panel = new JPanel(new GridLayout(1,1));
		panel.setBorder(new TitledBorder(null, title));
		JComboBox<String> combo = new JComboBox<String>();
		for (String s: options) {
			combo.addItem(s);
		}
		panel.add(combo);
		dialog.add(panel);
	}
	
	public void displayError(String err) {
		JOptionPane.showMessageDialog(this, err , "Error", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showAccusation() {
		System.out.println("makeAccusation pressed");
	}
	
	public static void main(String[] args) {
		ClueGame game = new ClueGame();
		game.setVisible(true);
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == notes) {
			notesDialog.setVisible(true);
			
			
		}
		else if (ev.getSource() == exit) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == nextPlayer) {
			if (board.whoseTurn == -1 && board.getHumanPlayer().finishTurn == true) {
				displayError("Finish turn before moving onto next player!");
				return;
			}
			board.updateWhoseTurn();
			control.updateWhoseTurn(board.getCurrentPlayer());
			int roll = (int)(Math.random() * 5) + 1;
			control.updateRoll(roll);
			// display roll
			if (board.whoseTurn == -1) {
				board.showHumanTargets(roll);
				repaint();
			}
			else {
				boolean disprove = board.doTurn(roll);
				if (disprove) {
					control.updateSuggestion(board.currentSuggestion, board.currentDisprove);
				}
				else control.updateSuggestion(null, null);
				repaint();
			}
		}
		else if (board.whoseTurn == -1 && board.getHumanPlayer().finishTurn == true) {
			if (e.getSource() == makeAccusation) {
				showAccusation();
			}
			System.out.println("inside else if");
			int x = e.getX();
			int y = e.getY();
			for (BoardCell b: board.getTargets()) {
				if (x > b.getColumn()*b.CELL_WIDTH && x < (b.getColumn()+1)*b.CELL_WIDTH && y > b.getRow()*b.CELL_HEIGHT && y < (b.getRow()+1)*b.CELL_HEIGHT) {
					board.moveHuman(b);
					repaint();
					return;
				}
					
			}
			displayError("That is not a valid target");
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
