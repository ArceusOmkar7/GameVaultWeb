package gamevaultbase.servlets.admin;

import gamevaultbase.entities.Game;
import gamevaultbase.servlets.base.AdminBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for handling the game management interface in the admin dashboard.
 * Provides functionality to view, filter, and sort games from the admin panel.
 */
@WebServlet(name = "GameManagementServlet", urlPatterns = { "/admin/game-management" })
public class GameManagementServlet extends AdminBaseServlet {

    @Override
    protected void processAdminGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get filter parameters
        String searchQuery = request.getParameter("searchQuery");
        String platformFilter = request.getParameter("platformFilter");
        String sortBy = request.getParameter("sortBy");

        // Get games with filters applied
        List<Game> games = getGameManagement().getAllGames(searchQuery, platformFilter, sortBy);

        // Count total games for pagination
        int totalGames = games.size();

        // Set attributes for the JSP
        request.setAttribute("games", games);
        request.setAttribute("totalGames", totalGames);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("platformFilter", platformFilter);
        request.setAttribute("sortBy", sortBy);

        // Forward to the game management JSP
        forwardToJsp(request, response, "/WEB-INF/jsp/adminGameManagement.jsp");
    }

    @Override
    protected void processAdminPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Post requests will be handled by separate servlets for specific actions
        // like add-game, edit-game, delete-game
        redirectTo(request, response, "/admin/game-management");
    }
}
