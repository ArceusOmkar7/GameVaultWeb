package gamevaultbase.servlets.user;

import gamevaultbase.entities.User;
import gamevaultbase.exceptions.GameAlreadyOwnedException;
import gamevaultbase.exceptions.GameNotFoundException;
import gamevaultbase.servlets.base.UserBaseServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for handling the add to cart functionality.
 * Only accessible to logged-in users.
 */
@WebServlet(name = "AddToCartServlet", urlPatterns = { "/addToCart" })
public class AddToCartServlet extends UserBaseServlet {

    @Override
    protected void processUserGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Adding items should be a POST action
        redirectWithMessage(request, response, "/viewCart",
                "Please use the 'Add to Cart' button on game pages", "error");
    }

    @Override
    protected void processUserPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = getLoggedInUser(request);

        String gameIdParam = request.getParameter("gameId");
        String message = null;
        String messageType = "error"; // Default to error
        boolean success = false;

        if (gameIdParam == null || gameIdParam.trim().isEmpty()) {
            message = "Invalid request: Missing game ID.";
        } else if (currentUser == null) {
            message = "Session expired or user not logged in.";
        } else {
            try {
                int gameId = Integer.parseInt(gameIdParam.trim());

                // Perform the add to cart operation
                boolean addResult = false;
                try {
                    getCartManagement().addGameToCart(currentUser.getUserId(), gameId);
                    addResult = true;
                } catch (Exception e) {
                    // If CartManagement throws, check if it's a known cause
                    Throwable cause = e.getCause();
                    if (cause != null) {
                        message = cause.getMessage();
                    } else {
                        message = e.getMessage();
                    }
                }
                if (addResult) {
                    message = "Game added to cart successfully.";
                    messageType = "success";
                    success = true;
                }
            } catch (NumberFormatException e) {
                message = "Invalid game ID format provided.";
            } catch (GameNotFoundException e) {
                message = "The requested game was not found.";
            } catch (Exception e) {
                System.err.println("Error adding to cart (User: "
                        + (currentUser != null ? currentUser.getUserId() : "null") + ", Game: "
                        + gameIdParam + "): " + e.getMessage());
                e.printStackTrace();
                message = "An error occurred while adding the game to your cart: " + e.getMessage();
            }
        }

        // Check if this is an AJAX request
        boolean isAjaxRequest = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        if (isAjaxRequest) {
            // Send JSON response for AJAX requests
            sendJsonResponse(response, success, message);
        } else {
            // For non-AJAX requests, use the redirect with message approach (backwards
            // compatibility)
            String referer = request.getHeader("Referer");
            if (referer != null && !referer.isEmpty()) {
                redirectWithMessage(request, response, referer, message, messageType);
            } else {
                redirectWithMessage(request, response, "/viewCart", message, messageType);
            }
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

        // Simple JSON creation without requiring a library
        out.print("{");
        out.print("\"success\": " + success + ",");
        out.print("\"message\": \"" + escapeJsonString(message) + "\"");
        out.print("}");

        out.flush();
    }

    /**
     * Escape special characters in a string for JSON
     */
    private String escapeJsonString(String input) {
        if (input == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            switch (ch) {
                case '"':
                    builder.append("\\\"");
                    break;
                case '\\':
                    builder.append("\\\\");
                    break;
                case '\b':
                    builder.append("\\b");
                    break;
                case '\f':
                    builder.append("\\f");
                    break;
                case '\n':
                    builder.append("\\n");
                    break;
                case '\r':
                    builder.append("\\r");
                    break;
                case '\t':
                    builder.append("\\t");
                    break;
                default:
                    builder.append(ch);
            }
        }
        return builder.toString();
    }
}