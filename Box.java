import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Box extends JButton{
  private Util.Symbol symbol;
  private onBoxClickedListener board;
  private Util.Types typeOfWin;
  private boolean gameRunning;
  private boolean isPartOfWinningMove;
  private int row;
  private int column;

  public interface onBoxClickedListener {
    public Player hasWinner(int row, int col);
    public Player getPerson();
    public void nextTurn();
    public void printErrorMessage();
    public void hasWinner(Player winner);
  }

  public Util.Symbol getSymbol(){
    return symbol;
  }
  
  public Box(int r, int c, onBoxClickedListener board){
    startGame();
    row = r;
    column = c;
    this.board = board;
    addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        if (gameRunning){
          if (symbol == Util.Symbol.BLANK){
            symbol = board.getPerson().getSymbol();
            board.nextTurn();
            Player winner = board.hasWinner(r, c);
            if (winner != null){
              board.hasWinner(winner);
            }
          }
          else {
            board.printErrorMessage();
          }
        }
      }
    });
    // setMaximumSize(new Dimension(100, 100));
    setPreferredSize(new Dimension(100, 100));
    // setMinimumSize(new Dimension(50, 50));
  }

  /*
   * This paints the button, based on what symbol it is 
   * assigned and whether it it is part of the winning 
   * move.
   *
   * @param   g   The graphics, used for drawing the button.
  */
  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Rectangle bounds = g.getClipBounds();
    int left = (int) bounds.getX();
    int up = (int) bounds.getY();
    int right = left + (int) bounds.getWidth();
    int down = up + (int) bounds().getHeight();
    Color oldColor = g.getColor();
    switch (symbol){
      case X: 
        g.setColor(Color.BLACK);
        g.drawLine(left, up, 
            right, down);
        g.drawLine(left, down, 
            right, up);
        break;
      case O:
        g.drawOval(0, 0, (int) bounds.getWidth(), (int) bounds.getHeight());
        break;
    }
    if (isPartOfWinningMove){
      g.setColor(Color.RED);
      drawWinningMove(g);
    }
    g.setColor(oldColor);
  }

  /*
   * Assuming this button is a part of the winning move,
   * It is drawn with a red line going through it. How 
   * that red line is drawn depends on what type of move
   * the winning one is (ex. vertical or horizontal)
   *
   *  @param  g   The graphics used to draw this button.
  */
  private void drawWinningMove(Graphics g){
    Rectangle bounds = g.getClipBounds();
    int left = (int) bounds.getX();
    int up = (int) bounds.getY();
    int right = left + (int) bounds.getWidth();
    int down = up + (int) bounds().getHeight();
    switch (typeOfWin){
      case ROWS:
        int middleVert = (up + down) / 2;
        g.drawLine(left, middleVert, right, middleVert);
        break;
      case COLUMNS:
        int middleHoriz = (left + right) / 2;
        g.drawLine(middleHoriz, up, middleHoriz, down);
        break;
      case LEFT_DIAGONAL:
        g.drawLine(left, up, right, down);
        break;
      case RIGHT_DIAGONAL:
        g.drawLine(right, up, left, down);
        break;
    }
  }

  /*
   * Used whenever a game needs to be started.
  */
  public void startGame() {
    symbol = Util.Symbol.BLANK;
    gameRunning = true;
    setPartOfWinningMove(null);
    isPartOfWinningMove = false;
  }

  /*
   * Called whenever a game is stopped.
  */
  public void stopGame(){    
    gameRunning = false;
  }

  /*
   * Called if this box is a part of a winning move.
   *
   * @param   typeOfWin   The type of win the player 
   *                      got. (ex. rows or diagonals.) 
  */
  public void setPartOfWinningMove(Util.Types typeOfWin ){
    isPartOfWinningMove = true;
    this.typeOfWin = typeOfWin;
  }
  
}