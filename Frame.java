// Import.
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
Public Class - A_Star_Search_Frame
Description - Creates a frame for the A* Search Project.
Extends - JFrame
Implements - MouseListener
*/
public class Frame extends JPanel 
                   implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
   // gameBoard node info.
   private int sizeX;
   private int sizeY;
   private int frameNodeSize;
   
   // A* object.
   private A_Star_Search_Main aStarSearchMainObject;
   
   // Last key that has been pressed on the keyboard.
   private char currentKey;
   
   // Is mouse being dragged.
   boolean isMouseDragged = false;
   
   // A JFrame Object.
   private JFrame window;
   
   /**
   Constructor - Frame
   Description - Creates a new frame object.
   */
   public Frame() {
      // Create a new JFrame object.                                
      window = new JFrame();      
      
      // Set JFrame properties.
      window.setTitle("A* Search Visualizer Project");
      window.setContentPane(this);      
      window.getContentPane().setPreferredSize(new Dimension(499, 499));
      //window.setSize(400, 400);
      window.pack();
      window.setLayout(null);
      window.setVisible(true);
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      
      window.setLocationRelativeTo(null); // Center the frame on the screen.
      
      // Set gameboard[][] and gameBoard node size properties.
      this.frameNodeSize = 50;
      this.sizeX = getHeight() / frameNodeSize; 
      this.sizeY = getWidth() / frameNodeSize; 

      // Create new A* object.
      this.aStarSearchMainObject = new A_Star_Search_Main(4,2,4,7,sizeX,sizeY,frameNodeSize);      
      
      // Initialize A* object.
      aStarSearchMainObject.initializeGrid();
      aStarSearchMainObject.initializeBorders();
      aStarSearchMainObject.setUpGrid();
      aStarSearchMainObject.greetingMessage();
      
      // Add components to panel.     
      window.addMouseListener(this);    
      window.addKeyListener(this);  
      window.addMouseMotionListener(this);
      window.addMouseWheelListener(this);
      this.currentKey = (char) 0;
      
      // Repaint JPanel.
      repaint();
   } // End main.
   
   /**
   Method - paintComponent()
   Description - Paints the JPanel.
   @param g - A graphics object.
   */
   public void paintComponent(Graphics g) {                  
      // Call function from JFrame superclass or parent class.   
      super.paintComponent(g); 
      
      // Variables + Constants.
      int jFrameWidth = getWidth();
      int jFrameHeight = getHeight();
      
      // Colors.
      Color lightGray = new Color(200, 207, 205);
      Color darkGray = new Color(33, 36, 35);            
      
      // Create grid of the JFrame; 
      g.setColor(lightGray); // Set color.     
      for (int x = 0; x < jFrameWidth; x += frameNodeSize) { //
         for (int y = 0; y < jFrameHeight; y += frameNodeSize) {
            g.drawRect(x, y, frameNodeSize, frameNodeSize); // Color the grid node borders.                       
         }
      } // End for-series.                             
      
       // Create searched nodes of the JFrame;
      g.setColor(Color.red);
      for (int x = 0; x < aStarSearchMainObject.getSearchedNodes().size(); x++) { //
         g.fillRect(aStarSearchMainObject.getSearchedNodes().get(x).getColumn()*frameNodeSize, 
                    aStarSearchMainObject.getSearchedNodes().get(x).getRow()*frameNodeSize,
					     frameNodeSize-1, frameNodeSize-1);
      } // End for-series. 

      // Create path nodes of the JFrame;
      g.setColor(Color.green);
      for (int x = 0; x < aStarSearchMainObject.getPaths().size(); x ++) { //
         g.fillRect(aStarSearchMainObject.getPaths().get(x).getColumn()*frameNodeSize, 
                    aStarSearchMainObject.getPaths().get(x).getRow()*frameNodeSize,
					     frameNodeSize-1, frameNodeSize-1);
      } // End for-series. 

      // Create start and end nodes of the JFrame;
      g.setColor(Color.blue);         
      g.fillRect(aStarSearchMainObject.getStartColumn()*frameNodeSize, 
                  aStarSearchMainObject.getStartRow()*frameNodeSize, frameNodeSize, frameNodeSize);    
      g.fillRect(aStarSearchMainObject.getEndColumn()*frameNodeSize, 
                  aStarSearchMainObject.getEndRow()*frameNodeSize, frameNodeSize, frameNodeSize); 

      // Create border nodes of the JFrame;
      g.setColor(Color.black);
      for (int x = 0; x < aStarSearchMainObject.getBorders().size(); x ++) { //
         g.fillRect(aStarSearchMainObject.getBorders().get(x).getColumn(), aStarSearchMainObject.getBorders().get(x).getRow(), 
                    frameNodeSize-1, frameNodeSize-1);         
      } // End for-series. 
            
      // Display F cost on nodes if nodes are > 50 px in size.
      if (frameNodeSize >= 50) {
         g.setColor(Color.darkGray);
         for (int x = 0; x < sizeX; x++) {
            for(int y = 0; y < sizeY; y++) {
               // Get node to be colored.
               Node current = aStarSearchMainObject.gameBoard[x][y];
               if (current != null) {          
   			      g.drawString("F: " + Integer.toString(current.getF()), current.getColumn()*frameNodeSize, 
                               current.getRow()*frameNodeSize+frameNodeSize-3); // -26 value when stacked with G and H.
                  //g.drawString("G: " + Integer.toString(current.getG()), current.getColumn()*frameNodeSize, 
                  //             current.getRow()*frameNodeSize+frameNodeSize);
   			      //g.drawString("H: " + Integer.toString(current.getH()), current.getColumn()*frameNodeSize, 
                  //             current.getRow()*frameNodeSize+frameNodeSize-12);
                  //g.drawString(Integer.toString(x) + " " + Integer.toString(y), current.getRow()*25, current.getColumn()*25);
               }
            }
         } // End for-series
      } // End if.
   } // End method.
   
   /** 
   Method - gameBoardCalculations
   Description - Calculates nodes on the grid.
   @param e - Default Mouse Event.
   */
   public void gameBoardCalculations(MouseEvent e) { 
      // Set the gameBoard's number of nodes in the x-axis and y-axis. 
      this.sizeX = (getHeight() / frameNodeSize) + 1; 
      this.sizeY = (getWidth() / frameNodeSize) + 1;
      
      // Initialize the gameBoard[][].   
      aStarSearchMainObject.setGameBoard(sizeX, sizeY);            
      aStarSearchMainObject.initializeGrid();
      aStarSearchMainObject.initializeBorders();
      aStarSearchMainObject.initializeHeuristicH();
      
      // Repaint.
      repaint();
      
      // Check for current mouse and keyboard input. 
      if (SwingUtilities.isLeftMouseButton(e) == true) {   
         // Get mouse x and y values. Convert these values to those of the respective x and y values on the gameBoard[][].      
         int borderX = e.getX()-8 - ((e.getX()-8) % frameNodeSize);
         int borderY = e.getY()-31 - ((e.getY()-31) % frameNodeSize);     
         
         // Change the position of the nodes on the gameBoard[][] grid.
         if (currentKey == 's') {
            // Change the position of the start node.
            aStarSearchMainObject.setStartRow(borderY/frameNodeSize);
            aStarSearchMainObject.setStartColumn(borderX/frameNodeSize);
         }
         else if (currentKey == 'e'){
            // Change the position of the end node.
            aStarSearchMainObject.setEndRow(borderY/frameNodeSize);
            aStarSearchMainObject.setEndColumn(borderX/frameNodeSize);
         }                  
         else if (currentKey == 'r') {
            // Create a new border node.           
            Node newBorderNode = new Node(borderY, borderX, 1);
            
            // Remove Border Nodes.
            aStarSearchMainObject.removeBorder(newBorderNode); 
            
            // Initialize the gameBoard[][[ grid with the new nodes.
            aStarSearchMainObject.initializeGrid();
            aStarSearchMainObject.initializeBorders();
            aStarSearchMainObject.initializeHeuristicH();
            
            // Display the new gameBoard[][] grid.
            System.out.println("\nNew Grid Layout");
            aStarSearchMainObject.displayGrid(2);         
            System.out.println(borderX + " " + borderY );
            System.out.print("XY: " + e.getX()  + " " + e.getY()); 
            System.out.println(); 
         }                                    
         else { 
            // Create a new border node.           
            Node newBorderNode = new Node(borderY, borderX, 1);
            
            // If there is a border node, remove it.
            // If there is no border node, create it.
            if (aStarSearchMainObject.checkForBorderNodeDuplicate(newBorderNode) == false)            
         	   aStarSearchMainObject.addBorder(newBorderNode);         
            else if (isMouseDragged == false)
               aStarSearchMainObject.removeBorder(newBorderNode); 
            
            // Initialize the gameBoard[][[ grid with the new nodes.
            aStarSearchMainObject.initializeGrid();
            aStarSearchMainObject.initializeBorders();
            aStarSearchMainObject.initializeHeuristicH();
            
            // Display the new gameBoard[][] grid.
            System.out.println("\nNew Grid Layout");
            aStarSearchMainObject.displayGrid(2);         
            System.out.println(borderX + " " + borderY );
            System.out.print("XY: " + e.getX()  + " " + e.getY()); 
            System.out.println();  
                                     
         } // End if-series.
         
         // Repaint.
         repaint();                         			
      } 
      if (SwingUtilities.isRightMouseButton(e) == true) {  
         // Create a new timer object.
         // The timer determines the speed at which the A* is being repainted on the screen.                       
         final  Timer timer = new Timer(26, null);
         timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               // Repaint.
               repaint();
               // Run A*.
               System.out.println("Processing");
               aStarSearchMainObject.run();
               // Stop A* if finishedStatus is finished (1).
               if (aStarSearchMainObject.finishedStatus ==1 ) timer.stop();
            }         
      });            
      // Start timer.
      timer.start();
                         
      } // End if-series.
      
      // Initialize the gameBoard grid with default values.
      aStarSearchMainObject.setUpGrid();

   } // End method.   
   
   // MouseListener methods.
   @Override
	public void mouseClicked(MouseEvent e) {
      // If mouse is clicked, perform steps in gameBoardCalculations().
      gameBoardCalculations(e);
	} // End method.

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {
   }

   // MouseMotionListener methods.
	@Override
	public void mouseDragged(MouseEvent e) {
      isMouseDragged = true;
      gameBoardCalculations(e);
      isMouseDragged = false;             
   } // End method.
   
   @Override
	public void mouseMoved(MouseEvent e) {      
   }
   
   // MouseWheelListener methods.
   @Override
	public void mouseWheelMoved(MouseWheelEvent e) {
      // Mouse wheel rotation amount.
      int mouseWheelRotation = e.getWheelRotation();
      int previousFrameNodeSize = frameNodeSize;
      int scroll = 5;
      double ratio;
      
      // Change size of grid based on mouse scroll.
      if (mouseWheelRotation == 1 && frameNodeSize - scroll >= 10) 
         frameNodeSize += -scroll;
      else if (mouseWheelRotation == -1 && frameNodeSize + scroll <= 200)
         frameNodeSize += scroll;
      
      // Set ratio.
      ratio = (double)frameNodeSize / (double)previousFrameNodeSize;
      
      // Change frameNodeSize in A* method.
      aStarSearchMainObject.setFrameNodeSize(frameNodeSize);
      aStarSearchMainObject.setFrameNodeSize(frameNodeSize);
      
      // Create border nodes of the JFrame;
      for (int x = 0; x < aStarSearchMainObject.getBorders().size(); x++) { 
         // Get row and column of current border node.
         int row = aStarSearchMainObject.getBorders().get(x).getRow();
         int column = aStarSearchMainObject.getBorders().get(x).getColumn();
                  
         // Adjust row and column values of the current border node.
         row = (int)Math.round(row*ratio);
         column = (int)Math.round(column*ratio);
         
         // Set new row and column values of the current border node.
         aStarSearchMainObject.getBorders().get(x).setRow(row);
         aStarSearchMainObject.getBorders().get(x).setColumn(column);         
      } // End for-series. 
      
      // Uncomment to remove all nodes from the grid.
      //aStarSearchMainObject.getBorders().clear();
      //aStarSearchMainObject.setUpGrid();
      
      // Repaint.
      repaint();
   } // End method.
   
   // KeyListener methods.
   
   @Override
	public void keyTyped(KeyEvent e) {
      char key = e.getKeyChar();
		currentKey = key;  
      
      // Start if space is pressed
		if (currentKey == KeyEvent.VK_SPACE) {
	      aStarSearchMainObject.getBorders().clear();         
         aStarSearchMainObject.initializeBorders();
         // Initialize the gameBoard grid with default values.
         aStarSearchMainObject.setUpGrid();
         System.out.println("\nNew Grid Layout");
         aStarSearchMainObject.displayGrid(2);
         repaint();         
		}    
   } // End method.

	@Override
	public void keyPressed(KeyEvent e) {
		char key = e.getKeyChar();
		currentKey = key;  
      
      // Start if space is pressed
		if (currentKey == KeyEvent.VK_SPACE) {
	      aStarSearchMainObject.getBorders().clear();
         aStarSearchMainObject.initializeBorders();
         repaint();
		}    
   }
	
	@Override
	public void keyReleased(KeyEvent e) {
		currentKey = (char) 0;
	} // End method.
	
} // End class
   
    