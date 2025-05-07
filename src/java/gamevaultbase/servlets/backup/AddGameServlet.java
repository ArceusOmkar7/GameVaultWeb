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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Servlet for handling the addition of new games from the admin dashboard.
 */
@WebServlet(name = "AddGameServlet", urlPatterns = { "/admin/add-game" })
public class AddGameServlet extends HttpServlet {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Check if user is logged in and is an admin
        if (session.getAttribute("loggedInUser") == null ||
                !Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            response.sendRedirect(request.getContextPath() + "/login?message=Admin access required&messageType=error");
            return;
        }

        // Extract game details from the form
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String developer = request.getParameter("developer");
        String platform = request.getParameter("platform");
        String priceStr = request.getParameter("price");
        String releaseDateStr = request.getParameter("releaseDate");
        String imagePath = request.getParameter("imagePath");
        String genre = request.getParameter("genre");
        String ratingStr = request.getParameter("rating");

        // Validate required fields
        if (title == null || title.trim().isEmpty() || priceStr == null || priceStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/game-management?message=Title and price are required&messageType=error");
            return;
        }

        // Parse numeric values
        float price = 0.0f;
        float rating = 0.0f;
        Date releaseDate = null;

        try {
            price = Float.parseFloat(priceStr);
            if (ratingStr != null && !ratingStr.trim().isEmpty()) {
                rating = Float.parseFloat(ratingStr);
            }
            if (releaseDateStr != null && !releaseDateStr.trim().isEmpty()) {
                releaseDate = DATE_FORMAT.parse(releaseDateStr);
            }
        } catch (NumberFormatException | ParseException e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/game-management?message=Invalid number format or date&messageType=error");
            return;
        }

        // Create a new Game object
        Game game = new Game();
        game.setTitle(title);
        game.setDescription(description);
        game.setDeveloper(developer);
        game.setPlatform(platform);
        game.setPrice(price);
        game.setReleaseDate(releaseDate);
        game.setImagePath(imagePath);
        game.setGenre(genre);
        game.setRating(rating);

        // Save the game
        GameStorage gameStorage = new GameStorage();
        gameStorage.save(game);

        // Redirect with success message
        response.sendRedirect(request.getContextPath()
                + "/admin/game-management?message=Game added successfully&messageType=success");
    }
}