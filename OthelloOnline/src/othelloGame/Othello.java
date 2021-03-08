package othelloGame;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;

import othelloGame.Board;


public class Othello {
	
	// The game board
	private Board board;
	
	// Each players scores
	int player1Score;
	int player2Score;
	
	// The current turn number
	int turnNumber;
	
	// Keeps track of the current player's turn
	// (black = player1; white = player2).
	Color currentPlayer;

	/**
	 * Creates the Othello game (w/o suggestions).
	 */
	public Othello()
	{
		board = new Board();
		
		player1Score = 2;
		player2Score = 2;
		
		turnNumber = 0;
		
		currentPlayer = Color.BLACK;
	}
	
	
	/**
	 * Saves the current state of this Othello game to the
	 * file pointed to be the provided PrintWriter object.
	 * 
	 * @param outfile The save file.
	 * @param finished Whether the current game state has concluded
	 * or not.
	 */
	public void saveGame(PrintWriter outfile)
	{		
		if (isFinished())
			outfile.print("finished:true\n");
		else
			outfile.print("finished:false\n");
		
		outfile.printf("turnNumber:%d\n", turnNumber);
		outfile.printf("player1Score:%d\n", player1Score);
		outfile.printf("player2Score:%d\n", player2Score);
		
		String currentPlayerStr = (currentPlayer == Color.BLACK) 
				? "BLACK" : "WHITE";
		outfile.printf("currentPlayer:%s\n", currentPlayerStr);
		
		outfile.print("board:\n");
		
		saveBoard(outfile);
	}
	
	
	/**
	 * Used by the function saveGame to save the current state of the
	 * game board.
	 * 
	 * @param outfile The save file.
	 */
	private void saveBoard(PrintWriter outfile)
	{
		Piece piece;
		
		for (int row = 0; row < board.getNumRows(); row++)
		{
			for (int col = 0; col < board.getNumCols(); col++)
			{
				piece = board.getPiece(row, col);
				
				if (piece == null)
					outfile.print("null;");
				
				else if (piece.getColor() == Color.BLACK)
				{
					if (piece.isPiece())
						outfile.print("B;");
					
					else
						outfile.print("b;");
				}
					
				else
				{
					if (piece.isPiece())
						outfile.print("W;");
					
					else
						outfile.print("w;");
				}
			}
			
			outfile.println();
		}
	}
	
	
	/**
	 * Loads the Othello game state from the save file 
	 * pointed to by the provided Scanner object.
	 * 
	 * @param infile The save file.
	 */
	public void loadGame(Scanner infile)
	{
		while (infile.hasNext())
		{
			String line[] = infile.next().split(":");
			
			switch (line[0]) {
			
			case "finished":
				if (line[1].compareTo("true") == 0)
				{
					createNewGame();
					return;
				}
				break;
			
			case "turnNumber":
				turnNumber = Integer.parseInt(line[1]);
				break;
			
			case "player1Score":
				player1Score = Integer.parseInt(line[1]);
				break;
				
			case "player2Score":
				player2Score = Integer.parseInt(line[1]);
				break;
				
			case "currentPlayer":
				currentPlayer = (line[1].compareTo("BLACK") == 0) 
					? Color.BLACK : Color.WHITE;
				break;
				
			case "board":
				loadBoard(infile);
				break;
				
			default:
				break;			
			}
		}
	}
	
	
	/**
	 * Used by loadGame. Takes the Scanner object starting at the first row of
	 * the board in the save file & loads the entire board into this game instance.
	 * 
	 * @param infile The Scanner object starting at the first row of the board
	 * in the save file.
	 */
	private void loadBoard(Scanner infile)
	{
		board.clearAll();
		
		int row = 0;
		
		while (infile.hasNext() && row < board.getNumRows())
		{			
			String nextRow[] = infile.next().split(";");
			for (int col = 0; col < nextRow.length; col++)
			{
				switch(nextRow[col]) {
			
				case "B":
					board.setPiece(row, col, new Piece(Color.BLACK, true));
					break;
					
				case "b":
					board.setPiece(row, col, new Piece(Color.BLACK, false));
					break;
					
				case "W":
					board.setPiece(row, col, new Piece(Color.WHITE, true));
					break;
					
				case "w":
					board.setPiece(row, col, new Piece(Color.WHITE, false));
					break;
					
				case "null":
					break;
					
				default:
					break;
				}
			}
			
			row++;
		}
	}
	
	
	/**
	 * Loads this game instance with a new game of Othello.
	 */
	public void createNewGame()
	{
		board = new Board();
		
		player1Score = 2;
		player2Score = 2;
		
		turnNumber = 0;
		
		currentPlayer = Color.BLACK;
		placeSuggestions();
	}
	
	
	/**
	 * Plays a single turn of Othello. No GUI.
	 * 
	 * @param row The selected suggest piece's row.
	 * @param col The selected suggest piece's column.
	 */
	public void playTurn(int row, int col)
	{	
		makeMove(row, col);
		
		board.clearSuggestions();
		
		changePlayer();
		
		placeSuggestions();	
		
		turnNumber++;
	}
	
	
	
	/**
	 * Plays a single turn of Othello. Uses a console based UI.
	 * 
	 * @param input Input source.
	 * @param out Output destination (for printing the UI).
	 */
	public void playTurn(Scanner input, PrintStream out)
	{
		GUI.displayScores(out, player1Score, player2Score);
		
		GUI.displayBoard(out, board);
		
		System.out.println("\n");
		
		makeMove(out, input);
		
		board.clearSuggestions();
		
		changePlayer();
		
		placeSuggestions();	
	}
	
	
	/**
	 * Plays an entire Othello game using a console based UI.
	 */
	public void run()
	{
		Scanner input = new Scanner(System.in);
		
		
		while (!isFinished())
		{
			placeSuggestions();
			
			GUI.displayScores(System.out, player1Score, player2Score);
			
			GUI.displayBoard(System.out, board);
			
			System.out.println("\n");
			
			makeMove(System.out, input);
			
			board.clearSuggestions();
			
			changePlayer();
		}
		
		input.close();
		
		displayFinishScreen(System.out);
	}
	
	
	/**
	 * Displays the final screen for the game using the 
	 * provided PrintStream object.
	 * 
	 * @param out The destination for the final screen.
	 */
	public void displayFinishScreen(PrintStream out)
	{
		GUI.displayBoard(System.out, board);
		
		System.out.println();
		
		GUI.displayScores(System.out, player1Score, player2Score);
		
		System.out.println();
		
		GUI.displayWinner(System.out, player1Score, player2Score);
	}
	
	
	/**
	 * Returns the opposite color of the provided one.
	 * 
	 * @param player A color representing the player.
	 * @return The color opposite the provided one.
	 */
	private Color getOppositePlayer(Color player)
	{
		return (player == Color.BLACK) 
				? Color.WHITE : Color.BLACK;
	}
	
	
	/**
	 * Changes this Othello object's current player to
	 * the opposite player.
	 */
	private void changePlayer()
	{
		currentPlayer = (currentPlayer == Color.BLACK) 
				? Color.WHITE : Color.BLACK; 
	}
	
	
	/**
	 * True if the game is finished, false if 
	 * otherwise.
	 * 
	 * @return True if the game is finished, false if
	 * otherwise.
	 */
	public boolean isFinished()
	{
		return (player1Score + player2Score == 64 || 
				player1Score == 0 || player2Score == 0
				|| board.getNumSuggestions(currentPlayer) == 0);
	}
	
	
	/**
	 * Places suggestion pieces (places where the current
	 * player can put a piece down) on the board.
	 */
	private void placeSuggestions()
	{			
		for (int r = 0; r < board.getNumRows(); r++)
		{
			for (int c = 0; c < board.getNumCols(); c++)
			{
				if (GameLogic.isValidSuggestion(board, currentPlayer, r, c))
					board.setPiece(r, c, new Piece(currentPlayer, false));
			}			
		}
	}
	
	
	/**
	 * Takes the row & column of the cell with a valid
	 * suggestion piece, then makes the move by flipping
	 * all required pieces & making the suggestion piece
	 * a true piece. No GUI.
	 * 
	 * @param row The selected row of the suggestion
	 * piece.
	 * @param col The selected column of the suggestion
	 * piece.
	 */
	private void makeMove(int row, int col)
	{		
		flipPieces(row, col);
		
		board.makePiece(row, col);
		
		changeScore(currentPlayer, 1);
	}
	
	
	
	/**
	 * Asks the current player to make a move, receives
	 * their input, and applies the change to the 
	 * game board.
	 * 
	 * @param out Output destination.
	 * @param input Input location.
	 */
	private void makeMove(PrintStream out, Scanner input)
	{
		int row, col;
		
		String playerName = (currentPlayer == Color.BLACK) 
				? "Player1" : "Player2";
		
		do {
			out.println(playerName + ", make your move...");
			
			row = getRow(out, input);
			
			col = getColumn(out, input);
			
			if (!GameLogic.isValidMove(board, currentPlayer, row, col))
				out.println("Invalid move. Pick a cell with a suggested cell.");
		}
		while (!GameLogic.isValidMove(board, currentPlayer, row, col));
		
		flipPieces(row, col);
		
		board.makePiece(row, col);
		
		changeScore(currentPlayer, 1);
	}
	
	
	/**
	 * Adds scoreChange to the score of the provided player.
	 * 
	 * @param player The player whose score will change.
	 * @param scoreChange The score change.
	 */
	private void changeScore(Color player, int scoreChange)
	{
		if (player == Color.BLACK)
			player1Score += scoreChange;
		
		else
			player2Score += scoreChange;
	}
	
	
	/**
	 * Given the row & column of a (correctly placed) suggest
	 * piece for the current player, flips all the surrounding pieces 
	 * that need to flip when the current player places their piece
	 * on said cell (but does not turn the suggest piece to
	 * a real piece).
	 * 
	 * @param suggestRow The row of the suggest piece.
	 * @param suggestCol The column of the suggest piece.
	 */
	private void flipPieces(int suggestRow, int suggestCol)
	{
		boolean[][] nextTo = GameLogic.getNextTo(board, 
				getOppositePlayer(currentPlayer), suggestRow, suggestCol);
		
		boolean[][] nextToValidLines = GameLogic.getNextToValidLines(board, nextTo, 
				currentPlayer, suggestRow, suggestCol);
		
		for (int r = 0; r < nextToValidLines.length; r++)
		{
			for (int c = 0; c < nextToValidLines[0].length; c++)
			{
				if (nextToValidLines[r][c])
					flipPiecesHelper(suggestRow, suggestCol, r, c);				
			}
		}
	}
	
	
	/**
	 * Takes the row & column of the suggestion piece & the
	 * row & column of a true cell from boolean map from
	 * GameLogic.nextToValidLines, and flips all pieces
	 * on the board in the direction of the true cell on the
	 * game board.
	 * 
	 * @param suggestRow The row of the suggestion cell.
	 * @param suggestCol The column of the suggestion cell.
	 * @param nextToValidLinesRow The row of the valid lines cell.
	 * @param nextToValidLinesCol The column of the valid lines cell.
	 */
	private void flipPiecesHelper(int suggestRow, int suggestCol, 
			int nextToValidLinesRow, int nextToValidLinesCol)
	{		
		int changeRow = nextToValidLinesRow - 1;
		int changeCol = nextToValidLinesCol - 1;
		
		int nextRow = suggestRow + changeRow;
		int nextCol = suggestCol + changeCol;
		
		while (board.getPiece(nextRow, nextCol).getColor() 
				!= currentPlayer)
		{
			board.flipPiece(nextRow, nextCol);
			
			changeScore(currentPlayer, 1);
			changeScore(getOppositePlayer(currentPlayer), -1);
			
			nextRow += changeRow;
			nextCol += changeCol;
		}
	}
	
	
	/**
	 * Prompts for, receives, and returns the player's
	 * selected row.
	 * 
	 * @param out Output destination.
	 * @param input Input location.
	 * @return The player's selected row.
	 */
	private int getRow(PrintStream out, Scanner input)
	{
		out.print("Pick a row: ");
		
		int result = getIntegerInput(out, input);
		
		while (!board.rowInBounds(result))
		{
			out.println("Row out of bounds.");
			out.print("Pick a row: ");
			
			result = getIntegerInput(out, input);
		}
		
		return result;
	}
	
	
	/**
	 * Prompts for, receives, and returns the player's
	 * selected column.
	 * 
	 * @param out Output destination.
	 * @param input Input location.
	 * @return The player's selected column.
	 */
	private int getColumn(PrintStream out, Scanner input)
	{
		out.print("Pick a column: ");
		
		int result = getIntegerInput(out, input);
		
		while (!board.columnInBounds(result))
		{
			out.println("Column out of bounds.");
			out.print("Pick a column: ");
			
			result = getIntegerInput(out, input);
		}
		
		return result;
	}
	
	
	/**
	 * Continually prompts the users to provide an integer input. Then,
	 * returns the integer input.
	 * 
	 * @param out Allows for printing to the console.
	 * @param input Allows for player input.
	 * @return The integer input.
	 */
	private int getIntegerInput(PrintStream out, Scanner input)
	{
		while (!input.hasNextInt())
		{
			input.next(); // Discard non-integer input
			out.print("Invalid input. Provide an integer value: ");
		}
		
		int result = input.nextInt();
		
		return result;
	}
}
