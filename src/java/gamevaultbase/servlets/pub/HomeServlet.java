package gamevaultbase.servlets.pub;

import gamevaultbase.entities.Game;
import gamevaultbase.servlets.base.PublicBaseServlet;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HomeServlet", urlPatterns = { "/home" })
public class HomeServlet extends PublicBaseServlet {

    @Override
    protected void processPublicGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Game> featuredGames = Collections.emptyList();
        Game mainFeaturedGame = null;

        try {
            featuredGames = getGameManagement().getFeaturedGames();

            if (featuredGames != null && !featuredGames.isEmpty()) {
                mainFeaturedGame = featuredGames.get(0);
            }

            if (featuredGames == null)
                featuredGames = Collections.emptyList();

        } catch (Exception e) {
            System.err.println("Error retrieving games for home page: " + e.getMessage());
            e.printStackTrace();
            setErrorMessage(request, "An error occurred while retrieving games.");
            featuredGames = Collections.emptyList();
        }

        request.setAttribute("featuredGamesList", featuredGames);
        request.setAttribute("mainFeaturedGame", mainFeaturedGame);

        // Forward to the JSP view
        forwardToJsp(request, response, "/WEB-INF/jsp/home.jsp");
    }
}
