package gamevaultbase.servlets;

import gamevaultbase.servlets.base.AdminBaseServlet;
import gamevaultbase.storage.GameStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for handling game deletion from the admin dashboard.
 */
@WebServlet(name = "DeleteGameServlet", urlPatterns = { "/admin/delete-game" })
public class DeleteGameServlet extends AdminBaseServlet {

    @Override
    protected void processAdminGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Deletion should be done via POST only
        redirectWithMessage(request, response, "/admin/game-management",
                "Game deletion requires a POST request", "error");
    }

    @Override
    protected void processAdminPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the game ID to delete
        String gameIdStr = request.getParameter("gameId");
        if (gameIdStr == null || gameIdStr.trim().isEmpty()) {
            redirectWithMessage(request, response, "/admin/game-management",
                    "No game specified for deletion", "error");
            return;
        }

        try {
            int gameId = Integer.parseInt(gameIdStr);

            // Delete the game
            GameStorage gameStorage = new GameStorage();
            gameStorage.delete(gameId);

            // Redirect with success message
            redirectWithMessage(request, response, "/admin/game-management",
                    "Game deleted successfully", "success");
        } catch (NumberFormatException e) {
            redirectWithMessage(request, response, "/admin/game-management",
                    "Invalid game ID", "error");
        } catch (Exception e) {
            // This could catch SQL exceptions like foreign key constraint violations
            redirectWithMessage(request, response, "/admin/game-management",
                    "Could not delete game. It may be referenced in orders or other records", "error");
        }
    }
}