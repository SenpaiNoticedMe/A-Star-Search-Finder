/**
A* Search Program - Node Class
*/

/**
Public Class - Node
Description - This forms the nodes needed for the game board grid in the A_Star_Search_Program class.
               Each node represents a tile in the nxn gameboard.
               It has fields for row, column, and other necessary info.
Implements - Comparable
             It implements Comparable and @Override in order to use Java's integrated utility class.            
*/
public class Node implements Comparable{
   // Variables + Constants.
   private int row; // Row position on grid.
   private int column; // Column position on grid.
   
   // Node types indicate wether a node is an unblocked node (0), blocked node (1), or final path node (2).
   private int type; // Node type.
   private int finalType; // Final Node type.
   
   private int G; // Distance from start node to current node.
   private int H; // Heuristic - Distance from end node to current node.
   private int F = 0; // Total Cost - H + G = F 
   
   private Node parent; // Parent Node reference;
   
   
   /**
   Arg-Constructor
   Description - This creates a new instance of the Node class and initialized some fields.
   @param row - The row position.
   @param column - The column position.
   @param type - The type of tile (unblocked(0), blocked(1), or final path node (2)).
   */
   public Node(int row, int column, int type) {
      this.parent = null;
      
      this.row = row;
      this.column = column;
      // 0 is an unblocked and 1 is a blocked path. 
      this.type = type;
      this.finalType = type;
   } // End Arg-Constructor.
   
   // Accessor/Getter methods to get values
   public void setParent(Node parent) {
      this.parent = parent;
   } // End method.
   
   public void setRow(int row) {
      this.row = row;
   } // End method.
   
   public void setColumn(int column) {
      this.column = column;
   } // End method.

   public void setType(int type) {
      this.type = type;
   } // End method.

   public void setFinalType(int finalType) {
      this.finalType = finalType;
   } // End method.

   public void setG(int G) {
      this.G = G;
   } // End method.

   public void setH(int H) {
      this.H = H;
   } // End method.

   public void setF() {
      F = this.H + this.G;
   } // End method.

   public Node getParent() {
      return parent;
   } // End method.
   
   public int getRow() {
      return row;
   } // End method.
   
   public int getColumn() {
      return column;
   } // End method.

   public int getType() {
      return type;
   } // End method.

   public int getFinalType() {
      return finalType;
   } // End method.

   public int getG() {
      return G;
   } // End method.

   public int getH() {
      return H;
   } // End method.

   public int getF() {
      return F;
   } // End method.

   /**
   Method - equals
   Description - 
   @param tempNode - A temporary variable for holding a node.
   @return - Row and Column of a node.
   */
   public boolean equals(Object node) {
      Node n = (Node) node;
      
      return row == n.getRow() && column == n.getColumn(); 
   } // End method.

   /**
    Method - toString()
    Description - Returns a string when the class instance is called.
    @return - A string with info about the Node.
    
    */
    public String toString(){
        return "Node: " + row + "_" + column;
    } // End method.   
    
    /**
    Method - compareTo()
    Description - Compares two nodes and puts the node with the lowest (f = h + g) cost 
                  at the top of the list. 
                  This is needed for Java's integrated utility class.
    @param compareNode - The node to be added to a utility list.
    @return - The position indicator for the utility list to use.            
    */
    @Override
    public int compareTo(Object compareNode) {
        // Variables + Constants.
        int positionIndicator;              
        Node listNode = (Node) compareNode;
        
        // Compare the Nodes.
        if (this.getF() == listNode.getF()) {
            positionIndicator = 0;
        }
        if (this.getF() < listNode.getF()) {
            positionIndicator = -1;
        }
        else {
            positionIndicator = 1;
        } // End if-series.
        
        // Return the position indicator.
        return positionIndicator;
    } // End method.

} // End class.