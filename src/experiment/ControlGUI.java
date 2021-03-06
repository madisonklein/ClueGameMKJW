package experiment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ControlGUI extends JPanel {

	public ControlGUI() {
		setLayout(new GridLayout(2,0));
		JPanel topPanel = createTopPanel();
		add(topPanel);
		JPanel bottomPanel = createBottomPanel();
		add(bottomPanel);
	}
	
	public JPanel createTopPanel() {
		JButton nextPlayer = new JButton("Next Player");
		JButton makeAccusation = new JButton("Make an accustion");
		JPanel panel = new JPanel();
		
		JPanel whoseTurn = new JPanel();
		JTextField text = new JTextField(20);
		text.setEditable(false);
		panel.add(text);
		panel.setBorder(new TitledBorder(null, "Whose Turn?"));
		
		panel.setLayout(new GridLayout(1,0));
		panel.add(whoseTurn);
		panel.add(nextPlayer);
		panel.add(makeAccusation);
		return panel;
	}
	
	public JPanel createBottomPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,0));
		panel.add(createTextPanel("Die", "Roll", false));
		panel.add(createTextPanel("Guess", "Guess", true));
		panel.add(createTextPanel("Guess Result", "Response", false));
		return panel;
	}
	
	public JPanel createTextPanel(String title, String name, boolean editable) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
	 	JLabel nameLabel = new JLabel(name);
	 	JTextField text = new JTextField(30);
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
}
