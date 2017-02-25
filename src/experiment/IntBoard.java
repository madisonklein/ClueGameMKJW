package experiment;

import java.util.*;

public class IntBoard {
	private BoardCell[][] board;
	private Map<BoardCell, Set <BoardCell>> adjacencyMap;
	private Set<BoardCell> targets;

	
	
	public IntBoard() {
		super();
		adjacencyMap = new HashMap<BoardCell, Set<BoardCell>>();
		targets = new HashSet<BoardCell>();
		board = new BoardCell[4][4];
	}
	

	public void calcAdjacencies(){
	}
	
	public void calcTargets(BoardCell startCell,int pathLength){	
	}
	
	public Set<BoardCell> getTargets(){
		return null;
	}
	
	public Set<BoardCell> getAdjList(BoardCell test){
		return null;
	}
	
	public BoardCell getCell(int row, int col){
		return null;
	}

}
