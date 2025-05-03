package gamevaultbase.servlets;

import gamevaultbase.entities.Game;
import gamevaultbase.exceptions.GameNotFoundException;
import gamevaultbase.management.GameManagement;
import java.io.IOException;
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

        GameManagement gameManagement = (GameManagement) getServletContext().getAttribute("gameManagement");

        if (gameManagement == null) {
             System.err.println("FATAL: GameManagement not found in ServletContext!");
             errorMessage = "Application error: Game service unavailable.";
        } else if (gameIdParam == null || gameIdParam.trim().isEmpty()) {
            errorMessage = "No game ID provided.";
        } else {
            try {
                int gameId = Integer.parseInt(gameIdParam.trim());
                game = gameManagement.getGame(gameId);
                // GameNotFoundException will be caught below if game is null
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

        // Pass messages from other actions (like AddToCart)
         String redirectMessage = request.getParameter("message");
         String redirectMessageType = request.getParameter("messageType");
         if (redirectMessage != null) {
             request.setAttribute("message", redirectMessage);
             request.setAttribute("messageType", redirectMessageType);
         }

        request.setAttribute("game", game);
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/jsp/gameDetail.jsp").forward(request, response);
    }
}