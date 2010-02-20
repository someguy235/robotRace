/*
 * Main.java
 *
 * Created on September 1, 2006, 2:56 PM
 *
 * by Ethan Shepherd
 */

/*
 * Main() initializes each of the components of the game and then hands them all off to GameController
 * for the remainder of execution. The ints player1Style and player2Style determine the type of 
 * robot for each player, while squareSize is the foundation for _most_ of the size calculations 
 * in the entire game. This should _not_ be changed however, as the original intent of scalability
 * based on this one value has not been fully implemented. 
 */

package robotrace;
import javax.swing.*;

public class Main {
    
    final static int squareSize = 45;
    
    public Main() {
    }
    
    public static void main(String[] args) {
        ImageLoader IL = new ImageLoader(squareSize);
        RobotChooser robotChooser = new RobotChooser();
        BoardChooser boardChooser = new BoardChooser();
        String boardType = boardChooser.chooseBoard(IL, squareSize);
        int player1Style = robotChooser.chooseRobot(IL, squareSize, 1);
        int player2Style = robotChooser.chooseRobot(IL, squareSize, 2);
        
        PlayerInfoPanel player1Panel = new PlayerInfoPanel(squareSize, "Player 1", player1Style, IL);
        PlayerInfoPanel player2Panel = new PlayerInfoPanel(squareSize, "Player 2", player2Style, IL);
        Player Player1 = new Player(player1Style, player1Panel);
        Player Player2 = new Player(player2Style, player2Panel);
        CurrentCardPool cardPool = new CurrentCardPool(squareSize, IL);
        
        RoboBoardBuilder roboBoardBuilder = new RoboBoardBuilder();
        RoboGamePanelBuilder roboGamePanelBuilder = new RoboGamePanelBuilder();
        RoboSquare[][] RoboArray = roboBoardBuilder.BuildRoboBoard(IL, boardType, squareSize);
        
        JFrame roboGamePanel = roboGamePanelBuilder.CreateRoboGamePanel(IL, squareSize, 
                player1Panel, player2Panel, cardPool);
        
        GameController GC = new GameController(IL, player1Panel, player2Panel, 
            Player1, Player2, roboGamePanel, RoboArray, cardPool);
        
        GC.Start();
    }
}
