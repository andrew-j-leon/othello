package servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet that creates the "game over" screen.
 * @author ubuntu
 *
 */
@WebServlet("/GameOver")
public class GameOver extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

	/**
	 * Creates & sends the "game over" screen.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		// Create the head information of the HTML page
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"css/website.css\">");
		out.println("<link rel=\"stylesheet\" href=\"css/gamePage.css\">");
		out.println("<link rel=\"stylesheet\" href=\"css/gameOver.css\">");
		out.println("</head>");
		
		
		// Read the save file
		File saveFile = new File(Util.SAVE_LOC);
		
		
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
				
			case "board":
				board = Util.makeHtmlBoard(infile, "", turnNumber);  // Should change so it's not dependent on turnNumber being already defined.
				break;
				
			default:
				break;			
			}
		}
		

		// Compile the parts for the HTML page
		htmlPage = board + "<div class=\"Box Information\">" + 
			Util.makeTurnNumber(turnNumber) +
			makeHtmlWinner(player1Score, player2Score) +
			Util.makeHtmlScores(player1Score, player2Score) + 
			makeNewGameButton() +
			Util.makeHiddenTurnNumber(turnNumber) +
			"</div>";
		
		return htmlPage;
	}
	
	
	/**
	 * Makes the game over screen's new game button, which 
	 * creates a new game.
	 * 
	 * @return An HTML string for a new game button.
	 */
	private String makeNewGameButton()
	{
		return "<div><a href=\"NewGame\" class=\"NewGame\">New Game</a></div>";
	}
	
	
	/**
	 * Makes an HTML element stating the game's winner.
	 * @param player1Score Player 1's final score.
	 * @param player2Score Player 2's final score.
	 * @return An HTML string for the game's winner.
	 */
	private String makeHtmlWinner(String player1Score, String player2Score)
	{
		int p1Score = Integer.parseInt(player1Score);
		int p2Score = Integer.parseInt(player2Score);
		
		String winner;
		
		if (p1Score > p2Score)
			winner = "BLACK";
		
		else if (p1Score < p2Score)
			winner = "WHITE";
		
		else
			winner = "It's a TIE";
		
		
		return "<div><h1>The winner is: " + winner + "</h1></div>";
	}
	
	
}
