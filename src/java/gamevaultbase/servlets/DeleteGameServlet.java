package gamevaultbase.servlets;

import gamevaultbase.storage.GameStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for handling game deletion from the admin dashboard.
 */
@WebServlet(name = "DeleteGameServlet", urlPatterns = { "/admin/delete-game" })
public class DeleteGameServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Check if user is logged in and is an admin
        if (session.getAttribute("loggedInUser") == null ||
                !Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            response.sendRedirect(request.getContextPath() + "/login?message=Admin access required&messageType=error");
            return;
        }

        // Get the game ID to delete
        String gameIdStr = request.getParameter("gameId");
        if (gameIdStr == null || gameIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/game-management?message=No game specified for deletion&messageType=error");
            return;
        }

        try {
            int gameId = Integer.parseInt(gameIdStr);

            // Delete the game
            GameStorage gameStorage = new GameStorage();
            gameStorage.delete(gameId);

            // Redirect with success message
            response.sendRedirect(request.getContextPath()
                    + "/admin/game-management?message=Game deleted successfully&messageType=success");
        } catch (NumberFormatException e) {
            response.sendRedirect(
                    request.getContextPath() + "/admin/game-management?message=Invalid game ID&messageType=error");
        } catch (Exception e) {
            // This could catch SQL exceptions like foreign key constraint violations
            response.sendRedirect(request.getContextPath() +
                    "/admin/game-management?message=Could not delete game. It may be referenced in orders or other records&messageType=error");
        }
    }
}