package gamevaultbase.servlets;

import gamevaultbase.entities.Game;
import gamevaultbase.storage.GameStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for handling the game management interface in the admin dashboard.
 * Provides functionality to view, filter, and sort games from the admin panel.
 */
@WebServlet(name = "GameManagementServlet", urlPatterns = { "/admin/game-management" })
public class GameManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Check if user is logged in and is an admin
        if (session.getAttribute("loggedInUser") == null ||
                !Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            response.sendRedirect(request.getContextPath() + "/login?message=Admin access required&messageType=error");
            return;
        }

        // Get filter parameters
        String searchQuery = request.getParameter("searchQuery");
        String platformFilter = request.getParameter("platformFilter");
        String sortBy = request.getParameter("sortBy");

        // Get games with filters applied
        GameStorage gameStorage = new GameStorage();
        List<Game> games = gameStorage.findAllWithFilters(searchQuery, platformFilter, sortBy);

        // Count total games for pagination
        int totalGames = games.size();

        // Set attributes for the JSP
        request.setAttribute("games", games);
        request.setAttribute("totalGames", totalGames);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("platformFilter", platformFilter);
        request.setAttribute("sortBy", sortBy);

        // Forward to the game management JSP
        request.getRequestDispatcher("/WEB-INF/jsp/adminGameManagement.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Post requests will be handled by separate servlets for specific actions
        // like add-game, edit-game, delete-game
        response.sendRedirect(request.getContextPath() + "/admin/game-management");
    }
}