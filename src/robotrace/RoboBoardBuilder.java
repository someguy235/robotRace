/*
 * RoboBoardBuilder.java
 *
 * Created on September 18, 2006, 4:12 PM
 *
 * by Ethan Shepherd
 */

/* 
 * RoboBoardBuilder creates an array of RoboSquares (12x12, one for each square of the game 
 * board), and adds the game elements to them as indicated in the "Board01.txt" configuration 
 * file. These squares are added to a JFrame to create the game board. This system allows
 * the game board to be changed simply by changing an entry in the txt file, and makes choosing
 * among different boards quite simple.
 */

package robotrace;

import java.io.*;
import java.io.Reader;
import java.util.StringTokenizer;

public class RoboBoardBuilder {
    //non-implemented 'boardType' would allow for choosing from different boards
    public RoboSquare[][] BuildRoboBoard(ImageLoader IL, String boardType, int squareSize) {
        //boardType = "Board-Exchange.txt";
        //boardType = "Board-Island.txt";
        //boardType = "Board-PitMaze.txt";
        //createSquares...12x12
        RoboSquare[][] RoboSquareArray = new RoboSquare[12][12];
        
        //create board frame
        int boardSize = squareSize*12;
        javax.swing.JFrame newBoard = new javax.swing.JFrame();
        javax.swing.JPanel BoardBottomLvl = new javax.swing.JPanel();
        BoardBottomLvl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        BoardBottomLvl.setBackground(new java.awt.Color(100, 100, 100));
        BoardBottomLvl.setSize(boardSize, boardSize);
        
        newBoard.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        newBoard.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        newBoard.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        newBoard.getContentPane().setBackground(new java.awt.Color(100, 0, 0));
        newBoard.getContentPane().add(BoardBottomLvl, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, boardSize, boardSize));
        
        newBoard.pack();
        
        //to do: automagically determine platform
        //Windows...
        //newBoard.setSize(boardSize+6, boardSize+32);
        //OSX...
        newBoard.setSize(boardSize+5, boardSize+22);
        
        newBoard.setLocation(450, 128);
        newBoard.setTitle("Robot Race Board");
        newBoard.setResizable(false);
        
        //fill all squares with the default blank floor
        for (int i=0; i<=11; i++){
            for (int j=0; j<=11; j++){
                RoboSquareArray[i][j] = new RoboSquare(squareSize, squareSize, false);
                BoardBottomLvl.add(RoboSquareArray[i][j], new org.netbeans.lib.awtextra.AbsoluteConstraints((squareSize*i), (squareSize*j), squareSize, squareSize));
            }
        }
        newBoard.setVisible(true);
        
        FileInputStream fin;
        DataInputStream dis;
        //String x = "x", y = "x", xloc = "x", yloc = "x";
        for (int k=0; k<12; k++){
                for (int j=0; j<12; j++){
                    RoboSquareArray[j][k].setBILayer(IL.getDiverseImage(0, 0), 0);
                }
        }
        try{
            // Open an input stream
            File file = new File(boardType);
            // not sure why this convolution is necessary, but is seems to be.
            // Trying to create the FileInputStream from the File above doesn't work
            String path = (file.getAbsolutePath());
            path = path.substring(0, path.length()-boardType.length());
            path = (path + "src" + file.separatorChar + "robotrace" + file.separatorChar);
            fin = new FileInputStream (path + "" + boardType);
            dis = new DataInputStream(fin);
            int xloc, yloc;
            String lineToken;
            String line = dis.readLine();
            StringTokenizer st = new StringTokenizer(line);
            while ((line = dis.readLine()) != null){
                st = new StringTokenizer(line);
                xloc = Integer.valueOf(st.nextToken());
                yloc = Integer.valueOf(st.nextToken());
                RoboSquare currSquare = RoboSquareArray[xloc][yloc];
                lineToken = st.nextToken();
                
                //iterate through the tokens in the line, processing each
                while(!(lineToken.matches(";"))){
                    if (lineToken.matches("pit")|lineToken.matches("1xRep")|lineToken.matches("2xRep")
                            |lineToken.matches("cWRot")|lineToken.matches("cCWRot")){
                        currSquare.setFloor(lineToken, IL);
                    }
                    else if (lineToken.startsWith("eBelt")|lineToken.startsWith("cBelt")){
                        currSquare.setBelt(lineToken, IL);
                    }
                    else if (lineToken.startsWith("push")){
                        currSquare.setPusher(lineToken, IL);
                    }
                    else if (lineToken.contains("Wall")){
                        currSquare.setWall(lineToken, IL);
                    }
                    else if (lineToken.contains("flag")){
                        currSquare.setFlag(lineToken, IL);
                    }
                    else if (lineToken.contains("Laser")){
                        currSquare.setLaser(lineToken, IL);
                        RoboSquare tempCurrSquare = RoboSquareArray[xloc][yloc];
                        int done = 0, currX = xloc, currY = yloc;
                                
                        if (lineToken.matches("rLaser")){
                            tempCurrSquare.setHorizLaserEnd(squareSize-18);
                            if (tempCurrSquare.getLWall().matches("lWall"))
                                tempCurrSquare.setHorizLaserStart(8);
                            else
                                tempCurrSquare.setHorizLaserStart(0);
                            while (done == 0){
                                tempCurrSquare = RoboSquareArray[--currX][currY];
                                if (tempCurrSquare.getRWall().matches("rWall"))
                                    done = 1;
                                else if (tempCurrSquare.getLWall().matches("lWall")){
                                    tempCurrSquare.setHorizLaserStart(8);
                                    tempCurrSquare.setHorizLaserEnd(squareSize);
                                    done = 1;
                                }
                                else {
                                    tempCurrSquare.setHorizLaserStart(0);
                                    tempCurrSquare.setHorizLaserEnd(squareSize);
                                }
                                
                            }
                        }//if (lineToken.matches("rLaser")){
                        if (lineToken.matches("lLaser")){
                            tempCurrSquare.setHorizLaserStart(18);
                            if (tempCurrSquare.getRWall().matches("rWall"))
                                tempCurrSquare.setHorizLaserEnd(squareSize-8);
                            else
                                tempCurrSquare.setHorizLaserEnd(squareSize);
                            while (done == 0){
                                tempCurrSquare = RoboSquareArray[++currX][currY];
                                if (tempCurrSquare.getLWall().matches("lWall"))
                                    done = 1;
                                else if (tempCurrSquare.getRWall().matches("rWall")){
                                    tempCurrSquare.setHorizLaserStart(0);
                                    tempCurrSquare.setHorizLaserEnd(squareSize-8);
                                    done = 1;
                                }
                                else {
                                    tempCurrSquare.setHorizLaserStart(0);
                                    tempCurrSquare.setHorizLaserEnd(squareSize);
                                }
                                
                            }
                        }//if (lineToken.matches("lLaser")){
                        if (lineToken.matches("uLaser")){
                            tempCurrSquare.setVertLaserStart(16);
                            if (tempCurrSquare.getDWall().matches("dWall"))
                                tempCurrSquare.setVertLaserEnd(squareSize-8);
                            else
                                tempCurrSquare.setVertLaserEnd(squareSize);
                            while (done == 0){
                                tempCurrSquare = RoboSquareArray[currX][++currY];
                                if (tempCurrSquare.getUWall().matches("uWall"))
                                    done = 1;
                                else if (tempCurrSquare.getDWall().matches("dWall")){
                                    tempCurrSquare.setVertLaserStart(0);
                                    tempCurrSquare.setVertLaserEnd(squareSize-8);
                                    done = 1;
                                }
                                else {
                                    tempCurrSquare.setVertLaserStart(squareSize);
                                    tempCurrSquare.setVertLaserEnd(0);
                                }
                                
                            }
                        }//if (lineToken.matches("uLaser")){
                        if (lineToken.matches("dLaser")){
                            tempCurrSquare.setVertLaserEnd(squareSize-16);
                            if (tempCurrSquare.getUWall().matches("uWall"))
                                tempCurrSquare.setVertLaserStart(8);
                            else
                                tempCurrSquare.setVertLaserStart(0);
                            while (done == 0){
                                tempCurrSquare = RoboSquareArray[currX][--currY];
                                if (tempCurrSquare.getDWall().matches("dWall"))
                                    done = 1;
                                else if (tempCurrSquare.getUWall().matches("uWall")){
                                    tempCurrSquare.setVertLaserStart(squareSize);
                                    tempCurrSquare.setVertLaserEnd(8);
                                    done = 1;
                                }
                                else {
                                    tempCurrSquare.setVertLaserStart(squareSize);
                                    tempCurrSquare.setVertLaserEnd(0);
                                }
                                
                            }
                        }//if (lineToken.matches("dLaser")){
                    }
                    else if (lineToken.matches("canner")){
                        currSquare.setCanner(lineToken, IL);
                    }
                    lineToken = st.nextToken();
                }//while (!(lineToken.matches("end")))
            }//while (line=dis.readLine()...
            // Close our input stream
            fin.close();
        }//try
	catch (IOException e){
            System.err.println ("Unable to read from file");
            System.exit(-1);
	}
        return RoboSquareArray;
    }//public ... BuildRoboBoard(
}
