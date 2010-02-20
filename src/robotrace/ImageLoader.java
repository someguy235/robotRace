/*
 * ImageLoader.java
 *
 * Created on September 8, 2006, 4:29 PM
 *
 * by Ethan Shepherd
 */

/* 
 * ImageLoader creates several arrays of images (using BufferedImageCreator) for the different
 * types of graphics in the game, and provides access to them through "get" methods. Note that
 * "dWall" and "rWall" are created by flipping the "uWall" and "lWall" images respectively.
 */
package robotrace;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class ImageLoader {
    BufferedImageCreator BIC = new BufferedImageCreator();
    Image im; 

    BufferedImage[][] diverse = new BufferedImage[6][5];
    BufferedImage[][] cBelts = new BufferedImage[5][5];
    BufferedImage[][] eBelts = new BufferedImage[5][5];
    BufferedImage[][] robos = new BufferedImage[4][8];
    BufferedImage[][] alphaRobos = new BufferedImage[4][8];
    BufferedImage[] movementCards = new BufferedImage[8];
    BufferedImage[] boards = new BufferedImage[6];
    BufferedImage dWall, rWall, boom, smallBoom;
    public ImageLoader(int squareSize) {
        File file = new File("Board01.txt");
        String path = (file.getAbsolutePath());
        path = path.substring(0, path.length()-11);
        String imageRoot = (path + "src" + file.separatorChar + "images" + file.separatorChar);
        //load the "diverse" image set
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "diverse.gif");
        
        for (int i=0; i<5; i++){
            for (int j=0; j<6; j++){
                diverse[j][i] = BIC.toBufferedImage(im, squareSize, squareSize, j*64, i*64, j*64+64, i*64+64, false);
            }
        }
        
        //rLaser
        diverse[2][2] = BIC.toBufferedImage (im, squareSize, squareSize, 128, 128, 192, 192, false);
        diverse[2][2] = BIC.toBufferedImage (diverse[2][2], squareSize, squareSize, 7, 0, squareSize+7, squareSize, false);
        //lLaser
        diverse[4][2] = BIC.toBufferedImage (im, squareSize, squareSize, 256, 128, 320, 192, false);
        diverse[4][2] = BIC.toBufferedImage (diverse[4][2], squareSize, squareSize, -7, 0, squareSize-7, squareSize, false);
        //uLaser
        diverse[3][2] = BIC.toBufferedImage (im, squareSize, squareSize, 192, 128, 256, 192, false);
        diverse[3][2] = BIC.toBufferedImage(diverse[3][2], squareSize, squareSize, 0, -6, squareSize, squareSize-6, false);
        //dLaser
        diverse[5][2] = BIC.toBufferedImage (im, squareSize, squareSize, 320, 128, 384, 192, false);
        diverse[5][2] = BIC.toBufferedImage(diverse[5][2], squareSize, squareSize, 0, 6, squareSize, squareSize+6, false);
        
        dWall = BIC.toBufferedImage(im, squareSize, squareSize, 64, 128, 128, 192, false);
        // Flip the image vertically
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -dWall.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        dWall = op.filter(dWall, null);
        
        rWall = BIC.toBufferedImage(im, squareSize, squareSize, 0, 128, 64, 192, false);
        // Flip the image horizontally
        tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-rWall.getWidth(null), 0);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        rWall = op.filter(rWall, null);
        
        //load the conveyor belt image set
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "cbelts.gif");
        for (int i=0; i<5; i++){
            for (int j=0; j<5; j++){
                cBelts[j][i] = BIC.toBufferedImage(im, squareSize, squareSize, j*64, i*64, j*64+64, i*64+64, false);
            }
        }
        
        //load the express conveyor belt image set
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "ebelts.gif");
        for (int i=0; i<5; i++){
            for (int j=0; j<5; j++){
                eBelts[j][i] = BIC.toBufferedImage(im, squareSize, squareSize, j*64, i*64, j*64+64, i*64+64, false);
            }
        }
        //load the robot image set
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "robos.gif");
        for (int i=0; i<8; i++){
            for (int j=0; j<4; j++){
                robos[j][i] = BIC.toBufferedImage(im, squareSize, squareSize, j*64, i*64, j*64+64, i*64+64, false);
            }
        }
        
        //load the semi-transparent robot image set
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "robos.gif");
        for (int i=0; i<8; i++){
            for (int j=0; j<4; j++){
                alphaRobos[j][i] = BIC.toBufferedImage(im, squareSize, squareSize, j*64, i*64, j*64+64, i*64+64, true);
            }
        }
        
        //load the movement cards image set
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "hidden.gif");
        movementCards[0] = BIC.toBufferedImage(im, squareSize, squareSize*2-10, 0, 0, 62, 102, false);
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "bu.gif");
        movementCards[1] = BIC.toBufferedImage(im, squareSize, squareSize*2-10, 0, 0, 62, 102, false);
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "m1.gif");
        movementCards[2] = BIC.toBufferedImage(im, squareSize, squareSize*2-10, 0, 0, 62, 102, false);
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "m2.gif");
        movementCards[3] = BIC.toBufferedImage(im, squareSize, squareSize*2-10, 0, 0, 62, 102, false);
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "m3.gif");
        movementCards[4] = BIC.toBufferedImage(im, squareSize, squareSize*2-10, 0, 0, 62, 102, false);
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "rl.gif");
        movementCards[5] = BIC.toBufferedImage(im, squareSize, squareSize*2-10, 0, 0, 62, 102, false);
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "rr.gif");
        movementCards[6] = BIC.toBufferedImage(im, squareSize, squareSize*2-10, 0, 0, 62, 102, false);
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "ut.gif");
        movementCards[7] = BIC.toBufferedImage(im, squareSize, squareSize*2-10, 0, 0, 62, 102, false);
        
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "boom.png");
        boom = BIC.toBufferedImage(im, squareSize, squareSize*2-10, 0, 0, 62, 102, false);
        //boom = BIC.toBufferedImage(im, squareSize, squareSize, 0, 0, 62, 24, false);
        smallBoom = BIC.toBufferedImage(im, 10, 10, 0, 0, 24, 24, false);
        
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "island.jpg");
        boards[0] = BIC.toBufferedImage(im, squareSize*5, squareSize*5, 0, 0, 541, 541, false);
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "exchange.jpg");
        boards[1] = BIC.toBufferedImage(im, squareSize*5, squareSize*5, 0, 0, 541, 541, false);
        im = Toolkit.getDefaultToolkit().getImage(imageRoot + "pitmaze.jpg");
        boards[2] = BIC.toBufferedImage(im, squareSize*5, squareSize*5, 0, 0, 541, 541, false);
        
    }
    public BufferedImage getDiverseImage(int x, int y){
        return diverse[x][y];
    }
    public BufferedImage getCBeltImage(int x, int y){
        return cBelts[x][y];
    }
    public BufferedImage getEBeltImage(int x, int y){
        return eBelts[x][y];
    }
    public BufferedImage getRobosImage(int x, int y){
        return robos[x][y];
    }
    public BufferedImage getAlphaRobosImage(int x, int y){
        return alphaRobos[x][y];
    }
    public BufferedImage getMovementCardImage(int x){
        return movementCards[x];
    }
    public BufferedImage getDWall(){
        return dWall;
    }
    public BufferedImage getRWall(){
        return rWall;
    }
    public BufferedImage getBoom(){
        return boom;
    }
    public BufferedImage getSmallBoom(){
        return smallBoom;
    }
    public BufferedImage getBoardImage(int x){
        return boards[x];
    }
    
}
