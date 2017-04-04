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
	private Solution solution;
	
	public static final int MAX_BOARD_SIZE = 40;
	
	private Board() {
		super();
		adjacencyMap = new HashMap<BoardCell, Set<BoardCell>>();
		visitedList = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		
	}
	
		
		//THE ONLY INSTANCE OF THE GAME BOARD
		private static Board theInstance = new Board();
	
	
		public void initialize() {
		
		legend = new HashMap<Character, String>();
		//READING IN THE FILES AND COUNTING ROWS AND COLUMN
        
		try {
		loadBoardConfig();
		loadRoomConfig();	
	    
		calcAdjacencies();
		
		loadPeople();
		createDeck();
		dealHands();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	//CALCADJACENCIES AND CALCTARGETS
	public void calcAdjacencies(){
		for(int row = 0; row < numRows; row++){
			for(int col = 0; col < numCols; col++){
			Set<BoardCell> temp = new HashSet<BoardCell>();
			if (isAdj(row, col, -1, 0)) temp.add(board[row-1][col]);
			if(isAdj(row, col, 1, 0)) temp.add(board[row + 1][col]);
			if(isAdj(row, col, 0, -1)) temp.add(board[row][col - 1]);
			if(isAdj(row, col, 0, 1)) temp.add(board[row][col + 1]);
			adjacencyMap.put(getCellAt(row, col), temp);
			}
		}
	}
	
	public void calcTargets(int row, int col,int pathLength){
		targets.clear();
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
		//Checking for out of bounds/ if boardcell is at edge of board
		if (row+a < 0 || row + a >= numRows || col+b < 0 || col+b >= numCols) return false;
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
		return targets;
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
	public void setSolution(Solution sol) {
		this.solution = sol;
	}
	public Solution getSolution() {
		return solution;
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
			ArrayList<String> allWeapons = new ArrayList<String>();
			for (String s : weapons) allWeapons.add(s);
			ArrayList<String> allPeople = new ArrayList<String>();
			for (ComputerPlayer p: people) allPeople.add(p.getName());
			allPeople.add(human.getName());
			ArrayList<String> allRooms = new ArrayList<String>();
			for (Character c : legend.keySet()) allRooms.add(legend.get(c));
			for (ComputerPlayer p: people) {
				p.setUnseenWeapons(allWeapons);
				p.setUnseenPeople(allPeople);
				p.setUnseenRooms(allRooms);
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
	
	public void loadBoardConfig() throws BadConfigFormatException, IOException, FileNotFoundException{
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
		        sb.append(line);
		        sb.append(",");
	   
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
		
	}
	//Solution Test Methods
	
	public boolean checkAccusation(Solution sol) {
		return solution.equals(sol);
		
	}
	public Card handleSuggestion(Solution sol, HumanPlayer human, ArrayList<ComputerPlayer> computerPlayers, int index) {
		int temp = index + 1;
		if (temp > computerPlayers.size() - 1) temp = -1;
		for (int i = temp; i < computerPlayers.size(); i++) {
			Card shownCard;
			if (i == -1) shownCard = human.disproveSuggestion(sol);
			else shownCard = computerPlayers.get(i).disproveSuggestion(sol);
			if (shownCard != null) return shownCard;
		}
		for (int i = -1; i < temp - 1; i++) {
			Card shownCard;
			if (i == -1) shownCard = human.disproveSuggestion(sol);
			else shownCard = computerPlayers.get(i).disproveSuggestion(sol);
			if (shownCard != null) return shownCard;
		}
		return null;
	}
}
	
