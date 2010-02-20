/*
 * RoboGamePanelBuilder.java
 *
 * Created on September 15, 2006, 12:42 PM
 *
 * by Ethan Shepherd
 */

/*
 * RoboGamePanelBuilder simply creates a JFrame holder, and adds the elements passed to it, in
 * this case the two PlayerInfoPanels and the CurrentCardPool.
 */

package robotrace;
import javax.swing.*;

public class RoboGamePanelBuilder {
    
    public JFrame CreateRoboGamePanel(ImageLoader IL, int squareSize,  PlayerInfoPanel player1Panel,
            PlayerInfoPanel player2Panel, CurrentCardPool playerHand){
        int w = squareSize*9, h = squareSize*12;
        JFrame newBoard = new JFrame();
        newBoard.setSize(w+7, h-125);
        newBoard.setLocation(25, 128);
        newBoard.setTitle("Robot Race Control Panel");
        newBoard.setResizable(false);
        
        javax.swing.JPanel BoardBottomLvl = new javax.swing.JPanel();
        BoardBottomLvl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        BoardBottomLvl.setBackground(new java.awt.Color(100, 100, 100));
        BoardBottomLvl.setSize(w, h);
        
        newBoard.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        newBoard.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        newBoard.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        newBoard.getContentPane().setBackground(new java.awt.Color(100, 0, 0));
        newBoard.getContentPane().add(BoardBottomLvl, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, w, h-125));
        
        newBoard.pack();
        
        BoardBottomLvl.add(player1Panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, w, h/5));
        BoardBottomLvl.add(player2Panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, h/5, w, h/5));
        BoardBottomLvl.add(playerHand, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 2*h/5, w, h/2-70));
        newBoard.setVisible(true);
        return newBoard;
        
    }
    
}