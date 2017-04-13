package clueGame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.*;

public class BoardCell extends JComponent {


	//THE INSTANCE VARIABLES
	private int row, column;
	private char roomInitial;
	private DoorDirection doorDirection;
	private static int CELL_HEIGHT = 25;
	private static int CELL_WIDTH = 25;

	//CONSTRUCTOR
	public BoardCell(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}
	
	public void draw(Graphics g) {
		Board board = Board.getInstance();
		if (this.isHallWay()) g.setColor(Color.yellow);
		else g.setColor(Color.gray);
//		else if (!this.isDoorway()) g.setColor(Color.gray);
//		else g.setColor(Color.blue);
		g.fillRect(column*CELL_WIDTH, row*CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
		if (this.isHallWay()) {
			g.setColor(Color.black);
			g.drawRect(column*CELL_WIDTH, row*CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
		}
		else if (this.isDoorway()) {
			g.setColor(Color.blue);
			switch (doorDirection) {
			case UP: 
				g.fillRect(column*CELL_WIDTH, row*CELL_HEIGHT, CELL_WIDTH, 5);
				break;
			case DOWN:
				g.fillRect(column*CELL_WIDTH, row*CELL_HEIGHT+20, CELL_WIDTH, 5);
				break;
			case RIGHT:
				g.fillRect(column*CELL_WIDTH+20, row*CELL_HEIGHT, 5, CELL_HEIGHT);
				break;
			case LEFT:
				g.fillRect(column*CELL_WIDTH, row*CELL_HEIGHT, 5, CELL_HEIGHT);
				break;
			case NAME:
				g.setColor(Color.black);
				g.setFont(new Font("Serif", Font.BOLD, 12));
				g.drawString(board.getLegend().get(roomInitial), column*CELL_WIDTH, row*CELL_HEIGHT);
				break;
			default:
				break;
			}	
		}
//		if (this.doorDirection == DoorDirection.NONE) {
//			g.setColor(Color.black);
//			g.setFont(new Font("Serif", Font.BOLD, 12));
//			g.drawString("test", 0, 0);
//			//g.drawString(board.getLegend().get(roomInitial), column*CELL_WIDTH, row*CELL_HEIGHT);
//		}
	}
	
	public void setRoomInitial(char roomInitial) {
		this.roomInitial = roomInitial;
	}

	//BOOLEANS FOR THE ROOMINITIAL
	public boolean isHallWay(){
		if(roomInitial == 'W'){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isDoorway(){
		if(this.doorDirection != DoorDirection.NONE){
			return true;
		}
			return false;
	}
	
	public void setDoorDirection(DoorDirection doorDirection) {
		this.doorDirection = doorDirection;
	}

	public boolean isRoom(){
		if ((roomInitial != 'W') && (roomInitial != 'X')) {
			if(this.doorDirection == DoorDirection.NONE){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}

	//THE GETTER FUNCTIONS
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public char getInitial(){
		return roomInitial;
	}
	
	public DoorDirection getDoorDirection(){
		return doorDirection;
	}

}
