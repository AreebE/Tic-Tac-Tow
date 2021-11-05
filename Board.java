import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements Box.onBoxClickedListener, Game.GameStateListener{
  
  private static final int STARTING_ROW = 0;
  private static final int STARTING_COL = 1;
  private static final int CHANGE_IN_ROW = 2;
  private static final int CHANGE_IN_COL = 3;

  Util.Types typeOfWinner;
  Point winningPoint;

  Util.Types[] allTypes = new Util.Types[]{
    Util.Types.ROWS, Util.Types.COLUMNS, Util.Types.LEFT_DIAGONAL, Util.Types.RIGHT_DIAGONAL
  };

  private Game game;
  private static final int SIZE = 3;
  private Box[][] boxes;
  int movesMade;

  /*
   * Intitializes a 2D array of boxes using the size element.
   * @param   game    A game object, used if methods 
   *                  need to be called.
  */
  public Board(Game game){
    movesMade = 0;
    typeOfWinner = null;
    winningPoint = null;
    setLayout(new GridLayout(SIZE, SIZE));
    this.game = game;
    boxes = new Box[SIZE][SIZE];
    for (int r = 1; r <= SIZE; r++){
      for (int c = 1; c <= SIZE; c++){
        Box current = new Box(r, c, this);
        boxes[r - 1][c - 1] = current;
        current.startGame();
        add(current);
      }
    }
  }

  /*
   * This checks if there is a winning player, once a 
   * move has been made. It only checks the relevant
   * categories, such as the row or column the move was 
   * in.
   *
   * If there is a winner, then the winning move is 
   * saved in this class, along with what type of win 
   * it was.
   *
   * @param   row   The row the move is in.
   *
   * @param   col   The column the move is in
   *
   * @return    the winning player. If there is none, it
   *            returns null.
  */
  @Override
  public Player hasWinner(int row, int col){
    movesMade++;
    for (Util.Types type: allTypes){
      int[] rowAndColValues = new int[] {0, 0, 0, 0};
      boolean setHasChanged = setRowAndColValues(rowAndColValues, type, row, col);
      if (!setHasChanged){
        continue;
      }
      // Initialize the first element
      int startingRow = rowAndColValues[STARTING_ROW];
      int startingCol = rowAndColValues[STARTING_COL];
      Util.Symbol current = boxes[startingRow][startingCol].getSymbol();
      startingRow += rowAndColValues[CHANGE_IN_ROW];
      startingCol += rowAndColValues[CHANGE_IN_COL];

      // Start the search
      if (current != Util.Symbol.BLANK){
        boolean hasStraightRow = true;
        while (startingCol < boxes.length && startingRow < boxes.length){
          if (boxes[startingRow][startingCol].getSymbol() != current){
            hasStraightRow = false;
            break;
          }
          startingRow += rowAndColValues[CHANGE_IN_ROW];
          startingCol += rowAndColValues[CHANGE_IN_COL];
        }
        if (hasStraightRow){
          typeOfWinner = type;
          winningPoint = new Point(row, col);
          return game.getPlayer(current);
        }
      }
    }
    if (movesMade == SIZE * SIZE){
      this.hasWinner(null);
    }
    return null;
  }

  /* 
   * Sets up certain values so the search algorithm knows
   * which boxes to check.
   *
   * @param   rowAndColValues   An array that contains the starting row, 
   *                            the starting column, the change in rows,
   *                            and the change in columns.
   *
   * @param   type              What type of win the algorithm is looking   
   *                            for.
   *
   * @param   row               The row of the current move.
   *
   * @param   col               The column of the current move.
   * 
   * @return    whether the array "rowAndColValues" changed or not. The 
   *          only reason it would not change is if it was not on the 
   *          diagonals and type was either "LEFT_DIAGONAL" or 
   *          "RIGHT_DIAGONAL". 
  */
  private boolean setRowAndColValues(int[] rowAndColValues, Util.Types type, int row, int col){
      switch (type){
        case ROWS:
          rowAndColValues[STARTING_ROW] = row - 1;
          rowAndColValues[CHANGE_IN_COL] = 1;
          break;
        case COLUMNS:
          rowAndColValues[STARTING_COL] = col - 1;
          rowAndColValues[CHANGE_IN_ROW] = 1;
          break;
        case LEFT_DIAGONAL:
          if (row == col) {
            rowAndColValues[CHANGE_IN_ROW] = 1;
            rowAndColValues[CHANGE_IN_COL] = 1;
          }
          else {
            return false;
          }
          break;
        case RIGHT_DIAGONAL:
          if (row + col == boxes.length + 1){
            rowAndColValues[STARTING_COL] = boxes.length - 1;
            rowAndColValues[CHANGE_IN_COL] = -1;
            rowAndColValues[CHANGE_IN_ROW] = 1;
          } 
          else {
            return false;
          }
          break;
      }
      return true;
  }
  
  /*
   * Called when a new game starts or the game needs to be reset.
   *
   * @param   currentPlayer   the current player.
  */
  @Override
  public void resetGame(Player currentPlayer) {
    movesMade = 0;
    typeOfWinner = null;
    winningPoint = null;
    repaint();
    for (int i = 0; i < boxes.length; i++){
      for (int j = 0; j < boxes[i].length; j++){
        boxes[i][j].startGame();
        boxes[i][j].repaint();
      }
    }
  }

  /*
   * Called when the game needs to stop.
  */
  @Override
  public void stopGame() {
    repaint();
    for (int i = 0; i < boxes.length; i++){
      for (int j = 0; j < boxes[i].length; j++){
        boxes[i][j].stopGame();
        if (typeOfWinner != null) {
          setWinningMoveOfBox(boxes[i][j], i + 1, j + 1);
        }
        boxes[i][j].repaint();
      }
    }
  }

  /*
   * Trigerred whenever there is a tie.
  */
  private void onTie(){
    hasWinner(null);
  }
  /*
   * Called when there is a winner and this method does a check to see if a 
   * box is a part of the winning move.
   *
   * @param 
  */
  private void setWinningMoveOfBox(Box box, int row, int col){
    switch (typeOfWinner){
            case ROWS:
              if (row == winningPoint.getX()){
                box.setPartOfWinningMove(typeOfWinner);
              }
              break;
            case COLUMNS:
              if (col == winningPoint.getY()){
                box.setPartOfWinningMove(typeOfWinner);
              }
              break;
            case LEFT_DIAGONAL:
              if (row == col){
                box.setPartOfWinningMove(typeOfWinner);
              }
              break;
            case RIGHT_DIAGONAL:
              if (row + col == SIZE + 1){
                box.setPartOfWinningMove(typeOfWinner);
              }
              break;
      }
  }

  /*
   * Asks the game to display an error message.
  */
  @Override
  public void printErrorMessage() {
    game.displayError();
  }

  /*
   * Asks the game for the current player
  */
  @Override
  public Player getPerson() {
    return game.getCurrentPlayer();
  }

  /*
   * Notifies the game to start the next turn.
  */
  @Override
  public void nextTurn(){
    game.nextTurn();
  }

  /*
   * Notifies the game there is a winner.
  */
  @Override
  public void hasWinner(Player winner){
    game.declareWinner(winner);
  }
}