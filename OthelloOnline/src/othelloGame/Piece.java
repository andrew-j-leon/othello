package othelloGame;

import othelloGame.Color;

public class Piece 
{
	// This piece's color.
	private Color color;
	
	// If true, its placement on the board signifies a 
	// placed piece. If false, signifies a possible location
	// the current player can put a piece.
	private boolean isPiece;
	
	
	/**
	 * Creates a Piece with the provided color.
	 * @param theColor The piece's starting color.
	 * @param theIsPiece True if this piece is a piece on the board, False
	 * if it's a possible location the current player can put a piece.
	 */
	public Piece(Color theColor, boolean theIsPiece)
	{
		color = theColor;
		isPiece = theIsPiece;
	}
	
	
	/**
	 * Returns true if this piece is a piece on the board & false if it's just
	 * a valid location the current player can place a piece.
	 * 
	 * @return True if this piece is a piece on the board & false if it's just
	 * a valid location the current player can place a piece.
	 */
	public boolean isPiece()
	{
		return isPiece;
	}
	
	
	/**
	 * Sets whether this Piece object should be a piece
	 * (true) or a suggestion (false).
	 * 
	 * @param theIsPiece If this Piece object should be
	 * a piece (true) or a suggestion (false).
	 */
	public void setPiece(boolean theIsPiece)
	{
		isPiece = theIsPiece;
	}
	
	
	/**
	 * Returns the color of this piece.
	 * @return This piece's color.
	 */
	public Color getColor()
	{
		return color;
	}
	
	
	/**
	 * Changes the color of this piece from black -> white
	 * & white -> black.
	 * 
	 * @return The new piece color.
	 */
	public Color flip()
	{
		if (color == Color.BLACK)
			color = Color.WHITE;
		
		else
			color = Color.BLACK;
		
		return color;
	}
	
	
	/**
	 * Primarily used for testing.
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
			return true;
		
		if (!(o instanceof Piece))
			return false;
		
		Piece piece = (Piece)o;
		
		return (this.color == piece.color && 
				this.isPiece == piece.isPiece);
	}
	
	/**
	 * Needed b/c we override equals.
	 */
	@Override
	public int hashCode()
	{
		int result = 17;
		
		result = 31 * result + color.hashCode();
		result = 31 * result + (isPiece ? 1 : 0); // 1 if isPiece = T; 0 if otherwise
		
		return result;
	}
}
