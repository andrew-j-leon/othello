package servlets;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Creates a new Othello game on the server side.
 * @author ubuntu
 *
 */
@WebServlet("/NewGame")
public class NewGame extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * Creates a new Othello game by altering the save file, then
	 * redirects to the game page.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {		
		
		// Read the save file
		File saveFile = new File(Util.SAVE_LOC);
		
		// Create a new game by altering the save file
		Util.createNewSaveFile(saveFile);
		
		// Go to the game page
		request.getRequestDispatcher("GamePage").forward(request, response);
	}
}
