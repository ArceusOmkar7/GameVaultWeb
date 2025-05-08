package gamevaultbase.servlets.admin;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Genre;
import gamevaultbase.entities.Platform;
import gamevaultbase.helpers.AppUtil;
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

    private static final int DEFAULT_PAGE_SIZE = 10;

    @Override
    protected void processAdminGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get filter parameters
        String searchQuery = request.getParameter("searchQuery");
        String platformFilter = request.getParameter("platformFilter");
        String genreFilter = request.getParameter("genreFilter");
        String sortBy = request.getParameter("sortBy");

        // Get all platforms and genres for the filter dropdowns
        List<Platform> platforms = getGameManagement().getAllPlatforms();
        List<Genre> genres = getGameManagement().getAllGenres();

        // Get games with filters applied
        List<Game> allGames = getGameManagement().getAllGames(searchQuery, platformFilter, genreFilter, sortBy);

        // Count total games for pagination
        int totalGames = allGames.size();

        // Handle pagination
        int pageSize = AppUtil.getPageSize(request, DEFAULT_PAGE_SIZE);
        int currentPage = handlePaginationRequest(request, totalGames, pageSize);

        // Calculate start and end indices for the current page
        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalGames);

        // Get subset of games for the current page
        List<Game> pagedGames;
        if (startIndex < totalGames) {
            pagedGames = allGames.subList(startIndex, endIndex);
        } else {
            pagedGames = allGames.subList(0, Math.min(pageSize, totalGames));
        }

        // Set attributes for the JSP
        request.setAttribute("games", pagedGames);
        request.setAttribute("platforms", platforms);
        request.setAttribute("genres", genres);
        request.setAttribute("totalGames", totalGames);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("platformFilter", platformFilter);
        request.setAttribute("genreFilter", genreFilter);
        request.setAttribute("sortBy", sortBy);

        // Calculate displayed game range for the current page
        int firstGame = totalGames > 0 ? startIndex + 1 : 0;
        int lastGame = Math.min(endIndex, totalGames);
        request.setAttribute("firstGame", firstGame);
        request.setAttribute("lastGame", lastGame);

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
