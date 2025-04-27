package gamevaultbase.servlets;

import gamevaultbase.entities.Game;
import gamevaultbase.management.GameManagement;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
// Import annotation if using
// import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Mapped in web.xml as /games
public class GamesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        GameManagement gameManagement = (GameManagement) getServletContext().getAttribute("gameManagement");
        String errorMessage = null;
        List<Game> games = Collections.emptyList(); // Initialize to empty list

        if (gameManagement == null) {
            System.err.println("FATAL: GameManagement not found in ServletContext!");
            errorMessage = "Application error: Game service not available.";
        } else {
            try {
                games = gameManagement.getAllGames();
                if (games == null) {
                    // This might happen if findAll returns null on error in storage layer
                    errorMessage = "Could not retrieve games list at this time.";
                    games = Collections.emptyList(); // Ensure games is not null for JSP
                }
            } catch (Exception e) {
                System.err.println("Error retrieving games: " + e.getMessage());
                e.printStackTrace(); // Log stack trace
                errorMessage = "An error occurred while retrieving games.";
                 games = Collections.emptyList(); // Ensure games is not null for JSP
            }
        }

        // Pass any redirect messages through
        String redirectMessage = request.getParameter("message");
        String redirectMessageType = request.getParameter("messageType");

        if (redirectMessage != null) {
             request.setAttribute("message", redirectMessage);
             request.setAttribute("messageType", redirectMessageType);
        }

        request.setAttribute("gamesList", games);
        request.setAttribute("errorMessage", errorMessage); // Pass potential error message to JSP
        request.getRequestDispatcher("/WEB-INF/jsp/games.jsp").forward(request, response);
    }
}