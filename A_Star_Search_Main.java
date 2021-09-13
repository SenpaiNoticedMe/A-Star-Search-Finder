/**
A* Search Program - Main Class
*/

/** 
Description - This class generates a 15x15 tile grid where 0 are navigable, but 1 are blocks.
              Each tile is a node from the Node class with fields that contain necessary info (example - type (0 or 1)).
              The class uses a PriorityQueue for keeping a openList and a HashSet for keeping a closedList.
              PriorityQueue allows for the nodes with the lowest (f = h+g ) heuristic cost to go first.
              HashSet allows for fast search and add node features for identifying if a node is already in the list.
              The final gameBoard Path will be displayed at the end and it shows the shortest final gameBoard Path from the start to end node.
              The start node is labeled as "S."
              The path nodes are labeled as "P."
              The end node is labeled as "E."
              Tiles are leabeled as "_."
              Blocks are labeled as "X."            
Helper Classes  - A_Star_Search_Node.java                          
*/

// Import.
import java.util.Scanner;
import java.util.Random;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.ArrayList;

public class A_Star_Search_Main {

   // ArrayLists.
   private ArrayList<Node> borders;
   private ArrayList<Node> paths;
   private ArrayList<Node> searchedNodes;
      
   // Start and End Nodes.
   private int startRow; // Start.
   private int startColumn; // Start.
   private int endRow; // End.
   private int endColumn; // End.
   
   // GameBoard grid generation info.
   int frameNodeSize;
   int size;
   int sizeX;
   int sizeY;
   
   // Determines if A*Star is unfinished (0) or has finished (1).
   int finishedStatus;
   
   // Variables + Constants.      
   private final int INITIAL_PATH_VALUE = 1;
   private final int FINAL_PATH_VALUE = 2;   
   private final int SAME_PATH_VALUE = 3;
    
   // GameBoard grid of nxn nodes.
   Node[][] gameBoard;
   
   // HashSet for Closed List. 
         // Has nodes that have been used.
   HashSet<Node> closedList;
   // Priority Queue for Open List.
         // Has nodes that are newly found, but not used.
   PriorityQueue<Node> openList;
   
   /**
   Constructor - A_Star_Search_Main()
   Description - Intitializes the variables and objects in the A_Star_Search_Main class.
   @param startRow - The start row.
   @param startColumn - The start column.
   @param endRow - The end row.
   @param endColumn - The end column.
   @param sizeX - The number of nodes along the x-axis of the gameBoard. 
   @param sizeY - The number of nodes along the -axis of the gameBoard.
   @param frameNodeSize - The n size in pixels of the gameBoard's nxn Nodes.       
   */
   public A_Star_Search_Main(int startRow, int startColumn, int endRow, int endColumn, int sizeX, int sizeY, int frameNodeSize) {         
      
      // ArrayLists.
      this.borders = new ArrayList<Node>();
      this.paths = new ArrayList<Node>();
      this.searchedNodes = new ArrayList<Node>();
      
      // Start and End Nodes.
      this.startColumn = startColumn; // Start.
      this.startRow = startRow; // Start.
      this.endColumn = endColumn; // End.
      this.endRow = endRow; // End.
      
      // GameBoard grid generation info.
      this.frameNodeSize = frameNodeSize;
      finishedStatus = 0;
      
      // GameBoard grid of nxn Nodes.
      this.gameBoard = new Node[sizeX][sizeY];
      
      // HashSet for Closed List. 
            // Has nodes that have been used.
      this.closedList = new HashSet<Node>();
      // Priority Queue for Open List.
            // Has nodes that are newly found, but not used.
      this.openList = new PriorityQueue<Node>();   
   } // End method.
   
   /**
   Method - greetingMessage()
   Description - Displays an initial greeting message for using the A_Star_Search_Main Program.
   */
   public void greetingMessage() {
      // Title Message.
      System.out.println("Welcome to the A* Search Program!\n");
       
      displayGrid(FINAL_PATH_VALUE);
      System.out.println("End of Grid");
   
   } // End method.
   
   /**
   Method - setUpGrid()
   Description - Sets up the gameBoard grid for a new start.                      
   */
   public void setUpGrid() {   
      // Set status to unfinished. 
      finishedStatus = 0;
      
      // Initialize lists.
      openList.clear();
      closedList.clear();  
      paths.clear();
      searchedNodes.clear();
      
      // Initialize the initial gameBoard[][] grid. 
      initializeGrid();
      // Initialize the gameBoard[][] borders.
      initializeBorders();
      
      // Initialize heuristic H for all tiles in the gameBoard.
      // Heuristics h holds the travel cost of distance from the end node to the current node.
      initializeHeuristicH();                           
               
      // Add the start node to the open list.
      gameBoard[startRow][startColumn].setG(0);
      gameBoard[startRow][startColumn].setF();
      openList.add(gameBoard[startRow][startColumn]);
   } // End method.
   
   /**
   Method - run()
   Description - Runs an iteration of A* Search.
   */
   public void run() {
      // If openList is empty, there are no more available nodes to be searched.
      // This means that a path to the end node was not found.
      if (openList.isEmpty() == true) {
         System.out.print("\n No path found.\n\n");
         // Set status to finished (1).
         finishedStatus = 1;              
      } // End if. 
      else {
         // Pop off node with lowest F travel cost and set as current node.
         Node currentNode = openList.remove();
         
         // Check if current node is the end node.
         if (currentNode.getRow() == endRow && currentNode.getColumn() == endColumn) {
            // Set all nodes in the path to type 2.
            while (currentNode.getParent().getParent() != null) {
               currentNode = currentNode.getParent();
               currentNode.setType(2);
               // Add nodes to the ArrayList paths containing the path nodes.
               paths.add(currentNode);
            } // End while.
            
            // Display grid with final path.
            displayGrid(FINAL_PATH_VALUE);
            // Set status to finished (1).
            finishedStatus = 1;             
         } // End if.                              
         
         // Generate the current node neighbors.
         findNeighbors(currentNode);
         
         // Add current node to the closedList.
         closedList.add(currentNode);      
      }
      
      // Print out thanks message if status is finished (1);
      if (finishedStatus == 1) {                                                                 
         // End message.
         System.out.print("Thank you for using this A* Search program!\n\n");         
      }
      
   } // End method.
      
   /**
   Method - findNeighbors()
   Description - Finds all the neighbors of a given node.     
   @param currentNode - The current node to be explored to find its neighbors.
   */
   public  void findNeighbors(Node currentNode) {
      // Variables + Constants.
      int row = currentNode.getRow();
      int column = currentNode.getColumn();
      
      // Get horizontal and vertical neighbors.
      determineIfNeighborIsValid(currentNode, row-1, column, 10);                                                                        
      determineIfNeighborIsValid(currentNode, row, column+1, 10);                                                                        
      determineIfNeighborIsValid(currentNode, row+1, column, 10);                                                                        
      determineIfNeighborIsValid(currentNode, row, column-1, 10);                                                                        
   
      // Get diagonal neighbors.
      determineIfNeighborIsValid(currentNode, row-1, column+1, 14);                                                                        
      determineIfNeighborIsValid(currentNode, row+1, column+1, 14);                                                                        
      determineIfNeighborIsValid(currentNode, row+1, column-1, 14);                                                                        
      determineIfNeighborIsValid(currentNode, row-1, column-1, 14);  
   } // End method.
   
   /**
   Method - determineIfNeighborIsValid()
   Description - Determines if a neighbor node is a valid node.     
   @param currentNode - The current node to be explored to find its neighbors.
   @param neighborRow - The row value of the neighbor node.
   @param neighborColumn - The column value of the neighbor node.
   @param G - The cost to travel to the neighbor node from the current node.   
   */
   public  void determineIfNeighborIsValid(Node currentNode, int neighborRow, int neighborColumn, int G) {                                                                                   
      // Check if the neighbor node is valid.
      // A valid node cannot be a border node,
      //              must be within the bounds of the gameBoard[][].
      //              must not be in the ckosed list (not been searched already).            
      if (neighborRow >= 0 && neighborRow < gameBoard.length && neighborColumn >= 0 && neighborColumn < gameBoard[neighborRow].length &&
          gameBoard[neighborRow][neighborColumn].getType() != 1 && closedList.contains(gameBoard[neighborRow][neighborColumn]) == false) {
          // Get the neighborNode from the given row and column values.
         Node neighborNode = gameBoard[neighborRow][neighborColumn];
          // Add neighborNode to the ArrayList of searchedNodes.
         searchedNodes.add(neighborNode);
          
          // Set new G value if the node is not in the openList (is a new node) or 
          // has a higher G cost than what the current node + G value is.
         if (openList.contains(neighborNode) == false || (currentNode.getG() + G < neighborNode.getG()) || neighborNode.getG() == 0) {            
            // Set parent node.
            neighborNode.setParent(currentNode);
            // Set new total G value.
            neighborNode.setG(G + currentNode.getG());
            // Set F = H + G value.
            neighborNode.setF();   
            
            // Add neighborNode to openList if it is not in it.                      
            if (openList.contains(neighborNode) == false)
               openList.add(neighborNode);
         }
      } // End if-series.                  
   } // End method.


   /**
   Method - initializeHeuristicH()
   Description - Initializes the heuristic H of all nodes in the gameBoard.   
   */
   public  void initializeHeuristicH() {
      // Initialize the H value for all nodes in the gameBoard[][].
      for (int row = 0; row < gameBoard.length; row++) {
         for (int column = 0; column < gameBoard[row].length; column++) {
            // Variables + Constants.
            int valueOfH = 0;
            
            // Calculate heuristic H.
            valueOfH = 10 * Math.abs(Math.abs(endRow - row) + Math.abs(endColumn - column)); // Multiply by 10 for sqrt(2) = 1.4 to be 14 instead of 1.4 for G cost.                    
         
            // Update the travel cost from the end node to the current node.
            gameBoard[row][column].setH(valueOfH); 
            // Update the total travel cost (F).
            gameBoard[row][column].setF();                                                                         
         }
      } // End for-series.
   } // End method.

   /**
   Method - initializeGrid()
   Description - Initializes the gameBoard grid by creating a new node for each tile.
   */
   public  void initializeGrid() {                    
      // Create a new node for every gameBoard tile.
      for (int row = 0; row < gameBoard.length; row++) {
         for (int column = 0; column < gameBoard[row].length; column++) {     
            // Create new empty nodes.                
            gameBoard[row][column] = new Node(row, column, 0);                                     
         }
      } // End for-series.      
   } // End method.
   
   /**
   Method - initializeBorders()
   Description - Initializes all border nodes in the gameBoard[][]. border nodes are found in the borders ArrayList.     
   */
   public void initializeBorders() {
      // Get every border node.
      for (int x = 0; x < borders.size(); x++) {
         if (borders.get(x).getRow()/frameNodeSize < gameBoard.length && borders.get(x).getColumn()/frameNodeSize < gameBoard[0].length) {   
            // Create a new border node.
            gameBoard[borders.get(x).getRow()/frameNodeSize][borders.get(x).getColumn()/frameNodeSize] = 
                     new Node(borders.get(x).getRow()/frameNodeSize, borders.get(x).getColumn()/frameNodeSize, 1);
         }
      } // End for.    
   } // End method.
         
   /**
   Method - displayGrid()
   Description - Displays the grid and its nodes.
   @param pathValue - The value determines whether to display the starting or final gameBoard grid.
   */
   public  void displayGrid(int pathValue) {
      // Display initial gameBoard[][].
      if (pathValue == 1) {
         // Display all nodes in the gameBoard[][].
         for (int row = 0; row < gameBoard.length; row++) {
            for (int column = 0; column < gameBoard[row].length; column++) {
               if (gameBoard[row][column].getType() == 0) System.out.print(" _ ");  
               else System.out.print(" X ");                                         
            }
            System.out.print("\n");
         } // End for-series.
      }
      // Display final gameBoard[][].
      else if (pathValue == 2){
         // Display all nodes in the gameBoard.
         for (int row = 0; row < gameBoard.length; row++) {
            for (int column = 0; column < gameBoard[row].length; column++) {
               if (row == startRow && column == startColumn) System.out.print("S"); 
               if (row == endRow && column == endColumn) System.out.print("E"); 
               
               if (row == startRow && column == startColumn || row == endRow && column == endColumn);
               else if (gameBoard[row][column].getType() == 0) System.out.print("_"); 
               else if (gameBoard[row][column].getType() == 1) System.out.print("X");                                                            
               else System.out.print("P");
               
               System.out.print("  ");                                         
            }
            System.out.print("\n");
         }
      } // End if-series.           
   } // End method.
   
      
   // Setters and Getters.
   
   /**
   Method - addBorder()
   Description - Add border node to the borders array list.
   @param borderNode - The border node to be added to the arraylist.
   */
   public  void addBorder(Node borderNode) {
      // Check if borderNode is already in the borders ArrayList.
      if (checkForBorderNodeDuplicate(borderNode) == false)
         // Add border node to the borders ArrayList.
         borders.add(borderNode);
   } // End method.
   
   /**
   Method - removeBorder()
   Description - Remove border node from the borders array list.
   @param borderNode - The border node to be removed from the arraylist.
   */
   public void removeBorder(Node borderNode) {
      // Check if borderNode is in the borders ArrayList.
      if (checkForBorderNodeDuplicate(borderNode) == true)
         // Remove border node from the borders ArrayList.
         borders.remove(borderNode);      
   } // End method.
     
   /**
   Method - checkForBorderNodeDuplicate
   Description - Check if a given border node is in the borders ArrayList.
   @param borderNode - The border node to be checked for in the borders ArrayList.
   @return - The status of the border node being in the the borders ArrayList.
   */
   public boolean checkForBorderNodeDuplicate (Node borderNode) {
      // Check for duplicate borderNode.
      for (int x = 0; x < borders.size(); x++) {
         if (borderNode.getRow() == borders.get(x).getRow() && borderNode.getColumn() == borders.get(x).getColumn())
            return true;
      } // End for series.
           
      // No duplicate.
      return false;
   } // End method.
   
   /**
   Method - setGameBoard()
   Description - Sets a new number of rows (x) and number of columns (y) for the gameBoard[][].
   @param x - The number of rows.
   @param y - The number of Columns.
   */
   public void setGameBoard(int x, int y) {
      gameBoard = new Node[x][y];
   } // End method.   
   
   /**
   Method - getGameBoard()
   Description - Gets the gameBoard[][].   
   @return - The gameBoard[][].
   */
   public Node[][] getGameBoard() {
      return gameBoard;
   } // End method.  
   
   /**
   Method - setBorders()
   Description - Sets the borders ArrayList variable.
   @param borders - An ArrayList Node object.
   */
   public void getBorders(ArrayList<Node> borders) {
      this.borders = borders;
   } // End method.
           
   /**
   Method - getBorders()
   Description - Accesses the borders ArrayList variable.
   @return - An ArrayList containing all the border nodes in the gameboard[][].
   */
   public ArrayList<Node> getBorders() {
      return borders;
   } // End method.
   
   /**
   Method - setPaths()
   Description - Sets the paths ArrayList variable.
   @param paths - An ArrayList Node object.
   */
   public void getPaths(ArrayList<Node> paths) {
      this.paths = paths;
   } // End method.

   /**
   Method - getPaths()
   Description - Accesses the paths ArrayList variable.
   @return - An ArrayList containing all the path nodes in the gameboard[][].
   */
   public ArrayList<Node> getPaths() {
      return paths;
   } // End method.

   /**
   Method - setSearchedNodes()
   Description - Sets the searchedNodes ArrayList variable.
   @param searchedNodes - An ArrayList Node object.
   */
   public void getSearchedNodes(ArrayList<Node> searchedNodes) {
      this.searchedNodes = searchedNodes;
   } // End method.
   
   /**
   Method - getSearchedNodes()
   Description - Accesses the searchedNodes ArrayList variable.
   @return - An ArrayList containing all the searched nodes in the gameboard[][].
   */
   public ArrayList<Node> getSearchedNodes() {
      return searchedNodes;
   } // End method.

   /**
   Method - setStartRow()
   Description - Sets the startRow value.
   @param startRow - The start row value.
   */
   public void setStartRow(int startRow) {
      this.startRow = startRow;
   } // End method.
   
   /**
   Method - getStartRow()
   Description - Access the startRow value.
   @return - The start row value.
   */
   public int getStartRow() {
      return startRow;
   } // End method.

    /**
   Method - setStartColumn()
   Description - Sets the startColumn value.
   @param startColumn - The start column value.
   */
   public void setStartColumn(int startColumn) {
      this.startColumn = startColumn;
   } // End method.
   
   /**
   Method - getStartColumn()
   Description - Access the startColumn value.
   @return - The start column value.
   */
   public int getStartColumn() {
      return startColumn;
   } // End method.
     
   /**
   Method - setEndRow()
   Description - Sets the endRow value.
   @param endRow - The end row value.
   */
   public void setEndRow(int endRow) {
      this.endRow = endRow;
   } // End method.
   
   /**
   Method - getEndRow()
   Description - Access the endRow value.
   @return - The end row value.
   */
   public int getEndRow() {
      return endRow;
   } // End method.
   
   /**
   Method - setEndColumn()
   Description - Sets the endColumn value.
   @param endColumn - Sets the end column value.
   */
   public void setEndColumn(int endColumn) {
      this.endColumn = endColumn;
   } // End method.
   
   /**
   Method - getEndColumn()
   Description - Access the endColumn value.
   @return - The end column value.
   */
   public int getEndColumn() {
      return endColumn;
   } // End method.
   
   /**
   Method - setFrameNodeSize()
   Description - Sets the frameNodeSize.
   @param frameNodeSize - The size of the frame node.
   */
   public void setFrameNodeSize(int frameNodeSize) {
      this.frameNodeSize = frameNodeSize;
   } // End method.
   
   /**
   Method - getFrameNodeSize()
   Description - Gets the frameNodeSize.
   @return - The size of the frame node.
   */
   public int getFrameNodeSize() {
      return frameNodeSize;
   } // End method.

   /**
   Method - setSizeX()
   Description - Sets the number of nodes in the x-axis for the gameBoard[][] .
   @param sizeX - The number of nodes in the x-axis for the gameBoard[][].
   */
   public void setSizeX(int sizeX) {
      this.sizeX = sizeX;
   } // End method.
   
   /**
   Method - getSizeX()
   Description - Gets the number of nodes in the x-axis for the gameBoard[][] .
   @return - The number of nodes in the x-axis for the gameBoard[][].
   */
   public int setSizeX() {
      return sizeX;
   } // End method.
   
   /**
   Method - setSizeY()
   Description - Sets the number of nodes in the y-axis for the gameBoard[][] .
   @param sizeY - The number of nodes in the y-axis for the gameBoard[][].
   */
   public void setSizeY(int sizeY) {
      this.sizeY = sizeY;
   } // End method.
   
   /**
   Method - getSizeY()
   Description - Gets the number of nodes in the y-axis for the gameBoard[][] .
   @return - The number of nodes in the y-axis for the gameBoard[][].
   */
   public int setSizeY() {
      return sizeY;
   } // End method.

   /**
   Method - setfinishedStatus()
   Description - Sets the status (unfinished - 0 or finished - 1) of the A* Search Process.
   @param finishedStatus - The status (unfinished - 0 or finished - 1) of the A* Search Process.
   */
   public void setfinishedStatus(int finishedStatus) {
      this.finishedStatus = finishedStatus;
   } // End method.
   
   /**
   Method - getfinishedStatus()
   Description - Gets the status (unfinished - 0 or finished - 1) of the A* Search Process.
   @return - The status (unfinished - 0 or finished - 1) of the A* Search Process.
   */
   public int setfinishedStatus() {
      return finishedStatus;
   } // End method.
   
   /**
   Method - setClosedList()
   Description - Sets the closedList.
   @param closedList - A HashSet Node object.
   */
   public void setClosedList(HashSet<Node> closedList) {
      this.closedList = closedList;
   } // End method.
   
   /**
   Method - getClosedList()
   Description - Gets the closedList.
   @return - A HashSet Node object that is the closedList.
   */
   public HashSet<Node> getClosedList() {
      return closedList;
   } // End method.
   
   /**
   Method - setOpenList()
   Description - Sets the openList.
   @param openList - A PriorityQueue Node object.
   */
   public void setOpenList(PriorityQueue<Node> openList) {
      this.openList = openList;
   } // End method.

   /**
   Method - getOpenList()
   Description - Gets the openList.
   @return - A PriorityQueue Node object that is the openList.
   */
   public PriorityQueue<Node> getOpenList() {
      return openList;
   } // End method.
   
} // End class.