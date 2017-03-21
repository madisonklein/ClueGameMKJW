package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Set;

public class ComputerPlayer extends Player{
	
	private ArrayList<BoardCell> visitedRooms;
	private Solution suggestion;
	private ArrayList<String> unseenWeapons, unseenPeople, unseenRooms;
	
	
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
		BoardCell target = targ.get(index);
		visitedRooms.add(target);
		setRow(target.getRow());
		setColumn(target.getColumn());
		return target;
	}
	
	public void makeAccusation() {
		
	}
	
	public void createSuggestion(String room) {
		int peopleIndex = (int) (Math.random()*unseenPeople.size());
		int weaponIndex = (int) (Math.random()*unseenWeapons.size());
		suggestion = new Solution(unseenPeople.get(peopleIndex), room, unseenWeapons.get(weaponIndex));
	}
	
	public void clearVisitedRooms() {
		visitedRooms.clear();
	}
	
	public void setUnseenWeapons(ArrayList<String> weapons) {
		this.unseenWeapons = weapons;
	}
	
	public void setUnseenPeople(ArrayList<String> people) {
		this.unseenPeople = people;
	}
	
	public void setUnseenRooms(ArrayList<String> rooms) {
		this.unseenRooms = rooms;
	}
	
	public Solution getSuggestion() {
		return suggestion;
	}

}
