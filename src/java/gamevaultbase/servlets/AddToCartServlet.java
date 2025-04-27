package gamevaultbase.servlets;

import gamevaultbase.entities.User;
import gamevaultbase.exceptions.GameAlreadyOwnedException;
import gamevaultbase.exceptions.GameNotFoundException;
import gamevaultbase.management.CartManagement;
import gamevaultbase.management.GameManagement; // Sometimes useful to get game details
import java.io.IOException;
import javax.servlet.ServletException;
// Import annotation if using
// import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Mapped in web.xml as /addToCart
public class AddToCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("loggedInUser") : null;

        // User must be logged in
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?message=Please login to add items to cart&messageType=error");
            return;
        }

        String gameIdParam = request.getParameter("gameId");
        String message = null;
        String messageType = "error"; // Default to error message styling
        String redirectUrl = request.getContextPath() + "/games"; // Default redirect back to games list

        if (gameIdParam == null || gameIdParam.trim().isEmpty()) {
            message = "Invalid request: Missing game ID.";
        } else {
            CartManagement cartManagement = (CartManagement) getServletContext().getAttribute("cartManagement");
             // Optional: GameManagement gameManagement = (GameManagement) getServletContext().getAttribute("gameManagement");

             if (cartManagement == null) {
                  System.err.println("FATAL: CartManagement not found in ServletContext!");
                  message = "Application error: Cart service unavailable.";
             } else {
                try {
                    int gameId = Integer.parseInt(gameIdParam.trim());

                    // Optional: Get game details if needed for message
                    // Game game = gameManagement.getGame(gameId);

                    cartManagement.addGameToCart(currentUser.getUserId(), gameId);
                    // Adjust message if you fetched game details
                    // message = game.getTitle() + " added to cart successfully!";
                    message = "Game added to cart successfully!";
                    messageType = "success";
                    // Optionally redirect to cart page after adding
                    // redirectUrl = request.getContextPath() + "/viewCart";

                } catch (NumberFormatException e) {
                    message = "Invalid game ID format provided.";
                } catch (GameNotFoundException e) {
                    message = "The selected game could not be found.";
                } catch (GameAlreadyOwnedException e) {
                    // This exception might be thrown if user tries to add owned game
                    message = "You already own this game.";
                } catch (Exception e) {
                    // Catch potential SQL exceptions from storage layer (e.g., duplicate entry in CartItems)
                    if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
                         message = "This game is already in your cart.";
                    } else {
                         System.err.println("Error adding to cart (User: " + currentUser.getUserId() + ", Game: " + gameIdParam + "): " + e.getMessage());
                         e.printStackTrace(); // Log stack trace
                         message = "An error occurred while adding the game to your cart.";
                    }
                }
            }
        }

        // Redirect back with a message
        redirectUrl += "?message=" + java.net.URLEncoder.encode(message, "UTF-8")
                    + "&messageType=" + messageType;
        response.sendRedirect(redirectUrl);
    }

     // Handle GET request - Adding to cart should ideally be POST, but handle GET if called via simple link
     @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         // For simplicity, just call doPost. Consider security implications if state is modified via GET.
         // Or, redirect to games page with a message?
         // resp.sendRedirect(req.getContextPath() + "/games?message=Please use the add button&messageType=error");
         doPost(req, resp);
    }
}