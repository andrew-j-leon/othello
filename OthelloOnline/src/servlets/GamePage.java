package servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import othelloGame.Color;
import othelloGame.Othello;
import othelloGame.Piece;

/**
 * Creates the HTML page for the main game screen.
 * @author ubuntu
 *
 */
@WebServlet("/GamePage")
public class GamePage extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * Generates (by reading the game save file) & sends the main game 
	 * screen to the client.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException, FileNotFoundException 
	{	
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"css/website.css\">");
		out.println("<link rel=\"stylesheet\" href=\"css/gamePage.css\">");
		out.println("</head>");
		
		// Read the save file
		File saveFile = new File(Util.SAVE_LOC);
		
		// If the save file doesn't exist, create it.
		if (!saveFile.exists())
			Util.createNewSaveFile(saveFile);
		
		// The game is finished.
		else if (isFinished(saveFile))
		{
			request.getRequestDispatcher("GameOver").forward(request, response);
			return;
		}
		
		
		// To read the save file
		Scanner infile = new Scanner(saveFile);
		
		// Output the html response
		out.println("<body>");
		out.println(makeHtmlPage(infile));
		out.println("<script src=\"js/reload.js\"></script>");
		out.println("</body>");
		out.println("</html>");
		
		// Close resources
		infile.close();
		out.close();
	}
	
	
	/**
	 * Takes the save file & returns true if it reads that
	 * the game is finished, false if otherwise.
	 * 
	 * @param saveFile The save file.
	 * @return True if the game is finishe, false if not.
	 * @throws IllegalArgumentException Thrown if saveFile isn't the
	 * save file.
	 * @throws FileNotFoundException Throw if the save file can't be
	 * accessed.
	 */
	private boolean isFinished(File saveFile) 
			throws IllegalArgumentException, FileNotFoundException
	{
		Scanner infile = new Scanner(saveFile);
		String line[] = infile.next().split(":");
		
		if (line[0].compareTo("finished") != 0)
		{
			infile.close();
			throw new IllegalArgumentException("File object should point to "
					+ "the first line in the save file.");
		}
			
		infile.close();
		return (line[1].compareTo("true") == 0);
	}
	
	
	
	/**
	 * Creates a new save file or overwrites
	 * the old save file if there is one.
	 * 
	 * @param saveLoc The path to the save file.
	 * @throws FileNotFoundException Thrown if the file
	 * cannot be created.
	 */
	private void createNewSaveFile(File saveFile) 
			throws FileNotFoundException
	{
		Othello game = new Othello();
		game.createNewGame();
		
		PrintWriter outfile = new PrintWriter(saveFile);
		game.saveGame(outfile);			
		outfile.close();
	}
	
	
	/**
	 * Takes a Scanner object pointing to the save file;
	 * returns a string containing the HTML page.
	 * 
	 * @param infile Points to the save file.
	 * @return The HTML page.
	 */
	private String makeHtmlPage(Scanner infile)
	{
		// The final page
		String htmlPage = "";
		
		// Important page components
		String player1Score = "";
		String player2Score = ""; 
		String currentPlayer = "";
		String board = "";
		String turnNumber = "";
		
		String[] nextLine;
		while (infile.hasNext())
		{
			nextLine = infile.next().split(":");
			
			switch (nextLine[0]) {
				
			case "turnNumber":
				turnNumber = nextLine[1];
				break;
			
			case "player1Score":
				player1Score = nextLine[1];
				break;
				
			case "player2Score":
				player2Score = nextLine[1];
				break;
				
			case "currentPlayer":
				currentPlayer = nextLine[1];
				break;
				
			case "board":
				board = Util.makeHtmlBoard(infile, "MakeMove", turnNumber);  // Should change so it's not dependent on turnNumber being already defined.
				break;
				
			default:
				break;			
			}
		}
		

		htmlPage = board + "<div class=\"Box Information\">" + 
				makeHtmlCurrentPlayer(currentPlayer) +
				Util.makeTurnNumber(turnNumber) +
				Util.makeHtmlScores(player1Score, player2Score) + 
				Util.makeHiddenTurnNumber(turnNumber) +
				"</div>";
		
		return htmlPage;
	}
	
	
	/**
	 * Creates the HTML div element that holds the
	 * turn number.
	 * 
	 * @param turnNumber The current turn number.
	 * @return An HTML div element with the turn number.
	 */
	private String makeTurnNumber(String turnNumber)
	{
		return "<div><h2>TurnNumber: " +
				turnNumber + "</h2></div>";
	}
	
	
	/**
	 * Creates the HTML div element that holds the current
	 * player scores.
	 * 
	 * @param player1Score Black player's score.
	 * @param player2Score White player's score.
	 * @return An HTML div element with the current scores.
	 */
	private String makeHtmlScores(String player1Score, 
			String player2Score)
	{
		return "<div> <p class=\"Score\">Black score: " + player1Score +
				"</p> <p class=\"Score\">White score: " + player2Score +
				"</p> </div>";
	}
	
	
	/**
	 * Creates an HTML div element that holds the color
	 * of the current player.
	 * 
	 * @param currentPlayer The current player's color.
	 * @return An HTML div element with the current player.
	 */
	private String makeHtmlCurrentPlayer(String currentPlayer)
	{
		return "<div><h1>Current Color: " +
				currentPlayer + "</div>";
	}
	
	
	
	/**
	 * Takes a Scanner object pointing to the first row of
	 * the game board in the save file; returns a string
	 * containing the HTML representation of that board.
	 * 
	 * @param infile Points to the first row of the game
	 * board in the save file.
	 * 
	 * @return An HTML representation of the board.
	 */
	private String makeHtmlBoard(Scanner infile, String turnNumber)
	{		
		String htmlBoard = "<div class=\"Box Board\"> <table>";
		
		htmlBoard += "<tr> <th> </th> <th>0</th> <th>1</th>"
				+ "<th>2</th> <th>3</th> <th>4</th>"
				+ "<th>5</th> <th>6</th> <th>7</th> </tr>";
		
		
		String nextRow[];
		int row = 0;
		
		while (infile.hasNext() && row < 8)
		{		
			nextRow = infile.next().split(";");
			
			htmlBoard += "<tr> <th>" + Integer.toString(row) 
				+ "</th>";
			
			for (int col = 0; col < nextRow.length; col++)
			{
				switch(nextRow[col]) {
				
				case "B":
					htmlBoard += "<td>" + "<div class=\"Piece\">" +
					"<img src=\"images/blackSolidPiece.png\" "
					+ "alt=\"Black Solid Piece\">"
						+ "</div>" + "</td>";
					break;
					
				case "b":
					
					htmlBoard += "<td>" + "<div class=\"Piece\">" +
					"<a href=\"MakeMove?row=" + Integer.toString(row) + 
					"&column=" + Integer.toString(col) +
					"&turnNumber=" + turnNumber + "\">" +
					"<img src=\"images/blackOutlinePiece.png\" "
					+ "alt=\"Black Outline Piece\">" +
						"</a>" + "</div>" + "</td>";
					break;
					
				case "W":
					htmlBoard += "<td>" + "<div class=\"Piece\">" +
					"<img src=\"images/whiteSolidPiece.png\" "
					+ "alt=\"White Solid Piece\">"
						+ "</div>" + "</td>";
					break;
					
				case "w":
					htmlBoard += "<td>" + "<div class=\"Piece\">" +
					"<a href=\"MakeMove?row=" + Integer.toString(row) + 
					"&column=" + Integer.toString(col) + 
					"&turnNumber=" + turnNumber + "\">" +
					"<img src=\"images/whiteOutlinePiece.png\" "
					+ "alt=\"White Outline Piece\">" +
						"</a>" + "</div>" + "</td>";
					break;
					
				case "null":
					htmlBoard += "<td>"
						+ "<div class=\"Piece\"></div>"
						+ "</td>";
					break;
					
				default:
					break;
				}
			}
			
			htmlBoard += "</tr>";
			
			row++;
		}
		
		htmlBoard += "</table> </div>";
		
		return htmlBoard;
	}
}
