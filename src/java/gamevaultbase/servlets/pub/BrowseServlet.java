package gamevaultbase.servlets.pub;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Genre;
import gamevaultbase.entities.Platform;
import gamevaultbase.servlets.base.PublicBaseServlet;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Mapped in web.xml as /browse
public class BrowseServlet extends PublicBaseServlet {

    private static final int DEFAULT_PAGE_SIZE = 12; // Show 12 games per page (3x4 grid)

    @Override
    protected void processPublicGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchQuery = request.getParameter("search");
        String filterPlatform = request.getParameter("filter-platform");
        String filterGenre = request.getParameter("filter-genre");
        String sortBy = request.getParameter("sort");
        int currentPage = 1;
        int pageSize = DEFAULT_PAGE_SIZE;

        // Parse page number from request
        try {
            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.trim().isEmpty()) {
                currentPage = Integer.parseInt(pageStr);
                if (currentPage < 1)
                    currentPage = 1;
            }
        } catch (NumberFormatException e) {
            currentPage = 1;
        }

        // For backward compatibility
        if (filterPlatform == null) {
            filterPlatform = request.getParameter("filter");
        }

        List<Game> games = Collections.emptyList();
        List<Platform> platforms = Collections.emptyList();
        List<Genre> genres = Collections.emptyList();

        try {
            // Get all available platforms and genres for filter dropdowns
            platforms = getGameManagement().getAllPlatforms();
            genres = getGameManagement().getAllGenres();

            // Get filtered games
            games = getGameManagement().getAllGames(searchQuery, filterPlatform, filterGenre, sortBy);
            if (games == null)
                games = Collections.emptyList();

            // Calculate pagination
            int totalGames = games.size();
            int totalPages = (int) Math.ceil((double) totalGames / pageSize);
            if (currentPage > totalPages)
                currentPage = totalPages;

            // Get subset of games for current page
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalGames);
            List<Game> pagedGames = games.subList(startIndex, endIndex);

            // Set attributes for the JSP
            request.setAttribute("gamesList", pagedGames);
            request.setAttribute("platforms", platforms);
            request.setAttribute("genres", genres);
            request.setAttribute("searchQuery", searchQuery);
            request.setAttribute("selectedPlatform", filterPlatform);
            request.setAttribute("selectedGenre", filterGenre);
            request.setAttribute("selectedSort", sortBy);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalGames", totalGames);
            request.setAttribute("pageSize", pageSize);

        } catch (Exception e) {
            setErrorMessage(request, "An error occurred while retrieving games.");
            games = Collections.emptyList();
        }

        forwardToJsp(request, response, "/WEB-INF/jsp/browse.jsp");
    }

    @Override
    protected void processPublicPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Browse page doesn't need POST handling, redirect to GET
        redirectTo(request, response, "/browse");
    }
}
