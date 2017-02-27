package experiment;

import java.util.*;

public class IntBoard {
	private BoardCell[][] board;
	private Map<BoardCell, Set <BoardCell>> adjacencyMap;
	private Set<BoardCell> targets;
	private Set<BoardCell> visitedList;

	
	
	public IntBoard() {
		super();
		adjacencyMap = new HashMap<BoardCell, Set<BoardCell>>();
		visitedList = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		//board = new BoardCell[4][4];
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				board[i][j] = new BoardCell(i,j);
			}
		}
	}
	

	public void calcAdjacencies(BoardCell cell){
		Set<BoardCell> temp = null;
		if((cell.getRow() > 0) && (cell.getRow() < 4)){
			if((cell.getColumn() >= 0) && (cell.getColumn() < 4)){
				temp.add(board[cell.getRow() - 1][cell.getColumn()]);
				temp.add(board[cell.getRow() + 1][cell.getColumn()]);
				temp.add(board[cell.getRow()][cell.getColumn() - 1]);
				temp.add(board[cell.getRow()][cell.getColumn() + 1]);						
			}
			if(cell.getColumn() == 0){
				temp.add(board[cell.getRow() - 1][cell.getColumn()]);
				temp.add(board[cell.getRow() + 1][cell.getColumn()]);
				temp.add(board[cell.getRow()][cell.getColumn() + 1]);	
			}
			if(cell.getColumn() == 3){
				temp.add(board[cell.getRow() - 1][cell.getColumn()]);
				temp.add(board[cell.getRow() + 1][cell.getColumn()]);
				temp.add(board[cell.getRow()][cell.getColumn() - 1]);
			}
		}
		else if(cell.getRow() == 0){
			if((cell.getColumn() > 0) && (cell.getColumn() < 3)){
				temp.add(board[cell.getRow() + 1][cell.getColumn()]);
				temp.add(board[cell.getRow()][cell.getColumn() - 1]);
				temp.add(board[cell.getRow()][cell.getColumn() + 1]);	
			}
			if(cell.getColumn() == 0){
				temp.add(board[cell.getRow() + 1][cell.getColumn()]);
				temp.add(board[cell.getRow()][cell.getColumn() + 1]);	
			}
			if(cell.getColumn() == 3){
				temp.add(board[cell.getRow() + 1][cell.getColumn()]);
				temp.add(board[cell.getRow()][cell.getColumn() - 1]);	
			}
		}
		else if(cell.getRow() == 3){
			if((cell.getColumn() > 0) && (cell.getColumn() < 3)){
				temp.add(board[cell.getRow() - 1][cell.getColumn()]);
				temp.add(board[cell.getRow()][cell.getColumn() - 1]);
				temp.add(board[cell.getRow()][cell.getColumn() + 1]);
			}
			if(cell.getColumn() == 0){
				temp.add(board[cell.getRow() - 1][cell.getColumn()]);
				temp.add(board[cell.getRow()][cell.getColumn() + 1]);	
			}
			if(cell.getColumn() == 3){
				temp.add(board[cell.getRow() - 1][cell.getColumn()]);
				temp.add(board[cell.getRow()][cell.getColumn() - 1]);
			}
		}
		adjacencyMap.put(cell, temp);
}
		
	
	public void calcTargets(BoardCell startCell,int pathLength){
		visitedList.add(startCell);
		findAllTargets(startCell, pathLength);
		
	}
	
	public void findAllTargets(BoardCell thisCell, int numSteps){
		Set<BoardCell> list = getAdjList(thisCell);
		for(BoardCell a : list){
			if(visitedList.contains(a)){
				return;
			}
			visitedList.add(a);
			if(numSteps == 1){
				 targets.add(a);
				}
			else{
				findAllTargets(a, numSteps - 1);
			}
			visitedList.remove(a);
		}
	}
	
	public Set<BoardCell> getTargets(){
		return null;
	}
	
	public Set<BoardCell> getAdjList(BoardCell test){
		return adjacencyMap.get(test);
	}
	
	public BoardCell getCell(int row, int col){
		return board[row][col];
	}

}
