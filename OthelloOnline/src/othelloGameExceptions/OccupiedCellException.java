package othelloGameExceptions;

/**
 * An exception that occurs when a specified cell
 * on the board is occupied.
 * 
 * @author ubuntu
 *
 */
public class OccupiedCellException extends RuntimeException
{
	private static final long serialVersionUID = 7264373199189944638L;

	public OccupiedCellException() {}
	
	public OccupiedCellException(String message)
	{
		super(message);
	}
}
