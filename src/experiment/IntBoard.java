package experiment;

import java.util.*;

import clueGame.BoardCell;

public class IntBoard {
	private BoardCell[][] board;
	private Map<BoardCell, Set <BoardCell>> adjacencyMap;
	private Set<BoardCell> targets;
	private Set<BoardCell> visitedList;

	public static final int SIZE = 4;
	
	
	public IntBoard() {
		super();
		adjacencyMap = new HashMap<BoardCell, Set<BoardCell>>();
		visitedList = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		board = new BoardCell[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++){
			for (int j = 0; j < SIZE; j++){
				board[i][j] = new BoardCell(i,j, 'W');
			}
			System.out.println(board[i][1].getRow());
		}
		calcAdjacencies();
	}
	

	public void calcAdjacencies(){
		for(int row = 0; row < SIZE; row++){
			for(int col = 0; col < SIZE; col++){
			Set<BoardCell> temp = new HashSet<BoardCell>();
			if((row > 0) && (row < 3)){
			if((col > 0) && (col < 3)){
				temp.add(board[row - 1][col]);
				temp.add(board[row + 1][col]);
				temp.add(board[row][col - 1]);
				temp.add(board[row][col + 1]);
				}
			else if(col == 0){
				temp.add(board[row - 1][col]);
				temp.add(board[row + 1][col]);
				temp.add(board[row][col + 1]);	
			}
			else if(col == 3){
				temp.add(board[row - 1][col]);
				temp.add(board[row + 1][col]);
				temp.add(board[row][col - 1]);
				}
			}
			else if(row == 0){
				if((col > 0) && (col < 3)){
					temp.add(board[row + 1][col]);
					temp.add(board[row][col - 1]);
					temp.add(board[row][col + 1]);	
				}
				else if(col == 0){
					temp.add(board[row + 1][col]);
					temp.add(board[row][col + 1]);	
				}
				else if(col == 3){
					temp.add(board[row + 1][col]);
					temp.add(board[row][col - 1]);	
				}
			}
			else if(row == 3){
				if((col > 0) && (col < 3)){
					temp.add(board[row - 1][col]);
					temp.add(board[row][col - 1]);
					temp.add(board[row][col + 1]);
				}
				if(col == 0){
					temp.add(board[row - 1][col]);
					temp.add(board[row][col + 1]);	
					}
				if(col == 3){
					temp.add(board[row - 1][col]);
					temp.add(board[row][col- 1]);
					}
				}
				adjacencyMap.put(this.getCell(row, col), temp);
			}				
		}
	}
	
	public void calcTargets(BoardCell startCell,int pathLength){
		visitedList.add(startCell);
		findAllTargets(startCell, pathLength);
	}
	
	public void findAllTargets(BoardCell thisCell, int numSteps){
		Set<BoardCell> list = getAdjList(thisCell);
		for(BoardCell a : list){
			if(visitedList.contains(a)){
			}
			else{
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
	}
	
	public Set<BoardCell> getTargets(){
		return targets;
	}
	
	public Set<BoardCell> getAdjList(BoardCell test){
		return adjacencyMap.get(test);
	}
	
	public BoardCell getCell(int row, int col){
		return board[row][col];
	}

}
