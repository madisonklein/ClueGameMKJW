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
	

	public void calcAdjacencies(BoardCell cell){
		if((cell.getRow() >= 0) && (cell.getRow() < 4)){
			for (int i = cell.getRow(); i <=1; i++){
				for (int j = cell.getColumn(); j <=1; j++){
					if (i != null && j != null){
						//set.add something
					}
				}
			}
			if((cell.getColumn() > 0) && (cell.getColumn() < 4)){
			}
			if(cell.getColumn() == 0){
			}
			if(cell.getColumn() == 3){
			}
		}
		else if(cell.getRow() == 0){
			if((cell.getColumn() > 0) && (cell.getColumn() < 3)){
			}
			if(cell.getColumn() == 0){
			}
			if(cell.getColumn() == 3){
			}
		}
		else if(cell.getRow() == 3){
			if((cell.getColumn() > 0) && (cell.getColumn() < 3)){
			}
			if(cell.getColumn() == 0){
			}
			if(cell.getColumn() == 3){
			}
		}
		
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
