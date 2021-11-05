import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Need to add reset
public class Game implements Runnable{

  public interface TurnChangeListener {
    public void onTurnChanged(Player p);
    public void displayWinner(Player winner);
    public void displayError();
    public void resetGame(Player currentPlayer);
    public void displayTie();
  }


  public interface GameStateListener {
    public void stopGame();
    public void resetGame(Player currentPlayer);
  }
  
  private TurnChangeListener listenerForText;
  private GameStateListener listenerForBoard;

  private Player[] players = new Player[]{
    new Player(Util.Symbol.X, "A"),
    new Player(Util.Symbol.O, "B")
  };

  private int currentPlayer;

  public Game(){
    currentPlayer = 0;
  }

  /*
   * The run method is used to create the entire display,
   * from the board to the text view.
  */
  @Override
  public void run() {
    // Sets up the main frame
    JFrame frame = new JFrame("HelloWorldSwing");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container container = frame.getContentPane();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

    // Sets up the text display
    TextDisplay text = new TextDisplay(players[0]);
    listenerForText = (TurnChangeListener) text; 
    container.add((JPanel) text);

    // Sets up the board
    Board panel = new Board(this);
    listenerForBoard = (GameStateListener) panel;
    container.add(panel);
    // System.out.println("Runs here");

    // Sets up the reset button    
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    JButton resetButton = new JButton();
    resetButton.setText("Reset");
    resetButton.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        currentPlayer = 0;
        text.resetGame(players[currentPlayer]);
        panel.resetGame(players[currentPlayer]);
      }
    });
    buttonPanel.add(resetButton);
    container.add(buttonPanel);

    // Makes the frame visible
    frame.pack();
    frame.setVisible(true);
  } 

  /* This will get a player, based on their symbol.
   * 
   * @param   symbol    The symbol associated with the 
   *                    specific player 
   *
   * @return  the player that the program wants
  */
  public Player getPlayer(Util.Symbol symbol){
    for (int i = 0; i < players.length; i++){
      if (players[i].getSymbol() == symbol){
        return players[i];
      }
    }
    return null;
  }

  /* When called, this  will display an error message.
  */
  public void displayError() {
    listenerForText.displayError();
  }

  /* This calls the current player.
  * 
  * @return   the current player
  */
  public Player getCurrentPlayer(){
    return players[currentPlayer];
  }

  /*
   * This is activated to start the next turn
  */
  public void nextTurn(){
    currentPlayer = (currentPlayer + 1) % 2;
    listenerForText.onTurnChanged(players[currentPlayer]);
  }

  /*
   * This method triggers when there is a winner
   *
   * @param   winner   the winning player
  */
  public void declareWinner(Player winner){
    listenerForBoard.stopGame();
    if (winner != null){
      winner.playerWon();
      listenerForText.displayWinner(winner);
    }
    else {
      listenerForText.displayTie();
    }
    
  }

}