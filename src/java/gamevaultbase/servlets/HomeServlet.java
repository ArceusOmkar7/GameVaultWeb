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

// Mapped in web.xml as /home
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- Get Parameters for Search/Filter/Sort ---
        String searchQuery = request.getParameter("search");
        String filterPlatform = request.getParameter("filter");
        String sortBy = request.getParameter("sort");

        // TODO: Use these parameters in GameManagement/GameStorage to filter/sort results
        System.out.println("HomeServlet GET: Search='" + searchQuery + "', Filter='" + filterPlatform + "', Sort='" + sortBy + "'");


        GameManagement gameManagement = (GameManagement) getServletContext().getAttribute("gameManagement");
        String errorMessage = null;
        List<Game> games = Collections.emptyList();
        List<Game> featuredGames = Collections.emptyList();
        Game mainFeaturedGame = null;

        if (gameManagement == null) {
            System.err.println("FATAL: GameManagement not found in ServletContext!");
            errorMessage = "Application error: Game service not available.";
        } else {
            try {
                 // TODO: Modify these calls to pass search/filter/sort parameters when implemented
                games = gameManagement.getAllGames(searchQuery, filterPlatform, sortBy);
                featuredGames = gameManagement.getFeaturedGames();

                if (featuredGames != null && !featuredGames.isEmpty()) {
                    mainFeaturedGame = featuredGames.get(0);
                } else if (games != null && !games.isEmpty()) {
                     mainFeaturedGame = games.get(0);
                }

                if (games == null) games = Collections.emptyList();
                if (featuredGames == null) featuredGames = Collections.emptyList();

            } catch (Exception e) {
                System.err.println("Error retrieving games for home page: " + e.getMessage());
                e.printStackTrace();
                errorMessage = "An error occurred while retrieving games.";
                games = Collections.emptyList();
                featuredGames = Collections.emptyList();
            }
        }

        // Pass redirect messages
        String redirectMessage = request.getParameter("message");
        String redirectMessageType = request.getParameter("messageType");
        if (redirectMessage != null) {
             request.setAttribute("message", redirectMessage);
             request.setAttribute("messageType", redirectMessageType);
        }

        // Pass back search/filter/sort values to keep them selected in the form
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("selectedPlatform", filterPlatform);
        request.setAttribute("selectedSort", sortBy);


        request.setAttribute("gamesList", games);
        request.setAttribute("featuredGamesList", featuredGames);
        request.setAttribute("mainFeaturedGame", mainFeaturedGame);
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);
    }
}