package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

public class ControlGUI extends JPanel {
	
	JPanel roll;
	JPanel guess;
	JPanel guessResult;
	JButton nextPlayer;
	JButton makeAccusation;
	JPanel whoseTurn;
	

	public ControlGUI() {
		setLayout(new GridLayout(2,0));
		JPanel topPanel = createTopPanel();
		add(topPanel);
		JPanel bottomPanel = createBottomPanel();
		add(bottomPanel);
	}
	
	public JPanel createTopPanel() {
		nextPlayer = new JButton("Next Player");
		makeAccusation = new JButton("Make an accustion");
		JPanel panel = new JPanel();
		
		whoseTurn = new JPanel();
		JTextField text = new JTextField(20);
		text.setEditable(true);
		whoseTurn.add(text);
		whoseTurn.setBorder(new TitledBorder(null, "Whose Turn?"));
		
		panel.setLayout(new GridLayout(1,0));
		panel.add(whoseTurn);
		panel.add(nextPlayer);
		panel.add(makeAccusation);
		return panel;
	}
	
	public JPanel createBottomPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,0));
		roll = createTextPanel("Die", "Roll", false);
		guess  = createTextPanel("Guess", "Guess", true);
		guessResult = createTextPanel("Guess Result", "Response", false);
		panel.add(roll);
		panel.add(guess);
		panel.add(guessResult);
		return panel;
	}
	
	public JPanel createTextPanel(String title, String name, boolean editable) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
	 	JLabel nameLabel = new JLabel(name);
	 	JTextComponent text = new JTextField(30);
	 	text.setEditable(editable);
		panel.add(nameLabel);
		panel.add(text);
		panel.setBorder(new TitledBorder (new EtchedBorder(), title));
		return panel;
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700,400);
		ControlGUI gui = new ControlGUI();
		frame.add(gui, BorderLayout.CENTER);
		frame.setVisible(true);
	}
	
	public void updateRoll(int roll) {
		JTextComponent text =  ((JTextComponent) this.roll.getComponent(1));
		text.setText(Integer.toString(roll));
	}
	
	public void updateSuggestion(Solution suggestion, Card card) {
		String sugg;
		String cardName;
		if (suggestion == null) {
			sugg = "";
			cardName = "";
		}
		else {
			sugg = suggestion.person + " " + suggestion.room + " " + suggestion.weapon;
			cardName = card.getCardName();
		}
		JTextComponent sug = (JTextComponent) this.guess.getComponent(1);
		sug.setText(sugg);
		JTextComponent text = (JTextComponent) this.guessResult.getComponent(1);
		text.setText(cardName);
	}
	
	public void updateWhoseTurn(String player) {
		JTextComponent turn = (JTextComponent) this.whoseTurn.getComponent(0);
		turn.setText(player);
	}
}
