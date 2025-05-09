package gamevaultbase.servlets.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet for handling game editing in the admin dashboard.
 * Supports both regular form submissions and AJAX requests.
 */
@WebServlet(name = "EditGameServlet", urlPatterns = { "/admin/edit-game" })
public class EditGameServlet extends AdminBaseServlet {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void processAdminGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the game ID to edit
        String gameIdStr = request.getParameter("id");
        if (gameIdStr == null || gameIdStr.trim().isEmpty()) {
            redirectWithMessage(request, response, "/admin/game-management",
                    "No game specified for editing", "error");
            return;
        }

        try {
            int gameId = Integer.parseInt(gameIdStr);

            // Fetch the game to edit
            GameStorage gameStorage = new GameStorage();
            Game game = gameStorage.findById(gameId);

            if (game == null) {
                redirectWithMessage(request, response, "/admin/game-management",
                        "Game not found", "error");
                return;
            } // Check if this is an AJAX request
            boolean isAjax = "true".equals(request.getParameter("ajax"));

            // Get all platforms and genres for the dropdown menus
            List<Platform> platforms = gameStorage.findAllPlatforms();
            List<Genre> genres = gameStorage.findAllGenres();

            if (isAjax) {
                // Create a response object with game data, platforms, and genres
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("game", game);
                responseData.put("platforms", platforms);
                responseData.put("genres", genres);

                // Return data as JSON
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(objectMapper.writeValueAsString(responseData));
                out.flush();
                return;
            }

            // For regular requests (non-AJAX), continue with the original implementation
            // Set attributes for the JSP
            request.setAttribute("game", game);
            request.setAttribute("platforms", platforms);
            request.setAttribute("genres", genres);
            request.setAttribute("editMode", true);

            // Forward to the edit game JSP
            forwardToJsp(request, response, "/WEB-INF/jsp/adminEditGame.jsp");

        } catch (NumberFormatException e) {
            redirectWithMessage(request, response, "/admin/game-management",
                    "Invalid game ID format", "error");
        } catch (Exception e) {
            System.err.println("Error loading game for editing: " + e.getMessage());
            e.printStackTrace();
            redirectWithMessage(request, response, "/admin/game-management",
                    "Error loading game: " + e.getMessage(), "error");
        }
    }

    @Override
    protected void processAdminPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get form data
        String gameIdStr = request.getParameter("gameId");
        if (gameIdStr == null || gameIdStr.trim().isEmpty()) {
            sendJsonResponse(response, false, "No game ID provided");
            return;
        }

        try {
            int gameId = Integer.parseInt(gameIdStr);

            // Get the existing game
            GameStorage gameStorage = new GameStorage();
            Game game = gameStorage.findById(gameId);

            if (game == null) {
                sendJsonResponse(response, false, "Game not found");
                return;
            } // Update game with form data
            game.setTitle(request.getParameter("title"));
            game.setDescription(request.getParameter("description"));
            game.setDeveloper(request.getParameter("developer")); // Handle multiple platforms
            String[] platformValues = request.getParameterValues("platform");
            System.out.println(
                    "Edit - Platform values received: " + (platformValues != null ? platformValues.length : "null"));

            // Clear existing platforms
            game.setPlatforms(new java.util.ArrayList<>());

            if (platformValues != null && platformValues.length > 0) {
                // Join multiple platforms with comma
                for (String platform : platformValues) {
                    System.out.println("Edit - Platform value: " + platform);
                }
                game.setPlatform(String.join(", ", platformValues));
                System.out.println("Edit - Joined platform string: " + game.getPlatform());

                // Also add as Platform objects for proper relationship handling
                for (String platformName : platformValues) {
                    Platform platform = new Platform(platformName.trim());
                    game.addPlatform(platform);
                }
            } else {
                System.out.println("Edit - No platform values received");
                game.setPlatform("");
            }

            // Handle multiple genres
            String[] genreValues = request.getParameterValues("genre");
            System.out.println("Edit - Genre values received: " + (genreValues != null ? genreValues.length : "null"));

            // Clear existing genres
            game.setGenres(new java.util.ArrayList<>());

            if (genreValues != null && genreValues.length > 0) {
                // Join multiple genres with comma
                for (String genre : genreValues) {
                    System.out.println("Edit - Genre value: " + genre);
                }
                game.setGenre(String.join(", ", genreValues));
                System.out.println("Edit - Joined genre string: " + game.getGenre());

                // Also add as Genre objects for proper relationship handling
                for (String genreName : genreValues) {
                    Genre genre = new Genre(genreName.trim());
                    game.addGenre(genre);
                }
            } else {
                System.out.println("Edit - No genre values received");
                game.setGenre("");
            }

            String priceStr = request.getParameter("price");
            if (priceStr != null && !priceStr.isEmpty()) {
                try {
                    game.setPrice(Float.parseFloat(priceStr));
                } catch (NumberFormatException e) {
                    // If conversion fails, retain existing price
                    System.err.println("Invalid price format: " + priceStr);
                }
            }

            // Handle release date
            String releaseDateStr = request.getParameter("releaseDate");
            if (releaseDateStr != null && !releaseDateStr.isEmpty()) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date releaseDate = dateFormat.parse(releaseDateStr);
                    game.setReleaseDate(releaseDate);
                } catch (ParseException e) {
                    // If parsing fails, retain existing date
                    System.err.println("Invalid date format: " + releaseDateStr);
                }
            }

            game.setImagePath(request.getParameter("imagePath"));

            String ratingStr = request.getParameter("rating");
            if (ratingStr != null && !ratingStr.isEmpty()) {
                try {
                    game.setRating(Float.parseFloat(ratingStr));
                } catch (NumberFormatException e) {
                    // If conversion fails, retain existing rating
                    System.err.println("Invalid rating format: " + ratingStr);
                }
            } // Update the game
            System.out.println("===== GAME DATA BEFORE UPDATE =====");
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

            try {
                gameStorage.update(game);
                System.out.println("Game updated successfully with ID: " + game.getGameId());

                // Check if request expects JSON response (from AJAX)
                String contentType = request.getContentType();
                boolean expectsJson = contentType != null &&
                        (contentType.contains("application/json") ||
                                request.getHeader("X-Requested-With") != null ||
                                "XMLHttpRequest".equals(request.getHeader("X-Requested-With")));

                if (expectsJson) {
                    sendJsonResponse(response, true, "Game updated successfully");
                } else {
                    // Redirect back to game management with success message for regular form posts
                    redirectWithMessage(request, response, "/admin/game-management",
                            "Game updated successfully", "success");
                }
            } catch (Exception e) {
                System.err.println("Error updating game: " + e.getMessage());
                e.printStackTrace();
                sendJsonResponse(response, false, "Error updating game: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "Invalid game ID format");
        } catch (Exception e) {
            System.err.println("Error updating game: " + e.getMessage());
            e.printStackTrace();
            sendJsonResponse(response, false, "Error updating game: " + e.getMessage());
        }
    }

    /**
     * Send a JSON response with success status and message
     */
    private void sendJsonResponse(HttpServletResponse response, boolean success, String message)
            throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", message);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(objectMapper.writeValueAsString(result));
        out.flush();
    }
}