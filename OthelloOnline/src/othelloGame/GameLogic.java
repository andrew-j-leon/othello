package othelloGame;

import othelloGame.*;

/**
 * A class with static functions that define the
 * game's rules.
 * 
 * @author ubuntu
 *
 */
final public class GameLogic 
{
	/**
	 * So no one foolishly tries to create an 
	 * object of this class.
	 */
	private GameLogic() {}
	
	/**
	 * Determines if the given color suggestion can be placed at the given row-column
	 * cell.
	 * 
	 * @param board The game board.
	 * @param row The cell row.
	 * @param col The cell column.
	 * @param playerColor The piece color.
	 * @return True if the given color piece can be placed, false if otherwise.
	 */
	static public boolean isValidSuggestion(Board board, Color playerColor, int row, int col)
	{
		// First, the cell must be empty. If not, return
		// false.
		if (board.getPiece(row, col) != null)
			return false;
		
		// Second, there must be a cell next to row-col
		// that contains a piece of the opposite color. 
		// If not, return false.
		Color oppositeColor = GameLogic.flipColor(playerColor);
		boolean[][] nextTo = getNextTo(board, oppositeColor, row, col);
		
		boolean found = false;
		for (int r = 0; r < nextTo.length; r++)
		{
			for (int c = 0; c < nextTo[0].length; c++)
			{
				if (r == 1 && c == 1)
					continue;
				
				if (nextTo[r][c])
					found = true;
			}
		}
		
		if (!found)
			return false;
		
		
		// Finally, there must be a straight path from 
		// the row-col cell in the direction of one of the
		// cells with a piece of opposite color. This straight path
		// must consist of only pieces of opposite color & must end 
		// in a valid cell containing a piece of the same color.
		
		
		boolean[][] nextToValidLines = getNextToValidLines(board, 
				nextTo, playerColor, row, col);
		
		for (int r = 0; r < nextToValidLines.length; r++)
		{
			for (int c = 0; c < nextToValidLines[0].length; c++)
			{
				if (nextToValidLines[r][c])
					return true;
			}
		}
		
		return false;
		
		/*int nextRow, nextCol;
		
		for (int r = 0; r < nextTo.length; r++)
		{
			for (int c = 0; c < nextTo[0].length; c++)
			{
				nextRow = row - 1 + r;
				nextCol = col - 1 + c;
				
				// There's a valid straight path from the row-col cell
				// in the direction of one of the cells with a piece of
				// opposite color.
				if (nextTo[r][c] && isValidLine(board, color, row, col, nextRow, nextCol))
					return true;
			}
		}
		
		return false;*/
	}
	
	
	/**
	 * Takes the output of the the function "getNextTo" & replaces
	 * all "true" cells with false if it does not lead to a valid line.
	 * 
	 * @param board The game board.
	 * @param nextTo The output from the function "nextTo".
	 * @param row The center row.
	 * @param col The center column
	 * @return nextTo, but cells that do not lead to a valid line are 
	 * false.
	 */
	static public boolean[][] getNextToValidLines(Board board, boolean[][] nextTo, 
			Color playerColor, int row, int col)
	{
		/*int nextRow, nextCol;
		
		for (int r = 0; r < nextTo.length; r++)
		{
			for (int c = 0; c < nextTo[0].length; c++)
			{
				nextRow = row - 1 + r;
				nextCol = col - 1 + c;
				
				// If there's NOT valid straight path from the row-col cell
				// in the direction of one of the cells with a piece of
				// opposite color, make that corresponding cell in nextTo false.
				// Otherwise, make it true.
				nextTo[r][c] = (nextTo[r][c] && 
						isValidLine(board, playerColor, row, col, nextRow, nextCol));
			}
		}*/
		
		for (int r = 0; r < nextTo.length; r++)
		{
			for (int c = 0; c < nextTo[0].length; c++)
			{				
				// If there's NOT valid straight path from the row-col cell
				// in the direction of one of the cells with a piece of
				// opposite color, make that corresponding cell in nextTo false.
				// Otherwise, make it true.
				nextTo[r][c] = (nextTo[r][c] && 
						isValidLine(board, playerColor, row, col, r, c));
			}
		}
		
		return nextTo;
	}
	
	
	/**
	 * Returns the opposite color of the provided color
	 * 
	 * @param color The provided color.
	 * @return Opposite color of the provided color.
	 */
	static private Color flipColor(Color color)
	{
		if (color == Color.BLACK)
			return Color.WHITE;
		
		else
			return Color.BLACK;
	}
	
	
	/**
	 * Returns a 3x3 boolean table where the center 
	 * represents the given board row-col cell and all other
	 * boolean table cells represent whether there is the
	 * given color'd piece (NOT suggestion) next to the center (T) or not (F).
	 * 
	 * @param color The color that the center row should be "next to".
	 * @param row The cell's row.
	 * @param col The cell's col.
	 * @return A 3x3 boolean table where the center 
	 * represents the given board row-col cell and all other
	 * boolean table cells represent whether there is the
	 * given color next to the center (T) or not (F).
	 */
	static public boolean[][] getNextTo(Board board, Color color, int row, int col)
	{	
		boolean[][] result = new boolean[3][3];
		int newRow, newCol;
		
		// Searches (from left -> right) the row above the given
		// row, the row at the given row, & the row below
		// the given row.
		for (int boolRow = 0; boolRow <= 2; boolRow++)
		{
			newRow = row - 1 + boolRow;
			for (int boolCol = 0; boolCol <= 2; boolCol++)
			{
				// Skip the provided cell
				if (boolRow == 1 && boolCol == 1)
					continue;
				
				newCol = col - 1 + boolCol;
				
				result[boolRow][boolCol] = (board.cellInBounds(newRow, newCol) && 
						board.getPiece(newRow, newCol) != null &&
						board.getPiece(newRow, newCol).getColor() == color &&
						board.getPiece(newRow, newCol).isPiece());
			}	
		}
		
		return result;
	}
	
	
	/**
	 * Determines if there's a "valid" straight path from 
	 * the centerRow-centerCol cell in the direction of the cell given by
	 * nextRow-nextCol. This straight path must consist of only pieces 
	 * of opposite color & must end in a valid cell containing a 
	 * piece (not a suggestion) of the same color as parameter "color".
	 * 
	 * @param board The game board.
	 * @param color The color of the center cell.
	 * @param centerRow The row of the center cell.
	 * @param centerCol The column of the center cell.
	 * @param nextToRow The row of boolean cell from getNextTo.
	 * @param nextCol The column of the boolean cell from getNextTo.
	 * @return True if it's a valid straight path, false if otherwise.
	 */
	static public boolean isValidLine(Board board, Color playerColor, int centerRow, 
			int centerCol, int nextToRow, int nextToCol) throws IllegalArgumentException
	{
		// From the center cell & next cell, we determine the direction
		// our line should go (w/ respect to the center cell).
		
		int changeRow = nextToRow - 1;
		int changeCol = nextToCol - 1;
		
		if (changeRow == -1 || changeRow == 0 || changeRow == 1)
		{
			if (changeCol == -1 || changeCol == 0 || changeCol == 1)
				return isValidLineHelper(board, playerColor, centerRow, 
						centerCol, changeRow, changeCol);
			
			else
				throw new IllegalArgumentException("Invalid nextToCol.");
		}
		
		else
			throw new IllegalArgumentException("Invalid nextToCol.");
	}
	
	
	/**
	 * Helper function for isValidLine. Returns true if there is a 
	 * valid straight line from the center cell (centerRow, centerCol)
	 * in the direction specified by rowChange & colChange.
	 * 
	 * @param board The game board.
	 * @param centerColor The center color
	 * @param centerRow The center cell's row.
	 * @param centerCol The center cell's column.
	 * @param rowChange The row change from the center cell needed to
	 * move in the direction of the line.
	 * @param colChange The column change from the center cell needed
	 * to move in the direction of the line.
	 * @return True if there is a valid straight line from the center 
	 * cell (centerRow, centerCol) in the direction specified by 
	 * rowChange & colChange.
	 */
	static private boolean isValidLineHelper(Board board, Color centerColor, 
			int centerRow, int centerCol, int rowChange, int colChange)
	{
		int nextRow = centerRow + rowChange;
		int nextCol = centerCol + colChange;
		
		Piece nextPiece = board.getPiece(nextRow, nextCol);
		
		if (nextPiece == null || nextPiece.isPiece() 
				&& nextPiece.getColor() == centerColor)
			return false;
		
		nextRow += rowChange; nextCol += colChange;
		while (board.cellInBounds(nextRow, nextCol))
		{		
			nextPiece = board.getPiece(nextRow, nextCol);

			if (nextPiece == null || !nextPiece.isPiece())
				return false;
			
			if (nextPiece.getColor() == centerColor)
				return true;
			
			nextRow += rowChange; nextCol += colChange;
		}
		
		return false;
	}
	
	
	/**
	 * Determines if the current player can put a piece at cell
	 * (row, col). Requires the cell to contain an accurate suggestion
	 * piece of the same color as the current player to return 
	 * true.
	 * 
	 * @param board The game board.
	 * @param currentPlayer The color of the current player.
	 * @param row The row of the cell in question.
	 * @param col The column of the cell in question.
	 * @return True if the suggested move is valid for the 
	 * current player, false if not.
	 * @Throws IndexOutOfBoundsException Thrown if the given cell
	 * is not on the board.
	 */
	static public boolean isValidMove(Board board, Color currentPlayer, 
			int row, int col) throws IndexOutOfBoundsException
	{
		try {
			Piece expected = new Piece(currentPlayer, false);
			Piece actual = board.getPiece(row, col);
			return (expected.equals(actual));
		}
		
		catch (IndexOutOfBoundsException e) {
			throw e;
		}
	}
}