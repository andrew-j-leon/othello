package servlets;

import servlets.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import othelloGame.Othello;

/**
 * Alters the game save file to reflect a move by the player.
 * @author ubuntu
 *
 */
@WebServlet("/MakeMove")
public class MakeMove extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Receives a VALID row and column of the cell the current player wishes to 
	 * place their piece, then alters the game save file to reflect that move.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		// Get the selected row, col, & turn number from the URL parameters
		int row = Integer.parseInt(request.getParameter("row"));
		int col = Integer.parseInt(request.getParameter("column"));
		int turnNumber = Integer.parseInt(request.getParameter("turnNumber"));
		
		
		// Get the turn number from the save file
		int expectedTurnNumber = Util.getTurnNumber(Util.SAVE_LOC);
		
		// WARNING: There's a chance that the out of date page & current page have the 
		// same turn number!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		if (turnNumber != expectedTurnNumber)
		{
			request.getRequestDispatcher("GamePage").forward(request, response);
			return;
		}
			
		
		// Create the game (create new game if there's no saved
		// game; load saved game if there is)
		Othello game = new Othello();
		File saveFile = new File(Util.SAVE_LOC);
				
				
		if (!saveFile.exists())
			game.createNewGame();
				
		else
		{
			Scanner infile = new Scanner(saveFile);
			game.loadGame(infile);			
			infile.close();
		}
		

		game.playTurn(row, col);
				

		// Save the game. 
		PrintWriter outfile = new PrintWriter(Util.SAVE_LOC);
		game.saveGame(outfile);			
		outfile.close();
		
		request.getRequestDispatcher("GamePage").forward(request, response);
	}	
}
