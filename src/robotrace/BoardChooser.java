/*
 * robotChooser.java
 *
 * Created on August 14, 2007, 8:18 AM
 *
 * by Ethan Shepherd
 */

package robotrace;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.*;

public class BoardChooser{
    public String chooseBoard(ImageLoader IL, int squareSize) {
        int w = squareSize*5, h = squareSize*5;
        String[] boardNames = {"Board-Island.txt", "Board-Exchange.txt", "Board-PitMaze.txt"};
        JFrame bottomFrame = new JFrame();
        bottomFrame.setSize(w*boardNames.length, h);
        bottomFrame.setLocation(200, 128);
        bottomFrame.setTitle("Choose your board...");
        bottomFrame.setResizable(false);
        
        javax.swing.JPanel bottomPanel = new javax.swing.JPanel();
        bottomPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        bottomPanel.setBackground(new java.awt.Color(100, 100, 100));
        bottomPanel.setSize(w*boardNames.length, h);
        
        bottomFrame.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        bottomFrame.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        bottomFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        bottomFrame.getContentPane().setBackground(new java.awt.Color(100, 0, 0));
        bottomFrame.getContentPane().add(bottomPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, w*boardNames.length, h));
        
        bottomFrame.pack();
        //add JPanel
        RoboSquare[] boardArray = new RoboSquare[boardNames.length];
        for (int i=0; i<boardNames.length; i++){
            boardArray[i] = new RoboSquare(w, h, true);
            boardArray[i].setBoardImage(i, IL);
            boardArray[i].setBorder(javax.swing.BorderFactory.createEtchedBorder(1, Color.BLUE, Color.BLACK));
            //boardArray[i].setBorder(javax.swing.BorderFactory.createLineBorder(Color.GRAY, 3));
            
            bottomPanel.add(boardArray[i], new org.netbeans.lib.awtextra.AbsoluteConstraints((w * i), 0, w, h));
            //bottomPanel.add(boardArray[i], new )
        }
        bottomFrame.setVisible(true);
        int chosenBoard = -1;
        while(chosenBoard < 0){
            try{Thread.currentThread().sleep(100);}
            catch(Exception e){}
            for (int i=0; i<boardNames.length; i++){
                if (boardArray[i].clicked)
                    chosenBoard = i;
            }
        }
        bottomFrame.setVisible(false);
        return boardNames[chosenBoard];   
    }
}