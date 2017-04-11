package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;

import javax.swing.border.TitledBorder;
import javax.swing.*;

public class ClueGame extends JFrame implements ActionListener, ItemListener{
	
	Board board;
	JMenuItem notes;
	JMenuItem exit;
	JDialog notesDialog;
	
	public ClueGame() {
		setTitle("Clue Game");
		setSize(700,700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board = new Board();
		board.setConfigFiles("Layout.csv", "Legend.txt", "People.txt");
		board.initialize();
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

}
