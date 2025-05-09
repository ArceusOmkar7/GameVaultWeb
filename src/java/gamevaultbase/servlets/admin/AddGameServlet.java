package gamevaultbase.servlets.admin;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Genre;
import gamevaultbase.entities.Platform;
import gamevaultbase.servlets.base.AdminBaseServlet;
import gamevaultbase.storage.GameStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

/**
 * Servlet for handling adding new games to the system from the admin dashboard.
 */
@WebServlet(name = "AddGameServlet", urlPatterns = { "/admin/add-game" })
public class AddGameServlet extends AdminBaseServlet {

    @Override
    protected void processAdminGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Adding games should be done via POST only
        redirectWithMessage(request, response, "/admin/game-management",
                "Game addition requires a POST request", "error");
    }

    @Override
    protected void processAdminPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            System.out.println("=== Processing admin POST request for adding game ===");

            // Log all parameters for debugging
            System.out.println("Request parameters:");
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String[] paramValues = request.getParameterValues(paramName);
                System.out.println(paramName + " = " + (paramValues != null ? String.join(", ", paramValues) : "null"));
            }

            // Create a new Game object and populate it with form data
            Game game = new Game();

            // Set basic properties
            String title = request.getParameter("title");
            System.out.println("Title parameter: " + title);

            if (title == null || title.trim().isEmpty()) {
                System.out.println("Title is empty or null, redirecting with error");
                redirectWithMessage(request, response, "/admin/game-management",
                        "Game title is required.", "error");
                return;
            }
            game.setTitle(title.trim());

            game.setDescription(request.getParameter("description"));
            game.setDeveloper(request.getParameter("developer"));

            // Handle multiple platforms
            String[] platformValues = request.getParameterValues("platform");
            System.out
                    .println("Platform values received: " + (platformValues != null ? platformValues.length : "null"));

            // Clear existing platforms to avoid duplicates
            game.setPlatforms(new ArrayList<>());

            if (platformValues != null && platformValues.length > 0) {
                // Join multiple platforms with comma for the string field
                StringBuilder platformStr = new StringBuilder();
                for (int i = 0; i < platformValues.length; i++) {
                    String platformName = platformValues[i].trim();
                    if (!platformName.isEmpty()) {
                        System.out.println("Processing platform: " + platformName);

                        // Add to string representation
                        if (i > 0)
                            platformStr.append(", ");
                        platformStr.append(platformName);

                        // Create and add Platform object
                        Platform platform = new Platform(platformName);
                        game.addPlatform(platform);
                    }
                }
                game.setPlatform(platformStr.toString());
                System.out.println("Joined platform string: " + game.getPlatform());
            } else {
                System.out.println("No platform values received");
                game.setPlatform("");
            }

            // Handle multiple genres
            String[] genreValues = request.getParameterValues("genre");
            System.out.println("Genre values received: " + (genreValues != null ? genreValues.length : "null"));

            // Clear existing genres to avoid duplicates
            game.setGenres(new ArrayList<>());

            if (genreValues != null && genreValues.length > 0) {
                // Join multiple genres with comma for the string field
                StringBuilder genreStr = new StringBuilder();
                for (int i = 0; i < genreValues.length; i++) {
                    String genreName = genreValues[i].trim();
                    if (!genreName.isEmpty()) {
                        System.out.println("Processing genre: " + genreName);

                        // Add to string representation
                        if (i > 0)
                            genreStr.append(", ");
                        genreStr.append(genreName);

                        // Create and add Genre object
                        Genre genre = new Genre(genreName);
                        game.addGenre(genre);
                    }
                }
                game.setGenre(genreStr.toString());
                System.out.println("Joined genre string: " + game.getGenre());
            } else {
                System.out.println("No genre values received");
                game.setGenre("");
            }

            // Set image path
            game.setImagePath(request.getParameter("imagePath"));

            // Handle price
            String priceStr = request.getParameter("price");
            if (priceStr != null && !priceStr.isEmpty()) {
                try {
                    float price = Float.parseFloat(priceStr.trim());
                    if (price < 0) {
                        redirectWithMessage(request, response, "/admin/game-management",
                                "Price cannot be negative.", "error");
                        return;
                    }
                    game.setPrice(price);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price format: " + priceStr);
                    redirectWithMessage(request, response, "/admin/game-management",
                            "Invalid price format.", "error");
                    return;
                }
            } else {
                game.setPrice(0.0f); // Default price if not provided
            }

            // Handle rating
            String ratingStr = request.getParameter("rating");
            if (ratingStr != null && !ratingStr.isEmpty()) {
                try {
                    float rating = Float.parseFloat(ratingStr.trim());
                    if (rating < 0 || rating > 5) {
                        System.err.println("Rating out of range (0-5): " + rating);
                        rating = Math.max(0, Math.min(5, rating)); // Clamp to valid range
                    }
                    game.setRating(rating);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid rating format: " + ratingStr);
                    game.setRating(0.0f); // Default to zero rating
                }
            } else {
                game.setRating(0.0f); // Default rating
            }

            // Handle release date
            String releaseDateStr = request.getParameter("releaseDate");
            if (releaseDateStr != null && !releaseDateStr.isEmpty()) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    dateFormat.setLenient(false); // Strict date parsing
                    Date releaseDate = dateFormat.parse(releaseDateStr);
                    game.setReleaseDate(releaseDate);
                } catch (ParseException e) {
                    System.err.println("Invalid date format: " + releaseDateStr);
                    game.setReleaseDate(new Date()); // Default to current date
                }
            } else {
                // Set current date as default if not provided
                game.setReleaseDate(new Date());
            }

            // Make sure the legacy fields are in sync with relationship collections
            game.updateLegacyFields();

            // Add the game to the database
            GameStorage gameStorage = new GameStorage();

            System.out.println("===== GAME DATA BEFORE SAVE =====");
            System.out.println("Title: " + game.getTitle());
            System.out.println("Description: " + game.getDescription());
            System.out.println("Developer: " + game.getDeveloper());
            System.out.println("Platform: " + game.getPlatform());
            System.out.println("Genre: " + game.getGenre());
            System.out.println("Price: " + game.getPrice());
            System.out.println("Rating: " + game.getRating());
            System.out.println("ImagePath: " + game.getImagePath());
            System.out.println("ReleaseDate: " + game.getReleaseDate());
            System.out
                    .println("Platforms size: " + (game.getPlatforms() != null ? game.getPlatforms().size() : "null"));
            System.out.println("Genres size: " + (game.getGenres() != null ? game.getGenres().size() : "null"));

            // Call save method
            gameStorage.save(game);

            if (game.getGameId() <= 0) {
                System.err.println("Error: Game was not saved properly. Game ID is invalid: " + game.getGameId());
                redirectWithMessage(request, response, "/admin/game-management",
                        "Error: Game could not be saved to the database.", "error");
                return;
            }

            System.out.println("Game saved successfully with ID: " + game.getGameId());

            // Redirect back to game management with success message
            redirectWithMessage(request, response, "/admin/game-management",
                    "Game added successfully", "success");
        } catch (Exception e) {
            System.err.println("Error adding game: " + e.getMessage());
            e.printStackTrace();
            redirectWithMessage(request, response, "/admin/game-management",
                    "Error adding game: " + e.getMessage(), "error");
        }
    }
}
