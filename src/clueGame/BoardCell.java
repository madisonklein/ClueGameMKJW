package clueGame;

public class BoardCell {


	//THE INSTANCE VARIABLES
	private int row, column;
	private char roomInitial;
	private DoorDirection doorDirection;
	

	//CONSTRUCTOR
	public BoardCell(int row, int column) {
		super();
		this.row = row;
		this.column = column;
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
