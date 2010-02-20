/*
 * CurrentCardPool.java
 *
 * Created on October 13, 2006, 10:30 AM
 *
 * by Ethan Shepherd
 */

/*
 * CurrentCardPool keeps track of what cards have been drawn from the deck for the player
 * to choose from this turn, and displays them in a JFrame. 
 * The class contains two buttons implemented as RoboSquares: one to allow the player to reset 
 * his card choices, and one to progress the game between turns and after each register has 
 * played out.
 */

package robotrace;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.*;

public class CurrentCardPool extends javax.swing.JPanel{
    private RoboSquare[] cardLocs = new RoboSquare[9];
    private MovementCard[] cards = new MovementCard[9];
    int squareSize;
    MovementCard hidden = new MovementCard(0, 0, "Hidden");
    RoboSquare Reset, Ready;
    
    public CurrentCardPool(int squareSize, ImageLoader IL) {
        this.squareSize = squareSize;
        initComponents(squareSize, IL);
    }
    
    private void initComponents(int squareSize, ImageLoader IL){
        this.setSize(squareSize*4, squareSize*3);
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.setBackground(new java.awt.Color(200, 200, 200));
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Player Hand"));
        
        //set up the pool with all blank cards, in two uneven rows
        for (int i=0; i<5; i++){
            cardLocs[i] = new RoboSquare(squareSize, squareSize*2-10, true);
            cardLocs[i].setBILayer(IL.getMovementCardImage(hidden.getIndex()), 0);
            cardLocs[i].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
            this.add(cardLocs[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(((squareSize+5)*i)+15, squareSize/2, squareSize, squareSize*2-10));
        }
        for (int i=0; i<4; i++){
            cardLocs[i+5] = new RoboSquare(squareSize, squareSize*2-10, true);
            cardLocs[i+5].setBILayer(IL.getMovementCardImage(hidden.getIndex()), 0);
            cardLocs[i+5].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
            this.add(cardLocs[i+5], new org.netbeans.lib.awtextra.AbsoluteConstraints(((squareSize+5)*i)+15+squareSize/2, squareSize/2+squareSize*2, squareSize, squareSize*2-10));
        }
        
        //build the Reset "button"
        Reset = new RoboSquare(squareSize*3/2, squareSize/2, true);
        Reset.setText("Reset", 14, 16, 14);
        Reset.setBorder(javax.swing.BorderFactory.createBevelBorder(0));
        Reset.setBackground(new java.awt.Color(150, 150, 150));
        this.add(Reset, new org.netbeans.lib.awtextra.AbsoluteConstraints(squareSize*13/2, squareSize*3/2+15, squareSize*3/2, squareSize/2));
        
        //build the ready "button"
        Ready = new RoboSquare(squareSize*7/2, squareSize/2, true);
        Ready.setBorder(javax.swing.BorderFactory.createBevelBorder(0));
        Ready.setBackground(new java.awt.Color(150, 150, 150));
        this.add(Ready, new org.netbeans.lib.awtextra.AbsoluteConstraints(squareSize*10/2+15, squareSize*5/2, squareSize*7/2, squareSize/2));
        
    }
    
    public void setCard(MovementCard card, int cardLocation, ImageLoader IL){
        cards[cardLocation] = card;
        cardLocs[cardLocation].setBILayer(IL.getMovementCardImage(card.getIndex()), 0);
        cardLocs[cardLocation].setText(""+card.getCardSpeed(), squareSize/3, squareSize/3, squareSize/5);
    }
    
    public MovementCard getCard(int cardLoc){
        return cards[cardLoc];
    }
    
    public RoboSquare getCardLoc(int cardLocIndex){
        return cardLocs[cardLocIndex];
    }
    
    public RoboSquare getReset(){
        return Reset;
    }
    
    public RoboSquare getReady(){
        return Ready;
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }
}
