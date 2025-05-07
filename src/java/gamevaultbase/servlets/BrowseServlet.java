package gamevaultbase.servlets;

import gamevaultbase.entities.Game;
import gamevaultbase.management.GameManagement;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Mapped in web.xml as /browse
public class BrowseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchQuery = request.getParameter("search");
        String filterPlatform = request.getParameter("filter");
        String sortBy = request.getParameter("sort");

        GameManagement gameManagement = (GameManagement) getServletContext().getAttribute("gameManagement");
        String errorMessage = null;
        List<Game> games = Collections.emptyList();
        if (gameManagement == null) {
            errorMessage = "Application error: Game service not available.";
        } else {
            try {
                games = gameManagement.getAllGames(searchQuery, filterPlatform, sortBy);
                if (games == null) games = Collections.emptyList();
            } catch (Exception e) {
                errorMessage = "An error occurred while retrieving games.";
                games = Collections.emptyList();
            }
        }
        request.setAttribute("gamesList", games);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("selectedPlatform", filterPlatform);
        request.setAttribute("selectedSort", sortBy);
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/jsp/browse.jsp").forward(request, response);
    }
}
