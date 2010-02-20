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

public class RobotChooser{
    public int chooseRobot(ImageLoader IL, int squareSize, int player) {
        int w = squareSize*8, h = squareSize*1;
        JFrame bottomFrame = new JFrame();
        bottomFrame.setSize(w, h);
        bottomFrame.setLocation(300, 128);
        bottomFrame.setTitle("Player " + player + " choose your robot...");
        bottomFrame.setResizable(false);
        
        javax.swing.JPanel bottomPanel = new javax.swing.JPanel();
        bottomPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        bottomPanel.setBackground(new java.awt.Color(100, 100, 100));
        bottomPanel.setSize(w, h);
        
        bottomFrame.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        bottomFrame.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        bottomFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        bottomFrame.getContentPane().setBackground(new java.awt.Color(100, 0, 0));
        bottomFrame.getContentPane().add(bottomPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, w, h));
        
        bottomFrame.pack();
        //add JPanel
        RoboSquare[] roboArray = new RoboSquare[8];
        for (int i=0; i<8; i++){
            roboArray[i] = new RoboSquare(squareSize, squareSize, true);
            roboArray[i].setFloor("blank", IL);
            roboArray[i].setRobot(0, i, IL);
            bottomPanel.add(roboArray[i], new org.netbeans.lib.awtextra.AbsoluteConstraints((squareSize * i), 0, squareSize, squareSize));
        }
        bottomFrame.setVisible(true);
        int chosenOne = -1;
        while(chosenOne < 0){
            for (int j=0; j<4; j++){
                if (chosenOne <0){
                    for (int k=0; k<10; k++){
                        try{Thread.currentThread().sleep(100);}
                        catch(Exception e){}
                        for (int i=0; i<8; i++){
                            if (roboArray[i].clicked){
                                chosenOne = i;
                                roboArray[i].boom(IL);
                                try{Thread.currentThread().sleep(200);}
                                catch(Exception e){}
                                break;
                            }
                        }
                    }
                }
                if (chosenOne<0){
                    for (int i=0; i<8; i++){
                        roboArray[i].setRobot(j, i, IL);
                    }
                }
            }
        }
        bottomFrame.setVisible(false);
        return chosenOne;   
    }
}