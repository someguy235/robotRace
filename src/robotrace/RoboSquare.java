/*
 * RoboSquare.java
 *
 * Created on September 7, 2006, 3:44 PM
 *
 * by Ethan Shepherd
 */

/*
 * The RoboSquare is the foundation for many of the RobotRace operations. It is a JPanel with
 * many added methods. The array of BufferedImages BILayers allows the board elements to be
 * displayed so that they can all be seen, with floor elements "lower" than robot elements and
 * so on. RoboSquare is also used for displaying MovementCards, thus the MouseListener to 
 * tell when cards have been selected. The class is also used to create the Reset and Ready 
 * buttons in RoboGamePanelBuilder.
 */

package robotrace;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.event.*;

public class RoboSquare extends javax.swing.JPanel implements MouseListener {
    boolean clicked = false;
    
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {
        this.clicked = true;
    }
    
    String text = null;
    BufferedImage[] BILayers = new BufferedImage[13];
    
    //floor   belt    ebelt   pusher  lwall   rwall   uwall   dwall   flag    laser  canner robots 
    int floorLayer = 0, pitLayer = 0, repairLayer = 0, rotatorLayer = 0, cBeltLayer = 1, 
            eBeltLayer = 1, pusherLayer = 2, lWallLayer = 3, rWallLayer = 4, uWallLayer = 5, 
            dWallLayer = 6, flagLayer = 7, laserLayer = 8, cannerLayer = 9, robotLayer = 10,
            smallBoomLayer = 11, boomLayer = 12;
    int squareSize;
    int textX, textY;
    int fontSize;
    int beltDirection;
    int flagInt;
    int horizLaserStart = 0, horizLaserEnd = 0, vertLaserStart = 0, vertLaserEnd = 0;
    String floor = "0", belt = "0", pusher = "0", lWall = "0", rWall = "0", 
            uWall = "0", dWall = "0", flag = "0", laser = "0", canner = "0", robot = "0";
    
    public RoboSquare(int w, int h, boolean mouseListener) {
        initComponents(w, h, mouseListener);
        this.squareSize = w;
    }
    private void initComponents(int w, int h, boolean mouseListener){
        this.setSize(w, h);
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        if (mouseListener)
            addMouseListener(this);
    }
    public void setFloor(String floorType, ImageLoader IL){
        floor = floorType;
        if (floorType.matches("blank")){
            setBILayer(IL.getDiverseImage(0, 0), floorLayer);
        }
        else if (floorType.matches("pit")){
            setBILayer(IL.getDiverseImage(3, 0), floorLayer);
        }
        else if (floorType.matches("1xRep")){
            setBILayer(IL.getDiverseImage(4, 0), repairLayer);
        }
        else if (floorType.matches("2xRep")){
            setBILayer(IL.getDiverseImage(5, 0), repairLayer);
        }
        else if (floorType.matches("cWRot")){
            setBILayer(IL.getDiverseImage(2, 0), rotatorLayer);
        }
        else if (floorType.matches("cCWRot")){
            setBILayer(IL.getDiverseImage(1, 0), rotatorLayer);
        }
    }
    public String getFloor(){
        return floor;
    }
    public void setBelt(String beltType, ImageLoader IL){
        belt = beltType;
        if (beltType.startsWith("e")){
            if (beltType.endsWith("U")){ //Upward pointing belts
                if (beltType.matches("eBeltU")){
                    setBILayer(IL.getEBeltImage(4, 2), eBeltLayer); 
                    beltDirection = 0;
                }
                else if (beltType.matches("eBeltRtoU")){
                    setBILayer(IL.getEBeltImage(1, 1), eBeltLayer); 
                    beltDirection = 0;
                }                    
                else if (beltType.matches("eBeltLtoU")){
                    setBILayer(IL.getEBeltImage(2, 1), eBeltLayer); 
                    beltDirection = 0;
                }                    
                else if (beltType.matches("eBeltURtoU")){
                    setBILayer(IL.getEBeltImage(0, 3), eBeltLayer); 
                    beltDirection = 0;
                }                    
                else if (beltType.matches("eBeltULtoU")){
                    setBILayer(IL.getEBeltImage(3, 3), eBeltLayer); 
                    beltDirection = 0;
                }                    
                else if (beltType.matches("eBeltRDLtoU")){
                    setBILayer(IL.getEBeltImage(2, 4), eBeltLayer); 
                    beltDirection = 3;
                }                            
            }
            else if (beltType.endsWith("R")){ //Rightward pointing belts
                if (beltType.matches("eBeltR")){
                    setBILayer(IL.getEBeltImage(4, 3), eBeltLayer); 
                    beltDirection = 1;
                }
                else if (beltType.matches("eBeltUtoR")){
                    setBILayer(IL.getEBeltImage(2, 0), eBeltLayer); 
                    beltDirection = 1;
                } 
                else if (beltType.matches("eBeltDtoR")){
                    setBILayer(IL.getEBeltImage(0, 1), eBeltLayer); 
                    beltDirection = 1;
                }                     
                else if (beltType.matches("eBeltURtoR")){
                    setBILayer(IL.getEBeltImage(2, 2), eBeltLayer); 
                    beltDirection = 1;
                }                     
                else if (beltType.matches("eBeltRDtoR")){
                    setBILayer(IL.getEBeltImage(1, 3), eBeltLayer); 
                    beltDirection = 1;
                }                    
                else if (beltType.matches("eBeltUDLtoR")){
                    setBILayer(IL.getEBeltImage(3, 4), eBeltLayer); 
                    beltDirection = 1;
                }                            
            }
            else if (beltType.endsWith("D")){ //Downward pointing belts
                if (beltType.matches("eBeltD")){
                    setBILayer(IL.getEBeltImage(4, 0), eBeltLayer); 
                    beltDirection = 2;
                }
                else if (beltType.matches("eBeltLtoD")){
                    setBILayer(IL.getEBeltImage(0, 0), eBeltLayer); 
                    beltDirection = 2;
                }
                else if (beltType.matches("eBeltRtoD")){
                    setBILayer(IL.getEBeltImage(3, 0), eBeltLayer); 
                    beltDirection = 2;
                }                    
                else if (beltType.matches("eBeltDLtoD")){
                    setBILayer(IL.getEBeltImage(0, 2), eBeltLayer); 
                    beltDirection = 2;
                }                
                else if (beltType.matches("eBeltRDtoD")){
                    setBILayer(IL.getEBeltImage(3, 2), eBeltLayer); 
                    beltDirection = 2;
                }                    
                else if (beltType.matches("eBeltRDLtoD")){
                    setBILayer(IL.getEBeltImage(0, 4), eBeltLayer); 
                    beltDirection = 2;
                }                    
            }
            else if (beltType.endsWith("L")){ //Leftward pointing belts
                if (beltType.matches("eBeltL")){
                    setBILayer(IL.getEBeltImage(4, 1), eBeltLayer); 
                    beltDirection = 3;
                }
                else if (beltType.matches("eBeltUtoL")){
                    setBILayer(IL.getEBeltImage(1, 0), eBeltLayer); 
                    beltDirection = 3;
                }                
                else if (beltType.matches("eBeltDtoL")){
                    setBILayer(IL.getEBeltImage(3, 1), eBeltLayer); 
                    beltDirection = 3;
                }                    
                else if (beltType.matches("eBeltULtoL")){
                    setBILayer(IL.getEBeltImage(1, 2), eBeltLayer); 
                    beltDirection = 3;
                }                            
                else if (beltType.matches("eBeltDLtoL")){
                    setBILayer(IL.getEBeltImage(2, 3), eBeltLayer); 
                    beltDirection = 3;
                }                    
                else if (beltType.matches("eBeltURDtoL")){
                    setBILayer(IL.getEBeltImage(1, 4), eBeltLayer); 
                    beltDirection = 3;
                }                    
            }
        }
        //***Regular converyor belts***
        else if (beltType.startsWith("c")){
            if (beltType.endsWith("U")){ //Upward pointing belts
                if (beltType.matches("cBeltU")){
                    setBILayer(IL.getCBeltImage(4, 2), cBeltLayer); 
                    beltDirection = 0;
                }
                else if (beltType.matches("cBeltRtoU")){
                    setBILayer(IL.getCBeltImage(1, 1), cBeltLayer); 
                    beltDirection = 0;
                }                    
                else if (beltType.matches("cBeltLtoU")){
                    setBILayer(IL.getCBeltImage(2, 1), cBeltLayer); 
                    beltDirection = 0;
                }                    
                else if (beltType.matches("cBeltURtoU")){
                    setBILayer(IL.getCBeltImage(0, 3), cBeltLayer); 
                    beltDirection = 0;
                }                    
                else if (beltType.matches("cBeltULtoU")){
                    setBILayer(IL.getCBeltImage(3, 3), cBeltLayer); 
                    beltDirection = 0;
                }                    
                else if (beltType.matches("cBeltRDLtoU")){
                    setBILayer(IL.getCBeltImage(2, 4), cBeltLayer); 
                    beltDirection = 3;
                }                            
            }
            else if (beltType.endsWith("R")){ //Rightward pointing belts
                if (beltType.matches("cBeltR")){
                    setBILayer(IL.getCBeltImage(4, 3), cBeltLayer); 
                    beltDirection = 1;
                }
                else if (beltType.matches("cBeltUtoR")){
                    setBILayer(IL.getCBeltImage(2, 0), cBeltLayer); 
                    beltDirection = 1;
                } 
                else if (beltType.matches("cBeltDtoR")){
                    setBILayer(IL.getCBeltImage(0, 1), cBeltLayer); 
                    beltDirection = 1;
                }                     
                else if (beltType.matches("cBeltURtoR")){
                    setBILayer(IL.getCBeltImage(2, 2), cBeltLayer); 
                    beltDirection = 1;
                }                     
                else if (beltType.matches("cBeltRDtoR")){
                    setBILayer(IL.getCBeltImage(1, 3), cBeltLayer); 
                    beltDirection = 1;
                }                    
                else if (beltType.matches("cBeltUDLtoR")){
                    setBILayer(IL.getCBeltImage(3, 4), cBeltLayer); 
                    beltDirection = 1;
                }                            
            }
            else if (beltType.endsWith("D")){ //Downward pointing belts
                if (beltType.matches("cBeltD")){
                    setBILayer(IL.getCBeltImage(4, 0), cBeltLayer); 
                    beltDirection = 2;
                }
                else if (beltType.matches("cBeltLtoD")){
                    setBILayer(IL.getCBeltImage(0, 0), cBeltLayer); 
                    beltDirection = 2;
                }
                else if (beltType.matches("cBeltRtoD")){
                    setBILayer(IL.getCBeltImage(3, 0), cBeltLayer); 
                    beltDirection = 2;
                }                    
                else if (beltType.matches("cBeltDLtoD")){
                    setBILayer(IL.getCBeltImage(0, 2), cBeltLayer); 
                    beltDirection = 2;
                }                
                else if (beltType.matches("cBeltRDtoD")){
                    setBILayer(IL.getCBeltImage(3, 2), cBeltLayer); 
                    beltDirection = 2;
                }                    
                else if (beltType.matches("cBeltRDLtoD")){
                    setBILayer(IL.getCBeltImage(0, 4), cBeltLayer); 
                    beltDirection = 2;
                }                    
            }
            else if (beltType.endsWith("L")){ //Leftward pointing belts
                if (beltType.matches("cBeltL")){
                    setBILayer(IL.getCBeltImage(4, 1), cBeltLayer); 
                    beltDirection = 3;
                }
                else if (beltType.matches("cBeltUtoL")){
                    setBILayer(IL.getCBeltImage(1, 0), cBeltLayer); 
                    beltDirection = 3;
                }                
                else if (beltType.matches("cBeltDtoL")){
                    setBILayer(IL.getCBeltImage(3, 1), cBeltLayer); 
                    beltDirection = 3;
                }                    
                else if (beltType.matches("cBeltULtoL")){
                    setBILayer(IL.getCBeltImage(1, 2), cBeltLayer); 
                    beltDirection = 3;
                }                            
                else if (beltType.matches("cBeltDLtoL")){
                    setBILayer(IL.getCBeltImage(2, 3), cBeltLayer); 
                    beltDirection = 3;
                }                    
                else if (beltType.matches("cBeltURDtoL")){
                    setBILayer(IL.getCBeltImage(1, 4), cBeltLayer); 
                    beltDirection = 3;
                }                    
            }
        }
    }
    public String getBelt(){
        return belt;
    }
    public int getBeltDirection(){
        return beltDirection;
    }
    public void setPusher(String pusherType, ImageLoader IL){
        pusher = pusherType;
        if (pusherType.matches("pushUp"))
            setBILayer(IL.getDiverseImage(2, 1), pusherLayer);
        else if (pusherType.matches("pushDown"))
            setBILayer(IL.getDiverseImage(1, 1), pusherLayer);
        else if (pusherType.matches("pushLeft"))
            setBILayer(IL.getDiverseImage(0, 1), pusherLayer);
        else if (pusherType.matches("pushRight"))
            setBILayer(IL.getDiverseImage(3, 1), pusherLayer);
    }
    public String getPusher(){
        return pusher;
    }
    public void setWall(String wallType, ImageLoader IL){
        if (wallType.matches("uWall")){
            uWall = wallType;
            setBILayer(IL.getDiverseImage(1, 2), uWallLayer);
        }
        else if (wallType.matches("dWall")){
            dWall = wallType;
            setBILayer(IL.getDWall(), dWallLayer);
        }
        else if (wallType.matches("rWall")){
            rWall = wallType;
            setBILayer(IL.getRWall(), rWallLayer);
        }
        else if (wallType.matches("lWall")){
            lWall = wallType;
            setBILayer(IL.getDiverseImage(0, 2), lWallLayer);
        }
    }
    public String getLWall(){
        return lWall;
    }
    public String getRWall(){
        return rWall;
    }
    public String getDWall(){
        return dWall;
    }
    public String getUWall(){
        return uWall;
    }
    public void setFlag(String flagNumber, ImageLoader IL){
        flag = flagNumber;
        flagNumber = (""+ (flagNumber.charAt(4)));
        flagInt = Integer.valueOf(flagNumber);
        setBILayer(IL.getDiverseImage(Integer.valueOf(flagNumber)-1, 3), flagLayer);
    }
    public String getFlag(){
        return flag;
    }
    public int getFlagNumber(){
        return flagInt;
    }
    public void setLaser(String laserType, ImageLoader IL){
        if (laserType.matches("uLaser")){
            laser = laserType;
            setBILayer(IL.getDiverseImage(3, 2), laserLayer);
        }
        else if (laserType.matches("dLaser")){
            laser = laserType;
            setBILayer(IL.getDiverseImage(5, 2), laserLayer);
        }
        else if (laserType.matches("rLaser")){
            laser = laserType;
            setBILayer(IL.getDiverseImage(2, 2), laserLayer);
        }
        else if (laserType.matches("lLaser")){
            laser = laserType;
            setBILayer(IL.getDiverseImage(4, 2), laserLayer);
        }
    }
    public String getLaser(){
        return laser;
    }
    
    //the following four methods allow the drawing of lasers for when a robot fires
    //on another robot. Calculations are performed in GameController to determine these
    //values, and the local paintComponent method looks at these values and draws a red
    //line accordingly.
    public void setHorizLaserStart(int x1){
        horizLaserStart = x1;
        super.repaint();
    }
    public void setHorizLaserEnd(int x2){
        horizLaserEnd = x2;
        super.repaint();
    }
    public void setVertLaserStart(int x1){
        vertLaserStart = x1;
        super.repaint();
    }
    public void setVertLaserEnd (int x2){
        vertLaserEnd = x2;
        super.repaint();
    }
    public void setCanner(String cannerType, ImageLoader IL){
        canner = cannerType;
        setBILayer(IL.getDiverseImage(4, 1), cannerLayer);
    }
    public String getCanner(){
        return canner;
    }
    public void setBoardImage(int boardType, ImageLoader IL){
        setBILayer(IL.getBoardImage(boardType), 0);
    }
    public void setRobot(int facing, int roboType, ImageLoader IL){
        setBILayer(IL.getRobosImage(facing, roboType), robotLayer);
        robot = "true";
    }
    public void setAlphaRobot(int facing, int roboType, ImageLoader IL){
        setBILayer(IL.getAlphaRobosImage(facing, roboType), robotLayer);
        robot = "true";
    }
    public void removeRobot(){
        removeBILayer(robotLayer);
        robot = "false";
    }
    public void boom(ImageLoader IL){
        setBILayer(IL.getBoom(), boomLayer);
    }
    public void smallBoom(ImageLoader IL){
        setBILayer(IL.getSmallBoom(), smallBoomLayer);
    }
    public void removeBoom(){
        removeBILayer(boomLayer);
    }
    public void removeSmallBoom(){
        removeBILayer(smallBoomLayer);
    }
    
    //remove all images from the square, but add the empty floor image
    public void clear(ImageLoader IL){
        for (int i=0; i<BILayers.length-1; i++)
            removeBILayer(i);
        setBILayer(IL.getDiverseImage(0, 0), floorLayer);
    }
    
    public void setBILayer(BufferedImage BI, int layerNum){
        BILayers[layerNum] = BI;
        this.repaint();
    }
    public void removeBILayer (int layerNum){
        BILayers[layerNum] = null;
        this.repaint();
    }
    public void setText (String t, int textX, int textY, int font){
        text = t;
        this.textX = textX;
        this.textY = textY;
        this.fontSize = font;
        this.repaint();
    }
    public void resetClicked(){
        this.clicked = false;
    }
    
    //removes the laser line, called after a robot fires
    public void resetLasers(){
        horizLaserStart = 0;
        vertLaserStart = 0;
        horizLaserEnd = 0;
        vertLaserEnd = 0;
        super.repaint();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        for (int i=0; i<BILayers.length-1; i++)
                g2.drawImage(BILayers[i], null, 0, 0);
        //this is the 'boom' layer, centered and not taking up the whole square
        g2.drawImage(BILayers[BILayers.length-1], null, 15, 12);
        if (text != null){
            g.setFont (new Font ("TimeNewRoman",Font.PLAIN, fontSize));
            g.drawString(text, textX, textY);    
        }
        if (!((horizLaserStart == 0)&&(horizLaserEnd == 0))){
            g2.setColor(Color.RED);
            g2.drawLine(horizLaserStart, squareSize/2, horizLaserEnd, squareSize/2);
            
        }
        if (!((vertLaserStart == 0)&&(vertLaserEnd == 0))){
            g2.setColor(Color.RED);
            g2.drawLine(squareSize/2, vertLaserStart, squareSize/2, vertLaserEnd);
        }
        
    }
}//class RoboSquare
