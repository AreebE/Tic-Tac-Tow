import javax.swing.*;
import java.awt.*;

public class TextDisplay extends JPanel implements Game.TurnChangeListener{

  private JLabel playerTurnDisplay;
  private Player current;
  
  public TextDisplay(Player p){
    current = p;
    setLayout(new FlowLayout());
    playerTurnDisplay = new JLabel();
    add(playerTurnDisplay);
    playerTurnDisplay.setText(getPlayerText());
  }

  /*
   * This method is called whenever the player's turn changes.
   *
   * @param    next   The next player
  */
  @Override
  public void onTurnChanged(Player next){
    current = next;
    playerTurnDisplay.setText(getPlayerText());
  }

  /*
   * Displays the winner of the game.
   *
   * @param   winner    the winning player
  */
  @Override 
  public void displayWinner(Player winner){
    playerTurnDisplay.setText("The winner is " + winner.getName() + "! (won " + winner.getTimesPlayerWon() + " times so far)");
  }
 
  /*
  * Called whenever the game is resetGame
   * 
   *  @param    currentPlayer   The first player, who would
   *            make the first move.
  */
  @Override
  public void resetGame(Player currentPlayer) {
    current = currentPlayer;
    playerTurnDisplay.setText(getPlayerText());
  }

  /*
   *      This method would display an error message.
  */
  @Override 
  public void displayError(){
    playerTurnDisplay.setText("That square is already selected. " + getPlayerText());
  }

  public void displayTie(){
    playerTurnDisplay.setText("The game is tied!");
  }
  /*
   *    Tells whose turn it is.
  */
  public String getPlayerText(){
    return current.getName() + "\'s turn.";
  }
}