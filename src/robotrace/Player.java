/*
 * Player.java
 *
 * Created on September 22, 2006, 11:24 AM
 *
 * by Ethan Shepherd
 */

/*
 * An instance of Player holds all of the information about a given player, including their
 * style of robot, damage taken, last flag tagged, and an array of MovementCards to represent
 * the player's hand. The few 
 */

package robotrace;

public class Player {
    int roboStyle = 0;
    int[] RoboLoc = {0, 0, 0}; //x, y, facing (0=U, 1=R, 2=D, 3=L)
    int[] lastSave = {0, 0, 0};
    int roboDamage = 0;
    int flagTagged = 0;
    int openRegisters = 5;
    boolean isCorporeal = true;
    PlayerInfoPanel infoPanel;
    MovementCard[] hand = new MovementCard[5];
    
    public Player(int roboStyle, PlayerInfoPanel playerPanel) {
        this.roboStyle = roboStyle;
        infoPanel = playerPanel;
    }
    
    // this is where the player will come back to life if they die
    public void setLastSave(int x, int y, int facing){
        lastSave[0] = x;
        lastSave[1] = y;
        lastSave[2] = facing;
    }
    public void setLastTagged(int i){
        flagTagged = i;
    }
    public int getLastTagged(){
        return flagTagged;
    }
    public int[] getLastSave(){
        return lastSave;
    }
    public void setCard(MovementCard card, int cardNum){
        hand[cardNum] = card;
    }
    public MovementCard[] getHand(){
        return hand;
    }
    public int getRoboStyle(){
        return roboStyle;
    }
    
    // Remove the old robot image and add a new one in the new board location.
    // Also updates the PlayerInfoPanel to reflect the new player coordinates and
    // robot facing. 
    public void setRoboLoc(ImageLoader IL, RoboSquare[][] RoboArray, int x, int y, int facing){
        RoboArray[RoboLoc[0]][RoboLoc[1]].removeRobot();
        RoboLoc[0] = x;
        RoboLoc[1] = y;
        RoboLoc[2] = facing;
        RoboArray[x][y].setRobot(facing, roboStyle, IL);
        infoPanel.setLocText('x', x);
        infoPanel.setLocText('y', y);
        infoPanel.setRoboIcon(roboStyle, facing, IL);
    }
    public int[] getRoboLoc(){
        return RoboLoc;
    }
    
    // For every damage over 4, 1 player register becomes "locked" and cannot be changed
    // until the player heals or dies.
    // Does a "death" check only to avoid out of bounds errors. Actual robot death is 
    // checked and handled by GameController.
    public void damageRobo(ImageLoader IL){
        if (this.roboDamage == 10)
            //dead robot
            ;
        else{
            if (this.roboDamage >= 4)
                this.openRegisters -= 1;
            infoPanel.addDamageMarker(roboDamage , IL);
            this.roboDamage += 1;
        }
    }
    public void repairRobo(int amount){
        for(int i = amount; i>0; i--){
            if (roboDamage > 0){
                roboDamage--;
                infoPanel.removeDamageMarker(roboDamage);
                if(openRegisters<5){
                    infoPanel.setCardBorder(openRegisters , 0, 0, 0);
                    openRegisters++;
                }
            }
        }
    }
    public int getRoboDamage(){
        return roboDamage;
    }
    public int getOpenRegisters(){
        return openRegisters;
    }
    
    // isCorporeal lets GameController know that the robot has died this turn and should
    // not execute its remaining movement cards, shoot lasers, take damage, or otherwise
    // affect the game in any way. A robot is automatically brought back to life at the 
    // beginning of the following turn, at the players "lastSave" location. 
    public void setCorporeal(boolean x){
        isCorporeal = x;
    }
    public boolean getCorporeal(){
        return isCorporeal;
    }
}
