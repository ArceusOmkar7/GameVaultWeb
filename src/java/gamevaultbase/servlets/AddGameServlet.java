package gamevaultbase.servlets;

import gamevaultbase.entities.Game;
import gamevaultbase.management.GameManagement;
import gamevaultbase.exceptions.InvalidUserDataException;
import gamevaultbase.storage.GameStorage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Servlet for adding new games to the database
 */
@WebServlet(name = "AddGameServlet", urlPatterns = { "/admin/add-game" })
public class AddGameServlet extends HttpServlet {

    /**
     * Handles the HTTP POST method for adding a new game
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Check if user is logged in and is an admin
        if (session.getAttribute("loggedInUser") == null ||
                !(boolean) session.getAttribute("isAdmin")) {
            response.sendRedirect(request.getContextPath() + "/login?message=Admin access required&messageType=error");
            return;
        }

        try {
            // Get parameters from the form
            String title = request.getParameter("title");
            String developer = request.getParameter("developer");
            String platform = request.getParameter("platform");
            String priceStr = request.getParameter("price");
            String releaseDateStr = request.getParameter("releaseDate");
            String genre = request.getParameter("genre");
            String description = request.getParameter("description");

            // Validate required fields
            if (title == null || title.trim().isEmpty() ||
                    developer == null || developer.trim().isEmpty() ||
                    platform == null || platform.trim().isEmpty() ||
                    priceStr == null || priceStr.trim().isEmpty() ||
                    releaseDateStr == null || releaseDateStr.trim().isEmpty() ||
                    genre == null || genre.trim().isEmpty() ||
                    description == null || description.trim().isEmpty()) {

                throw new InvalidUserDataException("form", "All fields are required.");
            }

            // Parse price
            float price;
            try {
                price = Float.parseFloat(priceStr);
                if (price < 0) {
                    throw new InvalidUserDataException("price", "Price cannot be negative.");
                }
            } catch (NumberFormatException e) {
                throw new InvalidUserDataException("price", "Invalid price format.");
            }

            // Parse and convert release date
            java.util.Date parsedDate;
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                parsedDate = format.parse(releaseDateStr);
            } catch (ParseException e) {
                throw new InvalidUserDataException("releaseDate", "Invalid date format.");
            }
            java.sql.Date releaseDate = new java.sql.Date(parsedDate.getTime());

            // Create a new Game object
            Game game = new Game();
            game.setTitle(title);
            game.setDeveloper(developer);
            game.setPlatform(platform);
            game.setPrice(price);
            game.setReleaseDate(releaseDate);
            game.setGenre(genre);
            game.setDescription(description);

            // Add the game to the database
            GameStorage gameStorage = new GameStorage();
            GameManagement gameManagement = new GameManagement(gameStorage);
            gameManagement.addGame(game);

            response.sendRedirect(request.getContextPath()
                    + "/admin/dashboard?message=Game added successfully&messageType=success");

        } catch (InvalidUserDataException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/adminDashboard.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/adminDashboard.jsp").forward(request, response);
        }
    }
}