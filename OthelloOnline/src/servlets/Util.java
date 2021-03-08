package servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import othelloGame.Othello;

/**
 * A bunch of static methods for servlets.
 * @author ubuntu
 *
 */
public class Util 
{
	
	static public final String SAVE_LOC = ".\\othelloSaves\\game1.txt";

	/**
	 * Ensures no one tries to make an object
	 * out of this stateless class.
	 */
	private Util()
	{
	}
	
	
	/**
	 * Gets the turn number from the save file.
	 * 
	 * @param saveLoc The save file location.
	 * @return The turn number from the save file.
	 * @throws FileNotFoundException If the save file doesn't exist.
	 * @throws IllegalArgumentException If the turn number isn't in the
	 * save file.
	 */
	static public int getTurnNumber(String saveLoc) 
			throws FileNotFoundException, IllegalArgumentException
	{
		File saveFile = new File(saveLoc);
		Scanner infile = new Scanner(saveFile);
		
		String line[];
		
		while (infile.hasNext())
		{
			line = infile.next().split(":");
			
			switch (line[0]) {
				
				case "turnNumber":
				{
					infile.close();
					return Integer.parseInt(line[1]);
				}
					
					
				default:
					break;
			}
		}
		
		infile.close();
		throw new IllegalArgumentException();
	}
	
	
	
	/**
	 * Creates the HTML div element that holds the current
	 * player scores.
	 * 
	 * @param player1Score Black player's score.
	 * @param player2Score White player's score.
	 * @return An HTML div element with the current scores.
	 */
	static public String makeHtmlScores(String player1Score, 
			String player2Score)
	{
		return "<div> <p class=\"Score\">Black score: " + player1Score +
				"</p> <p class=\"Score\">White score: " + player2Score +
				"</p> </div>";
	}
	
	
	
	/**
	 * Creates the HTML div element that holds the
	 * turn number.
	 * 
	 * @param turnNumber The current turn number.
	 * @return An HTML div element with the turn number.
	 */
	static public String makeTurnNumber(String turnNumber)
	{
		return "<div><h2>TurnNumber: " +
				turnNumber + "</h2></div>";
	}
	
	
	
	
	/**
	 * Takes a Scanner object pointing to the first row of
	 * the game board in the save file; returns a string
	 * containing the HTML representation of that board.
	 * 
	 * @param infile Points to the first row of the game
	 * board in the save file.
	 * @param link The file (ex: servlet) that the suggestion
	 * pieces will point to.
	 * @param turnNumber The current turn number.
	 * 
	 * @return An HTML representation of the board.
	 */
	static public String makeHtmlBoard(Scanner infile, String link, String turnNumber)
	{
		String baseUrl = (link.compareTo("") == 0) ? "" 
				: link + "?turnNumber=" + turnNumber;
		
		
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
				String url = "";
				
				switch(nextRow[col]) {
				
				case "B":
					htmlBoard += "<td>" + "<div class=\"Piece\">" +
					"<img src=\"images/blackSolidPiece.png\" "
					+ "alt=\"Black Solid Piece\">"
						+ "</div>" + "</td>";
					break;
					
				case "b":
					
					if (baseUrl.compareTo("") != 0)
					{
						url = baseUrl + "&row=" + Integer.toString(row) + 
										"&column=" + Integer.toString(col);
					}
					
					
					htmlBoard += "<td>" + "<div class=\"Piece\">" +
						"<a href=\"" + url + "\">" +
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
					if (baseUrl.compareTo("") != 0)
					{
						url = baseUrl + "&row=" + Integer.toString(row) + 
								"&column=" + Integer.toString(col);
					}
					
					
					htmlBoard += "<td>" + "<div class=\"Piece\">" +
						"<a href=\"" + url + "\">" +
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
	
	
	
	/**
	 * Creates a new save file or overwrites
	 * the old save file if there is one.
	 * 
	 * @param saveLoc The path to the save file.
	 * @throws FileNotFoundException Thrown if the file
	 * cannot be created.
	 */
	static public void createNewSaveFile(File saveFile) 
			throws FileNotFoundException
	{
		Othello game = new Othello();
		game.createNewGame();
		
		PrintWriter outfile = new PrintWriter(saveFile);
		game.saveGame(outfile);			
		outfile.close();
	}
	
	
	/**
	 * Makes an invisible HTML element with the game's turn number.
	 * Used by client-side Javascript to determine if the client 
	 * needs to reload an out-of-date page.
	 * 
	 * @param turnNumber The game's current turn number.
	 * @return An HTML string with a hidden turn number element.
	 */
	static public String makeHiddenTurnNumber(String turnNumber)
	{
		return "<p id=\"turnNumber\" hidden>" + turnNumber + "</p>";
	}
}
