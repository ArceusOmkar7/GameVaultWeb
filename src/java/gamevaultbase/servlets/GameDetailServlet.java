package gamevaultbase.servlets;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Review;
import gamevaultbase.exceptions.GameNotFoundException;
import gamevaultbase.management.GameManagement;
import gamevaultbase.management.ReviewManagement;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Mapped in web.xml as /game
public class GameDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String gameIdParam = request.getParameter("id");
        String errorMessage = null;
        Game game = null;
        List<Review> reviews = Collections.emptyList();

        GameManagement gameManagement = (GameManagement) getServletContext().getAttribute("gameManagement");
        ReviewManagement reviewManagement = (ReviewManagement) getServletContext().getAttribute("reviewManagement");

        if (gameManagement == null) {
             System.err.println("FATAL: GameManagement not found in ServletContext!");
             errorMessage = "Application error: Game service unavailable.";
        } else if (gameIdParam == null || gameIdParam.trim().isEmpty()) {
            errorMessage = "No game ID provided.";
        } else {
            try {
                int gameId = Integer.parseInt(gameIdParam.trim());
                game = gameManagement.getGame(gameId);
                
                // Fetch reviews for this game if review management is available
                if (reviewManagement != null) {
                    try {
                        reviews = reviewManagement.getReviewsForGame(gameId);
                    } catch (Exception e) {
                        System.err.println("Error retrieving reviews for game ID " + gameId + ": " + e.getMessage());
                        // Don't set error message to avoid disrupting main page functionality
                        // Just log the error and continue with empty reviews list
                    }
                }
            } catch (NumberFormatException e) {
                errorMessage = "Invalid game ID format.";
            } catch (GameNotFoundException e) {
                errorMessage = "Game not found.";
                // Optionally redirect to a 404 page or the games list
                // response.sendRedirect(request.getContextPath() + "/games?message=Game+not+found&messageType=error");
                // return;
            } catch (Exception e) {
                System.err.println("Error retrieving game details for ID " + gameIdParam + ": " + e.getMessage());
                e.printStackTrace();
                errorMessage = "An error occurred while retrieving game details.";
            }
        }

        // Pass messages from other actions (like AddToCart or ReviewServlet)
         String redirectMessage = request.getParameter("message");
         String redirectMessageType = request.getParameter("messageType");
         if (redirectMessage != null) {
             request.setAttribute("message", redirectMessage);
             request.setAttribute("messageType", redirectMessageType);
         }

        // Check if reviews attribute already exists (might be set by ReviewServlet)
        if (request.getAttribute("reviews") == null) {
            request.setAttribute("reviews", reviews);
        }
        
        request.setAttribute("game", game);
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/jsp/gameDetail.jsp").forward(request, response);
    }
}