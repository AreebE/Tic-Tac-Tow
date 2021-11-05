

class Main {

/*
Interface defining the user interaction:
 - get move, 
 - display intro, 
 - displays results, 
 - play again

A console class implementing the interface
A GameManager class that creates the classes and plays the game
A Board class that knows the placement of the pieces, if won or not, prints self
A Piece class that represents X or O
A Player class that abstracts the choices of any type of player
A Move class represents a location on the board
*/

/* Features to add:
[ ] - Main menu

[X]  - Reset board (meaning clear all symbols) 

[X]  - Mark winning combination

[ ]  - Player chooses the symbol to use.

[X] - Show winner

[? ] - Remote players

[ ] - AI (or algorithm)

[~ ] - More players 

[X]  - Board bigger than 3 x 3

[? ] - Betting (Tournament of TicTacToe)

*/

  public static void main(String[] args) {
  
    javax.swing.SwingUtilities.invokeLater(new Game());
    
  }
}