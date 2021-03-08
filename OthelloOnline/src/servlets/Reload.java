package servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Tells the client if their page is out of date (&, thus, needs
 * to be reloaded) by examining the client's current turn number
 * and comparing it to the server's turn number.
 * @author ubuntu
 *
 */
@WebServlet("/Reload")
public class Reload extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * Receives the client's turn number, compares it to the server's turn
	 * number, and sends a message to the client whether the two matched or
	 * were different.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PrintWriter out = response.getWriter();

		// The turn number in the URL
		int turnNumber = Integer.parseInt(request.getParameter("turnNumber"));

		
		// The turn number in the save file
		int expectedTurnNumber = Util.getTurnNumber(Util.SAVE_LOC);
		
		
		if (expectedTurnNumber != turnNumber)
			out.print("true");
		
		else
			out.print("false");
		
		
		out.close();
	}
}
