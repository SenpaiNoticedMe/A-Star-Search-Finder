/** 
ITCS 3135  
Project 2

Description - This class generates a 15x15 tile grid where 0 are navigable, but 1 are blocks.
              Each tile is a node from the Node class with fields that contain necessary info (example - type (0 or 1)).
              The class uses a PriorityQueue for keeping a openList and a HashSet for keeping a closedList.
              PriorityQueue allows for the nodes with the lowest (f = h+g ) heuristic cost to go first.
              HashSet allows for fast search and add node features for identifying if a node is already in the list.
              The final gameBoard Path will be displayed at the end and it shows the shortest final gameBoard Path from the start to end node.
              The start node is labeled as "S."
              The path nodes are labeled as "P."
              The end node is labeled as "E."
              Tiles are leabeled as "1."
              Blocks are labeled as "0."            
Helper Classes  - Node.java                          

This is a Java file. Run as .java file.                          
*/

// Import.
import java.util.Scanner;
import java.util.Random;
import java.util.PriorityQueue;
import java.util.HashSet;

/** 
Public class - Project2_A_Star_Search.
Description - This class generates a 15x15 tile grid where 0 are navigable, but 1 are blocks.
              The final gameBoard Path will be displayed at the end and it shows the shortest final gameBoard Path from the start to end node.
*/        
public class Project2_A_Star_Search {            
   
   /**
   Method - main
   Description - Main functions.
   @param args - The string array needed for the main method.
   */
   public static void main(String[] args) {
      // Variables + Constants.
      int sC = 0; // Start.
      int sR = 0;
      int eC = 0; // End.
      int eR = 0;
      int[] arrayValues = new int[4];
      
      int repeat = 0;
      int again = 1;
      int tileType = 1;      
      
      final int STANDARD_SIZE = 15;   
      final int INITIAL_PATH_VALUE = 1;
      final int FINAL_PATH_VALUE = 2;   
      final int SAME_PATH_VALUE = 3;
      
      Node[][] gameBoard;   
      gameBoard = new Node[STANDARD_SIZE][STANDARD_SIZE];          
      
      // Scanner object.
      Scanner keyboard = new Scanner(System.in);
      
      // Priority Queue for Open List.
         // Has nodes that are newly found, but not used.
      PriorityQueue<Node> openList = new PriorityQueue<Node>();
      // HashSet for Closed List. 
         // Has nodes that have been used.
      HashSet<Node> closedList = new HashSet<Node>();
         
      // Execute at least 1 time.
      // Ask to repeat.            
      do {
         // Display the main title and subtitle..
         displayMainTitle(STANDARD_SIZE);                   
         
         // Display the starting gameBoard so the user can view it.
         printGameBoard(STANDARD_SIZE, gameBoard, INITIAL_PATH_VALUE, sR, sC, eR, eC);
         
         // Get user input.
         arrayValues = getUserInput(keyboard, gameBoard, STANDARD_SIZE);
         
         // Store user inputs.
         sR = arrayValues[0];
         sC = arrayValues[1];
         eR = arrayValues[2];
         eC = arrayValues[3];

        if ( sR == eR && sC == eC) {
            System.out.print("\n");
            System.out.print("You are already at the destination.\n\n");                        
            printGameBoard(STANDARD_SIZE, gameBoard, SAME_PATH_VALUE, sR, sC, eR, eC);
        }
        else {        
           // Get the heuristics for all gameBoard tiles.  
           getManhattan(STANDARD_SIZE, gameBoard, eR, eC);
           
           // Initialize the Node. This will also initialize the total cost value.
           gameBoard[sR][sC].setG(0);
           
           // Clear the utility lists before use.
           initializeLists(openList, closedList);
                
           // Add the startNode to the list.     
           startOpenList(gameBoard, sR, sC, openList);
          
           // Get the final final gameBoard Path.   
           displayFinalGameBoardPath(STANDARD_SIZE, gameBoard, FINAL_PATH_VALUE,sR, sC, eR, eC, tileType, openList,
                                     closedList);
        } // End if series.
        
        // Ask the user for input.
        System.out.print("\nDo you want to try again? ( 1 - Yes and 0 - No): ");
        again = keyboard.nextInt();                                       
      } while (again == 1); // Repeat if the user wants to.
           
   } // End method.
   
   /**
   Method - displayMainTitle()
   Description - Prints the main title and subtitle.
   @param size - The size (n) of the gameboard nxn.
   */
   public static void displayMainTitle(int size) {
      // Display title.
      System.out.print("A_Star_Search" + "\n\n");     
      
      // Display subtitle.    
      System.out.print("Game Board (" + size + "x" + size + "):" + "\n\n");
   } // End method.
   
   /**
   Method - getUserInput()
   Description - Gets and Verifies user inputs.
   @param keyboard - A Java.util.Scanner class object.
   @param - A 2-D array that holds the gameBoard tile nodes.
   @param size - The (n) size of the gameboard nxn. 
   @retrun - An int array with 4 positions [startRow, startColumn, endRow, endColumn].
   */
   public static int[] getUserInput(Scanner keyboard, Node[][] gameBoard, int size) {
      // Variables + Constants.
      int sR;
      int sC;
      int eR;
      int eC;
      int[] values = new int[4];         
      final int STANDARD_SIZE = size; // Store size value.
      
      // Get user imput.
      System.out.print("\nPick the start node\n");
      System.out.print("Enter Row position (0-14): "); 
      sR = keyboard.nextInt();
      
      // Check input.
      while (sR < 0 || sR > (STANDARD_SIZE-1)) {         
         System.out.print("\nInvalid Position. Please enter a number (0-14) ");
         System.out.print("\n");
         System.out.print("Pick the start node\n");
         System.out.print("Enter Row position (0-14): "); 
         sR = keyboard.nextInt();    
      } // End while.
      
      // Get user input.      
      System.out.print("\n");
      System.out.print("Pick the start node\n");
      System.out.print("Enter Column position (0-14): "); 
      sC = keyboard.nextInt();            
      
      // Check user input.
      while (sC < 0 || sC > (STANDARD_SIZE-1)) {
         System.out.print("\n");
         System.out.print("Invalid Position. Please enter a number (0-14) ");
         System.out.print("\n");
         System.out.print("Pick the start node\n");
         System.out.print("Enter Column position (0-14): "); 
         sC = keyboard.nextInt();
         System.out.print("\n");
      } // End while.
            
      // Check for block.    
      while (gameBoard[sR][sC].getType() == 1) {
         // Display Text.
         System.out.print("\nError: You selected a block (1). Please enter again. \n");
      
         // Get user input.      
         System.out.print("\n");
         System.out.print("Pick the start node\n");
         System.out.print("Enter Row position (0-14): "); 
         sC = keyboard.nextInt();            
         
         // Check user input.
         while (sC < 0 || sC > (STANDARD_SIZE-1)) {
            System.out.print("\n");
            System.out.print("Invalid Position. Please enter a number (0-14) ");
            System.out.print("\n");
            System.out.print("Pick the start node\n");
            System.out.print("Enter Row position (0-14): "); 
            sC = keyboard.nextInt();
            System.out.print("\n");
         } // End while.
         
         // Get user imput.
         System.out.print("\nPick the start node\n");
         System.out.print("Enter Column position (0-14): "); 
         sR = keyboard.nextInt();
         
         while (sR < 0 || sR > (STANDARD_SIZE-1)) {         
            System.out.print("\nInvalid Position. Please enter a number (0-14) ");
            System.out.print("\n");
            System.out.print("Pick the start node\n");
            System.out.print("Enter Column position (0-14): "); 
            sR = keyboard.nextInt();    
         } // End while.
      } // End while.   
       
      // Get user input.     
      System.out.print("\nPick the end node\n");
      System.out.print("Enter Row position (0-14): "); 
      eR = keyboard.nextInt();
      
      while (eR < 0 || eR > (STANDARD_SIZE-1)) {   
         System.out.print("\n");
         System.out.print("Invalid Position. Please enter a number (0-14) ");
         System.out.print("\nPick the end node\n");
         System.out.print("Enter Row position (0-14): "); 
         eR = keyboard.nextInt();
      } // End while.
      
      System.out.print("\n");
      System.out.print("Pick the end node\n");
      System.out.print("Enter Column position (0-14): "); 
      eC = keyboard.nextInt();   
      
      while (eC < 0 || eC > (STANDARD_SIZE-1)) {   
         System.out.print("\n");
         System.out.print("Invalid Position. Please enter a number (0-14) ");
         System.out.print("\nPick the end node\n");
         System.out.print("Enter Column position (0-14): "); 
         eC = keyboard.nextInt();   
      } // End while.     
       
      // Check for block.    
      while (gameBoard[eR][eC].getType() == 1) {
         // Display Text.
         System.out.print("\nError: You selected a block (1). Please enter again. \n");
         // Get user input.     
         System.out.print("\nPick the end node\n");
         System.out.print("Enter Row position (0-14): "); 
         eR = keyboard.nextInt();
         
         while (eR < 0 || eR > (STANDARD_SIZE-1)) {   
            System.out.print("\n");
            System.out.print("Invalid Position. Please enter a number (0-14) ");
            System.out.print("\nPick the end node\n");
            System.out.print("Enter Row position (0-14): "); 
            eR = keyboard.nextInt();
         } // End while.
         
         System.out.print("Pick the end node\n");
         System.out.print("Enter Column position (0-14): "); 
         eC = keyboard.nextInt();   
         
         while (eC < 0 || eC > (STANDARD_SIZE-1)) {   
            System.out.print("\n");
            System.out.print("Invalid Position. Please enter a number (0-14) ");
            System.out.print("\nPick the end node\n");
            System.out.print("Enter Column position (0-14): "); 
            eC = keyboard.nextInt();   
         } // End while.     
      } // End method.    
      
      // Store Values.
      values[0] = sR;
      values[1] = sC;
      values[2] = eR;
      values[3] = eC;

      // Return values.
      return values;      
   } // End method.
   
   /**
   Method - printGameBoard()
   Description - Generates an nxn gameBoard with tiles and blocks.
                 It then prints out the generated gameBoard.
   @param gameBoard - A nxn tile board.
   @param gameBoardSize - The size n of an nxn gameBoard.   
   @param path - The value that determines if to print the starting or final path gameboard.
   @param sR - The start row position.
   @param sC - The start column position.
   @param eR - The end row position.
   @param eC - The end column position.             
   */
   public static void printGameBoard(int gameBoardSize, Node[][] gameBoard, int path, int sR, int sC,
                                     int eR, int eC) {
      // Variables + Constants.
      int count = 0;
      int chance = 0;
      final int startGameBoard = 1;
      final int finalGameBoard = 2;
      final int sameGameBoard = 3;
      Node gameBoardTile = null;
      Random randomBlock = new Random();
      
      if (path == startGameBoard) {           
         // Generate a board where 1/10 tiles are blocks.
         // Generate tiles for each rowxcolumn position.         
         for (int row = 0;  row < gameBoardSize; row++) {
            for(int column = 0; column < gameBoardSize; column++) {
               // Random ints (1-10).
               chance = randomBlock.nextInt(10) + 1;
               // 1/10 chance of a tile being a random block.
               if(chance == 1) {
                   gameBoardTile = new Node(row, column, 1);
                   count++;
               }
               else {
                   gameBoardTile = new Node(row, column, 0);
               } // End if-series.
               // Create tile Node.
               gameBoard[row][column] = gameBoardTile;
            }
         } // End for-series.
         
         // Print gameBoardTiles.
         for (int row = 0; row < gameBoardSize; row++) {
            for (int column = 0; column < gameBoardSize; column++) {
               // Determine if to print block or tile.
               if (gameBoard[row][column].getType() == 1) {
                  System.out.print("X"); 
               }
               else {
                   System.out.print("_");
               }
               System.out.print(" ");
             }
             
             System.out.print("\n");
         }
      } 
      else if (path == finalGameBoard) {
        System.out.print("\nShortest Path Found: " + "\n\n");
        
        // Print out final gameBoard with path.
        for(int row = 0; row < gameBoardSize; row++) {
            for(int col = 0; col < gameBoardSize; col++) {                
                // Print out all tiles.
                if(row == sR && col == sC) {
                  System.out.print("S");
                }
                else if ( row == eR && col == eC) {
                  System.out.print("E");
                }
                else if(gameBoard[row][col].getType() == 1) {
                  System.out.print("X");
                }
                else if(gameBoard[row][col].getFinalType() == 2) {                        
                  System.out.print("P");
                }
                else {
                  System.out.print("_");
                }
                System.out.print(" ");
            }
            System.out.print("\n");
        }
     } 
     if (path == sameGameBoard) {
         // Print gameBoardTiles.
         for (int row = 0; row < gameBoardSize; row++) {
            for (int column = 0; column < gameBoardSize; column++) {
               // Determine if to print block or tile.
               if ( row == sR && column == sC ){
                  System.out.print("SE");
               }
               else if (gameBoard[row][column].getType() == 1) {
                  System.out.print("X"); 
               }
               else {
                   System.out.print("_");
               }
               System.out.print(" ");
             }
             
             System.out.print("\n");
         }
      } // End if-series.                                   
   } // End method.
   
   /**
   Method - displayFinalGameBoardPath()
   Description - Clear both the open and closed lists.
   @param STANDARD_SIZE - The size n of an nxn gameBoard.   
   @param gameBoard - A nxn tile board.
   @finalPathValue - The value 2 that means to display the final path gameBoard.
   @param newNode - The new node to insert into the gameBoard.
   @param sR - The start row position.
   @param sC - The start column position.
   @param eR - The end row position.
   @param eC - The end column position.
   @param tileType - The tile type (1 - tile and 0 - Block).  
   @param openList - The openList.
   @param closedList - The closedList.
   */
   public static void displayFinalGameBoardPath(int STANDARD_SIZE, Node[][] gameBoard, int finalPathValue,int sR, int sC, int eR, int eC, int tileType,
                                                PriorityQueue<Node> openList, HashSet<Node> closedList) {
      // Variables + Constants.
      int repeat = 0;
      int oPValue; 
      int cLValue;
      int exitValue = 0;
      Node newNode;     
      
      // Get the gameBoardPath.
      while(repeat == 0) { 
      
         oPValue = checkOpenList(openList);
         
         if(oPValue != 0) {
            displayErrorPath();
            repeat = 1;
         }
         else {                 
            // Get a new node from the open list (min heap - priority queue).            
            newNode = getNewNode(openList);            
                        
            // Check the closed list.
            cLValue = checkClosedList(newNode, closedList);
            
            // Check closed list for new node. 
            if(cLValue == 1) {                              
               exitValue = generateFinalPath(STANDARD_SIZE, gameBoard, newNode, sR, sC, eR, eC);
               
               // Check to see if the path was generated.
               if (exitValue != 0) {                  
                  // Exit.
                  repeat = 1;
               }
               else {         
                  searchOtherNodes(STANDARD_SIZE, gameBoard, newNode, openList, closedList);
               } // End if-series.               
            } // End C-Value
         } // End if-series.                                                             
      } // End while-series.
   } // End method.
   
   /**
   Method - generateFinalPath()
   Description - Generate the final path.
   @param STANDARD_SIZE - The size n of an nxn gameBoard.   
   @param gameBoard - A nxn tile board.
   @param newNode - The new node to insert into the gameBoard.
   @param sR - The start row position.
   @param sC - The start column position.
   @param eR - The end row position.
   @param eC - The end column position.  
   @return - The exit value -1 that means that a final path was generated.
   */
   public static int generateFinalPath(int STANDARD_SIZE, Node[][] gameBoard, Node newNode, int sR, int sC, int eR, int eC) {
      // Variables + Constants.
      int exitValue = 0;
      int tileType = 1;
      int finalPathValue = 2;
      
      // Insert correct tile type into final board.
      if (newNode.getCol() == eC && newNode.getRow() == eR) {
         // Search all tiles.
         while (tileType == 1 ) {
            // Set path tiles.
            gameBoard[newNode.getRow()][newNode.getCol()].setFinalType(2);
            // Search until start node reached, which signals that all tiles have been searched.
            newNode = newNode.getParent();
            // Exit search when start node is reached.
            if(newNode.getRow() == sR && newNode.getCol() == sC) {
               tileType = 0;
            }
         }                        
         // Display the shortest path.
         printGameBoard(STANDARD_SIZE, gameBoard, finalPathValue, sR, sC, eR, eC);
         
         // Set exit value.
         exitValue = 1;
      } // End if-series.
      
      // Return exit value.
      return exitValue;                  
   } // End method.

   /**
   Method - displayErrorPath()
   Description - Display the error path message.
   */
   public static void displayErrorPath() {
      // Display Message.
      System.out.println("Error: No path found");
   } // End method.
   
   /**
   Method - initializeLists()
   Description - Clear both the open and closed lists.
   @param openList - A priority queue object.  
   @param closedList - A HashSet object.
   */
   public static void initializeLists(PriorityQueue<Node> openList, HashSet<Node> closedList) {
      // Initialize Lists.
      openList.clear();
      closedList.clear();   
   }
   
   /**
   Method - startOpenList()
   Description - Add the start node to the openList.
   @param gameBoard - A nxn tile board.
   @param sR - The start row position.
   @param sC - The start column position.
   @param openList - A PriorityQueue Object.   
   */
   public static void startOpenList(Node[][] gameBoard, int sR, int sC, PriorityQueue<Node> openList) {
      // Initialize List.
      openList.add(gameBoard[sR][sC]);
   }
   
   /**
   Method - checkOpenList()
   Description - Check to see if the openList is empty.
   @param openList - A PriorityQueue object.
   @return - 0 if not empty and 1 if empty.
   */
   public static int checkOpenList(PriorityQueue<Node> openList) {
      // Variables + Constants.
      int oPValue = 0;     
      
      // Check open list.
      if(!openList.isEmpty()) {
         oPValue = 0;
      }
      else {
         oPValue = 1;
      } // End if-series.
      
      // Return Value.
      return oPValue;
   }
   
   /**
   Method - checkClosedList()
   Description - Check the closed list for a certain node.
   @newNode - The node to check for.
   @closedList - The closed list.
   @return - 0 if found and 1 if not found.
   */
   public static int checkClosedList(Node newNode, HashSet<Node> closedList) {
      // Variables + Constants.
      int cLValue = 0;
      
      // Check closed list.     
      if (!closedList.contains(newNode)) {
         cLValue = 1;
      }
      else {
         cLValue = 0;
      }
      
      // Return value.
      return cLValue;
   } // End method.       
   
   /**
   Method - getNewNode()
   Description - Remove and get a new node from the open list.
   @param openList - A PriorityQueue object.
   @return - A new node removed from the open list.
   */
   public static Node getNewNode(PriorityQueue<Node> openList) {
      // Variables + Constants.
      Node newNode;
      
      // Get new node.
      newNode = openList.remove();
      
      // Return new Node.
      return newNode;            
      
   } // End method.         
   
      
   /**
   Method - searchOtherNodes()
   Description - Search for parent nodes and add the new node to the closed list.
   @param STANDARD_SIZE - The size n of an nxn gameBoard.   
   @param gameBoard - A nxn tile board.
   @param newNode - The new node to insert into the gameBoard.
   @param openList - The openList.
   @param closedList - The closedList.
   */
   public static void searchOtherNodes(int STANDARD_SIZE, Node[][] gameBoard, Node newNode, PriorityQueue<Node> openList, HashSet<Node> closedList) {
      // Variables + Constants.
      int value = 0;
      
      // Search for all parent nodes.
      searchAllParentNodes(STANDARD_SIZE, gameBoard, newNode, openList, closedList);         
      
      // Add node to closed list.
      closedList.add(newNode);
      
   } // End method.   

   /**
   Method - getManhattan.
   Description - Gets and sets all heuristics values for all nodes 
                 based on the end node.
   @param gameBoardSize - The game board size for RowsxColumns.
   @param gameBoard - The 2-D array that represents the game board.
   @param endRow - The end row position.
   @param endColumn - The end column position.
   */
   public static void getManhattan(int gameBoardSize, Node[][] gameBoard, int endRow, int endColumn) {
      // Variables and Constants.     
      int h = 0;    
      int startRow = 0;
      int startColumn = 0;
      
      // Calculate heuristics for each node tile
      // based on the end node.       
      for (int r = 0; r < gameBoardSize; r++) {
         for (int c = 0; c < gameBoardSize; c++) {   
            h = 0;
            // Calculate h.
            startRow = r;
            startColumn = c;
            if (startRow < endRow) {
               h += endRow-startRow;
            } 
            else {
               h += startRow-endRow;
            }                
            if (startColumn < endColumn) {
               h += endColumn-startColumn;
            } 
            else {
               h += startColumn-endColumn;
            }    
            gameBoard[r][c].setH(h);
         }
      } // End for-series.   
   } // End method.

   /**
   Method - searchAllParentNodes()
   Description - Finds and gets all parent nodes for a given node.   
   @param gameBoardSize - The game board size for RowsxColumns.
   @param gameBoard - The 2-D array that represents the game board.
   @param newNode - The new Node.
   @param openList - The openList.
   @param closedList - The closedList.
   */ 
   public static void searchAllParentNodes(int gameBoardSize, Node[][] gameBoard, Node newNode, PriorityQueue<Node> openList,
                                           HashSet<Node> closedList) {
     // Variables + Constants.
     Node currentNode = newNode;
     int row = currentNode.getRow();
     int col = currentNode.getCol();

     //Vertical and Horizontal neighbors
     neighbours(gameBoard, currentNode, 10, row+1, col, openList, closedList);
     neighbours(gameBoard, currentNode, 10, row-1, col, openList, closedList);
     neighbours(gameBoard, currentNode, 10, row, col+1, openList, closedList);
     neighbours(gameBoard, currentNode, 10, row, col-1, openList, closedList);

     //Diagonal neighbors
     neighbours(gameBoard, currentNode, 14, row+1, col-1, openList, closedList);
     neighbours(gameBoard, currentNode, 14, row-1, col+1, openList, closedList);
     neighbours(gameBoard, currentNode, 14, row+1, col+1, openList, closedList);
     neighbours(gameBoard, currentNode, 14, row-1, col-1, openList, closedList);
   } // End method.                                
   
   public static void neighbours(Node[][] gameBoard, Node currentNode, int cost, int row, int col,
                                 PriorityQueue<Node> openList, HashSet<Node> closedList) {
     // Variables + Constants.
     int totalCost = currentNode.getF();
     
     // Check boundaries and make sure that a tile is in the gameboard.
     // Check open and closed lists.
     if (row >= 0 && row <= 14 && col >= 0 && col <= 14 
         && gameBoard[row][col].getType() == 0 && !closedList.contains(gameBoard[row][col])) {
         openList.add(gameBoard[row][col]);
         gameBoard[row][col].setParent(currentNode);
     } // End if-series.
   } // End method. 
} // End class.