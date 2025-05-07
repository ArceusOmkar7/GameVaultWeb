package gamevaultbase.servlets.pub;

import gamevaultbase.entities.Game;
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
        String filterPlatform = request.getParameter("filter");
        String sortBy = request.getParameter("sort");

        List<Game> games = Collections.emptyList();

        try {
            games = getGameManagement().getAllGames(searchQuery, filterPlatform, sortBy);
            if (games == null)
                games = Collections.emptyList();
        } catch (Exception e) {
            setErrorMessage(request, "An error occurred while retrieving games.");
            games = Collections.emptyList();
        }

        request.setAttribute("gamesList", games);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("selectedPlatform", filterPlatform);
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
