package othelloGame;

import othelloGame.Piece;
import othelloGameExceptions.OccupiedCellException;


public class Board 
{
	// The board
	Piece[][] board;

	/**
	 * Creates a board with the initial pieces in the center.
	 */
	public Board()
	{
		board = new Piece[8][8];
		
		board[3][3] = new Piece(Color.WHITE, true);
		board[3][4] = new Piece(Color.BLACK, true);
		
		board[4][3] = new Piece(Color.BLACK, true);
		board[4][4] = new Piece(Color.WHITE, true);
	}
	
	/**
	 * Removes all pieces from the board (suggestion & real).
	 */
	public void clearAll()
	{
		for (int r = 0; r < board.length; r++)
		{
			for (int c = 0; c < board[0].length; c++)
				board[r][c] = null;
		}
	}
	
	/**
	 * Removes all suggestion pieces from the board.
	 */
	public void clearSuggestions()
	{
		for (int r = 0; r < board.length; r++)
		{
			for (int c = 0; c < board[0].length; c++)
			{
				if (board[r][c] != null && !board[r][c].isPiece())
					board[r][c] = null;
			}
		}
	}
	
	
	/**
	 * Flips the piece located at cell (row,col). Does
	 * nothing if there is no piece (suggestion or real)
	 * at that cell. Throws an exception if the cell is
	 * off the board.
	 * 
	 * @param row The cell's row.
	 * @param col The cell's column.
	 * @throws IndexOutOfBoundsException If the cell is off
	 * the board.
	 */
	public void flipPiece(int row, int col) throws IndexOutOfBoundsException
	{
		try {
			checkBounds(row, col);
			
			if (board[row][col] != null)
				board[row][col].flip();
		}
		
		catch (IndexOutOfBoundsException e) {
			throw e;
		}
	}
	
	
	/**
	 * Makes the piece located at cell (row, col) a piece
	 * (as opposed to a suggestion). Does nothing if the cell
	 * is empty or already contains a piece. Throws an exception
	 * if the cell is off the board.
	 * 
	 * @param row The cell's row.
	 * @param col The cell's column.
	 * @throws IndexOutOfBoundsException If the cell is off the board.
	 */
	public void makePiece(int row, int col) throws IndexOutOfBoundsException
	{
		try {
			checkBounds(row, col);
			
			if (board[row][col] != null)
				board[row][col].setPiece(true);
		}
		
		catch (IndexOutOfBoundsException e) {
			throw e;
		}
	}
	
	
	/**
	 * Returns a copy of the array that represents this board.
	 * Useful for when you want the board, but don't wanna
	 * fuck with it.
	 * 
	 * @return A copy of the array that represents this board.
	 */
	public Piece[][] getArray()
	{
		Piece[][] boardCopy = new Piece[8][8];
		
		for (int r = 0; r < board.length; r++)
		{
			for (int c = 0; c < board[0].length; c++)
				boardCopy[r][c] = board[r][c];
		}
			
		return boardCopy;
	}
	
	
	/**
	 * Places the given piece at the given row-column cell on this board.
	 * 
	 * @param row The cell's row.
	 * @param col The cell's column.
	 * @param piece The piece (could be a piece or suggestion).
	 * @throws IndexOutOfBoundsException Thrown when a cell outside this
	 * board is given.
	 * @throws OccupiedCellException Thrown when you are trying to put any
	 * piece in a cell with a normal piece.
	 */
	public void setPiece(int row, int col, Piece piece) 
			throws IndexOutOfBoundsException, OccupiedCellException
	{
		try {
			checkBounds(row, col);
			if (board[row][col] == null)
				board[row][col] = piece;
			
			else if (board[row][col].isPiece())
				throw new OccupiedCellException("There is a piece on that cell. If you wanna change the "
						+ "piece's color, flip it instead.");
			
			else
				board[row][col] = piece;
		}
		
		catch (IndexOutOfBoundsException e1) {
			throw e1;
		}
		
		catch (OccupiedCellException e2) {
			throw e2;
		}
	}
	
	
	/**
	 * Returns a copy of the piece located at cell row-col.
	 * Throws an exception if a cell off the board is specified.
	 * Returns null if there is no piece.
	 * 
	 * @param row The piece's row.
	 * @param col The piece's col.
	 * @return A copy of the piece so no one can mess with it.
	 * @throws IndexOutOfBoundsException Thrown when a cell outside this
	 * board is given. 
	 */
	public Piece getPiece(int row, int col) throws IndexOutOfBoundsException
	{
		try {
			checkBounds(row, col);
			Piece pieceCopy = board[row][col];
			return pieceCopy;
		}
		
		catch (IndexOutOfBoundsException e) {
			throw e;
		}
	}
	
	
	/**
	 * Checks that the given row-col cell is on the board. If not, throws an
	 * IndexOutOfBoundsException.
	 * 
	 * @param row The cell's row.
	 * @param col The cell's col.
	 * @throws IndexOutOfBoundsException Thrown when a cell outside this
	 * board is given.
	 */
	private void checkBounds(int row, int col) throws IndexOutOfBoundsException
	{
		if (row >= board.length || row < 0)
			throw new IndexOutOfBoundsException("Row out of bounds.");
		
		else if (col >= board[0].length || col < 0)
			throw new IndexOutOfBoundsException("Column out of bounds.");
		
		return;
	}
	
	
	/**
	 * Returns true if the given row-col cell is on the board
	 * & false if otherwise.
	 * @param row The cell's row.
	 * @param col The cell's column.
	 * @return True if the cell is on the board, false if 
	 * otherwise.
	 */
	public boolean cellInBounds(int row, int col)
	{
		try {
			checkBounds(row, col);
			return true;
		}
		
		catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	
	/**
	 * Returns true if the given row is within the 
	 * bounds of this board, false if not.
	 * 
	 * @param row The row.
	 * @return True if the given row is within the 
	 * bounds of this board, false if not.
	 */
	public boolean rowInBounds(int row)
	{
		return (row >= 0 && row < board.length);
	}
	
	/**
	 * Returns true if the given column is within the 
	 * bounds of this board, false if not.
	 * 
	 * @param column The row.
	 * @return True if the given row is within the 
	 * bounds of this board, false if not.
	 */
	public boolean columnInBounds(int column)
	{
		return (column >= 0 && column < board[0].length);
	}

	
	/**
	 * Returns the number of rows on this board.
	 * @return The number of rows on this board.
	 */
	public int getNumRows()
	{
		return board.length;
	}
	
	
	/**
	 * Returns the number of columns on this board.
	 * @return The number of columns on this board.
	 */
	public int getNumCols()
	{
		return board[0].length;
	}
	
	
	/**
	 * Returns the number of suggestion pieces of
	 * the provided color on this board.
	 * @return
	 */
	public int getNumSuggestions(Color color)
	{
		Piece piece;
		int sum = 0;
		for (int r = 0; r < board.length; r++)
		{
			for (int c = 0; c < board[0].length; c++)
			{
				piece = board[r][c];
				if (piece != null && piece.getColor() == color 
						&& !piece.isPiece())
					sum++;
			}
		}
		
		return sum;
	}
}
