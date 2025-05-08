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

    @Override
    protected void processPublicGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchQuery = request.getParameter("search");
        String filterPlatform = request.getParameter("filter-platform");
        String filterGenre = request.getParameter("filter-genre");
        String sortBy = request.getParameter("sort");

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
        } catch (Exception e) {
            setErrorMessage(request, "An error occurred while retrieving games.");
            games = Collections.emptyList();
        }

        request.setAttribute("gamesList", games);
        request.setAttribute("platforms", platforms);
        request.setAttribute("genres", genres);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("selectedPlatform", filterPlatform);
        request.setAttribute("selectedGenre", filterGenre);
        request.setAttribute("selectedSort", sortBy);

        forwardToJsp(request, response, "/WEB-INF/jsp/browse.jsp");
    }

    @Override
    protected void processPublicPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Browse page doesn't need POST handling, redirect to GET
        redirectTo(request, response, "/browse");
    }
}
