public class Player {
  private Util.Symbol playerSymbol;
  private String name;
  private int timesPlayerWon;
  
  public Player(Util.Symbol symbol, String name){
    playerSymbol = symbol;
    this.name = name;
    timesPlayerWon = 0;
  }

  /* 
  *   Gets the symbol of the player.
  *
  *   @return   The symbol that player has.
  */
  public Util.Symbol getSymbol(){
    return playerSymbol;
  }


  /*
   * Gets the name of the player.
   *
   * @return    the name of the player.
  */
  public String getName(){
    System.out.println("Called get name, " + name);
    return name;
  }

  /*
   * It is called everytime this player wins.
  */
  public void playerWon(){
    timesPlayerWon++;
  }

  /*
   * Gets how many times this player has won.
   * 
   * @return  how many times this player has won.
  */
  public int getTimesPlayerWon(){
    return timesPlayerWon;
  }
}