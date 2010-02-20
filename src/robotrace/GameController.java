/*
 * GameController.java
 *
 * Created on September 22, 2006, 11:55 AM
 *
 * by Ethan Shepherd
 */

/*
 * GameController is the heavy lifter of the RoboRace program, and it coordinates everything
 * after the initialization done by the Main method.
 * 
 * The Start() method places the players at their starting positions, and then loops through 
 * turns until somebody wins. If a player has died, Start makes them corporeal again at the
 * end of the turn. 
 * 
 * ChooseCards() resets the InfoPanels (so that players do not see each others' cards) and waits
 * for player input to display their cards for the turn. It then keeps track of which cards
 * have been chosen by the player, displays them in that players hand, and marks them as
 * inelligible for choosing (visually by creating a red border, and factually by ignoring furthur
 * clicks on that card location). If the reset button is clicked, all of the cards are put 
 * back in their starting location. This is repeated for both players.
 * 
 * PlayRegisters() checks that nobody has won the game, and then waits for user input to start the
 * register. This is so that players have a chance to understand what has happened in the 
 * previous register, as this can be somewhat confusing. PlayRegisters then calls three methods
 * to process each of the major register events, and then checks to see if any player has died.
 * After the fifth register, all unlocked cards are returned to the deck.
 * 
 * PlayCards() parses the card in each players current register and (if that player is
 * corporeal) takes the appropriate action, either moving or turning the player as needed. The 
 * player using the card with the highest speed rating goes first.
 *
 * BoardElements() is very similar to PlayCards, except that it interprets board squares instead
 * of player chosen cards. The is a prescribed order for the elements of the board to take
 * place. BoardElements also checks to see if a player has tagged a flag during this register, 
 * and if so it updates their information appropriately, or initiates the win sequence if 
 * it was the last flag.
 *
 * Lasers() first checks to see if a robot has an unimpeded forward line of sight to another 
 * robot and if so, then draws a "laser" (red line) along that path and an explosion, and 
 * damages the target robot by 1. If a robot has five damage, it's last MovementCard becomes
 * locked and will be played every turn in that location until the robot repairs or dies. This
 * is repeated for each damage above four. Lasers also makes a check for robot casualties.
 *
 * Move() does a number of checks to determine if a square is clear for a robot to move 
 * in to. It returns a failure (false) if the robot hits a wall, or if an intervening robot
 * cannot be pushed because of a wall. It succeeds (true) if the square is the edge of the
 * board, or is otherwise clear of obstacles. Note that if there is another robot in the 
 * target square, Move() calls itself in an attempt to move the other robot.
 *
 * RoboCheck() takes a location and checks the adjacent square in the given direction to see
 * whether that square contains another robot. Used for resolving movement
 *
 * Turn() simply changes the facing of the target robot, with a check to link facings 0 (up)
 * and 3 (left) to seem adjacent.
 *
 * WallCheck() checks whether a path is free of wall obstacles. This involves the current and 
 * adjacent squares (i.e. an attempt to move up involves two checks: whether the current square
 * has an "Up" wall, and whether the target square has a "Down" wall, either of which would 
 * block the movement. Note: can now safely be called before or after EdgeCheck(), as the method
 * now does it's own edge checking to avoid array out of bounds errors.
 *
 * EdgeCheck() checks to see if the provided location and movement direction result in a move 
 * off the board. Returns false if the move fails to stay on the board.
 * 
 * RobotDies() returns the player to their last "save point" (either the starting location or the
 * location of the last flag tagged), but marks them as non-corporeal, which means that the
 * player does not move for the rest of the turn, and neither takes nor deals damage. The 
 * method also resets the player's life to full, resets any "locked" register borders, and displays the
 * "blank" card in all registers to illustrate that they will not be played this turn.
 *
 * Reset() changes all of the unlocked register images and all of the card pool images to the 
 * blank card image, and resets the "clicked" status of all card locations.
 *
 * Opposite() is simply an easy way to keep track of facings. As the facings are defined as integers
 * (Up = 0, Right = 1, Down = 2, and Left = 3), this method defines the opposite of 0 to be 2, the
 * opposite of 1 to be 3, and so on. Used for backing up.
 *
 * PlayerWins() is the end game sequence. It clears the board of all elements and then spells out
 * "P X Win" (where "X" is the winning player's number) with images of the winning player's robot.
 * Probably a better way to do this.
 */

package robotrace;
import javax.swing.*;

public class GameController {
    ImageLoader IL;
    PlayerInfoPanel player1Panel, player2Panel;
    Player Player1, Player2;
    JFrame roboGamePanel;
    RoboSquare[][] RoboArray;
    CurrentCardPool currentCardPool;
    DeckOfCards deck = new DeckOfCards();
    Player[] players = new Player[2];
    PlayerInfoPanel[] playerInfoPanels = new PlayerInfoPanel[2];
    int whoseTurn, going, notGoing, cardsThisTurn, openRegisters, currentRegister, playerWins;
    

    public GameController(ImageLoader IL, PlayerInfoPanel player1Panel, PlayerInfoPanel player2Panel, 
            Player Player1, Player Player2, JFrame roboGamePanel, RoboSquare[][] RoboArray, 
            CurrentCardPool currentCardPool) {
        this.IL = IL;
        this.player1Panel = player1Panel;
        this.player2Panel = player2Panel;
        this.Player1 = Player1;
        this.Player2 = Player2;
        this.roboGamePanel = roboGamePanel;
        this.RoboArray = RoboArray;
        this.currentCardPool = currentCardPool;
    }
    
    public void Start(){
        playerWins = -1;
        players[0] = Player1;
        players[1] = Player2;
        playerInfoPanels[0] = player1Panel;
        playerInfoPanels[1] = player2Panel;
        players[0].setRoboLoc(IL, RoboArray, 1, 0, 1);
        players[0].setLastSave(1, 0, 1);
        players[1].setRoboLoc(IL, RoboArray, 0, 1, 2);
        players[1].setLastSave(0, 1, 2);
        
        //for (int i=0; i<4; i++){
        //    players[0].damageRobo(IL);
        //    playerInfoPanels[0].setDamage(players[0].getRoboDamage());
        //}
        
        //while nobody has won
        while(playerWins == -1){
            ChooseCards();
            PlayRegisters();
            
            //make sure that dead players are not brought back to life on top of other players
            if (!((players[0].getRoboLoc()[0] == players[1].getRoboLoc()[0])&
                (players[0].getRoboLoc()[1] == players[1].getRoboLoc()[1]))){
                players[0].setCorporeal(true);
                players[1].setCorporeal(true);
            }
        }
        PlayerWins(playerWins);
    }
    
    public void ChooseCards(){
        for (int whoseTurn = 0; whoseTurn < 2; whoseTurn++){
            currentCardPool.setBorder(javax.swing.BorderFactory.
                    createTitledBorder(javax.swing.BorderFactory.createLineBorder
                    (new java.awt.Color(0, 0, 0), 2), "Player" + (whoseTurn+1) +  " Card Pool"));
            Reset();
            currentCardPool.getReady().setText("P" + (whoseTurn+1) + ": click when ready", 14, 16, 14);
            
            while(!(currentCardPool.getReady().clicked)){
                try {Thread.sleep(200);}
                catch (Exception e){}
            }
            currentCardPool.getReady().setBorder(javax.swing.BorderFactory.createBevelBorder(1));
            currentCardPool.getReady().setText("P" + (whoseTurn+1) + ": choose cards", 14, 16, 14);
            
            cardsThisTurn = (9 - players[whoseTurn].getRoboDamage());
            
            for (int i=0; i<9; i++){
                if (i<cardsThisTurn){
                    currentCardPool.setCard(deck.getRandomCard(), i, IL);
                    currentCardPool.getCard(i).setIsInPool(true);
                }
                currentCardPool.getCardLoc(i).resetClicked();
            }

            openRegisters = players[whoseTurn].getOpenRegisters();
            currentRegister = 0;
        
            while (openRegisters > 0){
                for (int i=0; i<cardsThisTurn; i++){
                    if((currentCardPool.getCardLoc(i).clicked)&(currentCardPool.getCard(i).getIsInPool())){
                        currentCardPool.getCardLoc(i).setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
                        
                        currentCardPool.getCard(i).setIsInPool(false);
                        players[whoseTurn].setCard(currentCardPool.getCard(i), currentRegister);
                        playerInfoPanels[whoseTurn].setCardImage(currentRegister, 
                                currentCardPool.getCard(i).getIndex(), IL);
                        playerInfoPanels[whoseTurn].setCardText(currentRegister, 
                                ""+currentCardPool.getCard(i).getCardSpeed());
                        openRegisters--;
                        currentRegister++;
                    }
                }
                try {Thread.sleep(100);}
                catch (Exception e){}
                if(currentCardPool.getReset().clicked){
                    currentCardPool.getReset().setBorder(javax.swing.BorderFactory.createBevelBorder(1));
                    currentRegister = 0;
                    openRegisters = players[whoseTurn].getOpenRegisters();
                    for (int i=0; i<9; i++){
                        if (i<openRegisters){
                            playerInfoPanels[whoseTurn].setCardImage(i, 0, IL);
                        }
                        currentCardPool.getCardLoc(i).resetClicked();
                        currentCardPool.getCardLoc(i).setBorder(javax.swing.BorderFactory.createLineBorder
                        (new java.awt.Color(0, 0, 0), 2));
                        currentCardPool.getCard(i).setIsInPool(true);
                    }
                    try {Thread.sleep(100);}
                    catch (Exception e){}
                    currentCardPool.getReset().setBorder(javax.swing.BorderFactory.createBevelBorder(0));
                    currentCardPool.getReset().resetClicked();
                }
            }
            Reset();
            for(int i=0; i<cardsThisTurn; i++){
                if (currentCardPool.getCard(i).getIsInPool())
                    deck.returnCardToDeck(currentCardPool.getCard(i));
            }
        }
    }
    
    private void PlayRegisters(){
        for (int register = 0; register<5; register++){
            if(playerWins==-1){
                //playerInfoPanels[0].setDamage(players[0].getRoboDamage());
                //playerInfoPanels[1].setDamage(players[1].getRoboDamage());
                currentCardPool.getReady().setBorder(javax.swing.BorderFactory.createBevelBorder(0));
                currentCardPool.getReady().setText(("Ready for register " + (register+1) + "?"), 14, 16, 14);
                currentCardPool.getReady().resetClicked();
                //wait for user input to play the register
                while(!(currentCardPool.getReady().clicked)){
                    try {Thread.sleep(500);}
                    catch (Exception e){}
                }

                currentCardPool.getReady().setText(("        Moving..."), 14, 16, 14);
                currentCardPool.getReady().setBorder(javax.swing.BorderFactory.createBevelBorder(1));

                PlayCards(register);
                BoardElements();
                Lasers();
                
                //check for deaths
                if (players[0].getRoboDamage() == 10)
                    RobotDies(0);
                if (players[1].getRoboDamage() == 10)
                    RobotDies(1);
            }
        }
        //return unlocked cards to the deck
        for (int i=0; i<2; i++){
            for(int j=0; j<players[i].getOpenRegisters(); j++)
                deck.returnCardToDeck(players[i].getHand()[i]);
        }
    }
    
    private void PlayCards(int register){
        //players do not move if they have died this turn
        int player1Speed = players[0].getHand()[register].getCardSpeed();
        int player2Speed = players[1].getHand()[register].getCardSpeed();
        if (players[0].isCorporeal)
            playerInfoPanels[0].setCardImage(register, players[0].getHand()[register].getIndex(), IL);
            playerInfoPanels[0].setCardText(register, ""+player1Speed);
        if (players[1].isCorporeal)
            playerInfoPanels[1].setCardImage(register, players[1].getHand()[register].getIndex(), IL);
            playerInfoPanels[1].setCardText(register, ""+player2Speed);
        //determine who moves first, based on card speed
        going = ((player1Speed > player2Speed)? 0 : 1);
        notGoing = ((going == 0)? 1 : 0);
        //parse each card, and move the player as appropriate
        for (int i = 0; i<2; i++){
            int[] playerLoc = players[going].getRoboLoc();
            String cardType = players[going].getHand()[register].getCardType();
            if (cardType.matches("BackUp")){
                Move(going, 1, playerLoc, Opposite(playerLoc[2]));
                try {Thread.sleep(500);}
                catch (Exception e){}
            }
            else if (cardType. matches("Move1")){
                Move(going, 1, playerLoc, playerLoc[2]);
                try {Thread.sleep(500);}
                catch (Exception e){}
            }
            else if (cardType. matches("Move2")){
                Move(going, 1, playerLoc, playerLoc[2]);
                try {Thread.sleep(500);}
                catch (Exception e){}
                Move(going, 1, playerLoc, playerLoc[2]);
                try {Thread.sleep(500);}
                catch (Exception e){}
            }
            else if (cardType. matches("Move3")){
                Move(going, 1, playerLoc, playerLoc[2]);
                try {Thread.sleep(500);}
                catch (Exception e){}
                Move(going, 1, playerLoc, playerLoc[2]);
                try {Thread.sleep(500);}
                catch (Exception e){}
                Move(going, 1, playerLoc, playerLoc[2]);
                try {Thread.sleep(500);}
                catch (Exception e){}
            }
            else if (cardType. matches("RotateLeft")){
                Turn(going, -1, playerLoc);
            }
            else if (cardType. matches("RotateRight")){
                Turn(going, 1, playerLoc);
            }
            else if (cardType. matches("UTurn")){
                Turn(going, 1, playerLoc);
                Turn(going, 1, playerLoc);
            }
            //switch who is actively moving, for the other players turn
            going = (going == 0)? 1 : 0;
        }      
    }
    
    private void BoardElements(){
        for (int i = 0; i<2; i++){
            int x = players[i].getRoboLoc()[0];
            int y = players[i].getRoboLoc()[1];
            int face = players[i].getRoboLoc()[2];
            RoboSquare location = RoboArray[x][y];
            if (location.getFloor().matches("cWRot"))
                Turn(i, 1, players[i].getRoboLoc());
            if (location.getFloor().matches("cCWRot"))
                Turn(i, -1, players[i].getRoboLoc());
            if (location.getBelt().contains("Belt")){
                if (location.getBelt().startsWith("e")){
                    Move(i, 1, players[i].getRoboLoc(), location.getBeltDirection());
                    Move(i, 1, players[i].getRoboLoc(), location.getBeltDirection());
                }
                if (location.getBelt().startsWith("c")){
                    Move(i, 1, players[i].getRoboLoc(), location.getBeltDirection());
                }
            }
            if (location.getFloor().matches("1xRep"))
                players[i].repairRobo(1);
                playerInfoPanels[0].setDamage(players[0].getRoboDamage());
                playerInfoPanels[1].setDamage(players[1].getRoboDamage());
                
            if (location.getFloor().matches("2xRep"))
                players[i].repairRobo(2);
                playerInfoPanels[0].setDamage(players[0].getRoboDamage());
                playerInfoPanels[1].setDamage(players[1].getRoboDamage());
            if (location.getFlag().contains("flag")){
                if(location.getFlagNumber() == (players[i].getLastTagged()+1)){
                    players[i].setLastSave(x, y, face);
                    players[i].setLastTagged(location.getFlagNumber());
                    playerInfoPanels[i].setFlag(location.getFlagNumber());
                    if(location.getFlagNumber() == 2){
                        System.out.println("player wins");
                        playerWins = (i==0? 0:1);
                        System.out.println("player wins " + playerWins);
                    }
                }
            }
        }
    }
    
    private void Lasers(){
        int going, notGoing, direction, xStart, xEnd, yStart, yEnd, laseredSquares=-1, tempLaseredSquares = -1;
        boolean blocked, hit;
        int[] testLoc = {0, 0, 0};
        for (int i=0; i<2; i++){
            going = i;
            notGoing = ((i==0)? 1:0);
            if (players[going].isCorporeal & players[notGoing].isCorporeal){
                laseredSquares = 0;
                blocked = false;
                hit = false;
                testLoc[0] = players[i].getRoboLoc()[0];
                testLoc[1] = players[i].getRoboLoc()[1];
                testLoc[2] = players[i].getRoboLoc()[2];

                xStart = players[i].getRoboLoc()[0];
                yStart = players[i].getRoboLoc()[1];
                direction = players[i].getRoboLoc()[2];
                //test along the line of sight path until another robot is hit, 
                //or the shot hits a wall or goes off the board (misses)
                while (!blocked & !hit){
                    blocked = (!(WallCheck(i, testLoc, direction)));
                    if (!blocked)
                    blocked = (!(EdgeCheck(i, testLoc, direction)));
                    if (!blocked)
                    hit = RoboCheck(i, testLoc, direction);
                    if (!hit & !blocked){
                        switch(direction){
                            case 0:
                                testLoc[1]--;
                                break;
                            case 1:
                                testLoc[0]++;
                                break;
                            case 2:
                                testLoc[1]++;
                                break;
                            case 3: 
                                testLoc[0]--;
                                break;
                        }
                        laseredSquares++;
                    }
                }
                //actually draw the laser along the path from the firing robot to the
                //target robot, draw the explosion graphic, and remove all of this
                //after .5 seconds for a "firing" effect.
                if (hit){
                    tempLaseredSquares = laseredSquares;
                    switch(direction){
                        case 0:
                            testLoc[1]--;
                            RoboArray[xStart][yStart].setVertLaserStart(0);
                            RoboArray[xStart][yStart].setVertLaserEnd(15);
                            while(tempLaseredSquares>0){
                                RoboArray[xStart][yStart-tempLaseredSquares].setVertLaserStart(0);
                                RoboArray[xStart][yStart-tempLaseredSquares].setVertLaserEnd(45);
                                tempLaseredSquares--;
                            }
                            RoboArray[testLoc[0]][testLoc[1]].setVertLaserStart(30);
                            RoboArray[testLoc[0]][testLoc[1]].setVertLaserEnd(45);
                            RoboArray[testLoc[0]][testLoc[1]].boom(IL);
                            try {Thread.sleep(500);}
                            catch (Exception e){}
                            for(int d=yStart; d>=testLoc[1]; d--){
                                RoboArray[xStart][d].resetLasers();
                            }
                            RoboArray[testLoc[0]][testLoc[1]].removeBoom();
                            break;
                        case 1:
                            testLoc[0]++;
                            RoboArray[xStart][yStart].setHorizLaserStart(30);
                            RoboArray[xStart][yStart].setHorizLaserEnd(45);
                            while(tempLaseredSquares>0){
                                RoboArray[xStart+tempLaseredSquares][yStart].setHorizLaserStart(0);
                                RoboArray[xStart+tempLaseredSquares][yStart].setHorizLaserEnd(45);
                                tempLaseredSquares--;
                            }
                            RoboArray[testLoc[0]][testLoc[1]].setHorizLaserStart(0);
                            RoboArray[testLoc[0]][testLoc[1]].setHorizLaserEnd(15);
                            RoboArray[testLoc[0]][testLoc[1]].boom(IL);
                            try {Thread.sleep(500);}
                            catch (Exception e){}
                            for(int d=xStart; d<=testLoc[0]; d++){
                                RoboArray[d][yStart].resetLasers();
                            }
                            RoboArray[testLoc[0]][testLoc[1]].removeBoom();
                            break;
                        case 2:
                            testLoc[1]++;
                            RoboArray[xStart][yStart].setVertLaserStart(30);
                            RoboArray[xStart][yStart].setVertLaserEnd(45);
                            while(tempLaseredSquares>0){
                                RoboArray[xStart][yStart+tempLaseredSquares].setVertLaserStart(0);
                                RoboArray[xStart][yStart+tempLaseredSquares].setVertLaserEnd(45);
                                tempLaseredSquares--;
                            }
                            RoboArray[testLoc[0]][testLoc[1]].setVertLaserStart(0);
                            RoboArray[testLoc[0]][testLoc[1]].setVertLaserEnd(15);
                            RoboArray[testLoc[0]][testLoc[1]].boom(IL);
                            try {Thread.sleep(500);}
                            catch (Exception e){}
                            for(int d=yStart; d<=testLoc[1]; d++){
                                RoboArray[xStart][d].resetLasers();
                            }
                            RoboArray[testLoc[0]][testLoc[1]].removeBoom();
                            break;
                        case 3: 
                            testLoc[0]--;
                            RoboArray[xStart][yStart].setHorizLaserStart(0);
                            RoboArray[xStart][yStart].setHorizLaserEnd(15);
                            while(tempLaseredSquares>0){
                                RoboArray[xStart-tempLaseredSquares][yStart].setHorizLaserStart(0);
                                RoboArray[xStart-tempLaseredSquares][yStart].setHorizLaserEnd(45);
                                tempLaseredSquares--;
                            }
                            RoboArray[testLoc[0]][testLoc[1]].setHorizLaserStart(30);
                            RoboArray[testLoc[0]][testLoc[1]].setHorizLaserEnd(45);
                            RoboArray[testLoc[0]][testLoc[1]].boom(IL);
                            try {Thread.sleep(500);}
                            catch (Exception e){}
                            for(int d=xStart; d>=testLoc[0]; d--){
                                RoboArray[d][yStart].resetLasers();
                            }
                            RoboArray[testLoc[0]][testLoc[1]].removeBoom();
                            break;
                    }
                    //damage the shot robot, and lock registers if necessary
                    players[notGoing].damageRobo(IL);
                    playerInfoPanels[notGoing].setDamage(players[notGoing].getRoboDamage());
                    if((players[notGoing].getOpenRegisters() < 5)&(players[notGoing].getRoboDamage()<10))
                        playerInfoPanels[notGoing].setCardBorder(players[notGoing].getOpenRegisters(), 255, 0, 0);
                }
            }
        }
        //check for deaths
        if (players[0].getRoboDamage()>=10)
            RobotDies(0);
        if (players[1].getRoboDamage()>=10)
            RobotDies(1);
    }

    private boolean Move(int who, int howMany, int[] fromWhere, int direction){
        boolean success = false, wallCheck = false, edgeCheck = false, roboCheck = false;
        int otherRobot = ((who == 0)? 1 : 0);
        
        //if no walls in the way
        if (WallCheck(who, fromWhere, direction)){
            //and not the edge of the board
            if (EdgeCheck(who, fromWhere, direction)){
                //check if there is another robot in the way
                if(RoboCheck(who, fromWhere, direction)){
                    //try to push the other robot 1 square in the direction indicated
                    if(Move(otherRobot, 1, players[otherRobot].getRoboLoc(), direction))
                        success = true;
                    else
                        success = false;
                }
                else //if no robot in the way, all checks have passed, the route is clear
                    success = true;
            }
            //if robot went off edge of board it dies, but the move is considered successful
            else{
                RobotDies(who);
                success = true;
            }
        }
        else{
            success = false;
        }
        if(success & players[who].isCorporeal){
            //actually move the player
            switch(direction){
                case 0:
                    players[who].setRoboLoc(IL, RoboArray, (fromWhere[0]), (fromWhere[1]-howMany), fromWhere[2]);
                    break;
                case 1:
                    players[who].setRoboLoc(IL, RoboArray, (fromWhere[0]+howMany), (fromWhere[1]), fromWhere[2]);
                    break;
                case 2:
                    players[who].setRoboLoc(IL, RoboArray, (fromWhere[0]), (fromWhere[1]+howMany), fromWhere[2]);
                    break;
                case 3:
                    players[who].setRoboLoc(IL, RoboArray, (fromWhere[0]-howMany), (fromWhere[1]), fromWhere[2]);
                    break;
            }
        }
        //check for pits
        if(RoboArray[players[who].getRoboLoc()[0]][players[who].getRoboLoc()[1]].getFloor().matches("pit"))
            RobotDies(who);
        return success;
    }
    
    public boolean RoboCheck(int who, int[] fromWhere, int direction){
        //check whether an adjacent square in the specified direction is occupied
        boolean occupied = false;
        int otherRobot = ((who == 0)? 1 : 0);
        switch(direction){
            case 0:
                if((players[otherRobot].getRoboLoc()[0] == fromWhere[0])&
                   (players[otherRobot].getRoboLoc()[1] == (fromWhere[1]-1))&
                   (players[otherRobot].isCorporeal))
                   occupied = true;
                break;
            case 1:
                if((players[otherRobot].getRoboLoc()[0] == fromWhere[0]+1)&
                   (players[otherRobot].getRoboLoc()[1] == (fromWhere[1]))&
                   (players[otherRobot].isCorporeal))
                   occupied = true;
                    break;
            case 2:
                if((players[otherRobot].getRoboLoc()[0] == fromWhere[0])&
                   (players[otherRobot].getRoboLoc()[1] == (fromWhere[1]+1))&
                   (players[otherRobot].isCorporeal))
                   occupied = true;
                break;
            case 3:
                if((players[otherRobot].getRoboLoc()[0] == fromWhere[0]-1)&
                   (players[otherRobot].getRoboLoc()[1] == (fromWhere[1]))&
                   (players[otherRobot].isCorporeal))
                   occupied = true;
                break;
        }
        return occupied;
    }
   
    private void Turn(int who, int howMuch, int[] fromWhere){
        //change the facing of the target robot, facing sequence is 0, 1, 2, 3, back to 0
        if(players[who].isCorporeal){
            if ((fromWhere[2] == 0)&(howMuch == -1))
                players[who].setRoboLoc(IL, RoboArray, fromWhere[0], fromWhere[1], 3);
            else if ((fromWhere[2] == 3)&(howMuch == 1))
                players[who].setRoboLoc(IL, RoboArray, fromWhere[0], fromWhere[1], 0);
            else
                players[who].setRoboLoc(IL, RoboArray, fromWhere[0], fromWhere[1], (fromWhere[2]+howMuch));
            try {Thread.sleep(500);}
            catch (Exception e){}    
        }
    }
    
    private boolean WallCheck(int who, int[] fromWhere, int direction){
        //return true if the path is clear of walls
        boolean success = true;
        
        switch(direction){
            case 0:
                if(RoboArray[fromWhere[0]][fromWhere[1]].getUWall().matches("uWall"))
                    success = false;
                //edge checking to avoid array out of bounds
                if((success == true) & (!(fromWhere[1] == 0)))
                    if(RoboArray[fromWhere[0]][(fromWhere[1]-1)].getDWall().matches("dWall"))
                        success = false;
                break;
            case 1:
                if(RoboArray[fromWhere[0]][fromWhere[1]].getRWall().matches("rWall"))
                    success = false;
                if((success == true) & (!(fromWhere[0] == 11)))
                    if(RoboArray[(fromWhere[0]+1)][fromWhere[1]].getLWall().matches("lWall"))
                        success = false;
                break;
            case 2:
                if(RoboArray[fromWhere[0]][fromWhere[1]].getDWall().matches("dWall"))
                    success = false;    
                if((success == true) & (!(fromWhere[1] == 11)))
                    if (RoboArray[fromWhere[0]][(fromWhere[1]+1)].getUWall().matches("uWall"))
                        success = false;
                break;
            case 3:
                if(RoboArray[fromWhere[0]][fromWhere[1]].getLWall().matches("lWall"))
                    success = false;
                if((success == true) & (!(fromWhere[0] == 0)))
                    if(RoboArray[(fromWhere[0]-1)][fromWhere[1]].getRWall().matches("rWall"))
                        success = false;
                break;
        }
        return success;
    }
    
    private boolean EdgeCheck(int who, int[] fromWhere, int direction){
        //returns true if the player did not go out of bounds
        boolean stayedOnBoard = true;
        if (((fromWhere[0] == 0) & (direction == 3))|
            ((fromWhere[0] == 11) & (direction == 1))|
            ((fromWhere[1] == 0) & (direction == 0))|
            ((fromWhere[1] == 11) & (direction == 2))){
            stayedOnBoard = false;
        }
        return stayedOnBoard;
    }
    
    private void RobotDies(int who){
        players[who].setCorporeal(false);
        players[who].setRoboLoc(IL, RoboArray, players[who].getLastSave()[0], 
                players[who].getLastSave()[1], players[who].getLastSave()[2]);
        players[who].repairRobo(10);
        for (int i = 0; i<5; i++){
            playerInfoPanels[who].setCardBorder(i, 0, 0, 0);
            playerInfoPanels[who].setCardImage(i, 0, IL);
        }
    }
    
    private void Reset(){
        //reset the info panel card images in preparation for a new turn
        for (int i=0; i<9; i++){
                if (i<5){
                    if (i<players[0].getOpenRegisters())
                        playerInfoPanels[0].setCardImage(i, 0, IL);
                     //   playerInfoPanels[0].cards[i].setText("", 0, 0, 0);
                    if (i<players[1].getOpenRegisters())
                        playerInfoPanels[1].setCardImage(i, 0, IL);
                        playerInfoPanels[1].cards[i].setText("", 0, 0, 0);
                }
                currentCardPool.getCardLoc(i).setBILayer(IL.getMovementCardImage(0), 0);
                currentCardPool.getCardLoc(i).resetClicked();
                currentCardPool.getCardLoc(i).setBorder(javax.swing.BorderFactory.createLineBorder
                        (new java.awt.Color(0, 0, 0), 2));
                currentCardPool.getCardLoc(i).setText("", 0, 0, 0);
            }
            currentCardPool.getReady().resetClicked();
            currentCardPool.getReady().setBorder(javax.swing.BorderFactory.createBevelBorder(0));
    }
    
    private int Opposite(int x){
        int y = 0;
        switch (x){
            case 0:
                y = 2;
                break;
            case 1:
                y = 3;
                break;
            case 2:
            case 3:
                y = (x-2);
                break;
        }
        return y;
    }
    
    private void PlayerWins(int who){
        int robot = players[who].getRoboStyle();
        for (int i=0; i<2; i++)
            RoboArray[players[i].getRoboLoc()[0]][players[i].getRoboLoc()[1]].removeRobot();
        
        for (int i=0; i<12; i++){
            for(int j=0; j<12; j++){
                RoboArray[j][i].clear(IL);
            }
        }
        
        RoboArray[2][5].setRobot(0, robot, IL);
        RoboArray[2][5].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[2][4].setRobot(0, robot, IL);
        RoboArray[2][4].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[2][3].setRobot(0, robot, IL);
        RoboArray[2][3].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[2][2].setRobot(0, robot, IL);
        RoboArray[2][2].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[2][1].setRobot(0, robot, IL);
        RoboArray[2][1].setBILayer(IL.getDiverseImage(3, 0), 1);
        
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[3][1].setRobot(1, robot, IL);
        RoboArray[3][1].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[4][1].setRobot(1, robot, IL);
        RoboArray[4][1].setBILayer(IL.getDiverseImage(3, 0), 1);
        
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[4][2].setRobot(2, robot, IL);
        RoboArray[4][2].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[4][3].setRobot(2, robot, IL);
        RoboArray[4][3].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        
        RoboArray[3][3].setRobot(3, robot, IL);
        RoboArray[3][3].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        
        
        if(who==0){
            RoboArray[7][1].setRobot(2, robot, IL);
            RoboArray[7][1].setBILayer(IL.getDiverseImage(3, 0), 1);
            try {Thread.sleep(500);}
            catch (Exception e){}
            RoboArray[7][2].setRobot(2, robot, IL);
            RoboArray[7][2].setBILayer(IL.getDiverseImage(3, 0), 1);
            try {Thread.sleep(500);}
            catch (Exception e){}
            RoboArray[7][3].setRobot(2, robot, IL);
            RoboArray[7][3].setBILayer(IL.getDiverseImage(3, 0), 1);
            try {Thread.sleep(500);}
            catch (Exception e){}
            RoboArray[7][4].setRobot(2, robot, IL);
            RoboArray[7][4].setBILayer(IL.getDiverseImage(3, 0), 1);
            try {Thread.sleep(500);}
            catch (Exception e){}
            RoboArray[7][5].setRobot(2, robot, IL);
            RoboArray[7][5].setBILayer(IL.getDiverseImage(3, 0), 1);
            try {Thread.sleep(500);}
            catch (Exception e){}
            
        }
        else{
            RoboArray[6][1].setRobot(1, robot, IL);
            RoboArray[6][1].setBILayer(IL.getDiverseImage(3, 0), 1);
            try {Thread.sleep(500);}
            catch (Exception e){}
            RoboArray[7][1].setRobot(1, robot, IL);
            RoboArray[7][1].setBILayer(IL.getDiverseImage(3, 0), 1);
            try {Thread.sleep(500);}
            catch (Exception e){}
            RoboArray[8][1].setRobot(1, robot, IL);
            RoboArray[8][1].setBILayer(IL.getDiverseImage(3, 0), 1);
            try {Thread.sleep(500);}
            catch (Exception e){}
            RoboArray[8][2].setRobot(2, robot, IL);
            RoboArray[8][2].setBILayer(IL.getDiverseImage(3, 0), 1);
            try {Thread.sleep(500);}
            catch (Exception e){}
            RoboArray[8][3].setRobot(2, robot, IL);
            RoboArray[8][3].setBILayer(IL.getDiverseImage(3, 0), 1);
            try {Thread.sleep(500);}
            catch (Exception e){}
            RoboArray[7][3].setRobot(2, robot, IL);
            RoboArray[7][3].setBILayer(IL.getDiverseImage(3, 0), 1);
            try {Thread.sleep(500);}
            catch (Exception e){}
            RoboArray[6][3].setRobot(2, robot, IL);
            RoboArray[6][3].setBILayer(IL.getDiverseImage(3, 0), 1);
            try {Thread.sleep(500);}
            catch (Exception e){}
            RoboArray[6][4].setRobot(2, robot, IL);
            RoboArray[6][4].setBILayer(IL.getDiverseImage(3, 0), 1);
            try {Thread.sleep(500);}
            catch (Exception e){}
            RoboArray[6][5].setRobot(2, robot, IL);
            RoboArray[6][5].setBILayer(IL.getDiverseImage(3, 0), 1);
            try {Thread.sleep(500);}
            catch (Exception e){}
            RoboArray[7][5].setRobot(1, robot, IL);
            RoboArray[7][5].setBILayer(IL.getDiverseImage(3, 0), 1);
            try {Thread.sleep(500);}
            catch (Exception e){}
            RoboArray[8][5].setRobot(1, robot, IL);
            RoboArray[8][5].setBILayer(IL.getDiverseImage(3, 0), 1);
            try {Thread.sleep(500);}
            catch (Exception e){}
        }
        RoboArray[0][7].setRobot(2, robot, IL);
        RoboArray[0][7].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[0][8].setRobot(2, robot, IL);
        RoboArray[0][8].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[0][9].setRobot(2, robot, IL);
        RoboArray[0][9].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[1][10].setRobot(1, robot, IL);
        RoboArray[1][10].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[2][9].setRobot(0, robot, IL);
        RoboArray[2][9].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[2][8].setRobot(0, robot, IL);
        RoboArray[2][8].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[3][10].setRobot(1, robot, IL);
        RoboArray[3][10].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[4][9].setRobot(0, robot, IL);
        RoboArray[4][9].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[4][8].setRobot(0, robot, IL);
        RoboArray[4][8].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[4][7].setRobot(0, robot, IL);
        RoboArray[4][7].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        
        RoboArray[6][7].setRobot(2, robot, IL);
        RoboArray[6][7].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[6][8].setRobot(2, robot, IL);
        RoboArray[6][8].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[6][9].setRobot(2, robot, IL);
        RoboArray[6][9].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[6][10].setRobot(2, robot, IL);
        RoboArray[6][10].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        
        RoboArray[8][10].setRobot(0, robot, IL);
        RoboArray[8][10].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[8][9].setRobot(0, robot, IL);
        RoboArray[8][9].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[8][8].setRobot(0, robot, IL);
        RoboArray[8][8].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[8][7].setRobot(0, robot, IL);
        RoboArray[8][7].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[9][8].setRobot(1, robot, IL);
        RoboArray[9][8].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[10][9].setRobot(1, robot, IL);
        RoboArray[10][9].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[11][10].setRobot(0, robot, IL);
        RoboArray[11][10].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[11][9].setRobot(0, robot, IL);
        RoboArray[11][9].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[11][8].setRobot(0, robot, IL);
        RoboArray[11][8].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
        RoboArray[11][7].setRobot(0, robot, IL);
        RoboArray[11][7].setBILayer(IL.getDiverseImage(3, 0), 1);
        try {Thread.sleep(500);}
        catch (Exception e){}
    }
}
