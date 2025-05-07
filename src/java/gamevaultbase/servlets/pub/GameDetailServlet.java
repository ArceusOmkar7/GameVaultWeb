package gamevaultbase.servlets.pub;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Review;
import gamevaultbase.exceptions.GameNotFoundException;
import gamevaultbase.management.ReviewManagement;
import gamevaultbase.servlets.base.PublicBaseServlet;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "GameDetailServlet", urlPatterns = { "/game" })
public class GameDetailServlet extends PublicBaseServlet {

    @Override
    protected void processPublicGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String gameIdParam = request.getParameter("id");
        String errorMessage = null;
        Game game = null;
        List<Review> reviews = Collections.emptyList();

        ReviewManagement reviewManagement = (ReviewManagement) getServletContext().getAttribute("reviewManagement");

        if (gameIdParam == null || gameIdParam.trim().isEmpty()) {
            errorMessage = "No game ID provided.";
        } else {
            try {
                int gameId = Integer.parseInt(gameIdParam.trim());
                game = getGameManagement().getGame(gameId);

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
        forwardToJsp(request, response, "/WEB-INF/jsp/gameDetail.jsp");
    }
}
