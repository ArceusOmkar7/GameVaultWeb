package gamevaultbase.servlets.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
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
    {
        // Ensure all fields (even private) are serialized for Game and related entities
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    }

    @Override
    protected void processAdminGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the game ID to edit
        String gameIdStr = request.getParameter("id");
        System.out.println("Received edit request for game ID: " + gameIdStr);

        if (gameIdStr == null || gameIdStr.trim().isEmpty()) {
            System.out.println("No game ID provided in request");
            if (isAjaxRequest(request)) {
                sendJsonResponse(response, false, "No game specified for editing");
            } else {
                redirectWithMessage(request, response, "/admin/game-management",
                        "No game specified for editing", "error");
            }
            return;
        }

        try {
            int gameId = Integer.parseInt(gameIdStr);
            System.out.println("Parsed game ID: " + gameId);

            // Fetch the game to edit
            GameStorage gameStorage = new GameStorage();
            Game game = gameStorage.findById(gameId);
            System.out.println("Found game: " + (game != null ? game.getTitle() : "null"));

            if (game == null) {
                System.out.println("Game not found for ID: " + gameId);
                if (isAjaxRequest(request)) {
                    sendJsonResponse(response, false, "Game not found");
                } else {
                    redirectWithMessage(request, response, "/admin/game-management",
                            "Game not found", "error");
                }
                return;
            }

            // Check if this is an AJAX request
            boolean isAjax = isAjaxRequest(request);
            System.out.println("Is AJAX request: " + isAjax);

            // Get all platforms and genres for the dropdown menus
            List<Platform> platforms = gameStorage.findAllPlatforms();
            List<Genre> genres = gameStorage.findAllGenres();

            if (isAjax) {
                // Create a response object with game data, platforms, and genres
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("game", game);
                responseData.put("platforms", platforms);
                responseData.put("genres", genres);

                // Return data as JSON
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                String jsonResponse = objectMapper.writeValueAsString(responseData);
                System.out.println("Sending JSON response: " + jsonResponse);
                out.print(jsonResponse);
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
            if (isAjaxRequest(request)) {
                sendJsonResponse(response, false, "Invalid game ID format");
            } else {
                redirectWithMessage(request, response, "/admin/game-management",
                        "Invalid game ID format", "error");
            }
        } catch (Exception e) {
            System.err.println("Error loading game for editing: " + e.getMessage());
            e.printStackTrace();
            if (isAjaxRequest(request)) {
                sendJsonResponse(response, false, "Error loading game: " + e.getMessage());
            } else {
                redirectWithMessage(request, response, "/admin/game-management",
                        "Error loading game: " + e.getMessage(), "error");
            }
        }
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    private void sendJsonResponse(HttpServletResponse response, boolean success, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print("{\"success\":" + success + ",\"message\":\"" + message + "\"}");
        out.flush();
    }

    @Override
    protected void processAdminPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get form data
        String gameIdStr = request.getParameter("gameId");
        System.out.println("Received POST request with gameId: " + gameIdStr);
        System.out.println("Request parameters: " + request.getParameterMap());

        if (gameIdStr == null || gameIdStr.trim().isEmpty()) {
            System.out.println("No game ID provided in POST request");
            sendJsonResponse(response, false, "No game ID provided");
            return;
        }

        try {
            int gameId = Integer.parseInt(gameIdStr);
            System.out.println("Parsed game ID: " + gameId);

            // Get the existing game
            GameStorage gameStorage = new GameStorage();
            Game game = gameStorage.findById(gameId);
            System.out.println("Found game: " + (game != null ? game.getTitle() : "null"));

            if (game == null) {
                System.out.println("Game not found for ID: " + gameId);
                sendJsonResponse(response, false, "Game not found");
                return;
            }

            // Update game with form data
            String priceStr = request.getParameter("price");
            System.out.println("Received price: " + priceStr);
            if (priceStr != null && !priceStr.isEmpty()) {
                try {
                    float price = Float.parseFloat(priceStr);
                    game.setPrice(price);
                    System.out.println("Updated price to: " + price);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price format: " + priceStr);
                }
            }

            String ratingStr = request.getParameter("rating");
            System.out.println("Received rating: " + ratingStr);
            if (ratingStr != null && !ratingStr.isEmpty()) {
                try {
                    float rating = Float.parseFloat(ratingStr);
                    game.setRating(rating);
                    System.out.println("Updated rating to: " + rating);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid rating format: " + ratingStr);
                }
            }

            String imagePath = request.getParameter("imagePath");
            System.out.println("Received imagePath: " + imagePath);
            if (imagePath != null) {
                game.setImagePath(imagePath);
                System.out.println("Updated imagePath to: " + imagePath);
            }

            try {
                gameStorage.update(game);
                System.out.println("Game updated successfully with ID: " + game.getGameId());
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Game updated successfully");
                responseData.put("game", game);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(objectMapper.writeValueAsString(responseData));
                out.flush();
            } catch (Exception e) {
                System.err.println("Error updating game: " + e.getMessage());
                e.printStackTrace();
                sendJsonResponse(response, false, "Error updating game: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            System.err.println("Invalid game ID format: " + gameIdStr);
            sendJsonResponse(response, false, "Invalid game ID format");
        } catch (Exception e) {
            System.err.println("Error updating game: " + e.getMessage());
            e.printStackTrace();
            sendJsonResponse(response, false, "Error updating game: " + e.getMessage());
        }
    }
}