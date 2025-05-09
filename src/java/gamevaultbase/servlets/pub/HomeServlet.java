package gamevaultbase.servlets.pub;

import gamevaultbase.entities.Game;
import gamevaultbase.servlets.base.PublicBaseServlet;
import java.io.IOException;
import java.util.ArrayList;
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

        List<Game> featuredGames = new ArrayList<>();
        List<Game> actionGames = Collections.emptyList();
        List<Game> strategyGames = Collections.emptyList();
        List<Game> sportsGames = Collections.emptyList();
        List<Game> rpgGames = Collections.emptyList();

        try {
            // Get featured games from multiple genres to have a diverse selection
            List<Game> actionFeatured = getGameManagement().getAllGames(null, null, "Action", "rating_desc");
            List<Game> strategyFeatured = getGameManagement().getAllGames(null, null, "Strategy", "rating_desc");
            List<Game> rpgFeatured = getGameManagement().getAllGames(null, null, "RPG", "rating_desc");

            // Add top rated games from each genre to featured games
            if (actionFeatured != null)
                featuredGames.addAll(actionFeatured.subList(0, Math.min(3, actionFeatured.size())));
            if (strategyFeatured != null)
                featuredGames.addAll(strategyFeatured.subList(0, Math.min(3, strategyFeatured.size())));
            if (rpgFeatured != null)
                featuredGames.addAll(rpgFeatured.subList(0, Math.min(3, rpgFeatured.size())));

            // Get games by genre for each section
            actionGames = getGameManagement().getAllGames(null, null, "Action", "rating_desc");
            strategyGames = getGameManagement().getAllGames(null, null, "Strategy", "rating_desc");
            sportsGames = getGameManagement().getAllGames(null, null, "Sports", "rating_desc");
            rpgGames = getGameManagement().getAllGames(null, null, "RPG", "rating_desc");

            // Ensure lists are not null
            if (actionGames == null)
                actionGames = Collections.emptyList();
            if (strategyGames == null)
                strategyGames = Collections.emptyList();
            if (sportsGames == null)
                sportsGames = Collections.emptyList();
            if (rpgGames == null)
                rpgGames = Collections.emptyList();

        } catch (Exception e) {
            System.err.println("Error retrieving games for home page: " + e.getMessage());
            e.printStackTrace();
            setErrorMessage(request, "An error occurred while retrieving games.");
        }

        // Set attributes for the JSP
        request.setAttribute("featuredGamesList", featuredGames);
        request.setAttribute("actionGames", actionGames);
        request.setAttribute("strategyGames", strategyGames);
        request.setAttribute("sportsGames", sportsGames);
        request.setAttribute("rpgGames", rpgGames);

        // Forward to the JSP view
        forwardToJsp(request, response, "/WEB-INF/jsp/home.jsp");
    }
}
