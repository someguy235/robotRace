/*
 * PlayerInfoPanel.java
 *
 * Created on September 15, 2006, 1:20 PM
 *
 * by Ethan Shepherd
 */

/*
 * PlayerInfoPanel is the visual representaion of each player's information. Main creates
 * one panel for each player and they are added to the GamePanel JFrame. The initialization
 * makes five locations for card images (and borders to indicate locked registers), an image
 * of the players robot which updates to correspond to the location on the board, and indicators
 * of x and y coordinates, last flag tagged, and damage sustained. Methods are provided for
 * the updating of each of these components during the course of the game.
 */

package robotrace;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.*;

public class PlayerInfoPanel extends javax.swing.JPanel{
    RoboSquare[] cards = new RoboSquare[5];
    RoboSquare[] roboDamageMarker = new RoboSquare[9];
        
    
    RoboSquare roboIcon, roboXLoc, roboYLoc, roboDamage, roboLastFlag;
    int squareSize;
    
    public PlayerInfoPanel(int squareSize, String player, int robotStyle, ImageLoader IL) {
        this.squareSize = squareSize;
        initComponents(squareSize, player, robotStyle, IL);
    }
    
    private void initComponents(int squareSize, String panelName, int robotStyle, ImageLoader IL){
        this.setSize(squareSize*9, squareSize*3-10);
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.setBackground(new java.awt.Color(200, 200, 200));
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), panelName));
        
        // 5 locations for card images, initialized to the "blank" image
        for (int i=0; i<5; i++){
            cards[i] = new RoboSquare(squareSize, squareSize*2-10, true);
            setCardImage(i, 0, IL);
            cards[i].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
            this.add(cards[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(((squareSize+5)*i)+15, (squareSize/2), squareSize, squareSize*2-10));
        }
        
        // image of the player's robot, corresponding to the location on the board
        roboIcon = new RoboSquare(squareSize, squareSize, false);
        roboIcon.setBackground(new java.awt.Color(100, 100, 100));
        roboIcon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        setRoboIcon(robotStyle, 0, IL);
        this.add(roboIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(((squareSize)*7)+30, (squareSize/2+squareSize-10), squareSize, squareSize));
        
        // x coordinate location
        roboXLoc = new RoboSquare(squareSize+5, squareSize, false);
        roboXLoc.setBackground(new java.awt.Color(200, 200, 200));
        setLocText('x', 0);
        this.add(roboXLoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(((squareSize+5)*5)+20, (squareSize/2+squareSize-5), squareSize+5, squareSize/2));
        
        // y coordinate location
        roboYLoc = new RoboSquare(squareSize+5, squareSize, false);
        roboYLoc.setBackground(new java.awt.Color(200, 200, 200));
        setLocText('y', 0);
        this.add(roboYLoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(((squareSize+5)*5)+20, squareSize*2-10, squareSize+5, squareSize/2));
        
        roboDamage = new RoboSquare (squareSize, squareSize, false);
        roboDamage.setBackground(new java.awt.Color(200, 200, 200));
        setDamage(0);
        //this.add(roboDamage, new org.netbeans.lib.awtextra.AbsoluteConstraints(((squareSize+5)*5)+20, squareSize/2, squareSize*2, squareSize/2));
        this.add(roboDamage, new org.netbeans.lib.awtextra.AbsoluteConstraints(((squareSize+5)*5)+20, squareSize/2, squareSize+15, squareSize/2));
        
        for (int i=0; i<5; i++){
            roboDamageMarker[i] = new RoboSquare (10, 10, false);
            roboDamageMarker[i].setBackground(new java.awt.Color(0, 150, 0));
            roboDamageMarker[i].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 1));
            this.add(roboDamageMarker[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(((squareSize+5)*5)+80+(10*i), squareSize/2+2, 10, 10));
        }
        for (int i=0; i<4; i++){
            roboDamageMarker[i+5] = new RoboSquare (10, 10, false);
            roboDamageMarker[i+5].setBackground(new java.awt.Color(0, 150, 0));
            roboDamageMarker[i+5].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 1));
            this.add(roboDamageMarker[i+5], new org.netbeans.lib.awtextra.AbsoluteConstraints(((squareSize+5)*5)+85+(10*i), squareSize/2+12, 10, 10));
        }
        
        roboLastFlag = new RoboSquare (squareSize, squareSize, false);
        roboLastFlag.setBackground(new java.awt.Color(200, 200, 200));
        setFlag(0);
        //this.add(roboLastFlag, new org.netbeans.lib.awtextra.AbsoluteConstraints(((squareSize+5)*5)+20, squareSize-5, squareSize*2, squareSize/2));
        this.add(roboLastFlag, new org.netbeans.lib.awtextra.AbsoluteConstraints(((squareSize+5)*5)+20, squareSize-5, squareSize+18, squareSize/2));
        this.setVisible(true);
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }
    public void setCardImage(int cardNum, int imgNum, ImageLoader IL){
        cards[cardNum].setBILayer(IL.getMovementCardImage(imgNum), 0);
    }
    
    // adds the speed values to the card images
    public void setCardText(int cardNum, String text){
        cards[cardNum].setText(text, squareSize/3, squareSize/3+1, squareSize/5);
    }
    
    // black by default, red when locked (done by GameController)
    public void setCardBorder(int cardNum, int r, int g, int b){
        cards[cardNum].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(r, g, b), 2));
    }
    public void setLocText(char xy, int loc){
        if (xy=='x'){
            roboXLoc.setText("XLoc: " + loc, squareSize/9, squareSize/3, squareSize/5+1);
        }
        if (xy=='y'){
            roboYLoc.setText("YLoc: " + loc, squareSize/9, squareSize/3, squareSize/5+1);
        }
    }
    public void setRoboIcon(int roboNum, int facing, ImageLoader IL){
        roboIcon.setBILayer(IL.getAlphaRobosImage(facing, roboNum), 0);
    }
    public void setDamage(int damage){
        roboDamage.setText("Damage: " + damage, squareSize/9, squareSize/3, squareSize/5+1);
    }
    public void addDamageMarker(int i, ImageLoader IL){
        roboDamageMarker[i].smallBoom(IL);
    }
    public void removeDamageMarker (int i){
        roboDamageMarker[i].removeSmallBoom();
    }
    //to do (this displays overlap when a flag is tagged)
    public void setFlag(int flag){
        roboLastFlag.setText("Last Flag: " + flag, squareSize/9, squareSize/3, squareSize/5+1);
        //roboLastFlag.setText("Last Flag: " + flag, )
    }
}
