package clueGame;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Board {

	private BoardCell[][] board;
	private Map<BoardCell, Set <BoardCell>> adjacencyMap;
	private Set<BoardCell> targets;
	private Set<BoardCell> visitedList;
	private Map<Character,String> legend;	
	private int numRows;
	private int numCols;
	private String boardConfigFile;			
	private String roomConfigFile;	
	private String peopleConfigFile;
	private ArrayList<ComputerPlayer> people;
	private HumanPlayer human;
	private ArrayList<Card> deck;
	private Map<Player, ArrayList<Card>> hands;
	private String[] weapons = { "knife", "brass knuckles", "rifle", "banana", "flail", "Spear" };
	
	public static final int MAX_BOARD_SIZE = 40;
	
	private Board() {
		super();
		adjacencyMap = new HashMap<BoardCell, Set<BoardCell>>();
		visitedList = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		
	}
	
		
		//THE ONLY INSTANCE OF THE GAME BOARD
		private static Board theInstance = new Board();
	
	
		public void initialize() throws IOException, BadConfigFormatException {
		
		legend = new HashMap<Character, String>();
		//READING IN THE FILES AND COUNTING ROWS AND COLUMNS
		int counterRow = 0;
        int counterCol = 1;
        
		String layout;
		BufferedReader br = new BufferedReader(new FileReader(boardConfigFile));
        
	    StringBuilder sb = new StringBuilder();
	    String line = br.readLine();
        for(int i = 0; i < line.length(); i++){
          	if(line.charAt(i) == ',') counterCol++;
        }
    

	    while (line != null) {
	    	int countCol = 1;
	        sb.append(line);
	        sb.append(",");
   
	        for(int i = 0; i < line.length(); i++){
	        	if(line.charAt(i) == ',') countCol++;
	        }
	        
	        line = br.readLine();
	        counterRow++;

	    }

		layout = sb.toString();
        
	    br.close();
		
	    
	    //INITIALIZING THE BOARD
	    setNumRows(counterRow);
	    setNumCols(counterCol);
	    board = new BoardCell[numRows][numCols];
		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				board[i][j] = new BoardCell(i,j);
			}
		}
	    int col = 0;
	    int row = 0;
	    	    
	    //INITIALIZING THE BOARD LAYOUT
	    for (String splitting: layout.split(",")) {
	         if (col < numCols){
	        	 if(splitting.length() > 1){
	        		if((splitting.charAt(1) == 'D')) board[row][col].setDoorDirection(DoorDirection.DOWN);
	        		if((splitting.charAt(1) == 'U')) board[row][col].setDoorDirection(DoorDirection.UP);
	        		if((splitting.charAt(1) == 'L')) board[row][col].setDoorDirection(DoorDirection.LEFT);
	        		if((splitting.charAt(1) == 'R')) board[row][col].setDoorDirection(DoorDirection.RIGHT);
	        		if((splitting.charAt(1) == 'N')) board[row][col].setDoorDirection(DoorDirection.NONE);
	        	 }
	        	 	else{
	        	 		board[row][col].setDoorDirection(DoorDirection.NONE); 
	        	 	}

	        	 board[row][col].setRoomInitial(splitting.charAt(0));
	        	 col++;
	         }
	         else{
	        	 col = 0;
	        	 row += 1;
	        	 if(splitting.length() > 1){
		        		if((splitting.charAt(1) == 'D')) board[row][col].setDoorDirection(DoorDirection.DOWN);
		        		if((splitting.charAt(1) == 'U')) board[row][col].setDoorDirection(DoorDirection.UP);
		        		if((splitting.charAt(1) == 'L')) board[row][col].setDoorDirection(DoorDirection.LEFT);
		        		if((splitting.charAt(1) == 'R')) board[row][col].setDoorDirection(DoorDirection.RIGHT);
		        		if((splitting.charAt(1) == 'N')) board[row][col].setDoorDirection(DoorDirection.NONE);
		        	 }
		        else{
		       		 board[row][col].setDoorDirection(DoorDirection.NONE); 
		       	 }
	        	 board[row][col].setRoomInitial(splitting.charAt(0));
	        	 col += 1;
	         }
	      }
	
	    
	    //The code above is in the loadBoardConfig() but it gives us an error when we replace the code above with the function call below
	    
//		loadBoardConfig();

	    
		loadRoomConfig();	
	    
		calcAdjacencies();
		
		loadPeople();
		createDeck();
		dealHands();
	}

	//CALCADJACENCIES AND CALCTARGETS
	public void calcAdjacencies(){
		for(int row = 0; row < numRows; row++){
			for(int col = 0; col < numCols; col++){
			Set<BoardCell> temp = new HashSet<BoardCell>();
			if((row > 0) && (row < numRows - 1)){
			if((col > 0) && (col < numCols - 1)){
				if(isAdj(row, col, -1, 0)) temp.add(board[row - 1][col]);
				if(isAdj(row, col, 1, 0)) temp.add(board[row + 1][col]);
				if(isAdj(row, col, 0, -1)) temp.add(board[row][col - 1]);
				if(isAdj(row, col, 0, 1)) temp.add(board[row][col + 1]);
				}
			else if(col == 0){
				if(isAdj(row, col, -1, 0)) temp.add(board[row - 1][col]);
				if(isAdj(row, col, 1, 0)) temp.add(board[row + 1][col]);
				if(isAdj(row, col, 0, 1)) temp.add(board[row][col + 1]);	
			}
			else if(col == numCols - 1){
				if(isAdj(row, col, -1, 0)) temp.add(board[row - 1][col]);
				if(isAdj(row, col, 1, 0)) temp.add(board[row + 1][col]);
				if(isAdj(row, col, 0, -1)) temp.add(board[row][col - 1]);
				}
			}
			else if(row == 0){
				if((col > 0) && (col < numCols - 1)){
					if(isAdj(row, col, 1, 0)) temp.add(board[row + 1][col]);
					if(isAdj(row, col, 0, -1)) temp.add(board[row][col - 1]);
					if(isAdj(row, col, 0, 1)) temp.add(board[row][col + 1]);	
				}
				else if(col == 0){
					if(isAdj(row, col, 1, 0)) temp.add(board[row + 1][col]);
					if(isAdj(row, col, 0, 1)) temp.add(board[row][col + 1]);	
				}
				else if(col == numCols - 1){
					if(isAdj(row, col, 1, 0)) temp.add(board[row + 1][col]);
					if(isAdj(row, col, 0, -1)) temp.add(board[row][col - 1]);	
				}
			}
			else if(row == numRows - 1){
				if((col > 0) && (col < numCols - 1)){
					if(isAdj(row, col, -1, 0)) temp.add(board[row - 1][col]);
					if(isAdj(row, col, 0, -1)) temp.add(board[row][col - 1]);
					if(isAdj(row, col, 0, 1)) temp.add(board[row][col + 1]);
				}
				if(col == 0){
					if(isAdj(row, col, -1, 0)) temp.add(board[row - 1][col]);
					if(isAdj(row, col, 0, 1)) temp.add(board[row][col + 1]);	
					}
				if(col == numCols - 1){
					if(isAdj(row, col, -1, 0)) temp.add(board[row - 1][col]);
					if(isAdj(row, col, 0, -1)) temp.add(board[row][col- 1]);
					}
				}
				adjacencyMap.put(getCellAt(row, col), temp);
			}				
		}
	}
	
	public void calcTargets(int row, int col,int pathLength){
		visitedList.add(board[row][col]);
		findAllTargets(row, col, pathLength);
	}
	
	public void findAllTargets(int row, int col, int numSteps){
		Set<BoardCell> list = getAdjList(row, col);
		for(BoardCell a : list){
			if(visitedList.contains(a)){
			}
			else{
				visitedList.add(a);
				if((numSteps == 1) || (a.isDoorway())){
					targets.add(a);
					}
				else{
					findAllTargets(a.getRow(), a.getColumn(), numSteps - 1);
				}
				visitedList.remove(a);
			}
		}
	}
	
	public void setConfigFiles(String ClueLayout, String ClueLegend, String people){
		boardConfigFile = ClueLayout;
		roomConfigFile = ClueLegend;
		peopleConfigFile = people;
	}
	
	public boolean isAdj(int row, int col, int a, int b){
		if((board[row][col].isRoom()) || (board[row+a][col+b].isRoom())){
			return false;
		}
		if(board[row+a][col+b].isDoorway()){
			if((a == -1) && (board[row+a][col+b].getDoorDirection() == DoorDirection.DOWN)){
				return true;
			}
			else if((a == 1) && (board[row+a][col+b].getDoorDirection() == DoorDirection.UP)){
				return true;
			}
			else if((b == -1) && (board[row+a][col+b].getDoorDirection() == DoorDirection.RIGHT)){
				return true;
			}
			else if((b == 1) && (board[row+a][col+b].getDoorDirection() == DoorDirection.LEFT)){
				return true;
			}
			else{
				return false;
			}
		}
		if(board[row][col].isDoorway()){
			if((a == -1) && (board[row][col].getDoorDirection() == DoorDirection.UP)){
				return true;
			}
			else if((a == 1) && (board[row][col].getDoorDirection() == DoorDirection.DOWN)){
				return true;
			}
			else if((b == -1) && (board[row][col].getDoorDirection() == DoorDirection.LEFT)){
				return true;
			}
			else if((b == 1) && (board[row][col].getDoorDirection() == DoorDirection.RIGHT)){
				return true;
			}
			else{
				return false;
			}
		}
		if(!(board[row][col].isDoorway()) && !(board[row][col].isHallWay()) && !(board[row][col].isRoom())){
			return false;
		}
		if(!(board[row+a][col+b].isDoorway()) && !(board[row+a][col+b].isHallWay()) && !(board[row+a][col+b].isRoom())){
			return false;
		}
		return true;
	}
	
	public void createDeck() {
		  
		deck = new ArrayList<Card>();
		
		for (ComputerPlayer p: people) deck.add(new Card(p.getName(), CardType.PERSON));
		deck.add(new Card(human.getName(), CardType.PERSON));
		for (Character c: legend.keySet()) deck.add(new Card(legend.get(c), CardType.ROOM));
		for (String s: weapons) deck.add(new Card(s, CardType.WEAPON));
		
		
	}
	
	public void dealHands() {
		int minHandSize = deck.size()/ (people.size()+1);
		hands = new HashMap<Player, ArrayList<Card>>();
		for (ComputerPlayer p: people) {
			ArrayList<Card> temp = new ArrayList<Card>();
			hands.put(p, temp);
		}
		ArrayList<Card> temp = new ArrayList<Card>();
		hands.put(human, temp);
		for (Player p: hands.keySet()) {
			for (int i = 0; i < minHandSize; i++) {
				int index = (int) (Math.random() * (deck.size() -1));
				hands.get(p).add(deck.remove(index));
			}
		}
		if (deck.size() != 0) {
			for (Player p: hands.keySet()) {
				if (deck.size() == 0) break;
				else hands.get(p).add(deck.remove(0));
			}
		}
		
	}
	
	//SETTER & GETTER FUNCTIONS

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}

	public Map<BoardCell, Set<BoardCell>> getAdjacencyMap() {
		return adjacencyMap;
	}
	
	public static Board getInstance() {
		return theInstance;
	}
	
	public Set<BoardCell> getTargets(){
		Set<BoardCell> temp = new HashSet<BoardCell>();
		for(BoardCell a: targets){
			temp.add(a);
		}
		targets.clear();
		return temp;
	}
	
	public Set<BoardCell> getAdjList(int row, int col){
		return adjacencyMap.get(getCellAt(row, col));
	}
	
	public BoardCell getCellAt(int row, int col){
		return board[row][col];
	}
	
	public Map<Character, String> getLegend() {
		return legend;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numCols;
	}
	
	public ArrayList<Card> getDeck() {
		return deck;
	}
	
	public Map<Player, ArrayList<Card>> getHands() {
		return hands;
	}
	
	public HumanPlayer getHumanPlayer() {
		return human;
	}
	
	public ArrayList<ComputerPlayer> getPeople() {
		return people;
	}
	
	//LOAD THE CONFIGS
	
	public void loadRoomConfig() throws IOException, BadConfigFormatException{       
		
		BufferedReader bs = new BufferedReader(new FileReader(roomConfigFile));
        String line1 = bs.readLine();
        int position;
        String name;
        String option;
        Character key;
        while (line1 != null) {
         position = line1.lastIndexOf(",");
         key = line1.charAt(0);
         name = line1.substring(3, position);
         option = line1.substring(position + 2, line1.length());
         if (!(option.equals("Card")) && !(option.equals("Other"))){
        	 throw new BadConfigFormatException("One of the rooms is neither Card nor other");
         }
         legend.put(key, name);
         line1 = bs.readLine(); 
        }
        bs.close();
	}
	
	//loadBoardConfig() is the same as in the initialize function, but when we are trying to replace the code in the initialize with this function it is not working
	
	public void loadPeople() throws BadConfigFormatException {
		people = new ArrayList<ComputerPlayer>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(peopleConfigFile));
			String line = br.readLine();
			String[] temp = line.split(", ");
			if (temp.length != 4) throw new BadConfigFormatException();
			human = new HumanPlayer(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), convertColor(temp[3]));
			line = br.readLine();
			while (line != null) {
				temp = line.split(", ");
				people.add(new ComputerPlayer(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), convertColor(temp[3])));
				line = br.readLine();
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Color convertColor(String strColor) {
		Color color;
		try {
			Field field = Class.forName("java.awt.Color").getField(strColor.trim());
			color = (Color)field.get(null);
		}
		catch (Exception e) {
			color = null;
		}
		return color;
	}
	
	public void loadBoardConfig() throws BadConfigFormatException{
		int counterRow = 0;
        int counterCol = 1;
        
		String layout = null;
		try{BufferedReader br = new BufferedReader(new FileReader(boardConfigFile));
        
	    StringBuilder sb = new StringBuilder();
	    String line = br.readLine();
        for(int i = 0; i < line.length(); i++){
          	if(line.charAt(i) == ',') counterCol++;
        }
    

	    while (line != null) {
	    	int countCol = 1;
	        sb.append(line);
	        sb.append(",");

	        
	        for(int i = 0; i < line.length(); i++){
	        	if(line.charAt(i) == ',') countCol++;
	        }

	        if (countCol != counterCol){
	        	
	          	throw new BadConfigFormatException("Not same number of columns in each row");
	        }
	        
	        line = br.readLine();
	        counterRow++;

	    }

		layout = sb.toString();
        
	    br.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		
		
	    
	    //INITIALIZING THE BOARD
	    setNumRows(counterRow);
	    setNumCols(counterCol);
	    board = new BoardCell[numRows][numCols];
		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				board[i][j] = new BoardCell(i,j);
			}
		}
	    int col = 0;
	    int row = 0;
	    	    
	    //INITIALIZING THE BOARD LAYOUT
	    for (String splitting: layout.split(",")) {
	         if (col < numCols){
	        	 if(splitting.length() > 1){
	        		if((splitting.charAt(1) == 'D')) board[row][col].setDoorDirection(DoorDirection.DOWN);
	        		if((splitting.charAt(1) == 'U')) board[row][col].setDoorDirection(DoorDirection.UP);
	        		if((splitting.charAt(1) == 'L')) board[row][col].setDoorDirection(DoorDirection.LEFT);
	        		if((splitting.charAt(1) == 'R')) board[row][col].setDoorDirection(DoorDirection.RIGHT);
	        		if((splitting.charAt(1) == 'N')) board[row][col].setDoorDirection(DoorDirection.NONE);
	        	 }
	        	 	else{
	        	 		throw new BadConfigFormatException("Doors are wrong");
	        	 	}

	        	 board[row][col].setRoomInitial(splitting.charAt(0));
	        	 col++;
	         }
	         else{
	        	 col = 0;
	        	 row += 1;
	        	 if(splitting.length() > 1){
		        		if((splitting.charAt(1) == 'D')) board[row][col].setDoorDirection(DoorDirection.DOWN);
		        		if((splitting.charAt(1) == 'U')) board[row][col].setDoorDirection(DoorDirection.UP);
		        		if((splitting.charAt(1) == 'L')) board[row][col].setDoorDirection(DoorDirection.LEFT);
		        		if((splitting.charAt(1) == 'R')) board[row][col].setDoorDirection(DoorDirection.RIGHT);
		        		if((splitting.charAt(1) == 'N')) board[row][col].setDoorDirection(DoorDirection.NONE);
		        	 }
		        else{
		       		 board[row][col].setDoorDirection(DoorDirection.NONE); 
		       	 }
	        	 board[row][col].setRoomInitial(splitting.charAt(0));
	        	 col += 1;
	         }
	      }
		
	}
}
	
