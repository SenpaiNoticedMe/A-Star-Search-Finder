/**
ITCS 3135
Project 2

This has features from the provided node class.

This is a java file. Run as a .java file.
*/

/**
Public Class - Node
Descrription - This forms the nodes needed for the game board grid in the A_Star_Search_Program class.
               Each node represents a tile in the nxn gameboard.
               It has fields for row, column, and other necessary info.
Implements - Comparable
             It implements Comparable and @Override in order to use Java's integrated utility class.            
*/
public class Node implements Comparable{
    // Variables + Constants.
    private int row, col, G, H, type, finalType;
    private int F = 0;
    private Node parent;
   
    /**
    Arg-Constructor
    Description - This creates a new instance of the Node class and initialized some fields.
    @param row - The row position.
    @param column - The column position.
    @param type - The type of tile (1 or 0).
    */
    public Node(int row, int column, int type){
        this.parent = null;
        this.row = row;
        this.col = column;
        //type 0 is traverseable, 1 is not
        this.type = type;
        this.finalType = type;
    } // End method.

    // Mutator/Setter methods to set values
    public void setParent(Node parent){
        this.parent = parent;
    } // End method.  
    
    public void setH(int hValue){
        this.H = hValue;
        setF();
    } // End method.
    
    public void setF(){
        this.F = G + H;
    } // End method.
    
    public void setG(int gValue){
        this.G = gValue;
        setF();
    } // End method.
    
    public void setFinalType(int finalTypeValue) {
      this.finalType = finalTypeValue;
    } // End method.
            
    // Accessor/Getter methods to get values
    public int getRow(){
        return row;
    } // End method.
    
    public int getCol(){
        return col;
    } // End method.
    
    public Node getParent(){
        return parent;
    } // End method.
    
    public int getG(){
        return G;
    } // End method.
    
    public int getH(){
        return H;
    } // End method. 
    
    public int getF(){
        return F;
    } // End method.
      
    public int getType() {
        return type;
    } // End method.
    
    public int getFinalType() {
      return finalType;
    } // End method.    

    /**
    Method - equals
    Description - 
    @param tempNode - A temporary variable for holding a node.
    @return - Row and Column of a node.
    */
    public boolean equals(Object tempNode){
        //typecast to Node
        Node n = (Node) tempNode;

        return row == n.getRow() && col == n.getCol();
    } // End method.

    /**
    Method - toString()
    Description - Returns a string when the class instance is called.
    @return - A string with info about the Node.
    
    */
    public String toString(){
        return "Node: " + row + "_" + col;
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