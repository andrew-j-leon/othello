package othelloGame;

import java.io.PrintStream;

import othelloGame.Board;
import othelloGame.Piece;
import othelloGame.Color;


/**
 * A class with static functions to display the game's GUI.
 * 
 * @author ubuntu
 *
 */
final public class GUI
{	
	/**
	 * Supposedly a good practice to have a private constructor
	 * for a class with no state.
	 */
	private GUI()
	{
	}
	
	
	static public void displayScores(PrintStream out, int player1Score, 
			int player2Score)
	{
		out.println("Player 1 score: " + 
				Integer.toString(player1Score));
		
		out.println("Player 2 score: " + 
				Integer.toString(player2Score) + "\n");
	}
	
	
	/**
	 * Displays the winner of the game.
	 * 
	 * @param out The output destination.
	 * @param player1Score Player 1's score.
	 * @param player2Score Player 2's score.
	 */
	static public void displayWinner(PrintStream out, int player1Score, int player2Score)
	{
		if (player1Score == player2Score)
			out.println("It's a tie!");
		
		else
		{
			String winnerName = (player1Score > player2Score)
					? "Player1" : "Player2";
			
			out.println(winnerName + " wins!");
		}
	}
	
	
	/**
	 * Outputs the board to stdout in a human-readable format.
	 * 
	 * @param out Standard output
	 * @param board The board to be represented.
	 */
	static public void displayBoard(PrintStream out, Board board)
	{
		Piece[][] boardArray = board.getArray();
		
		out.print("     ");
		for (int c = 0; c < boardArray.length; c++)
		{
			if (c != 0)
				out.print("    ");
			
			out.print(c);
		}
		
		out.println();
		
		for (int r = 0; r < boardArray.length; r++)
		{
			out.print(r);
			out.print("  ||");
			for (int c = 0; c < boardArray[0].length; c++)
			{
				out.print(pieceToString(boardArray[r][c]));
				out.print(" || ");
			}
				
			out.println();
			
			if (r != (boardArray.length - 1))
				out.println("   =========================================");
		}
	}
	
	
	/**
	 * Converts a piece to a string for printing to standard out.
	 * 
	 * @param piece The Piece object.
	 * @return The translated Piece to String. Pieces on the board
	 * are upper-case; suggestions are lower-case.
	 * 
	 * Ex: A black piece is B; a black suggestion is b.
	 */
	static private String pieceToString(Piece piece)
	{
		if (piece == null)  //No piece
			return " ";
		
		if (piece.getColor() == Color.BLACK) //Piece is black
		{
			if (piece.isPiece())
				return "B";
			
			return "b";
		}
		
		else  //Piece is white
		{
			if (piece.isPiece())
				return "W";
			
			return "w";
		}
	}
}
