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

        GameManagement gameManagement = (GameManagement) getServletContext().getAttribute("gameManagement");
        String errorMessage = null;
        List<Game> featuredGames = Collections.emptyList();
        Game mainFeaturedGame = null;

        if (gameManagement == null) {
            System.err.println("FATAL: GameManagement not found in ServletContext!");
            errorMessage = "Application error: Game service not available.";
        } else {
            try {
                featuredGames = gameManagement.getFeaturedGames();

                if (featuredGames != null && !featuredGames.isEmpty()) {
                    mainFeaturedGame = featuredGames.get(0);
                }

                if (featuredGames == null) featuredGames = Collections.emptyList();

            } catch (Exception e) {
                System.err.println("Error retrieving games for home page: " + e.getMessage());
                e.printStackTrace();
                errorMessage = "An error occurred while retrieving games.";
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

        request.setAttribute("featuredGamesList", featuredGames);
        request.setAttribute("mainFeaturedGame", mainFeaturedGame);
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);
    }
}